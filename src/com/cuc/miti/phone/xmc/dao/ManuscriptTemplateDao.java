package com.cuc.miti.phone.xmc.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.cuc.miti.phone.xmc.domain.Enums.TemplateType;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Enums.XmcBool;

public class ManuscriptTemplateDao {
	private static SQLiteHelper sqlHelper;
	private static final int VERSION = 1;

	public ManuscriptTemplateDao(Context context) {
		sqlHelper = new SQLiteHelper(context, VERSION);
	}

	/**
	 * ����һ���û���Ϣ
	 * 
	 * @return �ɹ�����true��ʧ�ܷ���false
	 */
	public boolean addManuscriptTemplate(ManuscriptTemplate manuscriptTemplate) {
		int count = 0;
		if (manuscriptTemplate != null) {
			String sql = "INSERT INTO ManuscriptTemplate (mt_id,name,loginname,comefromDept,comefromDeptID,region,regionID,doctype,doctypeID,provtype,provtypeID,keywords,[language],languageID,priority,priorityID,sendarea,happenplace,reportplace,address,addressID,is3Tnews,isdefault,createtime,reviewstatus,defaulttitle,defaultcontents,issystemoriginal,author) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Object[] params = { manuscriptTemplate.getMt_id(),
					manuscriptTemplate.getName(),
					manuscriptTemplate.getLoginname(),
					manuscriptTemplate.getComefromDept(),
					manuscriptTemplate.getComefromDeptID(),
					manuscriptTemplate.getRegion(),
					manuscriptTemplate.getRegionID(),
					manuscriptTemplate.getDoctype(),
					manuscriptTemplate.getDoctypeID(),
					manuscriptTemplate.getProvtype(),
					manuscriptTemplate.getProvtypeID(),
					manuscriptTemplate.getKeywords(),
					manuscriptTemplate.getLanguage(),
					manuscriptTemplate.getLanguageID(),
					manuscriptTemplate.getPriority(),
					manuscriptTemplate.getPriorityID(),
					manuscriptTemplate.getSendarea(),
					manuscriptTemplate.getHappenplace(),
					manuscriptTemplate.getReportplace(),
					manuscriptTemplate.getAddress(),
					manuscriptTemplate.getAddressID(),
					manuscriptTemplate.getIs3Tnews().getValue().toString(),
					manuscriptTemplate.getIsdefault().getValue().toString(),
					manuscriptTemplate.getCreatetime(),
					manuscriptTemplate.getReviewstatus(),
					manuscriptTemplate.getDefaulttitle(),
					manuscriptTemplate.getDefaultcontents(),
					manuscriptTemplate.getIssystemoriginal(),
					manuscriptTemplate.getAuthor() };
			count = sqlHelper.insert(sql, params);
		}
		return count == 1 ? true : false;
	}

	/**
	 * ɾ������
	 * 
	 * @return
	 */
	public boolean deleteAllManuscriptTemplate() {
		String sql = "DELETE FROM ManuscriptTemplate";
		sqlHelper.deleteAll(sql);
		return true;

	}

	/**
	 * 
	 ���ģ��id��ȡһ��ģ��
	 */
	public ManuscriptTemplate getManuscriptTemplateById(String mtID) {
		ManuscriptTemplate manuscriptTemplate = null;
		if (mtID != null) {
			String sql = "select * from ManuscriptTemplate where mt_id = ?";
			String[] params = { mtID };
			Cursor cursor = sqlHelper.findQuery(sql, params);
			if (cursor != null && cursor.getCount() > 0) {
				manuscriptTemplate = new ManuscriptTemplate();

				commonMethod(cursor, manuscriptTemplate);

			}
			cursor.close();
		}
		return manuscriptTemplate;
	}
	
	
	/**
	 * ���ģ����ƺ��û����ж��Ƿ����ģ��
	 * @param templateName
	 * @param loginname
	 * @return 
	 */
	public ManuscriptTemplate getManuscriptTemplateByName(String templateName,String loginname) {
		ManuscriptTemplate manuscriptTemplate = null;
		if (templateName != null && loginname!=null) {
			String sql = "select * from ManuscriptTemplate where name = ? and loginname = ?" ;
			
			String[] params = { templateName,loginname };
			Cursor cursor = sqlHelper.findQuery(sql, params);
			if (cursor != null && cursor.getCount() > 0) {
				manuscriptTemplate = new ManuscriptTemplate();

				commonMethod(cursor, manuscriptTemplate);

			}
			cursor.close();
		}
		return manuscriptTemplate;
	}

	/**
	 * ���ģ����ƺ��û����ж��Ƿ����ͬ��ģ�壬������򷵻�true����֮��Ȼ
	 * @param templateName
	 * @param loginname
	 * @return 
	 */
	public boolean isNameExsit(String templateName,String loginname) {
		if (templateName != null && loginname!=null) {
			String sql = "select * from ManuscriptTemplate where name = '" + templateName + "' and loginname = '" + loginname + "'" ;
			int count = sqlHelper.rowCount(sql);
			if (count > 0) {
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	/**
	
	 */
	public boolean updateManuscriptTemplate(
			ManuscriptTemplate manuscriptTemplate) {
		int count = 0;
		if (manuscriptTemplate != null) {
			String sql = "UPDATE ManuscriptTemplate SET  name = ?, loginname = ?, comefromDept = ?, comefromDeptID = ?, region = ?, regionID = ?, doctype = ?, doctypeID = ?,provtype = ?, provtypeID = ?, keywords = ?, [language] = ?, languageID = ?, priority = ?, priorityID = ?,sendarea = ?, happenplace = ?, reportplace = ?, address = ?, addressID = ?, is3Tnews = ?, isdefault = ?, createtime = ?, reviewstatus = ?, defaulttitle = ?,defaultcontents = ?, issystemoriginal = ?,author = ? WHERE  mt_id = ?";
			String[] params = { manuscriptTemplate.getName(),
					manuscriptTemplate.getLoginname(),
					manuscriptTemplate.getComefromDept(),
					manuscriptTemplate.getComefromDeptID(),
					manuscriptTemplate.getRegion(),
					manuscriptTemplate.getRegionID(),
					manuscriptTemplate.getDoctype(),
					manuscriptTemplate.getDoctypeID(),
					manuscriptTemplate.getProvtype(),
					manuscriptTemplate.getProvtypeID(),
					manuscriptTemplate.getKeywords(),
					manuscriptTemplate.getLanguage(),
					manuscriptTemplate.getLanguageID(),
					manuscriptTemplate.getPriority(),
					manuscriptTemplate.getPriorityID(),
					manuscriptTemplate.getSendarea(),
					manuscriptTemplate.getHappenplace(),
					manuscriptTemplate.getReportplace(),
					manuscriptTemplate.getAddress(),
					manuscriptTemplate.getAddressID(),
					manuscriptTemplate.getIs3Tnews().getValue().toString(),
					manuscriptTemplate.getIsdefault().getValue().toString(),
					manuscriptTemplate.getCreatetime(),
					manuscriptTemplate.getReviewstatus(),
					manuscriptTemplate.getDefaulttitle(),
					manuscriptTemplate.getDefaultcontents(),
					manuscriptTemplate.getIssystemoriginal(),
					manuscriptTemplate.getAuthor(),
					manuscriptTemplate.getMt_id() };
			count = sqlHelper.update(sql, params);
		}
		return count == 1 ? true : false;
	}

	/**
	 * ����¼���ȡ��ǰ��������б���ĸ�ǩģ���б�
	 * 
	 * @return �ɹ�����List��ʧ�ܷ���null
	 */
	public List<ManuscriptTemplate> getManuscriptTemplateList(String loginname) {
		List<ManuscriptTemplate> manuscriptTemplateList = null;

		String sql = "select * from ManuscriptTemplate where loginname=?";
		String[] params = { loginname };
		Cursor cursor = sqlHelper.findQuery(sql, params);
		if (cursor != null && cursor.getCount() > 0) {
			manuscriptTemplateList = new ArrayList<ManuscriptTemplate>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				ManuscriptTemplate manuscriptTemplate = new ManuscriptTemplate();

				commonMethod(cursor, manuscriptTemplate);

				manuscriptTemplateList.add(manuscriptTemplate);
			}

		}
		cursor.close();
		return manuscriptTemplateList;
	}

	/**
	 * ��ȡϵͳ��ǩģ��
	 * 
	 * @return �ɹ�����List��ʧ�ܷ���null
	 */
	public List<ManuscriptTemplate> getManuscriptTemplateSystemList(
			String loginname, String issystemoriginal) {
		String sql = "select * from ManuscriptTemplate where loginname=? and issystemoriginal <> ?";
		String[] params = { loginname, issystemoriginal };
		Cursor cursor = sqlHelper.findQuery(sql, params);
		List<ManuscriptTemplate> manuscriptTemplateList = null;

		if (cursor != null && cursor.getCount() > 0) {
			manuscriptTemplateList = new ArrayList<ManuscriptTemplate>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				ManuscriptTemplate manuscriptTemplate = new ManuscriptTemplate();

				commonMethod(cursor, manuscriptTemplate);

				manuscriptTemplateList.add(manuscriptTemplate);
			}

		}
		cursor.close();
		return manuscriptTemplateList;
	}

	/**
	 * ��ȡ��ǰ�û��Ķ�Ӧϵͳ��Ѷģ��
	 * @param loginname
	 * @param issystemoriginal
	 * @return
	 */
	public ManuscriptTemplate getManuscriptTemplateSystem(String loginname,String issystemoriginal)
	{
		String sql = "select * from ManuscriptTemplate where loginname=? and issystemoriginal =?";
		String[] params = { loginname, issystemoriginal };
		Cursor cursor = sqlHelper.findQuery(sql, params);
		ManuscriptTemplate manuscriptTemplateItem = null;

		if (cursor != null && cursor.getCount() > 0) {
			manuscriptTemplateItem = new ManuscriptTemplate();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				commonMethod(cursor, manuscriptTemplateItem);
			}
		}
		cursor.close();
		return manuscriptTemplateItem;
	}
	
	/**
	 * ��ȡ�û��Զ����ǩģ��
	 * 
	 * @return �ɹ�����List��ʧ�ܷ���null
	 */
	public List<ManuscriptTemplate> getManuscriptTemplateCustomList(
			String loginname, String issystemoriginal) {
		String sql = "select * from ManuscriptTemplate where loginname=? and issystemoriginal =?";
		String[] params = { loginname, issystemoriginal };
		Cursor cursor = sqlHelper.findQuery(sql, params);
		List<ManuscriptTemplate> manuscriptTemplateList = null;

		if (cursor != null && cursor.getCount() > 0) {
			manuscriptTemplateList = new ArrayList<ManuscriptTemplate>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				ManuscriptTemplate manuscriptTemplate = new ManuscriptTemplate();

				commonMethod(cursor, manuscriptTemplate);

				manuscriptTemplateList.add(manuscriptTemplate);
			}

		}
		cursor.close();
		return manuscriptTemplateList;
	}
	
	
	/**
	 * ��ȡĬ�ϸ�ǩģ��
	 * 
	 * @return �ɹ�����List��ʧ�ܷ���null
	 */
	public ManuscriptTemplate getDefaultManuscriptTemplate(
			String loginname, String isdefault) {

		String sql = "select * from ManuscriptTemplate where loginname=? and isdefault=?";
		String[] params = { loginname, isdefault };
		Cursor cursor = sqlHelper.findQuery(sql, params);
		ManuscriptTemplate template = null;

		if (cursor != null && cursor.getCount() > 0) {
			template = new ManuscriptTemplate();

			commonMethod(cursor, template);

		}
		cursor.close();
		return template;
	}

	/**
	 * ��ȡ������
	 * 
	 */
	public long getManuscriptTemplateCount() {
		String sql = "select count(*) from ManuscriptTemplate";

		Cursor cursor = sqlHelper.findQuery(sql, null);
		cursor.moveToFirst();

		long returnvalue=cursor.getLong(0);
		cursor.close();
		return returnvalue;
	}

	/**
	 * ��ȡָ��ҳ��ķ�ҳ���,���ص�ǰҳ��������� SQL : Select * From TABLE_NAME Limit 0,10;
	 * ��ʾ��TABLE_NAME���ȡ��ݣ��ӵ�0����ݿ�ʼ��ȥ10��
	 * 
	 * @param pager
	 *            ��ҳ����(CurrentPage-��ǰҳ��PageSize-ÿҳ����totalNum-������<����ֵ>)
	 * @return
	 */
	public List<ManuscriptTemplate> getManuscriptTemplateByPage(Pager pager) {
		List<ManuscriptTemplate> manuscriptTemplateList = null;

		if (pager != null) {
			String sql = "select * from ManuscriptTemplate limit ?,?";
			int firstResult = ((pager.getCurrentPage() - 1) * pager
					.getPageSize()); // �ӵڼ�����ݿ�ʼ��ѯ
			int maxResult = pager.getPageSize(); // ÿҳ��ʾ��������¼
			String[] params = { String.valueOf(firstResult),
					String.valueOf(maxResult) };
			Cursor cursor = sqlHelper.findQuery(sql, params);

			if (cursor != null && cursor.getCount() > 0) {
				manuscriptTemplateList = new ArrayList<ManuscriptTemplate>();
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					ManuscriptTemplate manuscriptTemplate = new ManuscriptTemplate();
					commonMethod(cursor, manuscriptTemplate);
					manuscriptTemplateList.add(manuscriptTemplate);
				}

			}
			cursor.close();
		}
		return manuscriptTemplateList;
	}
	
	/**
	 * ����û������������е��û��Զ����ǩ��Ĭ��״̬Ϊ0(Ҳ��û��Ĭ�ϸ�ǩ)
	 * @param loginname
	 * @return
	 */
	public boolean resetDefaultStatus(String loginname){
		if(loginname !=null && loginname!=""){
			String sql = "Update ManuscriptTemplate set isdefault = 0 where loginname=? and issystemoriginal =?";
			String[] params = { loginname,TemplateType.NORMAL.toString()};
			sqlHelper.findQuery(sql,params);
			return true;
		}
		else{
			return false;
		}
		
	}
	
	/**
	 * �����û��Զ����ǩΪĬ�ϸ�ǩ
	 * @param mtInfo
	 * @return
	 */
	public boolean setTemplateAsDefault(ManuscriptTemplate mtInfo){
		if(mtInfo !=null){
			String loginname = mtInfo.getLoginname();
			if(resetDefaultStatus(loginname)){
				String sql = "Update ManuscriptTemplate set isdefault = 1 where loginname=? and mt_id =?";
				String[] params = { loginname,mtInfo.getMt_id()};
				sqlHelper.findQuery(sql,params);
				
				return true;
			}
			else{return false;}
		}else{
			return false;
		}
	}

	private void commonMethod(Cursor cursor,
			ManuscriptTemplate manuscriptTemplate) {
		manuscriptTemplate.setMt_id(cursor.getString(cursor
				.getColumnIndex("mt_id")));
		manuscriptTemplate.setName(cursor.getString(cursor
				.getColumnIndex("name")));
		manuscriptTemplate.setLoginname(cursor.getString(cursor
				.getColumnIndex("loginname")));
		manuscriptTemplate.setComefromDept(cursor.getString(cursor
				.getColumnIndex("comefromDept")));
		manuscriptTemplate.setComefromDeptID(cursor.getString(cursor
				.getColumnIndex("comefromDeptID")));
		manuscriptTemplate.setRegion(cursor.getString(cursor
				.getColumnIndex("region")));
		manuscriptTemplate.setRegionID(cursor.getString(cursor
				.getColumnIndex("regionID")));
		manuscriptTemplate.setDoctype(cursor.getString(cursor
				.getColumnIndex("doctype")));
		manuscriptTemplate.setDoctypeID(cursor.getString(cursor
				.getColumnIndex("doctypeID")));
		manuscriptTemplate.setProvtype(cursor.getString(cursor
				.getColumnIndex("provtype")));
		manuscriptTemplate.setProvtypeID(cursor.getString(cursor
				.getColumnIndex("provtypeID")));
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
		manuscriptTemplate.setIs3Tnews(XmcBool.parseFromValue(cursor.getString(cursor
				.getColumnIndex("is3Tnews"))));
		manuscriptTemplate.setIsdefault(XmcBool.parseFromValue(cursor.getString(cursor
				.getColumnIndex("isdefault"))));
		manuscriptTemplate.setCreatetime(cursor.getString(cursor
				.getColumnIndex("createtime")));
		manuscriptTemplate.setReviewstatus(cursor.getString(cursor
				.getColumnIndex("reviewstatus")));
		manuscriptTemplate.setDefaulttitle(cursor.getString(cursor
				.getColumnIndex("defaulttitle")));
		manuscriptTemplate.setDefaultcontents(cursor.getString(cursor
				.getColumnIndex("defaultcontents")));
		manuscriptTemplate.setIssystemoriginal(cursor.getString(cursor
				.getColumnIndex("issystemoriginal")));
		manuscriptTemplate.setAuthor(cursor.getString(cursor
				.getColumnIndex("author")));
	}

	/**
	 * ��ݴ����������ɾ����ݿ���е��û���Ϣ
	 * 
	 * @param mt_id
	 * @return �ɹ�����true��ʧ�ܷ���false
	 */
	public boolean deleteManuscriptTemplate(String mtID) {
		int count = 0;
		if (mtID != null) {
			String sql = "DELETE FROM ManuscriptTemplate WHERE mt_id = ?";
			String[] params = { mtID };

			count = sqlHelper.delete(sql, params);
		}

		return count == 1 ? true : false;
	}

}
