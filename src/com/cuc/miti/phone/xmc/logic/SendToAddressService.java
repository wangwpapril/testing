package com.cuc.miti.phone.xmc.logic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.cuc.miti.phone.xmc.dao.SendToAddressDao;
import com.cuc.miti.phone.xmc.domain.CommonXMLData;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.Language;
import com.cuc.miti.phone.xmc.domain.NewsCategory;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.ProvideType;
import com.cuc.miti.phone.xmc.domain.SendToAddress;
import com.cuc.miti.phone.xmc.domain.User;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;

import android.content.Context;

public class SendToAddressService {
	private Context context;
	private SendToAddressDao sendToAddressDao;

	public SendToAddressService(Context context) {
		this.context = context;
		this.sendToAddressDao = new SendToAddressDao(context);
	}

	public boolean addSendToAddress(SendToAddress sendToAddress) {
		return sendToAddressDao.addSendToAddress(sendToAddress);
	}

	public boolean deleteSendToAddress(String name) {
		return sendToAddressDao.deleteSendToAddress(name);
	}

	public boolean deleteAllSendToAddress() {
		return sendToAddressDao.deleteAllSendToAddress();
	}

	public boolean updateSendToAddress(SendToAddress sendToAddress) {
		return sendToAddressDao.updateSendToAddress(sendToAddress);
	}

	public SendToAddress getSendToAddress(String name) {
		return sendToAddressDao.getSendToAddress(name);
	}

	public List<SendToAddress> getSendToAddressList() {
		return sendToAddressDao.getSendToAddressList();
	}

	public List<SendToAddress> getSendToAddressByPage(Pager pager) {
		return sendToAddressDao.getSendToAddressByPage(pager);
	}

	public long getSendToAddressCount() {
		return sendToAddressDao.getSendToAddressCount();
	}

	public SendToAddress getSendToAddressById(int id) {
		return sendToAddressDao.getSendToAddressById(id);
	}

	public SendToAddress getSendToAddressByCode(String language, String code) {
		return sendToAddressDao.getSendToAddressByCode(language, code);
	}

	/**
	 * ��ȡ����ѡ��Ļ���ݶ����б�
	 * 
	 * @return
	 */
	public List<KeyValueData> getBaseDataForBind(String queyString) {
		// TODO ��Ҫ��ȡ��ǰ�ֻ��ϵͳ���������ò�ͬ�����ֲ�ѯ��Ŀǰ��д�������ġ�
		List<SendToAddress> sendToAddressList = sendToAddressDao
				.getSendToAddressList("zh-CHS", queyString);
		List<KeyValueData> keyValueDataList = null;
		KeyValueData kvDataInfo = null;
		if (sendToAddressList != null && sendToAddressList.size() > 0) {
			keyValueDataList = new ArrayList<KeyValueData>();
			for (SendToAddress pt : sendToAddressList) {
				kvDataInfo = new KeyValueData();
				kvDataInfo.setKey(pt.getName());
				kvDataInfo.setValue(pt.getCode());

				keyValueDataList.add(kvDataInfo);
			}
		}
		return keyValueDataList;
	}

	public List<KeyValueData> getAllBaseData(User user, String queyString) {
		// ��ʽԱ��
		if (user.getUserattribute().getRightSendNews() == true) {

			List<KeyValueData> extraList = getExtraBaseDataForBind(user);
			List<KeyValueData> baseDataList = this
					.getBaseDataForBind(queyString);

			if (extraList == null)
				return baseDataList;
			else {
				List<KeyValueData> extraListTemp = new ArrayList<KeyValueData>();
				if (!queyString.equals("")) {
					for (KeyValueData kv : extraList)
						if (kv.getKey().startsWith(queyString)) {
							extraListTemp.add(kv);
						}
					extraListTemp.addAll(baseDataList);
					return extraListTemp;
				} else {
					extraList.addAll(baseDataList);
					return extraList;
				}
			}
		}
		// ��Ա
		else if (user.getUserattribute().getRightTransferNews() == true) {
			List<KeyValueData> extraList = getExtraBaseDataForBind(user);
			if (!queyString.equals("")) {
				List<KeyValueData> extraListTemp = new ArrayList<KeyValueData>();
				for (KeyValueData kv : extraList)
					if (kv.getKey().startsWith(queyString)) {
						extraListTemp.add(kv);
					}
				return extraListTemp;
			} else
				return extraList;

		} else
			return null;
	}

