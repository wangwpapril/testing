package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.Enums.ManuscriptStatus;
import com.cuc.miti.phone.xmc.domain.Enums.XmcBool;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.utils.TimeFormatHelper;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;

public class ManuscriptsDao {

	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;
	private Context mContext = null;

	public ManuscriptsDao(Context context) {
		this.mContext = context;
		sqlHelper = new SQLiteHelper(context, VERSION);
	}

	/**
	 * ����һ���û���Ϣ
	 * 
	 * @return �ɹ�����true��ʧ�ܷ���false
	 */
	public boolean addManuscripts(Manuscripts manuscripts) {
		int count = 0;
		String sql = "INSERT INTO Manuscripts (m_id,loginname,createid,releid,newsid,title,title3T,usernameC,usernameE,groupnameC,groupcode,groupnameE,newstype,newstypeID,comefromDept,comefromDeptID,provtype,provtypeID,doctype,doctypeID,keywords,[language],languageID,priority,priorityID,sendarea,happenplace,reportplace,address,addressID,comment,is3Tnews,createtime,rejecttime,reletime,senttime,rereletime,reviewstatus,region,regionID,contents,contents3T,receivetime,newsIDBacktime,manuscriptsStatus,location,createtime,author) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		ManuscriptTemplate template = manuscripts.getManuscriptTemplate();
		if (template == null)
			template = new ManuscriptTemplate();

		Object[] params = { manuscripts.getM_id(), manuscripts.getLoginname(),
				manuscripts.getCreateid(), manuscripts.getReleid(),
				manuscripts.getNewsid(), manuscripts.getTitle(),
				manuscripts.getTitle3T(), manuscripts.getUsernameC(),
				manuscripts.getUsernameE(), manuscripts.getGroupnameC(),
				manuscripts.getGroupcode(), manuscripts.getGroupnameE(),
				manuscripts.getNewstype(), manuscripts.getNewstypeID(),
				template.getComefromDept(), template.getComefromDeptID(),
				template.getProvtype(), template.getProvtypeID(),
				template.getDoctype(), template.getDoctypeID(),
				template.getKeywords(), template.getLanguage(),
				template.getLanguageID(), template.getPriority(),
				template.getPriorityID(), template.getSendarea(),
				template.getHappenplace(), template.getReportplace(),
				template.getAddress(), template.getAddressID(),
				manuscripts.getComment(), template.getIs3Tnews().getValue(),
				manuscripts.getCreatetime(), manuscripts.getRejecttime(),
				manuscripts.getReletime(), manuscripts.getSenttime(),
				manuscripts.getRereletime(), template.getReviewstatus(),
				template.getRegion(), template.getRegionID(),
				manuscripts.getContents(), manuscripts.getContents3T(),
				manuscripts.getReceiveTime(), manuscripts.getNewsIDBackTime(),
				manuscripts.getManuscriptsStatus().toString(),
				manuscripts.getLocation(), manuscripts.getCreatetime(),
				manuscripts.getAuthor() };
		count = sqlHelper.insert(sql, params);
		return count == 1 ? true : false;
	}

	/**
	 * ��ݴ�����û���ɾ����ݿ���е��û���Ϣ
	 * 
	 * @param username
	 * @return �ɹ�����true��ʧ�ܷ���false
	 */
	public boolean deleteManuscripts(String loginname) {
		int count = 0;
		if (loginname != null) {
			String sql = "DELETE FROM Manuscripts WHERE loginname = ?";
			String[] params = { loginname };

			count = sqlHelper.delete(sql, params);
		}

		return count == 1 ? true : false;
	}

	/**
	 * ɾ������
	 * 
	 * @return
	 */
	public boolean deleteAllManuscripts() {
		String sql = "DELETE FROM Manuscripts";
		sqlHelper.deleteAll(sql);
		return true;

	}

	/**
	 * ��ݸ��IDɾ������Ϣ
	 * 
	 * @param id
	 *            ���ID
	 * @return �ɹ�����true��ʧ�ܷ���false
	 */
	public boolean deleteById(String id) {
		int count = 0;
		if (id != null && id != "") {
			String sql = "DELETE FROM Manuscripts WHERE m_id = ?";
			String[] params = { id };

			count = sqlHelper.delete(sql, params);
		}

		return count == 1 ? true : false;
	}

