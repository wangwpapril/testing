package com.cuc.miti.phone.xmc.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.dao.AccessoriesDao;
import com.cuc.miti.phone.xmc.dao.ManuscriptsDao;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.domain.Enums.ManuscriptStatus;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.User;
import com.cuc.miti.phone.xmc.domain.UploadTask;
import com.cuc.miti.phone.xmc.utils.ManuscriptIDCreator;
import com.cuc.miti.phone.xmc.utils.SendManuscriptsHelper;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;
import com.cuc.miti.phone.xmc.utils.uploadtask.FileUploadTaskHelper;
import com.cuc.miti.phone.xmc.utils.uploadtask.UploadTaskJob;

import android.content.Context;

public class ManuscriptsService {
	private ManuscriptsDao mDao;
	private AccessoriesService accessoriesService;
	private Context context;
	private UploadTaskService uploadTaskService = null;

	public ManuscriptsService(Context context) {
		this.context = context;
		this.mDao = new ManuscriptsDao(context);
		this.accessoriesService = new AccessoriesService(context);
		this.uploadTaskService = new UploadTaskService(context);
	}

	public boolean addManuscripts(Manuscripts manuscripts) {
		return mDao.addManuscripts(manuscripts);
	}

	public boolean deleteManuscripts(String username) {
		return mDao.deleteManuscripts(username);
	}

	/**
	 * ��ݸ��Idɾ������Ϣ������ɾ�����¸����б?
	 * 
	 * @param id
	 *            ���ID
	 * @return
	 */
	public boolean deleteById(String id) throws IOException {

		AccessoriesDao accDao = new AccessoriesDao(this.context);

		// ɾ�����µĸ�����Ϣ
		List<Accessories> accs = accDao.getAccessoriesListByMID(id);

		for (Accessories acc : accs) {
			File file = new File(acc.getUrl());
			if (file.exists()) {
				file.delete();
			}
		}

		boolean result = accDao.deleteAccessoriesByMID(id);

		if (result == true) {
			result = mDao.deleteById(id);
		}

		return result;
	}

	public boolean deleteAllManuscripts() {
		return mDao.deleteAllManuscripts();
	}

	public Manuscripts getManuscripts(String m_id) {
		return mDao.getManuscripts(m_id);
	}

	public boolean updateManuscripts(Manuscripts manuscripts) {
		return mDao.updateManuscripts(manuscripts);
	}

	public List<Manuscripts> getManuscriptsList(String loginname) {
		return mDao.getManuscriptsList(loginname);
	}

	public long getManuscriptsCount() {
		return mDao.getManuscriptsCount();
	}

	public List<Manuscripts> getManuscriptsByPage(Pager pager, String title,
			ManuscriptStatus status, String loginName, boolean fullObject) {
		return mDao.getManuscriptsByPage(pager, title, status, loginName,
				fullObject);
	}

	/**
	 * ��̭���
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	public boolean elimination(String id) {
		return mDao.updateStatus(id, ManuscriptStatus.Elimination);
	}

	/**
	 * ����
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	public boolean StandTo(String id) {
		return mDao.updateStatus(id, ManuscriptStatus.StandTo);
	}

	/**
	 * ������ͳɹ�
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	public boolean Send(String id) {
		return mDao.updateStatus(id, ManuscriptStatus.Sent);
	}

	/**
	 * �ָ����
	 * 
	 * @param id
	 * @return
	 */
	public boolean Editing(String id) {
		return mDao.updateStatus(id, ManuscriptStatus.Editing);
	}

