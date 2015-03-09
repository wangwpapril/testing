package com.cuc.miti.phone.xmc.logic;

import java.util.List;

import org.apache.commons.lang3.Validate;

import android.content.Context;
import android.test.AndroidTestCase;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.dao.ManuscriptTemplateDao;
import com.cuc.miti.phone.xmc.domain.Enums.TemplateType;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Enums.XmcBool;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.R;


public class ManuscriptTemplateService{

	private ManuscriptTemplateDao mtDao;
	private Context context;
	
	public ManuscriptTemplateService(Context context){
		this.context = context;
		this.mtDao = new ManuscriptTemplateDao(context);
	}
	
	/**
	 * ����ݿ��л�ȡ����ĸ�ǩ�б�
	 * @return ��ǩ�б�
	 */
	public List<ManuscriptTemplate> getManuscriptTemplateList(String loginname){
		return mtDao.getManuscriptTemplateList(loginname);
	}
	
	public List<ManuscriptTemplate> getManuscriptTemplateSystemList(String loginname,String issystemoriginal)
	{
		return mtDao.getManuscriptTemplateSystemList(loginname, issystemoriginal);
	}
	
	public ManuscriptTemplate getManuscriptTemplateSystem(String loginname,String issystemoriginal)
	{
		return mtDao.getManuscriptTemplateSystem(loginname, issystemoriginal);
	}
	
	
	public List<ManuscriptTemplate> getManuscriptTemplateCustomList(String loginname, String issystemoriginal) 
	{
		return mtDao.getManuscriptTemplateCustomList(loginname, issystemoriginal);
	}
	
	public ManuscriptTemplate getDefaultManuscriptTemplate(String loginname,String isdefault)
	{
		return mtDao.getDefaultManuscriptTemplate(loginname, isdefault);
	}
	
	public boolean addManuscriptTemplate(ManuscriptTemplate manuscriptTemplate){
		return mtDao.addManuscriptTemplate(manuscriptTemplate);
	}
	
	public boolean deleteAllManuscriptTemplate(){
		return mtDao.deleteAllManuscriptTemplate();
	}
	
	public boolean updateManuscriptTemplate(ManuscriptTemplate manuscriptTemplate){
		return mtDao.updateManuscriptTemplate(manuscriptTemplate);
	}
	
	public long getManuscriptTemplateCount(){
		return mtDao.getManuscriptTemplateCount();
	}
	
	public List<ManuscriptTemplate> getManuscriptTemplateByPage(Pager pager){
		return mtDao.getManuscriptTemplateByPage(pager);
	}
	
	public boolean deleteManuscriptTemplate(String mtID){
		return mtDao.deleteManuscriptTemplate(mtID);
	}
	
	/**
	 * ���ģ����ƺ��û����ж��Ƿ����ͬ��ģ�壬������򷵻�true����֮��Ȼ
	 * @param templateName
	 * @param loginname
	 * @return 
	 */
	public boolean isNameExsit(String templateName,String loginname) {
		return mtDao.isNameExsit(templateName, loginname);
	}
	public ManuscriptTemplate getManuscriptTemplateById(String mtID){
		return mtDao.getManuscriptTemplateById(mtID);
	}
	
	public ManuscriptTemplate getManuscriptTemplateByName(String templateName,String loginname) {
		return mtDao.getManuscriptTemplateByName(templateName, loginname);
	}

	
	public boolean setTemplateAsDefault(ManuscriptTemplate mtInfo){
		return mtDao.setTemplateAsDefault(mtInfo);
	} 
	public void TestAdd(){
		for(int i =0;i<5;i++){
			TestForInsert(String.valueOf(i));
		}
		/*for(int j=0;j<1;j++)
		{
			TestForInsertSys1(String.valueOf(5));
			TestForInsertSysAudio(String.valueOf(6));
			TestForInsertSysvedio(String.valueOf(7));
			TestForInsertSysPic(String.valueOf(8));
		}*/
		
	}
	