	/**
	 * ��ѯһ����Ϣ
	 */
	public Manuscripts getManuscripts(String m_id) {
		// to do where loginname=?
		Manuscripts manuscripts = null;
		if (m_id != null) {
			String sql = "select * from Manuscripts where m_id = ?";
			String[] params = { m_id };
			Cursor cursor = sqlHelper.findQuery(sql, params);

			if (cursor != null && cursor.getCount() > 0) {
				manuscripts = new Manuscripts();
				ManuscriptTemplate manuscriptTemplate = new ManuscriptTemplate();
				manuscriptTemplate.setLoginname(cursor.getString(cursor
						.getColumnIndex("loginname")));
				manuscriptTemplate.setComefromDept(cursor.getString(cursor
						.getColumnIndex("comefromDept")));
				manuscriptTemplate.setComefromDeptID(cursor.getString(cursor
						.getColumnIndex("comefromDeptID")));
				manuscriptTemplate.setProvtype(cursor.getString(cursor
						.getColumnIndex("provtype")));
				manuscriptTemplate.setProvtypeID(cursor.getString(cursor
						.getColumnIndex("provtypeID")));
				manuscriptTemplate.setDoctype(cursor.getString(cursor
						.getColumnIndex("doctype")));
				manuscriptTemplate.setDoctypeID(cursor.getString(cursor
						.getColumnIndex("doctypeID")));
				manuscriptTemplate.setKeywords(cursor.getString(cursor
						.getColumnIndex("keywords")));
				manuscriptTemplate.setLanguage(cursor.getString(cursor
						.getColumnIndex("language")));
				manuscriptTemplate.setLanguageID(cursor.getString(cursor
						.getColumnIndex("languageID")));
				manuscriptTemplate.setPriority(cursor.getString(cursor
						.getColumnIndex("priority")));
				manuscriptTemplate.setPriorityID(cursor.getString(cursor
						.getColumnIndex("priorityID")));
				manuscriptTemplate.setSendarea(cursor.getString(cursor
						.getColumnIndex("sendarea")));
				manuscriptTemplate.setHappenplace(cursor.getString(cursor
						.getColumnIndex("happenplace")));
				manuscriptTemplate.setReportplace(cursor.getString(cursor
						.getColumnIndex("reportplace")));
				manuscriptTemplate.setAddress(cursor.getString(cursor
						.getColumnIndex("address")));
				manuscriptTemplate.setAddressID(cursor.getString(cursor
						.getColumnIndex("addressID")));
				manuscriptTemplate.setIs3Tnews(XmcBool.parseFromValue(cursor
						.getString(cursor.getColumnIndex("is3Tnews"))));
				manuscriptTemplate.setCreatetime(cursor.getString(cursor
						.getColumnIndex("createtime")));
				manuscriptTemplate.setReviewstatus(cursor.getString(cursor
						.getColumnIndex("reviewstatus")));
				manuscriptTemplate.setRegion(cursor.getString(cursor
						.getColumnIndex("region")));
				manuscriptTemplate.setRegionID(cursor.getString(cursor
						.getColumnIndex("regionID")));
				manuscriptTemplate.setAuthor(cursor.getString(cursor
						.getColumnIndex("author")));
				manuscripts.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
				manuscripts.setComment(cursor.getString(cursor
						.getColumnIndex("comment")));
				manuscripts.setRejecttime(cursor.getString(cursor
						.getColumnIndex("rejecttime")));
				manuscripts.setReletime(cursor.getString(cursor
						.getColumnIndex("reletime")));
				manuscripts.setSenttime(cursor.getString(cursor
						.getColumnIndex("senttime")));
				manuscripts.setReletime(cursor.getString(cursor
						.getColumnIndex("reletime")));
				manuscripts.setContents(cursor.getString(cursor
						.getColumnIndex("contents")));
				manuscripts.setContents3T(cursor.getString(cursor
						.getColumnIndex("contents3T")));
				manuscripts.setReceiveTime(cursor.getString(cursor
						.getColumnIndex("receiveTime")));
				manuscripts.setNewsIDBackTime(cursor.getString(cursor
						.getColumnIndex("newsIDBackTime")));
				manuscripts.setManuscriptsStatus(ManuscriptStatus
						.valueOf(cursor.getString(cursor
								.getColumnIndex("manuscriptsStatus"))));
				manuscripts.setM_id(cursor.getString(cursor
						.getColumnIndex("m_id")));
				manuscripts.setLocation(cursor.getString(cursor
						.getColumnIndex("location")));
				manuscripts.setCreateid(cursor.getString(cursor
						.getColumnIndex("createid")));
				manuscripts.setReleid(cursor.getString(cursor
						.getColumnIndex("releid")));
				manuscripts.setNewsid(cursor.getString(cursor
						.getColumnIndex("newsid")));
				manuscripts.setTitle(cursor.getString(cursor
						.getColumnIndex("title")));
				manuscripts.setTitle3T(cursor.getString(cursor
						.getColumnIndex("title3T")));
				manuscripts.setUsernameC(cursor.getString(cursor
						.getColumnIndex("usernameC")));
				manuscripts.setUsernameE(cursor.getString(cursor
						.getColumnIndex("usernameE")));
				manuscripts.setGroupnameC(cursor.getString(cursor
						.getColumnIndex("groupnameC")));
				manuscripts.setGroupcode(cursor.getString(cursor
						.getColumnIndex("groupcode")));
				manuscripts.setGroupnameE(cursor.getString(cursor
						.getColumnIndex("groupnameE")));
				manuscripts.setNewstype(cursor.getString(cursor
						.getColumnIndex("newstype")));
				manuscripts.setNewstypeID(cursor.getString(cursor
						.getColumnIndex("newstypeID")));
				manuscripts.setRereletime(cursor.getString(cursor
						.getColumnIndex("rereletime")));
				manuscripts.setCreatetime(cursor.getString(cursor
						.getColumnIndex("createtime")));
				manuscripts.setAuthor(cursor.getString(cursor
						.getColumnIndex("author")));
				manuscripts.setLoginname(cursor.getString(cursor
						.getColumnIndex("loginname")));

				AccessoriesDao service = new AccessoriesDao(this.mContext);

				// ����Ԥ��ͼ
				List<Accessories> list = service
						.getAccessoriesListByMID(manuscripts.getM_id());

				if (list != null && list.size() > 0) {
					Accessories acc = list.get(0);

					// ���õ�һ��������ͼ��
					manuscripts.setPreViewImage(MediaHelper.createItemImage(
							acc.getUrl(), this.mContext, 100, 70));
					manuscripts.setHasAccessories(true);
				} else {
					manuscripts.setHasAccessories(false);
				}

				manuscripts.setManuscriptTemplate(manuscriptTemplate);
			}
			cursor.close();
		}
		
		return manuscripts;
	}

