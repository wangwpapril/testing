package com.cuc.miti.phone.xmc.xmpp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.OfflineMessageManager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.domain.MessageForUs;
import com.cuc.miti.phone.xmc.domain.Enums.MessageType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgOwnerType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgSendStatus;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.ReadOrNotType;
import com.cuc.miti.phone.xmc.domain.Enums.SendOrReceiveType;
import com.cuc.miti.phone.xmc.logic.MessageService;
import com.cuc.miti.phone.xmc.utils.Encrypt;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;

/**
 * XMPP�ͻ��˺ͷ�����֮�����ӽ��й�����
 *  
 * @author SongQing
 */
public class XmppManager {

    private static final String LOGTAG = LogUtil.makeLogTag(XmppManager.class);
    private static final String XMPP_RESOURCE_NAME = "AndroidpnClient";
    //private static final String XMPPHOST_STRING = "218.94.104.166";
    public static final String XMPPHOST_STRING = "chat.xinhuaenews.com";
    
    private static final int XMPPHOSTPORT_STRING = 15222;

    private Context mContext;
    private SharedPreferencesHelper spHelper;

    private NotificationService.TaskSubmitter taskSubmitter;
    private NotificationService.TaskTracker taskTracker;

    private ConnectionListener connectionListener;
    private PacketListener messagePacketListener;
    private PacketListener presencePacketListener;  
    private XMPPConnection connection;
    
    private String xmppHost;
    private int xmppPort;
    private String username;
    private String password;

    private Handler mHandler;

    private List<Runnable> taskList;

    private boolean running = false;

    private Future<?> futureTask;

    private Thread reconnection;
    
    private MessageService messageService;