	/**
	 * ��ȡ�ӵ�¼�ش���Ϣ�л�ȡ���û������ַ�б�
	 * 
	 * @param user
	 * @return
	 */
	public List<KeyValueData> getExtraBaseDataForBind(User user) {

		// //��ʽԱ��
		// if(user.getUserattribute().getRightSendNews() == true){
		// ArrayList<SendToAddress> addresses =
		// user.getUserattribute().getAddressList();
		// List<KeyValueData> keyValueDataList = null;
		// KeyValueData kvDataInfo = null;
		// if(addresses != null && addresses.size()>0){
		// keyValueDataList = new ArrayList<KeyValueData>();
		// for(SendToAddress pt : addresses){
		// kvDataInfo = new KeyValueData();
		// kvDataInfo.setKey(pt.getName());
		// kvDataInfo.setValue(pt.getCode());
		//
		// keyValueDataList.add(kvDataInfo);
		// }
		// }
		//
		// return keyValueDataList;
		// }
		// //��Ա
		// else if(user.getUserattribute().getRightTransferNews() == true){
		// ArrayList<KeyValueData> addresses =
		// user.getUserattribute().getTransferAddressList();
		// return addresses;
		// }
		// else {
		// return null;
		// }
		ArrayList<KeyValueData> addresses = user.getUserattribute()
				.getChoosedAddressList();
		return addresses;
	}

	public List<KeyValueData> getCombineBaseDataForBind(User user,
			String queryString) {
		// ��ʽԱ��
		if (user.getUserattribute().getRightSendNews() == true) {

			List<KeyValueData> extraList = getExtraBaseDataForBind(user);
			List<KeyValueData> baseDataList = null;

			// ���û���ѡ��ַ�л�ȡ��ѡ��ķ����ַ
			EmployeeSendToAddressService emService = new EmployeeSendToAddressService(
					this.context);
			baseDataList = emService.getBaseDataForBind();
			if (baseDataList == null || baseDataList.size() == 0)
				baseDataList = this.getBaseDataForBind(queryString);

			if (extraList == null)
				return baseDataList;
			else {
				extraList.addAll(baseDataList);
				return extraList;
			}
		}
		// ��Ա
		else if (user.getUserattribute().getRightTransferNews() == true) {
			List<KeyValueData> extraList = getExtraBaseDataForBind(user);
			return extraList;
		} else
			return null;
	}
	/**
	 * ��ȡ���ļ�����ַ�б�
	 * @return
	 */
	public List<KeyValueData> getInstantUploadBaseDataForBind(List<String> sendToAddressList){
		List<KeyValueData> keyValueDataList = null;
		KeyValueData kvDataInfo = null;
		SendToAddress sta=null;
			if(sendToAddressList != null && sendToAddressList.size()>0){
				keyValueDataList = new ArrayList<KeyValueData>();
				for(String pt : sendToAddressList){
					sta=sendToAddressDao.getSendToAddressByCode("zh-CHS",pt);

					kvDataInfo = new KeyValueData();
					kvDataInfo.setKey(sta.getName());
					kvDataInfo.setValue(sta.getCode());
					
					keyValueDataList.add(kvDataInfo);
				}
			}
			
		return keyValueDataList;
	}

	/**
	 * ���������������ݿ�(����)
	 * 
	 * @param sendToAddresseList
	 * @return
	 */
	public boolean multiInsert(List<SendToAddress> sendToAddresseList) {
		return sendToAddressDao.multiInsert(sendToAddresseList);
	}

	/**
	 * ��ȡ�����ַ����List
	 * 
	 * @param filePath
	 *            �������xml�ļ�·��
	 * @return
	 * @throws IOException
	 */
	public List<SendToAddress> GetSendToAddressDataFromXMLFile(String filePath)
			throws IOException {
		List<SendToAddress> staList = null;
		File file = new File(filePath);
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(file));
			List<CommonXMLData> cxdList = XMLDataHandle.parser(is);

			if (cxdList != null) {
				staList = this.GetFromCommonList(cxdList);
			}
		} catch (Exception ex) {
			Logger.e(ex);
		} finally {
			if (is != null) {
				is.close();
			}

		}
		return staList;
	}

	public List<SendToAddress> GetSendToAddressDataFromXMLFile(InputStream is)
			throws IOException {
		List<SendToAddress> staList = null;
		try {

			List<CommonXMLData> cxdList = XMLDataHandle.parserSendtoAddress(is);

			if (cxdList != null) {
				staList = this.GetFromCommonList(cxdList);
			}
		} catch (Exception ex) {
			Logger.e(ex);
		} finally {
			if (is != null) {
				is.close();
			}

		}
		return staList;
	}

	private List<SendToAddress> GetFromCommonList(List<CommonXMLData> cxmList) {

		List<SendToAddress> sendToAddressList = new ArrayList<SendToAddress>();

		for (CommonXMLData commonXMLData : cxmList) {

			List<KeyValueData> keyValueDatas = commonXMLData.getLanguageList();

			for (KeyValueData kv : keyValueDatas) {
				SendToAddress sendToAddress = new SendToAddress();

				sendToAddress.setCode(commonXMLData.getTopicId());
				sendToAddress.setOrder(commonXMLData.getOrder());
				sendToAddress.setName(commonXMLData.getName());
				sendToAddress.setLanguage(kv.getKey());

				sendToAddressList.add(sendToAddress);
			}
		}

		return sendToAddressList;

	}
}
