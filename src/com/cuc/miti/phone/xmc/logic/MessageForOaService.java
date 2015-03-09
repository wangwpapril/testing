package com.cuc.miti.phone.xmc.logic;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.dao.MessageDao;
import com.cuc.miti.phone.xmc.domain.MessageForOa;
import com.cuc.miti.phone.xmc.domain.MessageForUs;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Enums.MsgOwnerType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgSendStatus;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceType;
import com.cuc.miti.phone.xmc.domain.Enums.ReadOrNotType;
import com.cuc.miti.phone.xmc.domain.Enums.SendOrReceiveType;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;

import android.content.Context;
import android.widget.Toast;

public class MessageForOaService {
	private Context context;
	private List<MessageForOa> messageList;	
	private MessageForOa msg;
	private String sessionId = "";
	
	public MessageForOaService(Context context){
		this.context = context;
		sessionId = IngleApplication.getSessionId();
		messageList=new ArrayList();
		
	}
	
//	public List<MessageForOa> getMessageListByPage(Pager pager)
//	{
//		int currPage=pager.getCurrentPage(); //��ȡ��ǰҳ
//		int pageSize=pager.getPageSize();  //��ȡҳ����Ϣ����
//		
//		int start=(currPage-1)*pageSize;  //��ǰҳ��һ��λ��
//		int end=currPage*pageSize;	//��ǰҲ���һ��λ��
//
//		if(end>messageList.size())
//			end=messageList.size();
//		List<MessageForOa> tempList=messageList.subList(start, end);
//		
//		return tempList;
//	}
//	public List<MessageForOa> getMessageList()
//	{
//		return messageList;
//	}
//	public MessageForOa getMessageByID()
//	{
//		return msg;
//	}

//	public List<MessageForOa> getMessageListFromRemote(Pager pager,int limit,int start)
//	{		
//		try {
//			if(sessionId==null || sessionId.equals("")){
//				return null;
//			}
//			messageList=RemoteCaller.getMessageListForOa(sessionId, limit, start);
//			pager.setTotalNum(getMsg_totalCount()); //������Ϣ������
////			if(messageList !=null && messageList.size()>0){
////				Toast.makeText(context,"������Ϣ���Ѹ�����ϣ�",Toast.LENGTH_SHORT).show();
////			}else{
////				Toast.makeText(context,"������Ϣ�ɸ��£�",Toast.LENGTH_SHORT).show();
////			}
//			return messageList;
//			} catch (Exception e) {
//				Logger.e(e);
//				e.printStackTrace();
//				return null;
//			}
//	
//	}
	public List<MessageForOa> getMessageListFromRemote(int limit,int start)
	{		
		try {
			if(sessionId==null || sessionId.equals("")){
				return null;
			}
			messageList=RemoteCaller.getMessageListForOa(sessionId, limit, start);
			//pager.setTotalNum(getMsg_totalCount()); //������Ϣ������
//			if(messageList !=null && messageList.size()>0){
//				Toast.makeText(context,"������Ϣ���Ѹ�����ϣ�",Toast.LENGTH_SHORT).show();
//			}else{
//				Toast.makeText(context,"������Ϣ�ɸ��£�",Toast.LENGTH_SHORT).show();
//			}
			return messageList;
			} catch (Exception e) {
				Logger.e(e);
				e.printStackTrace();
				return null;
			}
	
	}
	public int getMsg_totalCount()
	{
		return RemoteCaller.getMsgForOa_totalCount();
	}
	public MessageForOa getMessageByIDFromRemote(int id)
	{
		try {
			if(sessionId==null || sessionId.equals("")){
				return null;
			}
			msg=RemoteCaller.getMessageForOa(sessionId, id);
			return msg;
		} catch (Exception e) {
			Logger.e(e);
			e.printStackTrace();
			return null;
		}
	}
	
	public String getWebContent(){
		try {
			if(sessionId==null || sessionId.equals("")|| msg==null){
				return null;
			}
			return String.valueOf(msg.getInfocontent()+"?sss="+sessionId);
		} catch (Exception e) {
			Logger.e(e);
			e.printStackTrace();
			return null;
		}
		
	}
	
	
}
