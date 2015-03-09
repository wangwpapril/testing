package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.MessageForUs;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Enums.MessageType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgOwnerType;
import com.cuc.miti.phone.xmc.domain.Enums.MsgSendStatus;
import com.cuc.miti.phone.xmc.domain.Enums.ReadOrNotType;
import com.cuc.miti.phone.xmc.domain.Enums.SendOrReceiveType;
import com.cuc.miti.phone.xmc.utils.MediaHelper;

import android.content.Context;
import android.database.Cursor;

public class MessageDao {
	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;
	private Context context = null;

	public MessageDao(Context context) {
		sqlHelper = new SQLiteHelper(context, VERSION);
	}

	/*
	 * ���һ����¼
	 */
	public boolean addMessage(MessageForUs message) {
		int count = 0;
		if (message != null) {
			String sql = "insert into Message(id,msgContent,loginName,sendOrReceiveType,msgOwner,msgOwnerType,msgType,msgFrom,msgSendOrReceiveTime,readOrNotType,msgSendStatus) values(?,?,?,?,?,?,?,?,?,?,?)";
			Object[] params = { message.getId(), message.getMsgContent(),
					message.getLoginName(),
					message.getSendOrReceiveType().getValue().toString(),
					message.getMsgOwner(),
					message.getMsgOwnerType().getValue().toString(),
					message.getMsgType().getValue().toString(),
					message.getMsgFrom(), message.getMsgSendOrReceiveTime(),
					message.getReadOrNotType().getValue().toString(),
					message.getMsgSendStatus().toString() };
			count = sqlHelper.insert(sql, params);
		}
		return count == 1 ? true : false;
	}

	/**
	 * ���������Ϣ��¼(����)
	 * 
	 * @param messageList
	 * 
	 */
	public void addMessage(List<MessageForUs> messageList) {
		if (messageList != null) {
			sqlHelper.beginTransaction();
			String sql = "insert into Message(id,msgContent,loginName,sendOrReceiveType,msgOwner,msgOwnerType,msgType,msgFrom,msgSendOrReceiveTime,readOrNotType,msgSendStatus) values(?,?,?,?,?,?,?,?,?,?,?)";
			for (MessageForUs message : messageList) {
				Object[] params = { message.getId(), message.getMsgContent(),
						message.getLoginName(),
						message.getSendOrReceiveType().getValue().toString(),
						message.getMsgOwner(),
						message.getMsgOwnerType().getValue().toString(),
						message.getMsgType().getValue().toString(),
						message.getMsgFrom(),
						message.getMsgSendOrReceiveTime(),
						message.getReadOrNotType().getValue().toString(),
						message.getMsgSendStatus().toString() };
				sqlHelper.insertTransaction(sql, params);
			}

			sqlHelper.endTransaction();
		}
	}

	/**
	 * ɾ��һ�����
	 * 
	 * @param name
	 * @return
	 */
	public boolean deleteMessageById(int msg_id) {
		int count = 0;

		String sql = "delete from Message where msg_id = ?";
		String[] params = { String.valueOf(msg_id) };
		count = sqlHelper.delete(sql, params);

		return count == 1 ? true : false;
	}

	/**
	 * ���msgFrom��msgOwnerɾ��������
	 * 
	 * @param name
	 * @return
	 */
	public boolean deleteMessageByMsgFromOwner(String msgFrom, String msgOwner) {
		int count = 0;

		String sql = "delete from Message where ( msgFrom = '" + msgFrom
				+ "' and msgOwner= '" + msgOwner + "') or ( msgFrom = '"
				+ msgOwner + "' and msgOwner= '" + msgFrom + "')";
		String[] params = {};
		count = sqlHelper.delete(sql, params);

		return count == 1 ? true : false;
	}

	/**
	 * ɾ��ͬһ����
	 * 
	 * @return
	 */
	public boolean deleteSameTypeMessage(String msgType) {
		int count = 0;

		if (msgType != null) {
			String sql = "delete from Accessories where msgType = ?";
			String[] params = { msgType };
			count = sqlHelper.delete(sql, params);
		}
		return count == 1 ? true : false;
	}

	/**
	 * ����
	 * 
	 * @param accessories
	 * @return
	 */
	public boolean updateMessage(MessageForUs message) {
		int count = 0;
		if (message != null) {
			String sql = "update Message set id = ?, msgContent=? ,loginName = ? ,sendOrReceiveType=? ,msgOwner=? ,msgOwnerType=? ,msgType=? ,msgFrom=?, msgSendOrReceiveTime=?,readOrNotType=?,msgSendStatus=? where msg_id = ?";
			Object[] params = { message.getId(), message.getMsgContent(),
					message.getLoginName(),
					message.getSendOrReceiveType().getValue().toString(),
					message.getMsgOwner(),
					message.getMsgOwnerType().getValue().toString(),
					message.getMsgType().getValue().toString(),
					message.getMsgFrom(), message.getMsgSendOrReceiveTime(),
					message.getReadOrNotType().getValue().toString(),
					message.getMsgSendStatus().toString(), message.getMsg_id() };
			count = sqlHelper.update(sql, params);
		}
		return count == 1 ? true : false;
	}

