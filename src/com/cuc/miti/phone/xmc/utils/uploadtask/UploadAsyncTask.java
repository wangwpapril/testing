package com.cuc.miti.phone.xmc.utils.uploadtask;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.UploadTaskStatus;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.UploadTask;
import com.cuc.miti.phone.xmc.domain.XmcException;
import com.cuc.miti.phone.xmc.http.DoRemoteResult;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.logic.ManuscriptsService;
import com.cuc.miti.phone.xmc.logic.UploadTaskService;
import com.cuc.miti.phone.xmc.utils.Encrypt;
import com.cuc.miti.phone.xmc.utils.Format;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.SendManuscriptsHelper;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;

/**
 * 异步执行任务类
 * 
 * @author SongQing
 * 
 */
public class UploadAsyncTask extends AsyncTask<Void, Integer, Boolean> {

	private static final int BLOCK_SIZE = 131072; // 128KB
	private boolean beFinished = true;
	private boolean paused = false;
	private UploadTaskService uploadTaskService = null;
	private ManuscriptsService manuscriptsService = null;
	private long eachBytesTransTime = -1;

	public boolean isPaused() {
		return paused;
	}

	UploadTaskJob mJob;

	public UploadAsyncTask(UploadTaskJob job) {
		mJob = job;
		this.uploadTaskService = new UploadTaskService(
				IngleApplication.getInstance());
		this.manuscriptsService = new ManuscriptsService(
				IngleApplication.getInstance());
	}

	public void setEachBytesTransTime(long time) {
		eachBytesTransTime = time;
	}

	public long getEachBytesTransTime() {
		return eachBytesTransTime;
	}

	/**
	 * (Javadoc) 停止下载
	 * 
	 * @see android.os.AsyncTask#onCancelled()
	 */
	@Override
	protected void onCancelled() {
		beFinished = false;
		mJob.notifyUploadCancelled();
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		beFinished = false;
		mJob.notifyUploadEnded(result);

		super.onPostExecute(result);
	}

