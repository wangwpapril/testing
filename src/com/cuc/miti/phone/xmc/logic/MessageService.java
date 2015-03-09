package com.cuc.miti.phone.xmc.logic;

import java.util.List;

import android.content.Context;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.dao.MessageDao;
import com.cuc.miti.phone.xmc.domain.Enums.MsgOwnerType;
import com.cuc.miti.phone.xmc.domain.MessageForUs;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Enums.MessageType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgSendStatus;
import com.cuc.miti.phone.xmc.domain.Enums.NetStatus;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceType;
import com.cuc.miti.phone.xmc.domain.Enums.ReadOrNotType;
import com.cuc.miti.phone.xmc.domain.Enums.SendOrReceiveType;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.ui.MainActivity;
import com.cuc.miti.phone.xmc.ui.MessageSecondActivity;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;


public class MessageService {
	
	private Context context;
	private MessageDao messageDao;
	private List<MessageForUs> messageList;					//��Ϣ�б�
	private SharedPreferencesHelper sharedPreferencesHelper;
	private String sessionId = "";
	private String currentUser = "";
	
	public MessageService(Context context){
		this.context = context;
		this.messageDao = new MessageDao(context);
		sessionId = IngleApplication.getSessionId();
		currentUser =IngleApplication.getInstance().getCurrentUser();
		sharedPreferencesHelper = new SharedPreferencesHelper(context);
		
	}
	
	public boolean addMessage(MessageForUs message){
		return this.messageDao.addMessage(message);
	}	
	public void addMessage(List<MessageForUs> messageList){
		this.messageDao.addMessage(messageList);	
	}

	public boolean deleteMessageById(int id){
		return messageDao.deleteMessageById(id);
	}
	public boolean deleteMessageByMsgFromOwner(String msgFrom,String msgOwner){
		return messageDao.deleteMessageByMsgFromOwner(msgFrom, msgOwner);
	}
	
	public boolean deleteSameTypeMessage(String msgType){		
		return messageDao.deleteSameTypeMessage(msgType);		
	}
	public boolean updateMessage(MessageForUs message){
		return messageDao.updateMessage(message);
	}
	public MessageForUs getMessageById(int id){
		return messageDao.getMessageById(id);
	  
	}
	/*public long getMessageCount(String id){
		return messageDao.getMessageCount(id);
	}
	*/
	public int getMessageNewCount(String loginName)
	{
		return messageDao.getMessageNewCount(loginName);
	}
	
	public boolean getMessageNewByMsgType(MessageType messageType,String loginName)
	{
		return this.messageDao.getMessageNewByMsgType(messageType,loginName);
	}
	public List<MessageForUs> getMessageAll(MessageType status, String loginName) {
		return messageDao.getMessageAll(status, loginName);
	}
	public List<MessageForUs> getMessageAllForInst(MessageType messageType,
			String loginname) {
		return messageDao.getMessageAllForInst(messageType, loginname);
	}
	public List<MessageForUs>  getMessageByMsgTypeList(Pager pager,MessageType messageType)
	{
		return messageDao.getMessageByMsgTypeListByPage(pager, messageType);
	}
	
	public List<MessageForUs> getMessageByTypeList(MessageType messageType,String loginName){
		return messageDao.getMessageByTypeList(messageType, loginName);
	}
	
	public List<MessageForUs> getMessageListBySORType(SendOrReceiveType sendOrReceiveType){
		return messageDao.getMessageListBySORType(sendOrReceiveType);
		
	}
	public List<MessageForUs> getMessageList(String loginName){
		return messageDao.getMessageList(loginName);
		
	}	
	public List<MessageForUs> getMessageByPage(Pager pager){
	
		return messageDao.getMessageByPage(pager);
		
	}
	public long getMessageCount(String loginName)
	{
		return messageDao.getMessageCount(loginName);
	}
	public List<MessageForUs> getMessageByPage(Pager pager,MessageType status,SendOrReceiveType type, String loginName) {
		return messageDao.getMessageByPage(pager, status, type, loginName);
	}
	public List<MessageForUs> getMessageByPage(Pager pager,MessageType status, String loginName) {
		return messageDao.getMessageByPage(pager, status, loginName);
	}
	public List<MessageForUs> getMessageJustByPage(Pager pager,MessageType status, String loginName) {
		return messageDao.getMessageJustByPage(pager, status, loginName);
	}
	public List<MessageForUs> getMessageByPageAndMsgFrom(Pager pager,MessageType status, String msgFrom, String loginName) {
		return messageDao.getMessageByPageAndMsgFrom(pager, status, msgFrom, loginName);
	}
	
	public List<MessageForUs> getMessageByMsgFrom(MessageType status, String msgFrom, String loginName) {
		return messageDao.getMessageByMsgFrom(status, msgFrom, loginName);
	}
	
	public List<MessageForUs> getMessageByMsgOwner(MessageType status, String msgOwner, String loginName) {
		return messageDao.getMessageByMsgOwner(status, msgOwner, loginName);
	}
	public List<MessageForUs> getMessageByPageForRecom(Pager pager,MessageType status, String loginName) {
		return messageDao.getMessageByPageForRecom(pager, status, loginName);
	}
	public List<MessageForUs> getMessageForRocommend(MessageType status, String loginName) {
		return messageDao.getMessageForRocommend(status, loginName);
	}
	public List<MessageForUs> getMsgByPageAndMsgFromForSys(Pager pager,MessageType status, String msgFrom, String loginName) {
		return messageDao.getMsgByPageAndMsgFromForSys(pager, status, msgFrom, loginName);
	}
	
	public int getMessageFromRemote()
	{
		//����ʵ�ְ���Ϣ�б�浽������ݿ�			
		String messageTime = sharedPreferencesHelper.GetUserPreferenceValue(PreferenceKeys.User_MessageTime.toString());
		try {
			if(sessionId==null || sessionId.equals("")){
				return -1;
			}
			messageList=RemoteCaller.getMessage(sessionId, currentUser,messageTime.equals("")?"0000000000000":messageTime);
			String lasttime= "0000000000000";
			if(messageList !=null && messageList.size()>0){
				for(MessageForUs msg : messageList)
				{
					msg.setMsgSendStatus(MsgSendStatus.Success);
					msg.setReadOrNotType(ReadOrNotType.valueOf("New"));
					msg.setSendOrReceiveType(SendOrReceiveType.valueOf("Receive"));
					msg.setLoginName(currentUser);
					if(msg.getMsgOwnerType().equals(MsgOwnerType.All)){		//�жϸ�����Ϣ�Ƿ��ǹ㲥��Ϣ����������޸�msgownerΪ��ǰ��¼�û�
						msg.setMsgOwner(currentUser);
					}
					
					try {
						if(Long.parseLong(msg.getMsgSendOrReceiveTime()) >Long.parseLong(lasttime)){
							lasttime = msg.getMsgSendOrReceiveTime();
						}
					} catch (Exception e) {
						Logger.e(e);
					}
					//MessageService.this.addMessage(msg);
				}
				this.addMessage(messageList);
				sharedPreferencesHelper.SaveUserPreferenceSettings(PreferenceKeys.User_MessageTime.toString(), PreferenceType.String, lasttime);
				return 1;
				//Toast.makeText(context,"������Ϣ���Ѹ�����ϣ�",Toast.LENGTH_SHORT).show();
			}else{
				return 0;
				//Toast.makeText(context,"������Ϣ�ɸ��£�",Toast.LENGTH_SHORT).show();
			}
			} catch (Exception e) {
				Logger.e(e);
				e.printStackTrace();
			}
			return -1;
	}
	
}