	/**
	 * ���͸��(��Ѷ����Ҫƴ�Ӹ������)
	 * 
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 */
	public boolean sendManuscripts(Manuscripts manuscript,
			List<Accessories> accessories) throws FileNotFoundException,
			IllegalArgumentException, IllegalStateException, IOException {

		List<Manuscripts> maList = new ArrayList<Manuscripts>();
		AccessoryType muType = AccessoryType.Text;
		if (accessories != null && accessories.size() != 0)
			muType = AccessoryType.valueOf(accessories.get(0).getType());

		setStandToManuscriptInfo(manuscript, muType);

		maList.add(manuscript);

		if (accessories != null && accessories.size() > 0) {
			for (int i = 1; i < accessories.size(); i++) {
				Manuscripts mu = new Manuscripts();
				Accessories acc = accessories.get(i);

				manuscript.copyTo(mu);

				setStandToManuscriptInfo(mu,AccessoryType.valueOf(acc.getType()));

				if(!acc.getTitle().trim().equals("") && !mu.getTitle().contains(acc.getTitle())){
					mu.setTitle(mu.getTitle().concat(" ").concat(acc.getTitle()));
				}
				if(!acc.getDesc().trim().equals("") && !mu.getContents().contains(acc.getDesc())){
					mu.setContents(mu.getContents().concat("\n").concat(acc.getDesc()));
				}
				mu.setM_id(UUID.randomUUID().toString()); // һ��������Ӧһ���¸��
				accessories.get(i).setM_id(mu.getM_id());

				accessoriesService.updateAccessories(accessories.get(i)); // ���¸��������Ķ�Ӧid

				maList.add(mu);
			}
			// ԭ����ı�����������һ�������ı���������ϲ�
			Manuscripts tempM = maList.get(0);
			if(!accessories.get(0).getTitle().trim().equals("") && !tempM.getTitle().contains(accessories.get(0).getTitle())){
				tempM.setTitle(tempM.getTitle().concat(" ").concat(accessories.get(0).getTitle()));
			}
			if(!accessories.get(0).getDesc().trim().equals("") && !tempM.getContents().contains(accessories.get(0).getDesc())){
				tempM.setContents(accessories.get(0).getDesc().concat("\n").concat(tempM.getContents()));
			}
		}

		// ���뷢�Ͷ��У�ͬʱ�޸ĸ��״̬
		return this.upload(maList, accessories);

	}
	
	/**
	 * ������ͨ���(��ƴ�Ӹ������)
	 * 
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 */
	public boolean sendNormalManuscripts(Manuscripts manuscript,
			List<Accessories> accessories) throws FileNotFoundException,
			IllegalArgumentException, IllegalStateException, IOException {

		List<Manuscripts> maList = new ArrayList<Manuscripts>();
		AccessoryType muType = AccessoryType.Text;
		if (accessories != null && accessories.size() != 0)
			muType = AccessoryType.valueOf(accessories.get(0).getType());

		setStandToManuscriptInfo(manuscript, muType);

		maList.add(manuscript);

		if (accessories != null && accessories.size() > 0) {
			for (int i = 1; i < accessories.size(); i++) {
				Manuscripts mu = new Manuscripts();
				Accessories acc = accessories.get(i);

				manuscript.copyTo(mu);

				setStandToManuscriptInfo(mu,AccessoryType.valueOf(acc.getType()));
				
				if(!acc.getTitle().trim().equals("") && !mu.getTitle().contains(acc.getTitle())){
					mu.setTitle(mu.getTitle().concat(" ").concat(acc.getTitle()));
				}
				if(!acc.getDesc().trim().equals("") && !mu.getContents().contains(acc.getDesc())){
					mu.setContents(mu.getContents().concat("\n").concat(acc.getDesc()));
				}

				mu.setM_id(UUID.randomUUID().toString()); // һ��������Ӧһ���¸��
				accessories.get(i).setM_id(mu.getM_id());

				accessoriesService.updateAccessories(accessories.get(i)); // ���¸��������Ķ�Ӧid

				maList.add(mu);
			}
			// ԭ����ı�����������һ�������ı���������ϲ�
			Manuscripts tempM = maList.get(0);
			if(!accessories.get(0).getTitle().trim().equals("") && !tempM.getTitle().contains(accessories.get(0).getTitle())){
				tempM.setTitle(tempM.getTitle().concat(" ").concat(accessories.get(0).getTitle()));
			}
			if(!accessories.get(0).getDesc().trim().equals("") && !tempM.getContents().contains(accessories.get(0).getDesc())){
				tempM.setContents(accessories.get(0).getDesc().concat("\n").concat(tempM.getContents()));
			}
			
		}

		// ���뷢�Ͷ��У�ͬʱ�޸ĸ��״̬
		return this.upload(maList, accessories);

	}