	/**
	
	 */
	public boolean updateManuscripts(Manuscripts manuscripts) {
		int count = 0;
		if (manuscripts != null) {
			String sql = "UPDATE Manuscripts SET createid = ?, releid = ?,newsid = ?,title = ?,title3T = ?,usernameC = ?,usernameE = ?,groupnameC = ?,groupcode = ?,groupnameE = ?,newstype = ?,newstypeID = ?,comment = ?,rejecttime = ?,reletime = ?,senttime = ?,rereletime = ?,contents = ?,contents3T = ?,receivetime = ?,newsIDBacktime = ?,manuscriptsStatus= ?,location=? ,loginname = ?,comefromDept = ?,comefromDeptID = ?,provtype = ?,provtypeID = ?,doctype = ?,doctypeID = ?,keywords = ?,[language] = ?,languageID = ?,	priority = ?,priorityID = ?,sendarea = ?,happenplace = ?,reportplace = ?,address = ?,addressID = ?,is3Tnews = ?,reviewstatus = ?,region = ?,regionID = ?,createtime=?,author=? WHERE  m_id = ?";

			ManuscriptTemplate template = manuscripts.getManuscriptTemplate();

			if (template == null)
				template = new ManuscriptTemplate();

			String[] params = { manuscripts.getCreateid(),
					manuscripts.getReleid(), manuscripts.getNewsid(),
					manuscripts.getTitle(), manuscripts.getTitle3T(),
					manuscripts.getUsernameC(), manuscripts.getUsernameE(),
					manuscripts.getGroupnameC(), manuscripts.getGroupcode(),
					manuscripts.getGroupnameE(), manuscripts.getNewstype(),
					manuscripts.getNewstypeID(), manuscripts.getComment(),
					manuscripts.getRejecttime(), manuscripts.getReletime(),
					manuscripts.getSenttime(), manuscripts.getRereletime(),
					manuscripts.getContents(), manuscripts.getContents3T(),
					manuscripts.getReceiveTime(),
					manuscripts.getNewsIDBackTime(),
					manuscripts.getManuscriptsStatus().toString(),
					manuscripts.getLocation(), manuscripts.getLoginname(),
					template.getComefromDept(), template.getComefromDeptID(),
					template.getProvtype(), template.getProvtypeID(),
					template.getDoctype(), template.getDoctypeID(),
					template.getKeywords(), template.getLanguage(),
					template.getLanguageID(), template.getPriority(),
					template.getPriorityID(), template.getSendarea(),
					template.getHappenplace(), template.getReportplace(),
					template.getAddress(), template.getAddressID(),
					template.getIs3Tnews().getValue(),
					template.getReviewstatus(),
					template.getRegion(), template.getRegionID(),
					manuscripts.getCreatetime(), manuscripts.getAuthor(),
					manuscripts.getM_id() };
			count = sqlHelper.update(sql, params);
		}
		return count == 1 ? true : false;
	}

