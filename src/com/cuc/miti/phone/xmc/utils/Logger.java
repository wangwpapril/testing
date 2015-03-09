package com.cuc.miti.phone.xmc.utils;

import android.R.integer;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cuc.miti.phone.xmc.domain.Enums.LogType;

public class Logger {

	/**
	 * ������־�Ĳ�������ö��
	 * 
	 * @author wenyujun
	 * 
	 */
	public enum OperationMessage {
		CreateM("�������"), EditM("�༭���"), DeleteM("ɾ����"),SaveMLocalization("���ر�����"),SaveMOnline("���籣����");

		private final String svalue;

		public String getValue() {
			return svalue;
		}

		// ������Ĭ��Ҳֻ����private, �Ӷ�֤���캯��ֻ�����ڲ�ʹ��
		OperationMessage(String value) {
			this.svalue = value;
		}
	}

	/**
	 * ��־��ʶ
	 */
	private static final String TAG = "XinhuaManuscriptCollection";

	/**
	 * ϵͳ��־����·��
	 */
	private static final String SYSTEM_LOG_SAVE_PATH = StandardizationDataHelper.getLogFileStorePath(LogType.System);// "sdcard/XinHuaStackTraceLog/System/";
	/**
	 * ������־����·��
	 */
	private static final String OPERATION_LOG_SAVE_PATH = StandardizationDataHelper.getLogFileStorePath(LogType.Operation);//"sdcard/XinHuaStackTraceLog/Operation/";

	/**
	 * ��־����¼������Ϣ
	 * 
	 * @param message
	 */
	public static void d(String message) {
		Log.d(TAG, message);

		//File file = checkSystemLogExist();

		//Log(message, file);
	}

	/**
	 * ��־����¼ϵͳ�쳣��������Ϣ
	 * 
	 * @param e
	 */
	public static void e(Throwable e) {
		Log.e(TAG, e.getMessage(), e);

		File file = checkSystemLogExist();

		Log(buildSystemMessage(e), file);
	}

	/**
	 * ��־����¼������Ϣ
	 * 
	 * @param message
	 */
	public static void o(OperationMessage message) {

		File file = checkOperationLogExist();

		Log(message.getValue(), file);
	}
	
	/**
	 * ɾ�������־
	 * @param startDate ��ʼʱ��
	 * @param endDate ����ʱ��
	 */
	public static void RemoveOperationLogs(Date startDate, Date endDate){
		DeleteFiles(startDate, endDate, OPERATION_LOG_SAVE_PATH);
	}

	/**
	 * ɾ��ϵͳ��־
	 * @param startDate ��ʼʱ��
	 * @param endDate ����ʱ��
	 */
	public static void ReomveSystemLogs(Date startDate, Date endDate) {
		DeleteFiles(startDate, endDate, SYSTEM_LOG_SAVE_PATH);
	}
	
	public static void logHeap(Class clazz) { 
	    Double allocated = new Double(Debug.getNativeHeapAllocatedSize())/new Double((1048576)); 
	    Double available = new Double(Debug.getNativeHeapSize())/1048576.0; 
	    Double free = new Double(Debug.getNativeHeapFreeSize())/1048576.0; 
	    DecimalFormat df = new DecimalFormat(); 
	    df.setMaximumFractionDigits(2); 
	    df.setMinimumFractionDigits(2); 
	 
	    Log.d(TAG, "debug. ================================="); 
	    Log.d(TAG, "debug.heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free) in [" + clazz.getName().replaceAll("com.myapp.android.","") + "]"); 
	    Log.d(TAG, "debug.memory: allocated: " + df.format(new Double(Runtime.getRuntime().totalMemory()/1048576)) + "MB of " + df.format(new Double(Runtime.getRuntime().maxMemory()/1048576))+ "MB (" + df.format(new Double(Runtime.getRuntime().freeMemory()/1048576)) +"MB free)"); 
	    System.gc(); 
	    System.gc(); 
	} 

	
	/**
	 * ɾ����־�ļ�
	 * @param startDate
	 * @param endDate
	 * @param logPath
	 */
	private static void DeleteFiles(Date startDate, Date endDate, String logPath){
		long sl = startDate.getTime();
		long el = endDate.getTime();
		long ei = el - sl;
		
		int interval = (int)ei / (1000 * 60 * 60 * 24);
		
		for(int i = 0; i <= interval; i++){
			try {
				
				Date computeDate = TimeFormatHelper.addDate(startDate, i);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr = sdf.format(computeDate);
				
				String logFileName = dateStr + ".txt";
				
				File file = checkLogFileIsExist(logPath, logFileName);
				
				if(file != null)
					file.delete();
				
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
			
		}
	}

	/**
	 * ����쳣����ƴд�쳣��־��Ϣ
	 * 
	 * @param e
	 * @return
	 */
	private static String buildSystemMessage(Throwable e) {

		String message = null;

		message = String.format("%s,%s,%s,%s",
				e.getStackTrace()[0].getClassName(),
				e.getStackTrace()[0].getMethodName(),
				e.getStackTrace()[0].getLineNumber(), e.toString());

		return message;
	}

	/**
	 * ��¼��־��Ϣ
	 * 
	 * @param message
	 *            ������־��Ϣ
	 * @param file
	 *            ��־�ļ�����
	 */
	private static void Log(String message, File file) {
		if (file == null)
			return;

		FileOutputStream fos = null;

		try {

			fos = new FileOutputStream(file, true);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			fos.write((sdf.format(new Date()) + "," + message).getBytes("gbk"));

			fos.write("\r\n".getBytes("gbk"));

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		} finally {

			try {

				if (fos != null) {
					fos.close();
					fos = null;
				}
			} catch (IOException e) {

				e.printStackTrace();

			}
			fos = null;
			file = null;
		}
	}

	/**
	 * ��������־�ļ��Ƿ��Ѿ����ڣ���������������־�ļ�����
	 * 
	 * @return ������־�ļ����󣬳��쳣����null
	 */
	private static File checkSystemLogExist() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(new Date());
		
		String logFileName = dateStr + ".txt";
		
		return checkLogFileIsExist(SYSTEM_LOG_SAVE_PATH, logFileName);
	}

	/**
	 * ��������־�ļ��Ƿ��Ѿ����ڣ���������������־�ļ�����
	 * 
	 * @return ������־�ļ����󣬳��쳣����null
	 */
	private static File checkOperationLogExist() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(new Date());
		
		String logFileName = dateStr + ".txt";
		
		return checkLogFileIsExist(OPERATION_LOG_SAVE_PATH, logFileName);
	}

	/**
	 * ���sd�����Ƿ������־�ļ�
	 * 
	 * @param path
	 *            ���·��
	 * @return Ѱ�һ򴴽�����־�ļ����������sd����������null
	 */
	private static File checkLogFileIsExist(String path, String LogFileName) {

		if(path == "")
			return null;

		File file = new File(path);

		if (!file.exists()) {
			file.mkdirs();
		}

		file = new File(path + "//" + LogFileName);

		// �����־�ļ��Ƿ����
		if (!file.exists()) {
			try {

				file.createNewFile();

			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		return file;
	}
}