	private void TestForInsert(String count){
		ManuscriptTemplate mtInfo = new ManuscriptTemplate();
		mtInfo.setAddress("address" + count);
		mtInfo.setAddressID("addressID" + count);
		mtInfo.setComefromDept("comeFromDept"+ count);
		mtInfo.setComefromDeptID("comeformDeptId"+ count);
		mtInfo.setCreatetime("createtime"+ count);
		mtInfo.setDefaultcontents("defaultContentsTest"+ count);
		mtInfo.setDefaulttitle("defaultTitleTest"+ count);
		mtInfo.setDoctype("testDocType"+ count);
		mtInfo.setDoctypeID("docTypeIDTest"+ count);
		mtInfo.setHappenplace("happenplace"+ count);
		mtInfo.setIs3Tnews(XmcBool.True);
		mtInfo.setIsdefault(XmcBool.False);
		mtInfo.setIssystemoriginal("NORMAL");
		mtInfo.setKeywords("Keywords"+ count);
		mtInfo.setLanguage("language"+ count);
		mtInfo.setLanguageID("languageID"+ count);
		mtInfo.setLoginname("LoginName");
		mtInfo.setMt_id(java.util.UUID.randomUUID().toString());
		mtInfo.setName("NAme"+count);
		mtInfo.setPriority("priority"+ count);
		mtInfo.setPriorityID("priorityID"+ count);
		mtInfo.setProvtype("provtype"+ count);
		mtInfo.setProvtypeID("provtypeID"+ count);
		mtInfo.setRegion("region"+ count);
		mtInfo.setRegionID("regionID"+ count);
		mtInfo.setReportplace("reportplace"+ count);
		mtInfo.setReviewstatus("reviewstatus"+ count);
		mtInfo.setSendarea("sendarea"+ count);
		mtInfo.setAuthor("author"+count);
		
		mtDao.addManuscriptTemplate(mtInfo);
	}
	