	/**
	 * 在 doInBackground之前被调用
	 */
	@Override
	protected void onPreExecute() {
		mJob.notifyUploadStarted();
		publishProgress(0);
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		// TODO Auto-generated method stub
		// mJob.notifyUploadProgress(progress[0].intValue());
		// resultTextView.setText(progress[0].toString());
		super.onProgressUpdate(progress);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			return judgeNewOrOldJob(mJob);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 上传稿件的入口，判断当前稿件是新上传还是断点续传
	 * 
	 * @throws XmcException
	 */
	private boolean judgeNewOrOldJob(UploadTaskJob taskJob) throws XmcException {

		if (taskJob.getMUploadTask().getStatus()
				.equals(UploadTaskStatus.Waiting)
				&& taskJob.getMUploadTask().getFileid().equals("")
				&& taskJob.getMUploadTask().getMessage().equals("")) {
			return this.uploadManuscript(taskJob, "New");
		} else {
			return this.uploadManuscript(taskJob, "Old");
		}
	}

	/**
	 * 发送前验证稿件
	 * 
	 * @return
	 */
	private boolean validateManuscript(UploadTaskJob taskJob, String uploadType) {
		if (taskJob != null) {
			Manuscripts manuscripts = manuscriptsService.getManuscripts(taskJob
					.getMUploadTask().getManuscriptId());

			KeyValueData kvData = new KeyValueData();

			if (!SendManuscriptsHelper.validateForSend(manuscripts, kvData,
					IngleApplication.getInstance(),
					uploadType)) {
				taskJob.cancel();

				// Toast.makeText(IngleApplication.getInstance(),
				// kvData.getValue(), Toast.LENGTH_SHORT);
				ToastHelper.showToast(kvData.getValue(), Toast.LENGTH_SHORT);
				uploadTaskService.delete(taskJob.getMUploadTask().getId());
				manuscriptsService.Editing(taskJob.getMUploadTask()
						.getManuscriptId());

				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * 稿件上传
	 * 
	 * @param taskJob
	 *            每一个稿件作为一个上传任务传入(xml+accessory)
	 * @return
	 */
	public boolean uploadManuscript(UploadTaskJob taskJob, String uploadType)
			throws XmcException {
		/****************************** Step1 发稿前校验用户发稿权限、发稿地址是否正确 ***********************************/
		if (!validateManuscript(taskJob, uploadType)) { // 发稿前的校验，如果不通过则将稿件还原为在编状态
			return false;
		}

		// TODO 服务器根据什么区分是新的上传还是断点续传，是否是sessionid？还是别的？需要保存在数据库中
		if (taskJob != null) {
			// IngleApplication.getInstance();
			String sessionId = IngleApplication
					.getSessionId(); // 调用接口必须(登录时服务端返回)
			UploadTask tempUploadTask = taskJob.getMUploadTask(); // 上传任务对象

			String fid_attachlist = ""; // 稿件的附件
			String fid_xml = ""; // 稿件的xml

			/****************************** Step2 获取稿件所包含上传文件列表 ***********************************/
			List<String> filePathList = prepareUploadData(tempUploadTask);

			/****************************** Step3 遍历上传文件列表，进行稿件文件上传 ***********************************/
			for (String filePath : filePathList) {
				if (filePath == null) {
					continue;
				}
				Log.i(IngleApplication.TAG, filePath);
				HashMap<String, Integer> hashMapParam = this
						.getFileUploadParameter(filePath, tempUploadTask,
								IngleApplication
										.getInstance());

				if (hashMapParam.get("FileLength") == 0) {// 说明文件不存在
					Log.i(IngleApplication.TAG,
							"error:找不到文件");
					tempUploadTask.setStatus(UploadTaskStatus.FailedRestart);
					uploadTaskService.update(tempUploadTask);// 同步更新数据库中相关信息
					return false;
				}

				// byte[] totalBytes = new byte[hashMapParam.get("FileLength")];
				// // 整个文件的文件字节

				Log.i(IngleApplication.TAG, "上传文件分块数量为:"
						+ String.valueOf(hashMapParam.get("BlockNumber")));

				String fileId = "";

				// 在这里做是否续传的判断
				if (uploadType.equals("New") || filePath.endsWith(".xml")) { // 新上载(或者是xml文件的上传)
					fileId = this.applySpaceFormServer(sessionId,
							hashMapParam.get("FileLength"));
					fileId = sessionId + "_" + fileId;

					taskJob.getMUploadTask().setMessage(sessionId); // 保存本次上传所用的SessionID
					taskJob.getMUploadTask()
							.setStatus(UploadTaskStatus.Sending); // 设置上传任务状态为Sending
					uploadTaskService.update(taskJob.getMUploadTask());
				} else { // 断点续传
					fileId = tempUploadTask.getFileid();
					sessionId = tempUploadTask.getMessage(); // 设置本次上传中所用的SessionID为记录中的值
					// fileId = sessionId + "_" + fileId;
					taskJob.getMUploadTask()
							.setStatus(UploadTaskStatus.Sending); // 设置上传任务状态为Sending
					uploadTaskService.update(taskJob.getMUploadTask());
				}

				if (fileId != "") { // 表明文件上传申请服务器空间成功(服务器返回分配的文件id)
					if (this.uploadFiles(hashMapParam, filePath,
							tempUploadTask, sessionId, fileId)) { // 文件上传至服务器
						if (this.uploadFilesCheck(hashMapParam, filePath,
								tempUploadTask, sessionId, fileId)) { // 文件上传数据校验成功
							if (filePathList.indexOf(filePath) == 0) {
								// fid_attachlist=hashMapParam.get("FileLength")+"/"+(0+1)+"/"+sessionId+"_"+fileId;
								fid_attachlist = hashMapParam.get("FileLength")
										+ "/" + (0 + 1) + "/" + fileId;
							} else {
								// fid_xml=hashMapParam.get("FileLength")+"/"+sessionId+"_"+fileId;
								fid_xml = hashMapParam.get("FileLength") + "/"
										+ fileId;
							}
						} else {
							if (!paused) { // 既不是暂停也不是重新开始引起的上传数据校验失败
								tempUploadTask
										.setStatus(UploadTaskStatus.FailedRestart);
								uploadTaskService.update(tempUploadTask);// 同步更新数据库中相关信息
							}
							return false;
						}
					} else {
						tempUploadTask.setFileid(fileId);
						if (!paused) {
							tempUploadTask
									.setStatus(UploadTaskStatus.FailedContinue);
							uploadTaskService.update(tempUploadTask);// 同步更新数据库中相关信息
						}
						return false;
					}
				} else {
					return false;
				}
			}
			/****************************** Step4 文件上传完成，进行稿件最后提交，校验通过则上传成功 ***********************************/
			return this.submitManuscript(fid_xml, fid_attachlist, sessionId,
					tempUploadTask);
		} else {
			return false;
		}
	}

	/**
	 * 获取上传的文件列表和稿件总大小
	 * 
	 * @param uploadTask
	 * @return
	 */
	private List<String> prepareUploadData(UploadTask uploadTask) {
		List<String> filePaths = new ArrayList<String>();
		filePaths.add(0, uploadTask.getFileurl()); // 获取稿件文件附件_稿件XML_必须
		filePaths.add(1, uploadTask.getXmlurl()); // 获取稿件文件附件

		int totalSize = 0;
		for (String path : filePaths) {
			if (path != null) {
				totalSize += (int) new File(path).length();
			}
		}
		uploadTask.setTotalsize(totalSize); // 设置上载稿件总大小

		return filePaths;
	}

	/**
	 * 获取某一个文件上传的基本参数，如文件大小、分块数量等
	 * <p>
	 * 分块大小的策略可以从系统偏好设置(SharePreference)中获取，如果获取为空，则采用默认值
	 * 
	 * @param filePath
	 *            文件路径
	 * @param uploadTask
	 *            稿件上载任务对象实例
	 * @param context
	 *            上下文环境，调用系统偏好设置(SharedPreferencesHelper)需要传入
	 * @return 文件上传相关参数:文件大小、文件分块数
	 */
	private HashMap<String, Integer> getFileUploadParameter(String filePath,
			UploadTask uploadTask, Context context) {

		HashMap<String, Integer> fileParams = new HashMap<String, Integer>();

		int blockSize = 0; // 文件分块大小
		int blockNumber = 0; // 分块数量
		int fileLength = 0; // 文件大小

		File file = new File(filePath);
		fileLength = (int) file.length(); // 文件长度

		if (fileLength > 0) { // 说明文件存在

			SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(
					context);

			String blockSizeStr = sharedPreferencesHelper
					.GetUserPreferenceValue(PreferenceKeys.User_FileBlockSize
							.toString());
			if (blockSizeStr == null || blockSizeStr.equals("")
					|| blockSizeStr.equals("0")) { // 系统设置的分块大小不存在
				blockSize = BLOCK_SIZE;
			} else {
				try {
					blockSize = Integer.parseInt(blockSizeStr);
				} catch (Exception e) {
					blockSize = BLOCK_SIZE;
				}

			}

			uploadTask.setBlocksize(blockSize); // 设置本次上传文件的分块大小，便于以后续传时做同样配置

			if (fileLength % blockSize == 0) {
				blockNumber = (int) (fileLength / blockSize);
			} else {
				blockNumber = (int) (fileLength / blockSize) + 1;
			}
		}

		fileParams.put("FileLength", fileLength);
		fileParams.put("BlockNumber", blockNumber);
		fileParams.put("BlockSize", blockSize);

		return fileParams;
	}

	/**
	 * 从服务器申请磁盘空间
	 */
	private String applySpaceFormServer(String sessionId, int fileLength) {
		String fileIdString = "";
		try {
			String[] result = DoRemoteResult.doResult(RemoteCaller.FileUpNew(
					sessionId, fileLength));

			if (Integer.valueOf(result[0]) != 0
					|| result[2].equals(String.valueOf(fileLength))) {
				Log.i(IngleApplication.TAG,
						"error:申请磁盘空间失败," + result[2]);
			} else {
				Log.i(IngleApplication.TAG,
						"磁盘空间申请成功!fId=" + result[1]);
				fileIdString = result[1];
			}
		} catch (XmcException e) {
			e.printStackTrace();
			Logger.e(e);
			Log.i(IngleApplication.TAG, "error:申请磁盘空间失败,"
					+ e.getMessage());
		}

		return fileIdString;
	}

	/**
	 * 上传稿件文件
	 * 
	 * @param filePath
	 */
	private boolean uploadFiles(HashMap<String, Integer> hashMapParam,
			String filePath, UploadTask uploadTask, String sessionId,
			String fileId) {
		try {
			String range = "";
			String checkCode = "";
			File file = new File(filePath);

			// String totalCheckCode = Encrypt.toMd5(totalBytes);
			String totalCheckCode = Encrypt.toMd5(filePath);
			Log.i(IngleApplication.TAG, "发送前整个文件的MD5验证码： "
					+ totalCheckCode);

			FileInputStream fs = new FileInputStream(file);

			// fs.read(totalBytes, 0, hashMapParam.get("FileLength"));

			Log.i(IngleApplication.TAG, "开始上传,共"
					+ hashMapParam.get("BlockNumber").toString() + "块....");
			// uploadTask.setStatus(UploadTaskStatus.Sending);
			// //更新上载任务对象中的上传状态为“发送中”
			// uploadTaskService.setStatus(uploadTask.getId(),UploadTaskStatus.Sending);
			// //同步更新数据库中的状态字段

			int tempBlockCount = -1;
			if (filePath.endsWith(".xml")) {
				tempBlockCount = 0;
			} else {
				if (uploadTask.getLastblocknum() > -1) {// 说明是续传
					fs.skip(uploadTask.getLastblocknum()
							* uploadTask.getBlocksize());
					uploadTask.setUploadedsize(uploadTask.getUploadedsize()
							- uploadTask.getBlocksize()); // 重复传一块，所以要把进度减掉
				}
				tempBlockCount = uploadTask.getLastblocknum() + 1;
				if (tempBlockCount != 0) {
					tempBlockCount = tempBlockCount - 1;
				}
			}

			for (int block_count = tempBlockCount; block_count < hashMapParam
					.get("BlockNumber"); block_count++) {
				// Added by SongQing.2013.10.30 解决高版本android系统中，无法暂停下载的问题。
				if (paused) {
					return false;
				}
				if (beFinished) {

					// while(paused){ //pause是内存级别的暂停控制，不写入数据库
					// Thread.sleep(1000);
					// }
					long t1 = System.currentTimeMillis();
					Log.i(IngleApplication.TAG, "上传第"
							+ block_count + "块");

					Log.i(IngleApplication.TAG,
							String.valueOf(block_count));
					Log.i(IngleApplication.TAG,
							String.valueOf(hashMapParam.get("BlockNumber")));

					byte[] eachBytes; // 每块文件字节
					if (block_count == (hashMapParam.get("BlockNumber") - 1)) {

						range = String.valueOf(uploadTask.getBlocksize()
								* block_count)
								+ "-"
								+ String.valueOf(hashMapParam.get("FileLength") - 1)
								+ "@"
								+ String.valueOf(hashMapParam.get("FileLength"));

						eachBytes = new byte[(int) (hashMapParam
								.get("FileLength") - uploadTask.getBlocksize()
								* block_count)];
						// fs.read(eachBytes, uploadTask.getBlocksize()*
						// block_count, (int) (hashMapParam.get("FileLength") -
						// uploadTask.getBlocksize()* block_count -1));
						fs.read(eachBytes);
						// System.arraycopy(totalBytes,
						// uploadTask.getBlocksize() * block_count,eachBytes, 0,
						// eachBytes.length);
					} else {
						range = String.valueOf(uploadTask.getBlocksize()
								* block_count)
								+ "-"
								+ String.valueOf(uploadTask.getBlocksize()
										* (block_count + 1) - 1)
								+ "@"
								+ String.valueOf(hashMapParam.get("FileLength"));

						eachBytes = new byte[uploadTask.getBlocksize()];
						fs.read(eachBytes);

						// fs.read(eachBytes, uploadTask.getBlocksize()*
						// block_count, uploadTask.getBlocksize()-1);
						// System.arraycopy(totalBytes,
						// uploadTask.getBlocksize() * block_count,eachBytes, 0,
						// uploadTask.getBlocksize());
					}

					Log.i(IngleApplication.TAG, range);

					checkCode = Encrypt.toMd5(eachBytes);
					Log.i(IngleApplication.TAG, "MD5验证码: "
							+ checkCode);

					String[] uploadResult = DoRemoteResult.doResult(Format
							.replaceBlank(RemoteCaller.FileUpPartBlock(
									sessionId, fileId, checkCode, range,
									eachBytes)));

					if (0 != Integer.valueOf(uploadResult[0])
							|| !(checkCode.equals(uploadResult[3]))
							|| !(range.equals(uploadResult[2]))
							|| !(fileId.equals(uploadResult[1]))) {

						uploadTask.setUploadedsize(uploadTask.getUploadedsize()
								+ eachBytes.length);
						int progress = (int) ((long) uploadTask
								.getUploadedsize() * 100 / uploadTask
								.getTotalsize());
						uploadTask.setProgress(progress - 1);

						publishProgress(progress - 1); // 发布上载进度

						// uploadTask.setMessage("第" + block_count + "块上传成功");
						uploadTask.setLastblocknum(block_count);
						uploadTask.setFileid(fileId);

						uploadTaskService.update(uploadTask);// 同步更新数据库中相关信息

						setEachBytesTransTime(-1);
						Log.i(IngleApplication.TAG, "第"
								+ block_count + "块上传失败，" + uploadResult[1]);
						fs.close();
						return false;
					} else {
						uploadTask.setUploadedsize(uploadTask.getUploadedsize()
								+ eachBytes.length);
						int progress = (int) ((long) uploadTask
								.getUploadedsize() * 100 / uploadTask
								.getTotalsize());
						uploadTask.setProgress(progress - 1);

						publishProgress(progress - 1); // 发布上载进度

						// uploadTask.setMessage("第" + block_count + "块上传成功");
						uploadTask.setLastblocknum(block_count);
						uploadTask.setFileid(fileId);

						uploadTaskService.update(uploadTask);// 同步更新数据库中相关信息

						long t2 = System.currentTimeMillis();
						setEachBytesTransTime(t2 - t1);// 设置当前块传输时间

						Log.i(IngleApplication.TAG, "第"
								+ block_count + "块上传成功");
						Log.i(IngleApplication.TAG,
								"Progress:" + progress);
					}
				}
			}
			fs.close();
			return true;
		} catch (Exception e) {
			Logger.e(e);
			if (paused) { // 说明是暂停任务中断上传引起的异常
				Log.i(IngleApplication.TAG,
						"上传暂停，" + e.getMessage());
			} else {
				e.printStackTrace();
				Log.i(IngleApplication.TAG,
						"上传失败，" + e.getMessage());
			}
			return false;
		}
	}

	/**
	 * 上传稿件附件成功校验
	 * 
	 * @return
	 */
	private boolean uploadFilesCheck(HashMap<String, Integer> hashMapParam,
			String filePath, UploadTask uploadTask, String sessionId,
			String fileId) {
		if (paused) { // 表明已经暂停或重新开始
			Log.i(IngleApplication.TAG, "文件上传已经正常中断....");
			return false;
		}

		Log.i(IngleApplication.TAG, "文件上传完成，正在等待确认....");

		String totalCheckCode = Encrypt.toMd5(filePath);
		Log.i(IngleApplication.TAG, "整个文件的MD5验证码： "
				+ totalCheckCode);

		try {

			String[] result = DoRemoteResult.doResult(Format
					.replaceBlank(RemoteCaller.FileUpConfirm(sessionId, fileId,
							hashMapParam.get("FileLength"), totalCheckCode)));

			if (0 != Integer.valueOf(result[0])
					|| !(totalCheckCode.equals(result[3]))
					|| !(String.valueOf(hashMapParam.get("FileLength"))
							.equals(result[2])) || !(fileId.equals(result[1]))) {

				Log.i(IngleApplication.TAG, "确认失败： "
						+ result[1]);
				return false;
			} else {
				Log.i(IngleApplication.TAG, "确认成功，文件上传完成");
			}
			return true;

		} catch (XmcException e) {
			Logger.e(e);
			e.printStackTrace();
			Log.i(IngleApplication.TAG,
					"确认失败： " + e.getMessage());
			// uploadTask.setMessage("确认失败： " + e.getMessage());
			return false;
		}
	}

	/**
	 * 提交稿件，服务器确认稿件上载
	 * 
	 * @param fid_xml
	 *            稿件xml的校验值
	 * @param fid_attachlist
	 *            稿件附件的校验值
	 * @return
	 */
	private boolean submitManuscript(String fid_xml, String fid_attachlist,
			String sessionId, UploadTask uploadTask) {
		if (paused) { // 表明已经暂停或重新开始
			Log.i(IngleApplication.TAG, "文件上传已经正常中断....");
			return false;
		}

		try {
			Log.i(IngleApplication.TAG, "开始提交稿件....");
			Log.i(IngleApplication.TAG, "fid_xml:"
					+ fid_xml);
			Log.i(IngleApplication.TAG, "fid_attachlist:"
					+ fid_attachlist);

			String resultStr = RemoteCaller.SaveNews(sessionId, fid_xml,
					fid_attachlist);

			Log.i(IngleApplication.TAG, resultStr);

			String result[] = DoRemoteResult.doResult(resultStr);
			if (0 != Integer.valueOf(result[0])) {
				Log.i(IngleApplication.TAG, "提交稿件失败，"
						+ result[1]);
				uploadTask.setStatus(UploadTaskStatus.FailedRestart);

				// uploadTask.setMessage("提交稿件失败，"+result[1]);
				// --------目前接口不支持断点，所以失败后需要重置progress、lastblock等字段信息---------
				// uploadTask.setFileid("");
				// uploadTask.setLastblocknum(-1);
				// uploadTask.setProgress(0);
				// uploadTask.setRepeattimes(1);
				// uploadTask.setUploadedsize(0);
				// --------------------------------------------------------------------------------------------------
				uploadTaskService.update(uploadTask);
				return false;
			} else {
				uploadTask.setStatus(UploadTaskStatus.Finished);
				// uploadTask.setMessage("稿件提交成功，"+result[1]);
				uploadTask.setProgress(100);
				uploadTask.setFinishtime(TimeFormatHelper
						.getGMTTime(new Date()));
				uploadTaskService.update(uploadTask);
				manuscriptsService.updateNewsId(uploadTask.getManuscriptId(),
						result[1], TimeFormatHelper.getGMTTime(new Date()));
				publishProgress(100);
				return true;
			}
		} catch (XmcException e) {
			Log.i(IngleApplication.TAG, e.getMessage());
			uploadTask.setStatus(UploadTaskStatus.FailedRestart);
			// --------目前接口不支持断点，所以失败后需要重置progress、lastblock等字段信息---------
			// uploadTask.setFileid("");
			// uploadTask.setLastblocknum(-1);
			// uploadTask.setProgress(0);
			// uploadTask.setRepeattimes(1);
			// uploadTask.setUploadedsize(0);
			// --------------------------------------------------------------------------------------------------
			// uploadTask.setMessage("提交稿件失败，"+e.getMessage());
			uploadTaskService.update(uploadTask);
			return false;
		}
	}

	/**
	 * 暂停下载
	 */
	public void pause() {
		paused = true;
		Log.d("debug", "paused------------" + paused);
		mJob.notifyUploadPaused();
	}

	/**
	 * 重新下载
	 */
	public void restart() {
		mJob.notifyUploadRestart();
	}

	/**
	 * 继续下载
	 */
	public void continued() {
		// paused=false;
		// Log.d("debug","paused------------"+paused);
		mJob.notifyUploadContinued();
	}
}