	/**
	 * ��ѯһ����Ϣ
	 * 
	 * @param name
	 * @return
	 */
	public MessageForUs getMessageById(int msg_id) {
		MessageForUs message = new MessageForUs();
		;

		String sql = "select * from Message where msg_id = ? ";
		String[] params = { String.valueOf(msg_id) };
		Cursor cursor = sqlHelper.findQuery(sql, params);

		if (cursor != null && cursor.getCount() > 0) {
			message.setMsg_id((cursor.getInt(cursor.getColumnIndex("msg_id"))));
			message.setId((cursor.getString(cursor.getColumnIndex("id"))));
			message.setMsgContent((cursor.getString(cursor
					.getColumnIndex("msgContent"))));
			message.setLoginName((cursor.getString(cursor
					.getColumnIndex("loginName"))));
			message.setSendOrReceiveType(SendOrReceiveType
					.parseFromValue(cursor.getString(cursor
							.getColumnIndex("sendOrReceiveType"))));
			message.setMsgOwner((cursor.getString(cursor
					.getColumnIndex("msgOwner"))));
			message.setMsgOwnerType(MsgOwnerType.parseFromValue(cursor
					.getString(cursor.getColumnIndex("msgOwnerType"))));
			message.setMsgType(MessageType.parseFromValue(cursor
					.getString(cursor.getColumnIndex("msgType"))));
			message.setMsgFrom((cursor.getString(cursor
					.getColumnIndex("msgFrom"))));
			message.setMsgSendOrReceiveTime((cursor.getString(cursor
					.getColumnIndex("msgSendOrReceiveTime"))));
			message.setReadOrNotType(ReadOrNotType.parseFromValue(cursor
					.getString(cursor.getColumnIndex("readOrNotType"))));
			message.setMsgSendStatus(MsgSendStatus.valueOf(cursor
					.getString(cursor.getColumnIndex("msgSendStatus"))));

		}
		cursor.close();

		return message;

	}

	/**
	 * @param id
	 * @return
	 */
	/*
	 * public long getMessageCount(String msg_id){ String sql =
	 * "select count(*) from Message where msg_id = '" + msg_id + "'";
	 * 
	 * return sqlHelper.rowCount(sql); }
	 */