	//����ʱ���4��Ĭ�ϵ�ϵͳģ��
	private void TestForInsertSys1(String count){
		ManuscriptTemplate mtInfo = new ManuscriptTemplate();
		mtInfo.setAddress("address" + count);
		mtInfo.setAddressID("addressID" + count);
		mtInfo.setComefromDept("comeFromDept"+ count);
		mtInfo.setComefromDeptID("comeformDeptId"+ count);
		mtInfo.setCreatetime("createtime"+ count);
		mtInfo.setDefaultcontents("defaultContentsTest"+ count);
		mtInfo.setDefaulttitle("defaultTitleTest"+ count);
		mtInfo.setDoctype("testDocType"+ count);
		mtInfo.setDoctypeID("docTypeIDTest"+ count);
		mtInfo.setHappenplace("happenplace"+ count);
		mtInfo.setIs3Tnews(XmcBool.False);
		mtInfo.setIsdefault(XmcBool.False);
		mtInfo.setIssystemoriginal("WORD");
		mtInfo.setKeywords("Keywords"+ count);
		mtInfo.setLanguage("language"+ count);
		mtInfo.setLanguageID("languageID"+ count);
		mtInfo.setLoginname("LoginName"+ count);
		mtInfo.setMt_id(java.util.UUID.randomUUID().toString());
		mtInfo.setName("���ֿ�Ѷ");
		mtInfo.setPriority("priority"+ count);
		mtInfo.setPriorityID("priorityID"+ count);
		mtInfo.setProvtype("provtype"+ count);
		mtInfo.setProvtypeID("provtypeID"+ count);
		mtInfo.setRegion("region"+ count);
		mtInfo.setRegionID("regionID"+ count);
		mtInfo.setReportplace("reportplace"+ count);
		mtInfo.setReviewstatus("reviewstatus"+ count);
		mtInfo.setSendarea("sendarea"+ count);
		mtInfo.setAuthor("author"+count);
		
		mtDao.addManuscriptTemplate(mtInfo);
	}
	private void TestForInsertSysAudio(String count){
		ManuscriptTemplate mtInfo = new ManuscriptTemplate();
		mtInfo.setAddress("address" + count);
		mtInfo.setAddressID("addressID" + count);
		mtInfo.setComefromDept("comeFromDept"+ count);
		mtInfo.setComefromDeptID("comeformDeptId"+ count);
		mtInfo.setCreatetime("createtime"+ count);
		mtInfo.setDefaultcontents("defaultContentsTest"+ count);
		mtInfo.setDefaulttitle("defaultTitleTest"+ count);
		mtInfo.setDoctype("testDocType"+ count);
		mtInfo.setDoctypeID("docTypeIDTest"+ count);
		mtInfo.setHappenplace("happenplace"+ count);
		mtInfo.setIs3Tnews(XmcBool.True);
		mtInfo.setIsdefault(XmcBool.False);
		mtInfo.setIssystemoriginal("VOICE");
		mtInfo.setKeywords("Keywords"+ count);
		mtInfo.setLanguage("language"+ count);
		mtInfo.setLanguageID("languageID"+ count);
		mtInfo.setLoginname("LoginName"+ count);
		mtInfo.setMt_id(java.util.UUID.randomUUID().toString());
		mtInfo.setName("��Ƶ��Ѷ");
		mtInfo.setPriority("priority"+ count);
		mtInfo.setPriorityID("priorityID"+ count);
		mtInfo.setProvtype("provtype"+ count);
		mtInfo.setProvtypeID("provtypeID"+ count);
		mtInfo.setRegion("region"+ count);
		mtInfo.setRegionID("regionID"+ count);
		mtInfo.setReportplace("reportplace"+ count);
		mtInfo.setReviewstatus("reviewstatus"+ count);
		mtInfo.setSendarea("sendarea"+ count);
		mtInfo.setAuthor("author"+count);
		
		mtDao.addManuscriptTemplate(mtInfo);
	}
	private void TestForInsertSysvedio(String count){
		ManuscriptTemplate mtInfo = new ManuscriptTemplate();
		mtInfo.setAddress("address" + count);
		mtInfo.setAddressID("addressID" + count);
		mtInfo.setComefromDept("comeFromDept"+ count);
		mtInfo.setComefromDeptID("comeformDeptId"+ count);
		mtInfo.setCreatetime("createtime"+ count);
		mtInfo.setDefaultcontents("defaultContentsTest"+ count);
		mtInfo.setDefaulttitle("defaultTitleTest"+ count);
		mtInfo.setDoctype("testDocType"+ count);
		mtInfo.setDoctypeID("docTypeIDTest"+ count);
		mtInfo.setHappenplace("happenplace"+ count);
		mtInfo.setIs3Tnews(XmcBool.False);
		mtInfo.setIsdefault(XmcBool.False);
		mtInfo.setIssystemoriginal("VIDEO");
		mtInfo.setKeywords("Keywords"+ count);
		mtInfo.setLanguage("language"+ count);
		mtInfo.setLanguageID("languageID"+ count);
		mtInfo.setLoginname("LoginName"+ count);
		mtInfo.setMt_id(java.util.UUID.randomUUID().toString());
		mtInfo.setName("��Ƶ��Ѷ");
		mtInfo.setPriority("priority"+ count);
		mtInfo.setPriorityID("priorityID"+ count);
		mtInfo.setProvtype("provtype"+ count);
		mtInfo.setProvtypeID("provtypeID"+ count);
		mtInfo.setRegion("region"+ count);
		mtInfo.setRegionID("regionID"+ count);
		mtInfo.setReportplace("reportplace"+ count);
		mtInfo.setReviewstatus("reviewstatus"+ count);
		mtInfo.setSendarea("sendarea"+ count);
		mtInfo.setAuthor("author"+count);
		
		mtDao.addManuscriptTemplate(mtInfo);
	}
	private void TestForInsertSysPic(String count){
		ManuscriptTemplate mtInfo = new ManuscriptTemplate();
		mtInfo.setAddress("address" + count);
		mtInfo.setAddressID("addressID" + count);
		mtInfo.setComefromDept("comeFromDept"+ count);
		mtInfo.setComefromDeptID("comeformDeptId"+ count);
		mtInfo.setCreatetime("createtime"+ count);
		mtInfo.setDefaultcontents("defaultContentsTest"+ count);
		mtInfo.setDefaulttitle("defaultTitleTest"+ count);
		mtInfo.setDoctype("testDocType"+ count);
		mtInfo.setDoctypeID("docTypeIDTest"+ count);
		mtInfo.setHappenplace("happenplace"+ count);
		mtInfo.setIs3Tnews(XmcBool.False);
		mtInfo.setIsdefault(XmcBool.False);
		mtInfo.setIssystemoriginal("PICTURE");
		mtInfo.setKeywords("Keywords"+ count);
		mtInfo.setLanguage("language"+ count);
		mtInfo.setLanguageID("languageID"+ count);
		mtInfo.setLoginname("LoginName"+ count);
		mtInfo.setMt_id(java.util.UUID.randomUUID().toString());
		mtInfo.setName("ͼƬ��Ѷ");
		mtInfo.setPriority("priority"+ count);
		mtInfo.setPriorityID("priorityID"+ count);
		mtInfo.setProvtype("provtype"+ count);
		mtInfo.setProvtypeID("provtypeID"+ count);
		mtInfo.setRegion("region"+ count);
		mtInfo.setRegionID("regionID"+ count);
		mtInfo.setReportplace("reportplace"+ count);
		mtInfo.setReviewstatus("reviewstatus"+ count);
		mtInfo.setSendarea("sendarea"+ count);
		mtInfo.setAuthor("author"+count);
		
		mtDao.addManuscriptTemplate(mtInfo);
	}
	