	/**
	 * ��ȡ��ǰ��������б�����û��б�
	 * 
	 * @return �ɹ�����List��ʧ�ܷ���null
	 */
	public List<Manuscripts> getManuscriptsList(String loginname) {
		String sql = "select * from Manuscripts where loginname=?";
		String[] params = { loginname };
		Cursor cursor = sqlHelper.findQuery(sql, params);
		List<Manuscripts> manuscriptsList = null;

		if (cursor != null && cursor.getCount() > 0) {
			manuscriptsList = new ArrayList<Manuscripts>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Manuscripts manuscripts = new Manuscripts();
				ManuscriptTemplate manuscriptTemplate = new ManuscriptTemplate();

				manuscriptTemplate.setLoginname(cursor.getString(cursor
						.getColumnIndex("loginname")));
				manuscripts.setCreateid(cursor.getString(cursor
						.getColumnIndex("createid")));
				manuscripts.setReleid(cursor.getString(cursor
						.getColumnIndex("releid")));
				manuscripts.setNewsid(cursor.getString(cursor
						.getColumnIndex("newsid")));
				manuscripts.setTitle(cursor.getString(cursor
						.getColumnIndex("title")));
				manuscripts.setTitle3T(cursor.getString(cursor
						.getColumnIndex("title3T")));
				manuscripts.setUsernameC(cursor.getString(cursor
						.getColumnIndex("usernameC")));
				manuscripts.setUsernameE(cursor.getString(cursor
						.getColumnIndex("usernameE")));
				manuscripts.setGroupnameC(cursor.getString(cursor
						.getColumnIndex("groupnameC")));
				manuscripts.setGroupcode(cursor.getString(cursor
						.getColumnIndex("groupcode")));
				manuscripts.setGroupnameE(cursor.getString(cursor
						.getColumnIndex("groupnameE")));
				manuscripts.setNewstype(cursor.getString(cursor
						.getColumnIndex("newstype")));
				manuscripts.setNewstypeID(cursor.getString(cursor
						.getColumnIndex("newstypeID")));
				manuscriptTemplate.setComefromDept(cursor.getString(cursor
						.getColumnIndex("comefromDept")));
				manuscriptTemplate.setComefromDeptID(cursor.getString(cursor
						.getColumnIndex("comefromDeptID")));
				manuscriptTemplate.setProvtype(cursor.getString(cursor
						.getColumnIndex("provtype")));
				manuscriptTemplate.setProvtypeID(cursor.getString(cursor
						.getColumnIndex("provtypeID")));
				manuscriptTemplate.setDoctype(cursor.getString(cursor
						.getColumnIndex("doctype")));
				manuscriptTemplate.setDoctypeID(cursor.getString(cursor
						.getColumnIndex("doctypeID")));
				manuscriptTemplate.setKeywords(cursor.getString(cursor
						.getColumnIndex("keywords")));
				manuscriptTemplate.setLanguage(cursor.getString(cursor
						.getColumnIndex("language")));
				manuscriptTemplate.setLanguageID(cursor.getString(cursor
						.getColumnIndex("languageID")));
				manuscriptTemplate.setPriority(cursor.getString(cursor
						.getColumnIndex("priority")));
				manuscriptTemplate.setPriorityID(cursor.getString(cursor
						.getColumnIndex("priorityID")));
				manuscriptTemplate.setSendarea(cursor.getString(cursor
						.getColumnIndex("sendarea")));
				manuscriptTemplate.setHappenplace(cursor.getString(cursor
						.getColumnIndex("happenplace")));
				manuscriptTemplate.setReportplace(cursor.getString(cursor
						.getColumnIndex("reportplace")));
				manuscriptTemplate.setAddress(cursor.getString(cursor
						.getColumnIndex("address")));
				manuscriptTemplate.setAddressID(cursor.getString(cursor
						.getColumnIndex("addressID")));
				manuscripts.setComment(cursor.getString(cursor
						.getColumnIndex("comment")));
				manuscriptTemplate.setIs3Tnews(XmcBool.parseFromValue(cursor
						.getString(cursor.getColumnIndex("is3Tnews"))));
				manuscriptTemplate.setCreatetime(cursor.getString(cursor
						
						.getColumnIndex("createtime")));
				manuscripts.setRejecttime(cursor.getString(cursor
						.getColumnIndex("rejecttime")));
				manuscripts.setReletime(cursor.getString(cursor
						.getColumnIndex("reletime")));
				manuscripts.setSenttime(cursor.getString(cursor
						.getColumnIndex("senttime")));
				manuscripts.setReletime(cursor.getString(cursor
						.getColumnIndex("reletime")));
				manuscripts.setLoginname(cursor.getString(cursor
						.getColumnIndex("loginname")));
				manuscriptTemplate.setReviewstatus(cursor.getString(cursor
						.getColumnIndex("reviewstatus")));
				manuscriptTemplate.setRegion(cursor.getString(cursor
						.getColumnIndex("region")));
				manuscriptTemplate.setRegionID(cursor.getString(cursor
						.getColumnIndex("regionID")));
				manuscripts.setContents(cursor.getString(cursor
						.getColumnIndex("contents")));
				manuscripts.setContents3T(cursor.getString(cursor
						.getColumnIndex("contents3T")));
				manuscripts.setReceiveTime(cursor.getString(cursor
						.getColumnIndex("receiveTime")));
				manuscripts.setNewsIDBackTime(cursor.getString(cursor
						.getColumnIndex("newsIDBackTime")));
				manuscripts.setManuscriptsStatus(ManuscriptStatus
						.valueOf(cursor.getString(cursor
								.getColumnIndex("manuscriptsStatus"))));
				manuscripts.setM_id(cursor.getString(cursor
						.getColumnIndex("m_id")));
				manuscripts.setLocation(cursor.getString(cursor
						.getColumnIndex("location")));
				manuscripts.setCreatetime(cursor.getString(cursor
						.getColumnIndex("createtime")));
				manuscripts.setAuthor(cursor.getString(cursor
						.getColumnIndex("author")));
				manuscriptTemplate.setAuthor(cursor.getString(cursor
						.getColumnIndex("author")));

				manuscripts.setManuscriptTemplate(manuscriptTemplate);
				manuscriptsList.add(manuscripts);
			}

		}
		cursor.close();
		return manuscriptsList;
	}

	/**
	 * ��ȡ������
	 * 
	 */
	public int getManuscriptsCount() {
		String sql = "select count(*) from Manuscripts";

		Cursor cursor = sqlHelper.findQuery(sql, null);
		cursor.moveToFirst();

		int returnvalue=cursor.getInt(0);
		cursor.close();
		return returnvalue;
	}

	/**
	 * ��ȡָ��ҳ��ķ�ҳ���,���ص�ǰҳ��������� SQL : Select * From TABLE_NAME Limit 0,10;
	 * ��ʾ��TABLE_NAME���ȡ��ݣ��ӵ�0����ݿ�ʼ��ȡ10��
	 * 
	 * @param pager
	 *            ��ҳ����(CurrentPage-��ǰҳ��PageSize-ÿҳ����totalNum-������<����ֵ>)
	 * @return
	 */
	public List<Manuscripts> getManuscriptsByPage(Pager pager, String title,
			ManuscriptStatus status, String loginName, boolean fullObject) {

		List<Manuscripts> manuscriptsList = null;
		if (pager != null) {
			int firstResult = ((pager.getCurrentPage() - 1) * pager
					.getPageSize()); // �ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize();

			// ��ѯ����
			String sql1 = "select * from Manuscripts where title like '%"
					+ title + "%'" + " and manuscriptsStatus = '"
					+ status.toString() + "'" + " and loginname = '"
					+ loginName + "'"; 
			
			//����
			switch (status) {
			case Elimination:
				sql1 = sql1.concat(" order by rereletime desc");
				break;
			case StandTo:
				sql1 = sql1.concat(" order by reletime desc");
				break;
			case Sent:
				sql1 = sql1.concat(" order by senttime desc");
				break;
			case Editing:
				sql1 = sql1.concat(" order by createtime desc");
				break;
			default:
				break;
			}
			// ��ҳ
			String sql2 = sql1 + " limit " + String.valueOf(firstResult) + ","
					+ String.valueOf(maxResult);
			Cursor cursor = sqlHelper.findQuery(sql2);

			int i = 0;

			if (cursor != null && cursor.getCount() > 0) {
				manuscriptsList = new ArrayList<Manuscripts>();
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					Manuscripts manuscripts = new Manuscripts();
					ManuscriptTemplate manuscriptTemplate = new ManuscriptTemplate();

					// manuscriptTemplate.setLoginname(cursor.getString(cursor
					// .getColumnIndex("loginname")));
					// manuscripts.setCreateid(cursor.getString(cursor
					// .getColumnIndex("createid")));
					// manuscripts.setReleid(cursor.getString(cursor
					// .getColumnIndex("releid")));
					// manuscripts.setNewsid(cursor.getString(cursor
					// .getColumnIndex("newsid")));
					manuscripts.setTitle(cursor.getString(cursor
							.getColumnIndex("title")));
					// manuscripts.setTitle3T(cursor.getString(cursor
					// .getColumnIndex("title3T")));
					// manuscripts.setUsernameC(cursor.getString(cursor
					// .getColumnIndex("usernameC")));
					// manuscripts.setUsernameE(cursor.getString(cursor
					// .getColumnIndex("usernameE")));
					// manuscripts.setGroupnameC(cursor.getString(cursor
					// .getColumnIndex("groupnameC")));
					// manuscripts.setGroupcode(cursor.getString(cursor
					// .getColumnIndex("groupcode")));
					// manuscripts.setGroupnameE(cursor.getString(cursor
					// .getColumnIndex("groupnameE")));
					// manuscripts.setNewstype(cursor.getString(cursor
					// .getColumnIndex("newstype")));
					// manuscripts.setNewstypeID(cursor.getString(cursor
					// .getColumnIndex("newstypeID")));
					// manuscriptTemplate.setComefromDept(cursor.getString(cursor
					// .getColumnIndex("comefromDept")));
					// manuscriptTemplate
					// .setComefromDeptID(cursor.getString(cursor
					// .getColumnIndex("comefromDeptID")));
					// manuscriptTemplate.setProvtype(cursor.getString(cursor
					// .getColumnIndex("provtype")));
					// manuscriptTemplate.setProvtypeID(cursor.getString(cursor
					// .getColumnIndex("provtypeID")));
					// manuscriptTemplate.setDoctype(cursor.getString(cursor
					// .getColumnIndex("doctype")));
					// manuscriptTemplate.setDoctypeID(cursor.getString(cursor
					// .getColumnIndex("doctypeID")));
					// manuscriptTemplate.setKeywords(cursor.getString(cursor
					// .getColumnIndex("keywords")));
					// manuscriptTemplate.setLanguage(cursor.getString(cursor
					// .getColumnIndex("language")));
					// manuscriptTemplate.setLanguageID(cursor.getString(cursor
					// .getColumnIndex("languageID")));
					// manuscriptTemplate.setPriority(cursor.getString(cursor
					// .getColumnIndex("priority")));
					// manuscriptTemplate.setPriorityID(cursor.getString(cursor
					// .getColumnIndex("priorityID")));
					// manuscriptTemplate.setSendarea(cursor.getString(cursor
					// .getColumnIndex("sendarea")));
					// manuscriptTemplate.setHappenplace(cursor.getString(cursor
					// .getColumnIndex("happenplace")));
					// manuscriptTemplate.setReportplace(cursor.getString(cursor
					// .getColumnIndex("reportplace")));
					// manuscriptTemplate.setAddress(cursor.getString(cursor
					// .getColumnIndex("address")));
					// manuscriptTemplate.setAddressID(cursor.getString(cursor
					// .getColumnIndex("addressID")));
					// manuscripts.setComment(cursor.getString(cursor
					// .getColumnIndex("comment")));
					// manuscriptTemplate.setIs3Tnews(cursor.getString(cursor
					// .getColumnIndex("is3Tnews")));
					/*
					 * manuscriptTemplate.setCreatetime(cursor.getString(cursor
					 * .getColumnIndex("createtime")));
					 */
					manuscripts.setRejecttime(cursor.getString(cursor
							.getColumnIndex("rejecttime")));
					manuscripts.setReletime(cursor.getString(cursor
							.getColumnIndex("reletime")));
					manuscripts.setSenttime(cursor.getString(cursor
							.getColumnIndex("senttime")));
					manuscripts.setRereletime(cursor.getString(cursor
							.getColumnIndex("rereletime")));
					// manuscriptTemplate.setReviewstatus(cursor.getString(cursor
					// .getColumnIndex("reviewstatus")));
					// manuscriptTemplate.setRegion(cursor.getString(cursor
					// .getColumnIndex("region")));
					// manuscriptTemplate.setRegionID(cursor.getString(cursor
					// .getColumnIndex("regionID")));
					manuscripts.setContents(cursor.getString(cursor
							.getColumnIndex("contents")));
					// manuscripts.setContents3T(cursor.getString(cursor
					// .getColumnIndex("contents3T")));
					manuscripts.setReceiveTime(cursor.getString(cursor
							.getColumnIndex("receiveTime")));
					// manuscripts.setNewsIDBackTime(cursor.getString(cursor
					// .getColumnIndex("newsIDBackTime")));
					manuscripts.setManuscriptsStatus(ManuscriptStatus
							.valueOf(cursor.getString(cursor
									.getColumnIndex("manuscriptsStatus"))));
					manuscripts.setM_id(cursor.getString(cursor
							.getColumnIndex("m_id")));
					// manuscripts.setLocation(cursor.getString(cursor
					// .getColumnIndex("location")));
					manuscripts.setCreatetime(cursor.getString(cursor
							.getColumnIndex("createtime")));
					/*
					 * manuscripts.setAuthor(cursor.getString(cursor
					 * .getColumnIndex("author")));
					 */
					manuscripts.setManuscriptTemplate(manuscriptTemplate);

					AccessoriesDao service = new AccessoriesDao(this.mContext);
					// long count =
					// service.getAccessoriesCount(manuscripts.getM_id());
					List<Accessories> list = service
							.getAccessoriesListByMID(manuscripts.getM_id());

					manuscripts
							.setHasAccessories((list != null && list.size() > 0) ? true
									: false);

					// // ����Ԥ��ͼ
					if (fullObject == true) {
						// AccessoriesDao service = new AccessoriesDao(
						// this.mContext);
						// List<Accessories> list = service
						// .getAccessoriesListByMID(manuscripts.getM_id());
						//
						// if (list != null && list.size() > 0) {
						// Accessories acc = list.get(0);
						//
						// // ���õ�һ��������ͼ��
						// manuscripts.setPreViewImage(MediaHelper
						// .createItemImage(acc.getUrl(),
						// this.mContext, 100, 70));
						// }
						manuscripts.setPreViewImage(MediaHelper
								.getManuscriptPreview(manuscripts.getM_id(),
										this.mContext));
					}

					i++;

					manuscriptsList.add(manuscripts);
				}
				pager.setTotalNum(sqlHelper.rowCount(sql1));
			} else {
				pager.setCurrentPage(1);
				pager.setTotalNum(0);
			}
			cursor.close();
		}
		
		return manuscriptsList;

	}

	/**
	 * ���¸��״̬
	 * 
	 * @param ��״̬��Ϣ
	 * @return
	 */
	public boolean updateStatus(String m_id, ManuscriptStatus status) {
		int count = -1;

		String sql = "update Manuscripts set manuscriptsStatus = ? ";

		switch (status) {
		case Elimination:
			sql = sql.concat(", rereletime = ? ");
			break;
		case StandTo:
			sql = sql.concat(", reletime = ? ");
			break;
		case Sent:
			sql = sql.concat(", senttime = ? ");
			break;
		default:
			break;
		}

		sql = sql.concat("where m_id = ?");

		String[] params = null;

		switch (status) {
		case Elimination:
		case StandTo:
		case Sent:
			params = new String[] { status.toString(),
					TimeFormatHelper.getFormatTime(new Date()), m_id };
			break;
		default:
			params = new String[] { status.toString(), m_id };
			break;
		}
		// String[] params = { status.toString(), m_id };

		if (m_id != null && !m_id.equals("")) {
			count = sqlHelper.update(sql, params);
		}
		return count == 1 ? true : false;
	}
}
