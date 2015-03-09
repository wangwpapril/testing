package com.cuc.miti.phone.xmc.domain;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class UserAttribute {

	private String userNameC;
	private String userNameE;
	private String groupNameC;
	private String groupNameE;
	private String groupCode;
	private boolean RightDisabled;
	private boolean RightSendNews;
	private boolean RightReleNews;
	private boolean RightTransferNews;
	private ArrayList<SendToAddress> addressList; // ����ר�õ�ַ
	private ArrayList<KeyValueData> transferAddressList; // ��Աר�õ�ַ
	private ArrayList<KeyValueData> choosedAddressList; // Ĭ��ר�õ�ַ

	/**
	 * @return the userNameC
	 */
	public String getUserNameC() {
		return userNameC;
	}

	/**
	 * @param userNameC
	 *            the userNameC to set
	 */
	public void setUserNameC(String userNameC) {
		this.userNameC = userNameC;
	}

	/**
	 * @return the userNameE
	 */
	public String getUserNameE() {
		return userNameE;
	}

	/**
	 * @param userNameE
	 *            the userNameE to set
	 */
	public void setUserNameE(String userNameE) {
		this.userNameE = userNameE;
	}

	/**
	 * @return the groupNameC
	 */
	public String getGroupNameC() {
		return groupNameC;
	}

	/**
	 * @param groupNameC
	 *            the groupNameC to set
	 */
	public void setGroupNameC(String groupNameC) {
		this.groupNameC = groupNameC;
	}

	/**
	 * @return the groupNameE
	 */
	public String getGroupNameE() {
		return groupNameE;
	}

	/**
	 * @param groupNameE
	 *            the groupNameE to set
	 */
	public void setGroupNameE(String groupNameE) {
		this.groupNameE = groupNameE;
	}

	/**
	 * @return the groupCode
	 */
	public String getGroupCode() {
		return groupCode;
	}

	/**
	 * @param groupCode
	 *            the groupCode to set
	 */
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	/**
	 * @return the rightDisabled
	 */
	public boolean getRightDisabled() {
		return RightDisabled;
	}

	/**
	 * @param rightDisabled
	 *            the rightDisabled to set
	 */
	public void setRightDisabled(boolean rightDisabled) {
		RightDisabled = rightDisabled;
	}

	/**
	 * @return the rightSendNews
	 */
	public boolean getRightSendNews() {
		return RightSendNews;
	}

	/**
	 * @param rightSendNews
	 *            the rightSendNews to set
	 */
	public void setRightSendNews(boolean rightSendNews) {
		RightSendNews = rightSendNews;
	}

	/**
	 * @return the rightReleNews
	 */
	public boolean getRightReleNews() {
		return RightReleNews;
	}

	/**
	 * @param rightReleNews
	 *            the rightReleNews to set
	 */
	public void setRightReleNews(boolean rightReleNews) {
		RightReleNews = rightReleNews;
	}

	/**
	 * @return the rightTransferNews
	 */
	public boolean getRightTransferNews() {
		return RightTransferNews;
	}

	/**
	 * @param rightTransferNews
	 *            the rightTransferNews to set
	 */
	public void setRightTransferNews(boolean rightTransferNews) {
		RightTransferNews = rightTransferNews;
	}

	public ArrayList<SendToAddress> getAddressList() {
		return addressList;
	}

	public void setAddressList(ArrayList<SendToAddress> addressList) {
		this.addressList = addressList;
	}

	public ArrayList<KeyValueData> getTransferAddressList() {
		return transferAddressList;
	}

	public void setTransferAddressList(
			ArrayList<KeyValueData> transferAddressList) {
		this.transferAddressList = transferAddressList;
	}

	public ArrayList<KeyValueData> getChoosedAddressList() {
		return choosedAddressList;
	}

	public void addChoosedAddress(KeyValueData kv) {
		if (choosedAddressList == null)
			choosedAddressList = new ArrayList<KeyValueData>();
		choosedAddressList.add(kv);
	}

	public void cleanChoosedAddress() {
		choosedAddressList=null;
	}

	public void setChoosedAddressList(ArrayList<KeyValueData> choosedAddressList) {
		this.choosedAddressList = choosedAddressList;
	}

	public void initializeChoosedAddressList() {
		if (RightSendNews) {
			ArrayList<KeyValueData> keyValueDataList = null;
			KeyValueData kvDataInfo = null;
			if (addressList != null && addressList.size() > 0) {
				keyValueDataList = new ArrayList<KeyValueData>();
				for (SendToAddress pt : addressList) {
					kvDataInfo = new KeyValueData();
					kvDataInfo.setKey(pt.getName());
					kvDataInfo.setValue(pt.getCode());

					keyValueDataList.add(kvDataInfo);
				}
			}
			choosedAddressList = keyValueDataList;
		} else {
			choosedAddressList = transferAddressList;
		}
	}
}