	/**
	 * ����ǰ�����뷢����ص���Ϣ
	 * 
	 * @param mu
	 * @param accType
	 */
	private void setStandToManuscriptInfo(Manuscripts mu, AccessoryType accType) {

		ManuscriptIDCreator idCreator = new ManuscriptIDCreator(this.context);

		mu.setNewstype(accType.getValue());
		mu.setNewstypeID(accType.toString());

		String createid = idCreator.generateCreateID(mu);
		mu.setCreateid(createid);
		mu.setReleid(createid);
		mu.setNewsid(createid);
		mu.setReletime(TimeFormatHelper.getFormatTime(new Date()));
		mu.setReceiveTime(TimeFormatHelper.getFormatTime(new Date()));

		User user = IngleApplication.getInstance().currentUserInfo;
		mu.setGroupcode(user.getUserattribute().getGroupCode());
		mu.setGroupnameC(user.getUserattribute().getGroupNameC());
		mu.setGroupnameE(user.getUserattribute().getGroupNameE());
		mu.setUsernameC(user.getUserattribute().getUserNameC());
		mu.setUsernameE(user.getUserattribute().getUserNameE());
		mu.setLoginname(user.getUsername());
	}

	/**
	 * ��ʼ���ͣ�������񣬽�������뷢�Ͷ���
	 * 
	 * @param maList
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 */
	private boolean upload(List<Manuscripts> maList,
			List<Accessories> accessories) throws FileNotFoundException,
			IllegalArgumentException, IllegalStateException, IOException {
		boolean updateResult = false;
		for (int i = 0; i < maList.size(); i++) {

			Manuscripts mu = maList.get(i);

			Accessories acc = null;
			if (accessories != null && accessories.size() > 0)
				acc = accessories.get(i);

			String path = XMLDataHandle.Serializer(mu, acc, mu.getCreateid());

			mu.setManuscriptsStatus(ManuscriptStatus.StandTo);

			Manuscripts temp = this.getManuscripts(mu.getM_id());
			if (temp != null)
				updateResult = this.updateManuscripts(mu);
			else {
				updateResult = this.addManuscripts(mu);
			}

			// ���뷢�Ͷ���
			String accPath = null;
			if (acc != null)
				accPath = acc.getUrl();
			final String[] urls = { accPath, path };

			UploadTask uploadTask = this.addUploadTask(urls, mu);
			UploadTaskJob uploadTaskJob = new UploadTaskJob(uploadTask);

			FileUploadTaskHelper.addToUploads(uploadTaskJob);
		}

		return updateResult;
	}

	/**
	 * ����ʱ���һ���ϴ�������񲢲�����ݿ�
	 * 
	 * @param urls
	 * @return
	 */
	private UploadTask addUploadTask(String[] urls, Manuscripts manuItem) {
		int utId = uploadTaskService.add(urls, manuItem.getM_id(), manuItem
				.getManuscriptTemplate().getPriorityID());
		return uploadTaskService.get(utId);
	}

	/**
	 * �ϴ��ɹ���������ʽ�ĸ��id
	 * 
	 * @param id
	 * @return
	 */
	public boolean updateNewsId(String id, String newsId,String uploadTime) {
		Manuscripts manuscripts = mDao.getManuscripts(id);
		if (manuscripts != null) {
			manuscripts.setSenttime(uploadTime);
			manuscripts.setNewsid(newsId);
			manuscripts.setNewsIDBackTime(TimeFormatHelper.getGMTTime(new Date()));
			return mDao.updateManuscripts(manuscripts);
		} else {
			return false;
		}
	}
}