	/**
	 * 
	 * �����ϢsendOrReceiveType����ѯ��Ϣ�б�
	 */
	public List<MessageForUs> getMessageListBySORType(
			SendOrReceiveType sendOrReceiveType) {
		List<MessageForUs> messageList = new ArrayList<MessageForUs>();
		String sql = "select * from Message where sendOrReceiveType = ?";

		String[] params = { sendOrReceiveType.getValue().toString() };

		Cursor cursor = sqlHelper.findQuery(sql, params);

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				MessageForUs message = new MessageForUs();
				message.setMsg_id((cursor.getInt(cursor
						.getColumnIndex("msg_id"))));
				message.setId((cursor.getString(cursor.getColumnIndex("id"))));
				message.setMsgContent((cursor.getString(cursor
						.getColumnIndex("msgContent"))));
				message.setLoginName((cursor.getString(cursor
						.getColumnIndex("loginName"))));
				message.setSendOrReceiveType(SendOrReceiveType
						.parseFromValue(cursor.getString(cursor
								.getColumnIndex("sendOrReceiveType"))));
				message.setMsgOwner((cursor.getString(cursor
						.getColumnIndex("msgOwner"))));
				message.setMsgOwnerType(MsgOwnerType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("msgOwnerType"))));
				message.setMsgType(MessageType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("msgType"))));
				message.setMsgFrom((cursor.getString(cursor
						.getColumnIndex("msgFrom"))));
				message.setMsgSendOrReceiveTime((cursor.getString(cursor
						.getColumnIndex("msgSendOrReceiveTime"))));
				message.setReadOrNotType(ReadOrNotType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("readOrNotType"))));
				message.setMsgSendStatus(MsgSendStatus.valueOf(cursor
						.getString(cursor.getColumnIndex("msgSendStatus"))));

				messageList.add(message);
			}
		}
		cursor.close();
		return messageList;

	}

	/**
	 * 
	 * ��ѯ�б�
	 */
	public List<MessageForUs> getMessageList(String loginName) {
		List<MessageForUs> messageList = new ArrayList<MessageForUs>();
		String sql = "select * from Message and loginName = '" + loginName
				+ "' ";
		Cursor cursor = sqlHelper.findQuery(sql);

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				MessageForUs message = new MessageForUs();
				message.setMsg_id((cursor.getInt(cursor
						.getColumnIndex("msg_id"))));
				message.setId((cursor.getString(cursor.getColumnIndex("id"))));
				message.setMsgContent((cursor.getString(cursor
						.getColumnIndex("msgContent"))));
				message.setLoginName((cursor.getString(cursor
						.getColumnIndex("loginName"))));
				message.setSendOrReceiveType(SendOrReceiveType
						.parseFromValue(cursor.getString(cursor
								.getColumnIndex("sendOrReceiveType"))));
				message.setMsgOwner((cursor.getString(cursor
						.getColumnIndex("msgOwner"))));
				message.setMsgOwnerType(MsgOwnerType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("msgOwnerType"))));
				message.setMsgType(MessageType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("msgType"))));
				message.setMsgFrom((cursor.getString(cursor
						.getColumnIndex("msgFrom"))));
				message.setMsgSendOrReceiveTime((cursor.getString(cursor
						.getColumnIndex("msgSendOrReceiveTime"))));
				message.setReadOrNotType(ReadOrNotType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("readOrNotType"))));
				message.setMsgSendStatus(MsgSendStatus.valueOf(cursor
						.getString(cursor.getColumnIndex("msgSendStatus"))));

				messageList.add(message);
			}
		}
		cursor.close();
		return messageList;

	}

	/**
	 * 
	 * ����Ϣ���ͷ����б� GuanWei
	 */
	public List<MessageForUs> getMessageByTypeList(MessageType messageType,
			String loginName) {
		List<MessageForUs> messageList = new ArrayList<MessageForUs>();
		String sql = "select * from Message where msgType = '"
				+ messageType.getValue().toString() + "'"
				+ " and loginName = '" + loginName + "' ";

		Cursor cursor = sqlHelper.findQuery(sql);

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				MessageForUs message = new MessageForUs();
				message.setMsg_id((cursor.getInt(cursor
						.getColumnIndex("msg_id"))));
				message.setId((cursor.getString(cursor.getColumnIndex("id"))));
				message.setMsgContent((cursor.getString(cursor
						.getColumnIndex("msgContent"))));
				message.setLoginName((cursor.getString(cursor
						.getColumnIndex("loginName"))));
				message.setSendOrReceiveType(SendOrReceiveType
						.parseFromValue(cursor.getString(cursor
								.getColumnIndex("sendOrReceiveType"))));
				message.setMsgOwner((cursor.getString(cursor
						.getColumnIndex("msgOwner"))));
				message.setMsgOwnerType(MsgOwnerType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("msgOwnerType"))));
				message.setMsgType(MessageType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("msgType"))));
				message.setMsgFrom((cursor.getString(cursor
						.getColumnIndex("msgFrom"))));
				message.setMsgSendOrReceiveTime((cursor.getString(cursor
						.getColumnIndex("msgSendOrReceiveTime"))));
				message.setReadOrNotType(ReadOrNotType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("readOrNotType"))));
				message.setMsgSendStatus(MsgSendStatus.valueOf(cursor
						.getString(cursor.getColumnIndex("msgSendStatus"))));

				messageList.add(message);
			}
		}
		cursor.close();
		return messageList;

	}

	/**
	 * ��ȡָ��ҳ��ķ�ҳ���,���ص�ǰҳ��������� SQL : Select * From TABLE_NAME Limit 0,10;
	 * ��ʾ��TABLE_NAME���ȡ��ݣ��ӵ�0����ݿ�ʼ��ȥ10��
	 * 
	 * @param pager
	 *            ��ҳ����(CurrentPage-��ǰҳ��PageSize-ÿҳ����totalNum-������<����ֵ>)
	 * @return
	 */
	public List<MessageForUs> getMessageByPage(Pager pager) {

		List<MessageForUs> messageList = null;

		if (pager != null) {
			String sql = "select * from Message limit ?,?";
			int firstResult = (pager.getCurrentPage() - 1)
					* pager.getPageSize();
			int maxResult = pager.getPageSize();
			String[] params = { String.valueOf(firstResult),
					String.valueOf(maxResult) };
			Cursor cursor = sqlHelper.findQuery(sql, params);
			messageList = new ArrayList<MessageForUs>();
			if (cursor != null && cursor.getCount() > 0) {
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					MessageForUs message = new MessageForUs();
					message.setMsg_id((cursor.getInt(cursor
							.getColumnIndex("msg_id"))));
					message.setId((cursor
							.getString(cursor.getColumnIndex("id"))));
					message.setMsgContent((cursor.getString(cursor
							.getColumnIndex("msgContent"))));
					message.setLoginName((cursor.getString(cursor
							.getColumnIndex("loginName"))));
					message.setSendOrReceiveType(SendOrReceiveType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("sendOrReceiveType"))));
					message.setMsgOwner((cursor.getString(cursor
							.getColumnIndex("msgOwner"))));
					message.setMsgOwnerType(MsgOwnerType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgOwnerType"))));
					message.setMsgType(MessageType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgType"))));
					message.setMsgFrom((cursor.getString(cursor
							.getColumnIndex("msgFrom"))));
					message.setMsgSendOrReceiveTime((cursor.getString(cursor
							.getColumnIndex("msgSendOrReceiveTime"))));
					message.setReadOrNotType(ReadOrNotType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("readOrNotType"))));
					message
							.setMsgSendStatus(MsgSendStatus.valueOf(cursor
									.getString(cursor
											.getColumnIndex("msgSendStatus"))));

					messageList.add(message);
				}
			}
			cursor.close();
		}

		return messageList;

	}

	/**
	 * ��ȡ������
	 */
	public long getMessageCount(String loginName) {
		String sql = "select count(*) from Message and loginName = '"
				+ loginName + "' ";

		Cursor cursor = sqlHelper.findQuery(sql, null);
		cursor.moveToFirst();

		long returnvalue = cursor.getLong(0);
		cursor.close();
		return returnvalue;
	}

	public int getMessageNewCount(String loginName) {
		String sql = "select count(*) from Message where readOrNotType='"
				+ ReadOrNotType.New.getValue() + "' and loginName='"
				+ loginName + "' and msgFrom !=msgOwner";

		Cursor cursor = sqlHelper.findQuery(sql, null);
		cursor.moveToFirst();

		int returnvalue = cursor.getInt(0);
		cursor.close();
		return returnvalue;
	}

	public List<MessageForUs> getMessageAll(MessageType status, String loginName) {
		List<MessageForUs> messageList = null;

		// ��ѯ����
		String sql1 = "select tmp.* from (select * from Message where msgType = '"
				+ status.getValue().toString()
				+ "' and loginName = '"
				+ loginName
				+ "' and msgFrom != '"
				+ loginName
				+ "' order by msgSendOrReceiveTime asc) as tmp  group by msgFrom order by readOrNotType desc,msgSendOrReceiveTime desc ";

		Cursor cursor = sqlHelper.findQuery(sql1);

		if (cursor != null && cursor.getCount() > 0) {
			messageList = new ArrayList<MessageForUs>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {

				MessageForUs message = new MessageForUs();
				message.setMsg_id((cursor.getInt(cursor
						.getColumnIndex("msg_id"))));
				message.setId((cursor.getString(cursor.getColumnIndex("id"))));
				message.setMsgContent((cursor.getString(cursor
						.getColumnIndex("msgContent"))));
				message.setLoginName((cursor.getString(cursor
						.getColumnIndex("loginName"))));
				message.setSendOrReceiveType(SendOrReceiveType
						.parseFromValue(cursor.getString(cursor
								.getColumnIndex("sendOrReceiveType"))));
				message.setMsgOwner((cursor.getString(cursor
						.getColumnIndex("msgOwner"))));
				message.setMsgOwnerType(MsgOwnerType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("msgOwnerType"))));
				message.setMsgType(MessageType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("msgType"))));
				message.setMsgFrom((cursor.getString(cursor
						.getColumnIndex("msgFrom"))));
				message.setMsgSendOrReceiveTime((cursor.getString(cursor
						.getColumnIndex("msgSendOrReceiveTime"))));
				message.setReadOrNotType(ReadOrNotType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("readOrNotType"))));
				message.setMsgSendStatus(MsgSendStatus.valueOf(cursor
						.getString(cursor.getColumnIndex("msgSendStatus"))));

				messageList.add(message);

			}
		}
		cursor.close();
		return messageList;
	}

	public List<MessageForUs> getMessageAllForInst(MessageType status,
			String loginName) {
		List<MessageForUs> messageList = null;
		List<String> msgFromList = null;

		// ��ѯ����
		String sql1 = "select tmp.* from (select * from Message where msgType = '"
				+ status.getValue().toString()
				+ "' and loginName = '"
				+ loginName
				+ "' and msgFrom != msgOwner order by msgSendOrReceiveTime asc) as tmp  group by msgFrom , msgOwner order by readOrNotType desc,msgSendOrReceiveTime desc ";
		String sql2 = "select tmp.msgFrom from (select * from Message where msgType = '"
				+ status.getValue().toString()
				+ "' and loginName = '"
				+ loginName
				+ "' and msgFrom != '"
				+ loginName
				+ "' order by msgSendOrReceiveTime asc) as tmp  group by msgFrom order by readOrNotType desc,msgSendOrReceiveTime desc ";
		Cursor cursor_msgFrom = sqlHelper.findQuery(sql2);
		if (cursor_msgFrom != null && cursor_msgFrom.getCount() > 0) {
			msgFromList = new ArrayList<String>();
			for (cursor_msgFrom.moveToFirst(); !cursor_msgFrom.isAfterLast(); cursor_msgFrom
					.moveToNext()) {
				String messageFrom = cursor_msgFrom.getString(cursor_msgFrom
						.getColumnIndex("msgFrom"));
				msgFromList.add(messageFrom);
			}
		}
		cursor_msgFrom.close();

		Cursor cursor = sqlHelper.findQuery(sql1);

		if (cursor != null && cursor.getCount() > 0) {
			messageList = new ArrayList<MessageForUs>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				boolean equal = false;
				if (msgFromList != null) {
					for (int i = 0; i < msgFromList.size(); i++)
						if (cursor.getString(cursor.getColumnIndex("msgOwner"))
								.equals(msgFromList.get(i))) {
							equal = true;
							break;
						}
				}
				if (equal == false) {
					MessageForUs message = new MessageForUs();
					message.setMsg_id((cursor.getInt(cursor
							.getColumnIndex("msg_id"))));
					message.setId((cursor
							.getString(cursor.getColumnIndex("id"))));
					message.setMsgContent((cursor.getString(cursor
							.getColumnIndex("msgContent"))));
					message.setLoginName((cursor.getString(cursor
							.getColumnIndex("loginName"))));
					message.setSendOrReceiveType(SendOrReceiveType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("sendOrReceiveType"))));
					message.setMsgOwner((cursor.getString(cursor
							.getColumnIndex("msgOwner"))));
					message.setMsgOwnerType(MsgOwnerType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgOwnerType"))));
					message.setMsgType(MessageType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgType"))));
					message.setMsgFrom((cursor.getString(cursor
							.getColumnIndex("msgFrom"))));
					message.setMsgSendOrReceiveTime((cursor.getString(cursor
							.getColumnIndex("msgSendOrReceiveTime"))));
					message.setReadOrNotType(ReadOrNotType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("readOrNotType"))));
					message
							.setMsgSendStatus(MsgSendStatus.valueOf(cursor
									.getString(cursor
											.getColumnIndex("msgSendStatus"))));

					if (equal == false)
						messageList.add(message);
				}
			}
		}
		cursor.close();
		return messageList;
	}

	public List<MessageForUs> getMessageByMsgTypeListByPage(Pager pager,
			MessageType messageType) {
		List<MessageForUs> messageList = null;
		if (pager != null) {
			int firstResult = ((pager.getCurrentPage() - 1) * pager
					.getPageSize()); // �ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();

			// ��ѯ����
			String sql1 = "select * from Message where  msgType = '"
					+ messageType.getValue().toString() + "'";
			// ��ҳ
			String sql2 = sql1 + " limit " + String.valueOf(firstResult) + ","
					+ String.valueOf(maxResult);
			Cursor cursor = sqlHelper.findQuery(sql2);

			int i = 0;

			if (cursor != null && cursor.getCount() > 0) {
				messageList = new ArrayList<MessageForUs>();
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					MessageForUs message = new MessageForUs();
					message.setMsg_id((cursor.getInt(cursor
							.getColumnIndex("msg_id"))));
					message.setId((cursor
							.getString(cursor.getColumnIndex("id"))));
					message.setMsgContent((cursor.getString(cursor
							.getColumnIndex("msgContent"))));
					message.setLoginName((cursor.getString(cursor
							.getColumnIndex("loginName"))));
					message.setSendOrReceiveType(SendOrReceiveType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("sendOrReceiveType"))));
					message.setMsgOwner((cursor.getString(cursor
							.getColumnIndex("msgOwner"))));
					message.setMsgOwnerType(MsgOwnerType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgOwnerType"))));
					message.setMsgType(MessageType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgType"))));
					message.setMsgFrom((cursor.getString(cursor
							.getColumnIndex("msgFrom"))));
					message.setMsgSendOrReceiveTime((cursor.getString(cursor
							.getColumnIndex("msgSendOrReceiveTime"))));
					message.setReadOrNotType(ReadOrNotType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("readOrNotType"))));
					message
							.setMsgSendStatus(MsgSendStatus.valueOf(cursor
									.getString(cursor
											.getColumnIndex("msgSendStatus"))));

					i++;
					messageList.add(message);
				}
				pager.setTotalNum(sqlHelper.rowCount(sql1));
			} else {
				pager.setCurrentPage(1);
				pager.setTotalNum(0);
			}
			cursor.close();
		}
		return messageList;

	}

	public boolean getMessageNewByMsgType(MessageType messageType,
			String loginName) {
		String sql = "select count(*)  from Message where readOrNotType='"
				+ ReadOrNotType.New.getValue() + "'" + " and msgType = '"
				+ messageType.getValue().toString() + "'and loginName = '"
				+ loginName + "'and msgFrom !=msgOwner";

		Cursor cursor = sqlHelper.findQuery(sql, null);
		cursor.moveToFirst();
		if (cursor.getInt(0) > 0) {
			return true;
		}
		cursor.close();
		return false;
	}

	/**
	 * ��ȡָ��ҳ��ķ�ҳ���,���ص�ǰҳ��������� SQL : Select * From TABLE_NAME Limit 0,10;
	 * ��ʾ��TABLE_NAME���ȡ��ݣ��ӵ�0����ݿ�ʼ��ȡ10��
	 * 
	 * @param pager
	 *            ��ҳ����(CurrentPage-��ǰҳ��PageSize-ÿҳ����totalNum-������<����ֵ>)
	 * @return
	 */
	public List<MessageForUs> getMessageByPage(Pager pager, MessageType status,
			SendOrReceiveType type, String loginName) {

		List<MessageForUs> messageList = null;
		if (pager != null) {
			int firstResult = ((pager.getCurrentPage() - 1) * pager
					.getPageSize()); // �ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();

			// ��ѯ����
			String sql1 = "select * from Message where msgType = '"
					+ status.getValue().toString()
					+ "'"
					+ " and sendOrReceiveType = '"
					+ type.getValue().toString()
					+ "'"
					+ " and loginName = '"
					+ loginName
					+ "'  group by msgFrom order by readOrNotType desc, msgSendOrReceiveTime desc";
			// ��ҳ
			String sql2 = sql1 + " limit " + String.valueOf(firstResult) + ","
					+ String.valueOf(maxResult);
			Cursor cursor = sqlHelper.findQuery(sql2);

			int i = 0;

			if (cursor != null && cursor.getCount() > 0) {
				messageList = new ArrayList<MessageForUs>();
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					MessageForUs message = new MessageForUs();
					message.setMsg_id((cursor.getInt(cursor
							.getColumnIndex("msg_id"))));
					message.setId((cursor
							.getString(cursor.getColumnIndex("id"))));
					message.setMsgContent((cursor.getString(cursor
							.getColumnIndex("msgContent"))));
					message.setLoginName((cursor.getString(cursor
							.getColumnIndex("loginName"))));
					message.setSendOrReceiveType(SendOrReceiveType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("sendOrReceiveType"))));
					message.setMsgOwner((cursor.getString(cursor
							.getColumnIndex("msgOwner"))));
					message.setMsgOwnerType(MsgOwnerType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgOwnerType"))));
					message.setMsgType(MessageType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgType"))));
					message.setMsgFrom((cursor.getString(cursor
							.getColumnIndex("msgFrom"))));
					message.setMsgSendOrReceiveTime((cursor.getString(cursor
							.getColumnIndex("msgSendOrReceiveTime"))));
					message.setReadOrNotType(ReadOrNotType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("readOrNotType"))));
					message
							.setMsgSendStatus(MsgSendStatus.valueOf(cursor
									.getString(cursor
											.getColumnIndex("msgSendStatus"))));

					i++;
					messageList.add(message);
				}
				pager.setTotalNum(sqlHelper.rowCount(sql1));
			} else {
				pager.setCurrentPage(1);
				pager.setTotalNum(0);
			}
			cursor.close();
		}
		return messageList;

	}

	/**
	 * ��ȡָ��ҳ��ķ�ҳ���,���ص�ǰҳ��������� SQL : Select * From TABLE_NAME Limit 0,10;
	 * ��ʾ��TABLE_NAME���ȡ��ݣ��ӵ�0����ݿ�ʼ��ȡ10��
	 * 
	 * @param pager
	 *            ��ҳ����(CurrentPage-��ǰҳ��PageSize-ÿҳ����totalNum-������<����ֵ>)
	 * @return
	 */
	public List<MessageForUs> getMessageByPage(Pager pager, MessageType status,
			String loginName) {

		List<MessageForUs> messageList = null;
		if (pager != null) {
			int firstResult = ((pager.getCurrentPage() - 1) * pager
					.getPageSize()); // �ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();

			// ��ѯ����
			String sql1 = "select tmp.* from (select * from Message where msgType = '"
					+ status.getValue().toString()
					+ "' and loginName = '"
					+ loginName
					+ "' and msgFrom != '"
					+ loginName
					+ "' order by msgSendOrReceiveTime asc) as tmp  group by msgFrom order by readOrNotType desc,msgSendOrReceiveTime desc";

			// ��ҳ
			String sql2 = sql1 + " limit " + String.valueOf(firstResult) + ","
					+ String.valueOf(maxResult);
			Cursor cursor = sqlHelper.findQuery(sql2);

			int i = 0;

			if (cursor != null && cursor.getCount() > 0) {
				messageList = new ArrayList<MessageForUs>();
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					MessageForUs message = new MessageForUs();
					message.setMsg_id((cursor.getInt(cursor
							.getColumnIndex("msg_id"))));
					message.setId((cursor
							.getString(cursor.getColumnIndex("id"))));
					message.setMsgContent((cursor.getString(cursor
							.getColumnIndex("msgContent"))));
					message.setLoginName((cursor.getString(cursor
							.getColumnIndex("loginName"))));
					message.setSendOrReceiveType(SendOrReceiveType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("sendOrReceiveType"))));
					message.setMsgOwner((cursor.getString(cursor
							.getColumnIndex("msgOwner"))));
					message.setMsgOwnerType(MsgOwnerType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgOwnerType"))));
					message.setMsgType(MessageType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgType"))));
					message.setMsgFrom((cursor.getString(cursor
							.getColumnIndex("msgFrom"))));
					message.setMsgSendOrReceiveTime((cursor.getString(cursor
							.getColumnIndex("msgSendOrReceiveTime"))));
					message.setReadOrNotType(ReadOrNotType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("readOrNotType"))));
					message
							.setMsgSendStatus(MsgSendStatus.valueOf(cursor
									.getString(cursor
											.getColumnIndex("msgSendStatus"))));

					i++;
					messageList.add(message);
				}
				pager.setTotalNum(sqlHelper.rowCount(sql1));
			} else {
				pager.setCurrentPage(1);
				pager.setTotalNum(0);
			}
			cursor.close();
		}
		return messageList;

	}

	/**
	 * һ����Ϣ��Ϊһ����ʾ�����ǰ���һ������һ����ʾ By GuanWei ��ȡָ��ҳ��ķ�ҳ���,���ص�ǰҳ��������� SQL : Select *
	 * From TABLE_NAME Limit 0,10; ��ʾ��TABLE_NAME���ȡ��ݣ��ӵ�0����ݿ�ʼ��ȡ10��
	 * 
	 * @param pager
	 *            ��ҳ����(CurrentPage-��ǰҳ��PageSize-ÿҳ����totalNum-������<����ֵ>)
	 * @return
	 */
	public List<MessageForUs> getMessageJustByPage(Pager pager,
			MessageType status, String loginName) {

		List<MessageForUs> messageList = null;
		if (pager != null) {
			int firstResult = ((pager.getCurrentPage() - 1) * pager
					.getPageSize()); // �ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();

			// ��ѯ����
			String sql1 = "select * from Message where msgType = '"
					+ status.getValue().toString()
					+ "' and loginName = '"
					+ loginName
					+ "' and msgFrom != '"
					+ loginName
					+ "' order by readOrNotType desc, msgSendOrReceiveTime desc  ";

			// ��ҳ
			String sql2 = sql1 + " limit " + String.valueOf(firstResult) + ","
					+ String.valueOf(maxResult);
			Cursor cursor = sqlHelper.findQuery(sql2);

			int i = 0;

			if (cursor != null && cursor.getCount() > 0) {
				messageList = new ArrayList<MessageForUs>();
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					MessageForUs message = new MessageForUs();
					message.setMsg_id((cursor.getInt(cursor
							.getColumnIndex("msg_id"))));
					message.setId((cursor
							.getString(cursor.getColumnIndex("id"))));
					message.setMsgContent((cursor.getString(cursor
							.getColumnIndex("msgContent"))));
					message.setLoginName((cursor.getString(cursor
							.getColumnIndex("loginName"))));
					message.setSendOrReceiveType(SendOrReceiveType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("sendOrReceiveType"))));
					message.setMsgOwner((cursor.getString(cursor
							.getColumnIndex("msgOwner"))));
					message.setMsgOwnerType(MsgOwnerType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgOwnerType"))));
					message.setMsgType(MessageType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgType"))));
					message.setMsgFrom((cursor.getString(cursor
							.getColumnIndex("msgFrom"))));
					message.setMsgSendOrReceiveTime((cursor.getString(cursor
							.getColumnIndex("msgSendOrReceiveTime"))));
					message.setReadOrNotType(ReadOrNotType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("readOrNotType"))));
					message
							.setMsgSendStatus(MsgSendStatus.valueOf(cursor
									.getString(cursor
											.getColumnIndex("msgSendStatus"))));

					i++;
					messageList.add(message);
				}
				pager.setTotalNum(sqlHelper.rowCount(sql1));
			} else {
				pager.setCurrentPage(1);
				pager.setTotalNum(0);
			}
			cursor.close();
		}
		return messageList;

	}

	/**
	 * ������Ϣ�����߷�ҳȡ����Ϣ ��ȡָ��ҳ��ķ�ҳ���,���ص�ǰҳ��������� SQL : Select * From TABLE_NAME Limit
	 * 0,10; ��ʾ��TABLE_NAME���ȡ��ݣ��ӵ�0����ݿ�ʼ��ȡ10��
	 * 
	 * @param pager
	 *            ��ҳ����(CurrentPage-��ǰҳ��PageSize-ÿҳ����totalNum-������<����ֵ>)
	 * @return
	 */
	public List<MessageForUs> getMessageByPageAndMsgFrom(Pager pager,
			MessageType status, String msgFrom, String loginName) {

		List<MessageForUs> messageList = null;
		if (pager != null) {
			int firstResult = ((pager.getCurrentPage() - 1) * pager
					.getPageSize()); // �ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();

			// ��ѯ����
			String sql1 = "select * from Message where msgType = '"
					+ status.getValue().toString() + "'" + " and ( msgFrom = '"
					+ msgFrom + "' and msgOwner= '" + loginName
					+ "') or ( msgFrom = '" + loginName + "' and msgOwner= '"
					+ msgFrom + "') and loginName = '" + loginName
					+ "' order by msgSendOrReceiveTime desc";
			// ��ҳ
			String sql2 = sql1 + " limit " + String.valueOf(firstResult) + ","
					+ String.valueOf(maxResult);
			Cursor cursor = sqlHelper.findQuery(sql2);

			int i = 0;

			if (cursor != null && cursor.getCount() > 0) {
				messageList = new ArrayList<MessageForUs>();
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					MessageForUs message = new MessageForUs();
					message.setMsg_id((cursor.getInt(cursor
							.getColumnIndex("msg_id"))));
					message.setId((cursor
							.getString(cursor.getColumnIndex("id"))));
					message.setMsgContent((cursor.getString(cursor
							.getColumnIndex("msgContent"))));
					message.setLoginName((cursor.getString(cursor
							.getColumnIndex("loginName"))));
					message.setSendOrReceiveType(SendOrReceiveType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("sendOrReceiveType"))));
					message.setMsgOwner((cursor.getString(cursor
							.getColumnIndex("msgOwner"))));
					message.setMsgOwnerType(MsgOwnerType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgOwnerType"))));
					message.setMsgType(MessageType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgType"))));
					message.setMsgFrom((cursor.getString(cursor
							.getColumnIndex("msgFrom"))));
					message.setMsgSendOrReceiveTime((cursor.getString(cursor
							.getColumnIndex("msgSendOrReceiveTime"))));
					message.setReadOrNotType(ReadOrNotType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("readOrNotType"))));
					message
							.setMsgSendStatus(MsgSendStatus.valueOf(cursor
									.getString(cursor
											.getColumnIndex("msgSendStatus"))));

					i++;
					messageList.add(message);
				}
				pager.setTotalNum(sqlHelper.rowCount(sql1));
			} else {
				pager.setCurrentPage(1);
				pager.setTotalNum(0);
			}
			cursor.close();
		}
		return messageList;

	}

	/**
	 * By GuanWei ϵͳ��Ϣ ������Ϣ�����߷�ҳȡ����Ϣ ��ȡָ��ҳ��ķ�ҳ���,���ص�ǰҳ��������� SQL : Select * From
	 * TABLE_NAME Limit 0,10; ��ʾ��TABLE_NAME���ȡ��ݣ��ӵ�0����ݿ�ʼ��ȡ10��
	 * 
	 * @param pager
	 *            ��ҳ����(CurrentPage-��ǰҳ��PageSize-ÿҳ����totalNum-������<����ֵ>)
	 * @return
	 */
	public List<MessageForUs> getMsgByPageAndMsgFromForSys(Pager pager,
			MessageType status, String msgFrom, String loginName) {

		List<MessageForUs> messageList = null;
		if (pager != null) {
			int firstResult = ((pager.getCurrentPage() - 1) * pager
					.getPageSize()); // �ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();

			// ��ѯ����
			String sql1 = "select * from Message where msgType = '"
					+ status.getValue().toString() + "'" + " and ( msgFrom = '"
					+ msgFrom + "' and msgOwner= '" + loginName
					+ "') or ( msgFrom = '" + loginName + "' and msgOwner= '"
					+ msgFrom + "') and loginName = '" + loginName
					+ "' and msgFrom != '" + loginName
					+ "' order by msgSendOrReceiveTime desc";
			// ��ҳ
			String sql2 = sql1 + " limit " + String.valueOf(firstResult) + ","
					+ String.valueOf(maxResult);
			Cursor cursor = sqlHelper.findQuery(sql2);

			int i = 0;

			if (cursor != null && cursor.getCount() > 0) {
				messageList = new ArrayList<MessageForUs>();
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					MessageForUs message = new MessageForUs();
					message.setMsg_id((cursor.getInt(cursor
							.getColumnIndex("msg_id"))));
					message.setId((cursor
							.getString(cursor.getColumnIndex("id"))));
					message.setMsgContent((cursor.getString(cursor
							.getColumnIndex("msgContent"))));
					message.setLoginName((cursor.getString(cursor
							.getColumnIndex("loginName"))));
					message.setSendOrReceiveType(SendOrReceiveType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("sendOrReceiveType"))));
					message.setMsgOwner((cursor.getString(cursor
							.getColumnIndex("msgOwner"))));
					message.setMsgOwnerType(MsgOwnerType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgOwnerType"))));
					message.setMsgType(MessageType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgType"))));
					message.setMsgFrom((cursor.getString(cursor
							.getColumnIndex("msgFrom"))));
					message.setMsgSendOrReceiveTime((cursor.getString(cursor
							.getColumnIndex("msgSendOrReceiveTime"))));
					message.setReadOrNotType(ReadOrNotType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("readOrNotType"))));
					message
							.setMsgSendStatus(MsgSendStatus.valueOf(cursor
									.getString(cursor
											.getColumnIndex("msgSendStatus"))));

					i++;
					messageList.add(message);
				}
				pager.setTotalNum(sqlHelper.rowCount(sql1));
			} else {
				pager.setCurrentPage(1);
				pager.setTotalNum(0);
			}
			cursor.close();
		}
		return messageList;

	}

	/**
	 * By GuanWei ������Ϣ�����߷�ҳȡ����Ϣ ��ȡָ��ҳ��ķ�ҳ���,���ص�ǰҳ��������� SQL : Select * From
	 * TABLE_NAME Limit 0,10; ��ʾ��TABLE_NAME���ȡ��ݣ��ӵ�0����ݿ�ʼ��ȡ10��
	 * 
	 * @param pager
	 *            ��ҳ����(CurrentPage-��ǰҳ��PageSize-ÿҳ����totalNum-������<����ֵ>)
	 * @return
	 */
	public List<MessageForUs> getMessageByPageForRecom(Pager pager,
			MessageType status, String loginName) {

		List<MessageForUs> messageList = null;
		if (pager != null) {
			int firstResult = ((pager.getCurrentPage() - 1) * pager
					.getPageSize()); // �ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();

			// ��ѯ����
			String sql1 = "select * from Message where msgType = '"
					+ status.getValue().toString() + "'" + " and loginName = '"
					+ loginName + "' order by msgSendOrReceiveTime desc";
			// ��ҳ
			String sql2 = sql1 + " limit " + String.valueOf(firstResult) + ","
					+ String.valueOf(maxResult);
			Cursor cursor = sqlHelper.findQuery(sql2);

			int i = 0;

			if (cursor != null && cursor.getCount() > 0) {
				messageList = new ArrayList<MessageForUs>();
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					MessageForUs message = new MessageForUs();
					message.setMsg_id((cursor.getInt(cursor
							.getColumnIndex("msg_id"))));
					message.setId((cursor
							.getString(cursor.getColumnIndex("id"))));
					message.setMsgContent((cursor.getString(cursor
							.getColumnIndex("msgContent"))));
					message.setLoginName((cursor.getString(cursor
							.getColumnIndex("loginName"))));
					message.setSendOrReceiveType(SendOrReceiveType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("sendOrReceiveType"))));
					message.setMsgOwner((cursor.getString(cursor
							.getColumnIndex("msgOwner"))));
					message.setMsgOwnerType(MsgOwnerType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgOwnerType"))));
					message.setMsgType(MessageType.parseFromValue(cursor
							.getString(cursor.getColumnIndex("msgType"))));
					message.setMsgFrom((cursor.getString(cursor
							.getColumnIndex("msgFrom"))));
					message.setMsgSendOrReceiveTime((cursor.getString(cursor
							.getColumnIndex("msgSendOrReceiveTime"))));
					message.setReadOrNotType(ReadOrNotType
							.parseFromValue(cursor.getString(cursor
									.getColumnIndex("readOrNotType"))));
					message
							.setMsgSendStatus(MsgSendStatus.valueOf(cursor
									.getString(cursor
											.getColumnIndex("msgSendStatus"))));

					i++;
					messageList.add(message);
				}
				pager.setTotalNum(sqlHelper.rowCount(sql1));
			} else {
				pager.setCurrentPage(1);
				pager.setTotalNum(0);
			}
			cursor.close();
		}
		return messageList;

	}

	// 2012-7-17���msgFromȡ���䷢����������Ϣ��ѭ����ѯ�Ƿ���δ����

	public List<MessageForUs> getMessageByMsgFrom(MessageType status,
			String msgFrom, String loginName) {

		List<MessageForUs> messageList = new ArrayList<MessageForUs>();
		String sql = "select * from Message where msgType = '"
				+ status.getValue().toString() + "' and loginName = '"
				+ loginName + "' and msgFrom = '" + msgFrom
				+ "' and msgOwner='" + loginName + "'";
		Cursor cursor = sqlHelper.findQuery(sql);

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				MessageForUs message = new MessageForUs();
				message.setMsg_id((cursor.getInt(cursor
						.getColumnIndex("msg_id"))));
				message.setId((cursor.getString(cursor.getColumnIndex("id"))));
				message.setMsgContent((cursor.getString(cursor
						.getColumnIndex("msgContent"))));
				message.setLoginName((cursor.getString(cursor
						.getColumnIndex("loginName"))));
				message.setSendOrReceiveType(SendOrReceiveType
						.parseFromValue(cursor.getString(cursor
								.getColumnIndex("sendOrReceiveType"))));
				message.setMsgOwner((cursor.getString(cursor
						.getColumnIndex("msgOwner"))));
				message.setMsgOwnerType(MsgOwnerType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("msgOwnerType"))));
				message.setMsgType(MessageType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("msgType"))));
				message.setMsgFrom((cursor.getString(cursor
						.getColumnIndex("msgFrom"))));
				message.setMsgSendOrReceiveTime((cursor.getString(cursor
						.getColumnIndex("msgSendOrReceiveTime"))));
				message.setReadOrNotType(ReadOrNotType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("readOrNotType"))));
				message.setMsgSendStatus(MsgSendStatus.valueOf(cursor
						.getString(cursor.getColumnIndex("msgSendStatus"))));

				messageList.add(message);
			}
		}
		cursor.close();
		return messageList;

	}

	// By GuanWei 2012-12-15������Ϣ��ȡ���䷢����������Ϣ��ѭ����ѯ�Ƿ���δ����

	public List<MessageForUs> getMessageForRocommend(MessageType status,
			String loginName) {

		List<MessageForUs> messageList = new ArrayList<MessageForUs>();
		String sql = "select * from Message where msgType = '"
				+ status.getValue().toString() + "' and loginName = '"
				+ loginName + "' and msgOwner='" + loginName
				+ "' order by msgSendOrReceiveTime desc";
		Cursor cursor = sqlHelper.findQuery(sql);

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				MessageForUs message = new MessageForUs();
				message.setMsg_id((cursor.getInt(cursor
						.getColumnIndex("msg_id"))));
				message.setId((cursor.getString(cursor.getColumnIndex("id"))));
				message.setMsgContent((cursor.getString(cursor
						.getColumnIndex("msgContent"))));
				message.setLoginName((cursor.getString(cursor
						.getColumnIndex("loginName"))));
				message.setSendOrReceiveType(SendOrReceiveType
						.parseFromValue(cursor.getString(cursor
								.getColumnIndex("sendOrReceiveType"))));
				message.setMsgOwner((cursor.getString(cursor
						.getColumnIndex("msgOwner"))));
				message.setMsgOwnerType(MsgOwnerType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("msgOwnerType"))));
				message.setMsgType(MessageType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("msgType"))));
				message.setMsgFrom((cursor.getString(cursor
						.getColumnIndex("msgFrom"))));
				message.setMsgSendOrReceiveTime((cursor.getString(cursor
						.getColumnIndex("msgSendOrReceiveTime"))));
				message.setReadOrNotType(ReadOrNotType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("readOrNotType"))));
				message.setMsgSendStatus(MsgSendStatus.valueOf(cursor
						.getString(cursor.getColumnIndex("msgSendStatus"))));

				messageList.add(message);
			}

		}
		cursor.close();
		return messageList;

	}

	// 2012-7-17��Ϣ����ҳ��ȡ�����µ�һ�����������Լ����͵ģ�Ҳ�����ǶԷ������Լ���(������ȡ����ӦmsgForm�ı��˻ظ����б�)

	public List<MessageForUs> getMessageByMsgOwner(MessageType status,
			String msgOwner, String loginName) {
		List<MessageForUs> messageList = new ArrayList<MessageForUs>();
		String sql = "select * from Message where msgType = '"
				+ status.getValue().toString() + "' and loginName = '"
				+ loginName + "' and msgFrom = '" + loginName
				+ "' and msgOwner='" + msgOwner
				+ "' order by msgSendOrReceiveTime desc";
		Cursor cursor = sqlHelper.findQuery(sql);

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				MessageForUs message = new MessageForUs();
				message.setMsg_id((cursor.getInt(cursor
						.getColumnIndex("msg_id"))));
				message.setId((cursor.getString(cursor.getColumnIndex("id"))));
				message.setMsgContent((cursor.getString(cursor
						.getColumnIndex("msgContent"))));
				message.setLoginName((cursor.getString(cursor
						.getColumnIndex("loginName"))));
				message.setSendOrReceiveType(SendOrReceiveType
						.parseFromValue(cursor.getString(cursor
								.getColumnIndex("sendOrReceiveType"))));
				message.setMsgOwner((cursor.getString(cursor
						.getColumnIndex("msgOwner"))));
				message.setMsgOwnerType(MsgOwnerType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("msgOwnerType"))));
				message.setMsgType(MessageType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("msgType"))));
				message.setMsgFrom((cursor.getString(cursor
						.getColumnIndex("msgFrom"))));
				message.setMsgSendOrReceiveTime((cursor.getString(cursor
						.getColumnIndex("msgSendOrReceiveTime"))));
				message.setReadOrNotType(ReadOrNotType.parseFromValue(cursor
						.getString(cursor.getColumnIndex("readOrNotType"))));
				message.setMsgSendStatus(MsgSendStatus.valueOf(cursor
						.getString(cursor.getColumnIndex("msgSendStatus"))));

				messageList.add(message);
			}
		}
		cursor.close();
		return messageList;

	}

}