    /**
     * ���캯����Ҫ����NotificationService
     * @param notificationService
     */
    public XmppManager(NotificationService notificationService) {
    	mContext = notificationService;
        taskSubmitter = notificationService.getTaskSubmitter();
        taskTracker = notificationService.getTaskTracker();

        xmppHost = XMPPHOST_STRING;
        xmppPort = XMPPHOSTPORT_STRING;
        
        spHelper = new SharedPreferencesHelper(mContext);
		
        
        username =spHelper.getPreferenceValue(PreferenceKeys.Sys_CurrentUser.toString());
        password = Encrypt.toMD5(spHelper.getPreferenceValue(PreferenceKeys.Sys_CurrentPassword.toString()));

        connectionListener = new PersistentConnectionListener(this);
        messagePacketListener = new MessagePacketListener(this);
        presencePacketListener=new PresencePacketListener(this);
        
        mHandler = new Handler();
        taskList = new ArrayList<Runnable>();
        reconnection = new ReconnectionThread(this);
        
        messageService=new MessageService(this.getContext());
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * �����������������
     */
    public void connect() {
        Log.d(LOGTAG, "connect()...");
        addTask(new ConnectTask());			//���������Ϣ���ͷ���������
        addTask(new LoginTask());				//��ӵ�¼��֤����
        runTask();
    }
    
    /**
     * �Ͽ��������������
     */
    public void disconnect() {
        Log.d(LOGTAG, "disconnect()...");
        Runnable runnable = new Runnable() {
            final XmppManager xmppManager = XmppManager.this;

            public void run() {
                if (xmppManager.isConnected()) {
                    Log.d(LOGTAG, "terminatePersistentConnection()... run()");
                    xmppManager.getConnection().removePacketListener(xmppManager.getMessagePacketListener());
                    xmppManager.getConnection().disconnect();
                }
                
            }

        };
        addTask(runnable);
        runTask();
    }

    /**
     * ���³��������ͷ�������������
     */
    public void startReconnectionThread() {
        synchronized (reconnection) {
            if (!reconnection.isAlive()) {
                reconnection.setName("Xmpp Reconnection Thread");
                reconnection.start();
            }
        }
    }

    public void runTask() {
        Log.d(LOGTAG, "runTask()...");
        synchronized (taskList) {
            running = false;
            futureTask = null;
            if (!taskList.isEmpty()) {
                Runnable runnable = (Runnable) taskList.get(0);
                taskList.remove(0);
                running = true;
                futureTask = taskSubmitter.submit(runnable);
                if (futureTask == null) {
                    taskTracker.decrease();
                }
            }
        }
        taskTracker.decrease();
        Log.d(LOGTAG, "runTask()...done");
    }

    /**
     * �ж��û��Ƿ��Ѿ������ӵ���Ϣ���ͷ�����
     * @return
     */
    private boolean isConnected() {
        return connection != null && connection.isConnected();
    }

    /**
     * �ж��û��Ƿ��Ѿ������ӵ���Ϣ���ͷ�����������֤ͨ��
     * @return
     */
    private boolean isAuthenticated() {
        return connection != null && connection.isConnected() && connection.isAuthenticated();
    }

    private void addTask(Runnable runnable) {
        Log.d(LOGTAG, "addTask(runnable)...");
        taskTracker.increase();
        synchronized (taskList) {
            if (taskList.isEmpty() && !running) {
                running = true;
                futureTask = taskSubmitter.submit(runnable);
                if (futureTask == null) {
                    taskTracker.decrease();
                }
            } else {
                taskList.add(runnable);
            }
        }
        Log.d(LOGTAG, "addTask(runnable)... done");
    }


    /**
     * A runnable task to connect the server. 
     */
    private class ConnectTask implements Runnable {

        final XmppManager xmppManager;

        private ConnectTask() {
            this.xmppManager = XmppManager.this;
        }

        public void run() {
            Log.i(LOGTAG, "ConnectTask.run()...");

            if (!xmppManager.isConnected()) {
            	/*try {
            		// packet provider
            		//ProviderManager.getInstance().addIQProvider("message",Constants.NOTIFICATION_NAMESPACE,new MessageIQProvider());
                    // Connect to the server
                	connection = SmackUtils.getConnection(xmppHost,xmppPort);
                    connection.connect();
                    xmppManager.setConnection(connection);
                   
                    Log.i(LOGTAG, "XMPP connected successfully");
                   
                } catch (XMPPException e) {
                    Log.e(LOGTAG, "XMPP connection failed", e);
                }

                xmppManager.runTask();*/
                
                
                // Create the configuration for this new connection
                ConnectionConfiguration connConfig = new ConnectionConfiguration(
                        xmppHost, xmppPort);
                // connConfig.setSecurityMode(SecurityMode.disabled);
                connConfig.setSecurityMode(SecurityMode.required);
                connConfig.setSASLAuthenticationEnabled(false);
                connConfig.setCompressionEnabled(false);
                //��һ��:���ӣ�״̬Ҫ��Ϊ����
                connConfig.setSendPresence(false);
                
                XMPPConnection connection = new XMPPConnection(connConfig);
              
                xmppManager.setConnection(connection);

                try {
                    // Connect to the server
                    connection.connect();
                    Log.i(LOGTAG, "XMPP connected successfully");

                 // packet provider
//                  ProviderManager.getInstance().addIQProvider("message",Constants.NOTIFICATION_NAMESPACE,new MessageIQProvider());


                } catch (XMPPException e) {
                    Log.e(LOGTAG, "XMPP connection failed");
                }

                //xmppManager.runTask();
                
                
                
                

            } else {
                Log.i(LOGTAG, "XMPP connected already");
                //xmppManager.runTask();
            }
        }
   
    }


    /**
     * A runnable task to log into the server. 
     */
    private class LoginTask implements Runnable {

        final XmppManager xmppManager;

        private LoginTask() {
            this.xmppManager = XmppManager.this;
        }

        public void run() {
            Log.i(LOGTAG, "LoginTask.run()...");

            if (!xmppManager.isAuthenticated()) {
                Log.d(LOGTAG, "username=" + username);
                Log.d(LOGTAG, "password=" + password);

                try {
                	//SmackUtils.Login(xmppManager.getConnection(), xmppManager.getUsername(), xmppManager.getPassword());
                	//�ڶ���:��½
                	xmppManager.getConnection().login(xmppManager.getUsername(), xmppManager.getPassword(), XMPP_RESOURCE_NAME);
                	//xmppManager.getConnection().login("testapple", Encrypt.toMD5("testapple"), XMPP_RESOURCE_NAME);
                	Log.i(LOGTAG, "Logged in successfully");
//                    xmppManager.getConnection().login(
//                            xmppManager.getUsername(),
//                            xmppManager.getPassword(), XMPP_RESOURCE_NAME);
                    // connection listener
                    if (xmppManager.getConnectionListener() != null) {
                        xmppManager.getConnection().addConnectionListener(xmppManager.getConnectionListener());
                    }
                    
                    IngleApplication.setConnection(connection);
                   /*//��½�ɹ�֮���ȡ�����б�
                    Roster roster = connection.getRoster();
                    
//                 Collection<RosterEntry> entries = roster.getEntries();
//                 Collection<RosterGroup> entriesGroup = roster.getGroups();
                                     
                     Collection<RosterEntry> entries = roster.getEntries();
                     for (RosterEntry entry : entries) {
                    	 Presence presence = roster.getPresence(entry.getUser());
                		 if(presence.isAvailable() == true){//�жϺ����Ƿ�����
                			 Log.i("---", "name: "+entry.getName() + "_OnLine");  
                         }else{
                        	 Log.i("---", "name: "+entry.getName() + "_OffLine");  
                         }
                     }
*/
                    // packet filter  the packet filter to use
//                    PacketFilter packetFilter = new PacketTypeFilter(MessageIQ.class);
                    //������ļ����Լ���Ĺ��ˣ�������Ϣ��ʱ�ͻ�㲥������ע��ļ���Ȼǰ����Ҫͨ��packetFilter�Ĺ���
                    //�ӿھ����ĸ��ر�Ľ��ᱻ���ݵ�PacketCollector��PacketListener��
                    //org.jivesoftware.smack.filter package���������Ԥ����Ĺ�������
                    //packetListener ��һ���ܹ�������ʱ���ܵ��˵�packet������,packetFilter�ܹ��ж�packet�Ƿ��ɸ�packetListener����
                    //�ǵĻ������processPacket()��������packet. ���������̴������

                    /*PacketFilter packetFilter = new PacketFilter() {
						
						@Override
						public boolean accept(Packet packet) {
							Log.i(LOGTAG, "PacketFilter --accept" + packet.toXML());
							//ToastHelper.showToast(packet.toXML(), Toast.LENGTH_SHORT);
							return true;
						}
					};*/
                    
                    PacketFilter packetMsgFilter = new PacketTypeFilter(org.jivesoftware.smack.packet.Message.class);
                    PacketFilter packetPresenceFilter = new PacketTypeFilter(org.jivesoftware.smack.packet.Presence.class);
                    
                    // packet listener  the packet listener to notify of new received packets
                    PacketListener packetMsgListener = xmppManager.getMessagePacketListener();
                    PacketListener packetPresenceListener = xmppManager.getPresencePacketListener();
                    
                    //Registers a packet listener with this connection. A packet filter determines which packets will be delivered to the listener. 
                    //If the same packet listener is added again with a different filter, only the new filter will be used. 
                    connection.addPacketListener(packetMsgListener, packetMsgFilter);
                    connection.addPacketListener(packetPresenceListener, packetPresenceFilter);
                    
                    //��½�����Ȼ�ȡ������Ϣ
                   // SmackUtils.getOfflineMessage(connection, XMPPHOST_STRING, xmppManager.getContext(), messageService,xmppManager.getUsername(), xmppManager.getPassword());
                  //����:��������Ϣ	
//            		OfflineMessageManager offlineManager = new OfflineMessageManager(connection); 
//            		
//            		try {
//            			Iterator<org.jivesoftware.smack.packet.Message> it = offlineManager.getMessages();
//            			//Log.i(LOGTAG, String.valueOf(offlineManager.supportsFlexibleRetrieval()));  
//            			Log.i(LOGTAG,"������Ϣ����: " + offlineManager.getMessageCount());
//            			
//            			Map<String,ArrayList<Message>> offlineMsgs = new HashMap<String,ArrayList<Message>>();  
//            			while (it.hasNext()) {
//            				Message  message = it.next(); 	
//            				Log.i(LOGTAG,"�յ�������Ϣ, Received from [" + message.getFrom()  + "] message: " + message.getBody());  
//            				String fromUser = message.getFrom().split("/")[0]; 
//            				if(offlineMsgs.containsKey(fromUser)){
//            					offlineMsgs.get(fromUser).add(message);  
//            				}else{
//            					ArrayList<Message> temp = new ArrayList<Message>();  
//            					temp.add(message);  
//            					offlineMsgs.put(fromUser, temp);  
//            				}
//            			}
//            			
//            			//��������д���������Ϣ����......  
//            			Set<String> keys = offlineMsgs.keySet();  
//            			Iterator<String> offIt = keys.iterator();  
//            			while(offIt.hasNext()){
//            				String key = offIt.next(); 
//            				ArrayList<Message> ms = offlineMsgs.get(key);  
//            				//�Զ�����Ϣ������չʾ
////            				TelFrame tel = new TelFrame(key);  
////            				ChatFrameThread cft = new ChatFrameThread(key, null);  
////            				cft.setTel(tel);  
////            				cft.start();
//            				for (int i = 0; i < ms.size(); i++) {  
////            					tel.messageReceiveHandler(ms.get(i));  
//            					
//            					String messageId = ms.get(i).getPacketID();
//            	                String messageType = ms.get(i).getType().toString();
//            	                String messageSender = ms.get(i).getFrom();
//            	                String messageReceiver = ms.get(i).getTo();
//            	                String messageContent = ms.get(i).getBody();
//            	                
//            	                //�˴����Ƚ��п���Ϣ������Ϣ��Ϊ�գ�����
//            	                if(!"".equals(messageContent)&&messageContent!=null)
//            	                {
//            	                	  //�ѵõ�����Ϣ���б��س־û��洢
//            	                    MessageForUs msg =new MessageForUs();
//            	            		
//            	                    msg.setMsgContent(messageContent);
//            	                    msg.setLoginName(messageReceiver.substring(0,messageReceiver.indexOf("@")));
//            	                    msg.setMsgFrom(messageSender.substring(0,messageSender.indexOf("@")));
//            	                    msg.setMsgOwner(messageReceiver.substring(0,messageReceiver.indexOf("@")));
//            	                    msg.setMsgOwnerType(MsgOwnerType.Person);
//            	            		Date date = new Date();
//            	            		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            	            		
//            	            		msg.setMsgSendOrReceiveTime(String.valueOf(TimeFormatHelper.convertDateToLong(sFormat.format(date))));
//            	            		msg.setMsgType(MessageType.InstantMsg);
//            	            		msg.setReadOrNotType(ReadOrNotType.New);
//            	            		msg.setSendOrReceiveType(SendOrReceiveType.Receive);
//            	            		msg.setId(messageId);
//            	            		msg.setMsgSendStatus(MsgSendStatus.Success);
//            	    				messageService.addMessage(msg);
//            	                    
//            	    				//�µõ�����ϢҪ������ʾ��ʾ
//            	                    Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION);
//            	                    intent.putExtra(Constants.MESSAGE_ID, messageId);
//            	                    intent.putExtra(Constants.MESSAGE_TYPE,messageType);
//            	                    intent.putExtra(Constants.MESSAGE_SENDER,messageSender);
//            	                    intent.putExtra(Constants.MESSAGE_RECEIVER,messageReceiver);
//            	                    intent.putExtra(Constants.MESSAGE_CONTENT, messageContent);
//            	                    mContext.sendBroadcast(intent);
//            	                    //xmppManager.getContext().sendBroadcast(intent);
//            	                }
//            					
//            					
//            					
//            				}
//            			}
//            			
//            			//���Ҫ��������Ϣɾ��֪ͨ������ɾ��������Ϣ,�����´�������Ϣ������
//            			offlineManager.deleteMessages();  
//
//            		} catch (Exception e) {
//            			Logger.e(e);
//            		}
                    

            		//���Ĳ�:����
            		Presence presence = new Presence(Presence.Type.available);
                    connection.sendPacket(presence);
                    
                    
                    //xmppManager.runTask();

                } catch (XMPPException e) {
                    Log.e(LOGTAG, "LoginTask.run()... xmpp error");
                    Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: "+ e.getMessage());
                    String INVALID_CREDENTIALS_ERROR_CODE = "401";
                    String errorMessage = e.getMessage();
                    if (errorMessage != null && errorMessage.contains(INVALID_CREDENTIALS_ERROR_CODE)) {
                        //xmppManager.reregisterAccount();
                        return;
                    }
                    xmppManager.startReconnectionThread();

                } catch (Exception e) {
                    Log.e(LOGTAG, "LoginTask.run()... other error");
                    Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: "+e.getMessage());
                    xmppManager.startReconnectionThread();
                }

            } else {
                Log.i(LOGTAG, "Logged in already");
               // xmppManager.runTask();
            }
        }
    }
    
    public XMPPConnection getConnection() {
        return connection;
    }
    public void setConnection(XMPPConnection connection) {
        this.connection = connection;
    }
   
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ConnectionListener getConnectionListener() {
        return connectionListener;
    }
    public PacketListener getMessagePacketListener() {
        return messagePacketListener;
    }
    
    public PacketListener getPresencePacketListener() {
		return presencePacketListener;
	}

	public Handler getHandler() {
        return mHandler;
    }
    public List<Runnable> getTaskList() {
        return taskList;
    }
    public Future<?> getFutureTask() {
        return futureTask;
    }
}
