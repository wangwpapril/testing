package com.cuc.miti.phone.xmc.utils;

/**
 * Smack�������ͨ�����賣�ù��ܽӿڷ�װ��
 * @author SongQing
 * @date 2013-2-19
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.OfflineMessageManager;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.search.UserSearchManager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cuc.miti.phone.xmc.domain.MessageForUs;
import com.cuc.miti.phone.xmc.domain.UserBean;
import com.cuc.miti.phone.xmc.domain.Enums.MessageType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgOwnerType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgSendStatus;
import com.cuc.miti.phone.xmc.domain.Enums.ReadOrNotType;
import com.cuc.miti.phone.xmc.domain.Enums.SendOrReceiveType;
import com.cuc.miti.phone.xmc.logic.MessageService;
import com.cuc.miti.phone.xmc.xmpp.Constants;

public class SmackUtils {

	private static String TAG = "SmackUtils";
	/**
	 * ������XMPP������������(�޶˿ں�)
	 * @param domain
	 * @return
	 * @throws XMPPException
	 * @Eg  XMPPConnection connection = new XMPPConnection("gmail.com");
	 */
	public static XMPPConnection getConnection(String domain) throws XMPPException{
		XMPPConnection connection = new XMPPConnection(domain);
		connection.connect();  
		return connection;
	}
	
	/**
	 * ������XMPP������������(�ж˿ں�)
	 * @param domain
	 * @return
	 * @throws XMPPException
	 */
	public static XMPPConnection getConnection(String domain,int port) throws XMPPException{
		// Create the configuration for this new connection
		ConnectionConfiguration config = new ConnectionConfiguration(domain,port);  
		config.setSecurityMode(SecurityMode.required);
		config.setSASLAuthenticationEnabled(false);
		config.setCompressionEnabled(false);
		XMPPConnection connection = new XMPPConnection(config);
		connection.connect();  
		return connection;
	}
	
	/**
	 * �Ͽ���XMPP������������
	 * @param connection
	 * @throws XMPPException
	 * @Eg connection.login("javatest2011@gmail.com", "*****");
	 */
	public static void releaseConnection(XMPPConnection connection)throws XMPPException{
		connection.disconnect();
	}
	
	/**
	 * ��½XMPP������(ǰ��������:��������XMPP������������)
	 * @param connection
	 * @param username
	 * @param password
	 * @throws XMPPException
	 */
	public static void Login(XMPPConnection connection,String username,String password)throws XMPPException{
		connection.login(username, password);
	}
	
	/**
	 * ע���û�
	 * @param connection
	 * @param regUserName
	 * @param regUserPwd
	 * @return
	 */
	public static boolean createAccount(XMPPConnection connection,String regUserName,String regUserPwd){
		try {
			connection.getAccountManager().createAccount(regUserName, regUserPwd);  
			return true;  
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * ɾ��ǰ�û� 
	 * @param connection
	 * @return
	 */
	public static boolean deleteAccount(XMPPConnection connection){
		try {
			connection.getAccountManager().deleteAccount();  
			return true;  
		} catch (Exception e) {
			return false;  
		}
	} 
	
	/**
	 * �޸�����
	 * @param connection
	 * @param pwd
	 * @return
	 */
	public static boolean changePassword(XMPPConnection connection,String pwd){
		try {
			connection.getAccountManager().changePassword(pwd);  
			return true;
		} catch (Exception e) {
			return false;
		}
	}  
	
	/**
	 * ������������Ϣ <RosterGroup> 
	 * @param roster
	 * @return List(RosterGroup)
	 */
	public static List<RosterGroup> getGroups(Roster roster){
		List<RosterGroup> groupsList = new ArrayList<RosterGroup>();
		Collection<RosterGroup> rosterGroup = roster.getGroups();
		Iterator<RosterGroup> i = rosterGroup.iterator();  
		while (i.hasNext()){
			groupsList.add(i.next());  
		}  
		return groupsList;  	
	}
	
	/**
	 * ������Ӧ(groupName)����������û�<RosterEntry> 
	 * @param roster
	 * @param groupName
	 * @return List(RosterEntry) 
	 */
	public static List<RosterEntry> getEntriesByGroup(Roster roster,String groupName) { 
		List<RosterEntry> entriesList = new ArrayList<RosterEntry>();  
		RosterGroup rosterGroup = roster.getGroup(groupName);  
		Collection<RosterEntry> rosterEntry = rosterGroup.getEntries();  
		Iterator<RosterEntry> i = rosterEntry.iterator();  
		while (i.hasNext()){ 
			entriesList.add(i.next());
		}
		return entriesList;  
	}
	
	/**
	 * ���������û���Ϣ <RosterEntry> 
	 * @param roster
	 * @return List(RosterEntry) 
	 */
	public static List<RosterEntry> getAllEntries(Roster roster) {  
		List<RosterEntry> entriesList = new ArrayList<RosterEntry>();  
		Collection<RosterEntry> rosterEntry = roster.getEntries();  
		Iterator<RosterEntry> i = rosterEntry.iterator();  
		while (i.hasNext()){
			entriesList.add(i.next());  
		}  
		return entriesList;  
	}
	
	/**
	 * ���һ���� 
	 * @param roster
	 * @param groupName
	 * @return
	 */
	public static boolean addGroup(Roster roster,String groupName){
		try {
			roster.createGroup(groupName); 
			return true;  
		} catch (Exception e) {
			Logger.e(e);
			return false;  
		}
	}  
	
	/**
	 * ���һ������  �޷��� 
	 * @param roster
	 * @param userName
	 * @param name
	 * @return
	 */
	public static boolean addUser(Roster roster,String userName,String name){ 
		try {
			roster.createEntry(userName, name, new String[] { "unfiled entry" });
			
			return true;
		} catch (Exception e) {
			Logger.e(e);
			return false;  
		}
	}
	
	/**
	 * ���һ�����ѵ����� 
	 * @param roster
	 * @param userName
	 * @param name
	 * @param groupName
	 * @return
	 */
	public static boolean addUser(Roster roster,String userName,String name,String groupName){
		try {
			roster.createEntry(userName, name, new String[]{ groupName});  
			return true;
		} catch (Exception e) {
			Logger.e(e);
			return false;  
		}
	} 
	
	/**
	 * ɾ��һ������ 
	 * @param roster
	 * @param userName
	 * @return
	 */
	public static boolean removeUser(Roster roster,String userName){
		try {
//			if(userName.contains("@")){
//				userName = userName.split("@")[0];
//			}  
			
			RosterEntry entry = roster.getEntry(userName);
			Log.d(TAG, "ɾ�����:"+userName);
			Log.d(TAG, ("User: "+(roster.getEntry(userName) == null)));
			roster.removeEntry(entry);  
			return true;  
			
		} catch (Exception e) {
			Logger.e(e);
			return false;  
		}
	} 
	
	
	/**
	 * ��ѯ�û�
	 * @param connection
	 * @param serverDomain
	 * @param userName
	 * @return
	 * @throws XMPPException
	 */
	public static List<UserBean> searchUsers(XMPPConnection connection,String serverDomain,String userName) throws XMPPException{
		List<UserBean> results = new ArrayList<UserBean>();  
		Log.i(TAG, "��ѯ��ʼ..............."+connection.getHost()+connection.getServiceName());
		
		UserSearchManager usm = new UserSearchManager(connection);  
		Form searchForm = usm.getSearchForm(serverDomain); 
		Form answerForm = searchForm.createAnswerForm();  
		
		answerForm.setAnswer("Username", true);  
		answerForm.setAnswer("search", userName);  
		
		ReportedData data = usm.getSearchResults(answerForm, serverDomain);  
		
		Iterator<Row> it = data.getRows();  
		Row row = null;  
		UserBean user = null;  
		while(it.hasNext()){
			user = new UserBean();  
			row = it.next();  
			user.setUserName(row.getValues("Username").next().toString());  
			user.setName(row.getValues("Name").next().toString());  
			user.setEmail(row.getValues("Email").next().toString());
			user.setJid(row.getValues("jid").next().toString());
			 
			results.add(user);  
		 }
		//�����ڣ����з���,UserNameһ���ǿգ����������������裬һ���ǿ�  
		return results;  
	}  
	
	/**
	 * ��ȡ�û���vcard��Ϣ 
	 * @param connection
	 * @param user
	 * @return
	 * @throws XMPPException
	 */
	public static VCard getUserVCard(XMPPConnection connection, String user) throws XMPPException{
		VCard vcard = new VCard();  
		vcard.load(connection, user);  
		
		return vcard;  
	}
	
	/**
	 * ����������Ϣ��(���ӣ�ʵ��ʹ��������ʵ�ʴ���������ʵ��)
	 * @return
	 */
	public static MessageListener createMsgListener(){
		MessageListener msgListener = new MessageListener() {
			
			public void processMessage(Chat chat, Message msg) {
				if (msg != null && msg.getBody() != null){
					// ������������������û���Ϣ�Ĵ���
					System.out.println("�յ���Ϣ:" + msg.getBody());  
				}  	
			}
		};
		
		return msgListener;
	}
	
	/**
	 * ������ǰ�û���userName֮��ĶԻ�(���ӣ�ʵ��ʹ��������ʵ�ʴ���������ʵ��)
	 * @param connection
	 * @param userName
	 * @param msgListener
	 * @return
	 */
	public static Chat createChat(XMPPConnection connection,String userName,MessageListener msgListener){
		Chat chat = connection.getChatManager().createChat(userName, msgListener);  
		return chat;
	}
	
	/**
	 * ֱ�ӷ���һ���ı�������Է�
	 * @param chat
	 * @param message
	 * @throws XMPPException
	 */
	public static void sendMessage(Chat chat,String message) throws XMPPException {  
		chat.sendMessage(message);  
	}
	
	/**
	 * ����һ��Message����(��һЩ��Ϣ��һ��ʹ�ñ���������Ϊ��Ҫ��װ�������Ϣ)
	 * @param chat
	 * @param message
	 * @throws XMPPException
	 */
	public static void sendMessage(Chat chat,Message message) throws XMPPException { 
		chat.sendMessage(message);
	}
	
	/**
	 * ÿ��connection��chatManager��������һ����Ϣ������(���ӣ�ʵ��ʹ��������ʵ�ʴ���������ʵ��)
	 * IM����ʵ�����˶��㷢��ỰҲ�ᵯ�����ڣ����Լ�������������Ự��Ҳ���Խ������˷���ĻỰ
	 * @param connection
	 * @return
	 */
	public static ChatManager getChatManager(XMPPConnection connection){
		ChatManager manager = connection.getChatManager();  
		manager.addChatListener(new ChatManagerListener() {
			
			public void chatCreated(Chat chat, boolean arg1) {
				chat.addMessageListener(new MessageListener() {	
					public void processMessage(Chat chat, Message msg) {
						 //�������촰���Ѵ��ڣ�����Ϣת��Ŀǰ����  
						//���Ǵ��ڲ����ڣ����µĴ��ڲ�ע��  
					}
				});	
			}
		});
		
		return manager;
	}
	
	/**
	 *  һ��ʹ��Message����Ϣ���з�װ(���ӣ�ʵ��ʹ��������ʵ�ʴ���������ʵ��)
	 * @param chat �������
	 * @param size �����С
	 * @param kind ��������
	 * @param bold �Ӵ�
	 * @param italic б��
	 * @param underline �»���
	 * @param content ��������
	 * @return
	 */
	public static Message sendMessage(Chat chat,String size,String kind,String bold,String italic,String underline,String content){
		Message msg = new Message();
		msg.setProperty("size", size);
		msg.setProperty("kind", kind);
		msg.setProperty("bold", bold);  
		msg.setProperty("italic", italic);  
		msg.setProperty("underline", underline);  
		msg.setBody(content);//�������Ϣ 
				
//		chat.sendMessage(msg);  		//������Ϣ
		return msg;		
	}
	
	/**
	 * ���������޸��Լ���״̬(�������ߣ�����)
	 * Presence.Type.available			
	 * Presence.Type.unavailable
	 * @param connection
	 * @param type
	 */
	public static void updateStateToEveryone(XMPPConnection connection,Presence.Type type){
		Presence presence = new Presence(type);  
		connection.sendPacket(presence);  
	}
	
	/**
	 * ��ĳ���޸��Լ���״̬(�������ߣ�����)
	 * @param connection
	 * @param userName
	 * @param type
	 */
	public static void updateStateToUnAvailableToSomeone(XMPPConnection connection,String userName,Presence.Type type){
		Presence presence = new Presence(type);  
		presence.setTo(userName);  
		connection.sendPacket(presence);  
	}  
	
	/**
	 * �޸�����
	 * @param connection
	 * @param status
	 */
	public static void changeStateMessage(XMPPConnection connection,String status){
		Presence presence = new Presence(Presence.Type.available);
		presence.setStatus(status);  
		connection.sendPacket(presence); 
	}
	
	public void addRosterListener(XMPPConnection connection){
		Roster roster = connection.getRoster();  
		roster.addRosterListener(new RosterListener() {
			
			public void presenceChanged(Presence arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void entriesUpdated(Collection<String> arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void entriesDeleted(Collection<String> arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void entriesAdded(Collection<String> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	/**
	 * ��ȡ������Ϣ(�����ڷ���������Ϣ֮ǰȥ��ȡ������Ϣ)(���ӣ�ʵ��ʹ��������ʵ�ʴ���������ʵ��)
	 * The OfflineMessageManager helps manage offline messages even before the user has sent an available presence. 
	 * When a user asks for his offline messages before sending an available presence then the server will not send a flood with all the offline messages when the user becomes online. 
	 * The server will not send a flood with all the offline messages to the session that made the offline messages request or to any other session used by the user that becomes online.
	 */
	public static void getOfflineMessage(XMPPConnection connection,String serverDomain,Context context,MessageService messageService,String userName,String password) throws XMPPException{
		
		//��һ��:���ӣ�״̬Ҫ��Ϊ����
		ConnectionConfiguration connConfig = new ConnectionConfiguration(serverDomain);  
		connConfig.setSendPresence(false);// where connConfig is object of.
		connection = new XMPPConnection(connConfig);
		connection.connect();
		//�ڶ���:��½
		connection.login(userName, password);
		//����:��������Ϣ	
		OfflineMessageManager offlineManager = new OfflineMessageManager(connection); 
		
		try {
			Iterator<org.jivesoftware.smack.packet.Message> it = offlineManager.getMessages();
			Log.i(TAG, String.valueOf(offlineManager.supportsFlexibleRetrieval()));  
			Log.i(TAG,"������Ϣ����: " + offlineManager.getMessageCount());
			
			Map<String,ArrayList<Message>> offlineMsgs = new HashMap<String,ArrayList<Message>>();  
			while (it.hasNext()) {
				Message  message = it.next(); 	
				Log.i(TAG,"�յ�������Ϣ, Received from [" + message.getFrom()  + "] message: " + message.getBody());  
				String fromUser = message.getFrom().split("/")[0]; 
				if(offlineMsgs.containsKey(fromUser)){
					offlineMsgs.get(fromUser).add(message);  
				}else{
					ArrayList<Message> temp = new ArrayList<Message>();  
					temp.add(message);  
					offlineMsgs.put(fromUser, temp);  
				}
			}
			
			//��������д���������Ϣ����......  
			Set<String> keys = offlineMsgs.keySet();  
			Iterator<String> offIt = keys.iterator();  
			while(offIt.hasNext()){
				String key = offIt.next(); 
				ArrayList<Message> ms = offlineMsgs.get(key);  
				//�Զ�����Ϣ������չʾ
//				TelFrame tel = new TelFrame(key);  
//				ChatFrameThread cft = new ChatFrameThread(key, null);  
//				cft.setTel(tel);  
//				cft.start();
				for (int i = 0; i < ms.size(); i++) {  
//					tel.messageReceiveHandler(ms.get(i));  
					
					String messageId = ms.get(i).getPacketID();
	                String messageType = ms.get(i).getType().toString();
	                String messageSender = ms.get(i).getFrom();
	                String messageReceiver = ms.get(i).getTo();
	                String messageContent = ms.get(i).getBody();
	                
	                //�˴����Ƚ��п���Ϣ������Ϣ��Ϊ�գ�����
	                if(!"".equals(messageContent)&&messageContent!=null)
	                {
	                	  //�ѵõ�����Ϣ���б��س־û��洢
	                    MessageForUs msg =new MessageForUs();
	            		
	                    msg.setMsgContent(messageContent);
	                    msg.setLoginName(messageReceiver.substring(0,messageReceiver.indexOf("@")));
	                    msg.setMsgFrom(messageSender.substring(0,messageSender.indexOf("@")));
	                    msg.setMsgOwner(messageReceiver.substring(0,messageReceiver.indexOf("@")));
	                    msg.setMsgOwnerType(MsgOwnerType.Person);
	            		Date date = new Date();
	            		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            		
	            		msg.setMsgSendOrReceiveTime(String.valueOf(TimeFormatHelper.convertDateToLong(sFormat.format(date))));
	            		msg.setMsgType(MessageType.XMPPMsg);
	            		msg.setReadOrNotType(ReadOrNotType.New);
	            		msg.setSendOrReceiveType(SendOrReceiveType.Receive);
	            		msg.setId(messageId);
	            		msg.setMsgSendStatus(MsgSendStatus.Success);
	    				messageService.addMessage(msg);
	                    
	    				//�µõ�����ϢҪ������ʾ��ʾ
	                    Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION);
	                    intent.putExtra(Constants.MESSAGE_ID, messageId);
	                    intent.putExtra(Constants.MESSAGE_TYPE,messageType);
	                    intent.putExtra(Constants.MESSAGE_SENDER,messageSender);
	                    intent.putExtra(Constants.MESSAGE_RECEIVER,messageReceiver);
	                    intent.putExtra(Constants.MESSAGE_CONTENT, messageContent);
	                    context.sendBroadcast(intent);
	                    //xmppManager.getContext().sendBroadcast(intent);
	                }
					
					
					
				}
			}
			
			//���Ҫ��������Ϣɾ��֪ͨ������ɾ��������Ϣ,�����´�������Ϣ������
			offlineManager.deleteMessages();  

		} catch (Exception e) {
			Logger.e(e);
		}
		
		//���Ĳ�:����
		Presence presence = new Presence(Presence.Type.available);
        connection.sendPacket(presence);
	}
	
}