	/**
	 * У���ǩ�������Ƿ�����
	 * @return
	 */
	public boolean validateTemplate(TemplateType templateType,KeyValueData message){
		
		boolean returnValue = true;
		message.setValue("");
		
		ManuscriptTemplate manuscriptTemplate = mtDao.getManuscriptTemplateSystem(IngleApplication.getInstance().getCurrentUser(),templateType.toString());
				
		if(manuscriptTemplate==null){
			return false;
		}
		if(manuscriptTemplate.getDefaulttitle().equals("")){
			//message.setValue(message.getValue().concat("Ĭ�ϱ���\n"));
			message.setValue(message.getValue().concat(ToastHelper.getStringFromResources(R.string.validate_titleNull)));
			returnValue = false;
		}
		if(manuscriptTemplate.getAuthor().equals("")){
			//message.setValue(message.getValue().concat("����\n"));
			message.setValue(message.getValue().concat(ToastHelper.getStringFromResources(R.string.validate_authorNull)));
			returnValue = false;
		}
		if (manuscriptTemplate.getKeywords().equals("")) {
			//message.setValue(message.getValue().concat("�ؼ��\n"));
			message.setValue(message.getValue().concat(ToastHelper.getStringFromResources(R.string.validate_keyWordNull)));
			returnValue = false;
		}
		if (manuscriptTemplate.getLanguage().equals("")) {
			//message.setValue(message.getValue().concat("����\n"));
			message.setValue(message.getValue().concat(ToastHelper.getStringFromResources(R.string.validate_languageNull)));
			returnValue = false;
		}
		if (manuscriptTemplate.getPriority().equals("")) {
			//message.setValue(message.getValue().concat("���ȼ�\n"));
			message.setValue(message.getValue().concat(ToastHelper.getStringFromResources(R.string.validate_priorityNull)));
			returnValue = false;
		}
		if (manuscriptTemplate.getProvtype().equals("")) {
			//message.setValue(message.getValue().concat("�������\n"));
			message.setValue(message.getValue().concat(ToastHelper.getStringFromResources(R.string.validate_provtypeNull)));
			returnValue = false;
		}
		if (manuscriptTemplate.getDoctype().equals("")) {
			//message.setValue(message.getValue().concat("����\n"));
			message.setValue(message.getValue().concat(ToastHelper.getStringFromResources(R.string.validate_doctypeNull)));
			returnValue = false;
		}
		if (manuscriptTemplate.getComefromDept().equals("")) {
			//message.setValue(message.getValue().concat("��Դ\n"));
			message.setValue(message.getValue().concat(ToastHelper.getStringFromResources(R.string.validate_comefromDeptNull)));
			returnValue = false;
		}
		if (manuscriptTemplate.getRegion().equals("")) {
			//message.setValue(message.getValue().concat("����\n"));
			message.setValue(message.getValue().concat(ToastHelper.getStringFromResources(R.string.validate_regionNull)));
			returnValue = false;
		}
		if (manuscriptTemplate.getAddress().equals("")) {
			//message.setValue(message.getValue().concat("�����ַ\n"));
			message.setValue(message.getValue().concat(ToastHelper.getStringFromResources(R.string.validate_addressNull)));
			returnValue = false;
		}
		if (manuscriptTemplate.getReviewstatus().equals("")) {
			//message.setValue(message.getValue().concat("�������\n"));
			message.setValue(message.getValue().concat(ToastHelper.getStringFromResources(R.string.validate_reviewstatusNull)));
			returnValue = false;
		}
	
	return returnValue;
	
		
	}
}
