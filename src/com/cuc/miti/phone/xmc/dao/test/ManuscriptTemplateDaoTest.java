package com.cuc.miti.phone.xmc.dao.test;

import java.util.List;

import com.cuc.miti.phone.xmc.dao.ManuscriptTemplateDao;
import com.cuc.miti.phone.xmc.dao.ManuscriptsDao;
import com.cuc.miti.phone.xmc.domain.Enums.ManuscriptStatus;
import com.cuc.miti.phone.xmc.domain.Enums.XmcBool;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.Pager;

import android.test.AndroidTestCase;
import android.util.Log;

public class ManuscriptTemplateDaoTest extends AndroidTestCase{
	
	private static final String TAG="ManuscriptTemplate";
	private static final String TAG1="Manuscripts";
	
	public void testAddManuscriptTemplate(){
		ManuscriptTemplateDao manuscriptTemplateDao = new ManuscriptTemplateDao(this.getContext());
		
		for(int i=15;i<=17;i++){
			ManuscriptTemplate manuscriptTemplate = new ManuscriptTemplate();
			manuscriptTemplate.setMt_id("mt_id"+ i);
			manuscriptTemplate.setName("name"+ i);
			manuscriptTemplate.setLoginname("loginname");			
			manuscriptTemplate.setComefromDept("comefromDept"+ i);
			manuscriptTemplate.setComefromDeptID("comefromDeptID"+ i);
			manuscriptTemplate.setRegion("region"+ i);
			manuscriptTemplate.setRegionID("regionID"+ i);
			manuscriptTemplate.setDoctype("doctype"+ i);
			manuscriptTemplate.setDoctypeID("doctypeID"+ i);
			manuscriptTemplate.setProvtype("provtype"+ i);
			manuscriptTemplate.setProvtypeID("provtypeID"+ i);
			manuscriptTemplate.setKeywords("keywords"+ i);
			manuscriptTemplate.setLanguage("language"+ i);
			manuscriptTemplate.setLanguageID("languageID"+ i);
			manuscriptTemplate.setPriority("priority"+ i);
			manuscriptTemplate.setPriorityID("priorityID"+ i);
			manuscriptTemplate.setSendarea("sendarea"+ i);
			manuscriptTemplate.setHappenplace("happenplace"+ i);
			manuscriptTemplate.setReportplace("reportplace"+ i);
			manuscriptTemplate.setAddress("address"+ i);
			manuscriptTemplate.setAddressID("addressID"+ i);
			manuscriptTemplate.setIs3Tnews(XmcBool.True);
			manuscriptTemplate.setIsdefault(XmcBool.True);
			manuscriptTemplate.setCreatetime("createtime"+ i);
			manuscriptTemplate.setReviewstatus("reviewstatus"+ i);
			manuscriptTemplate.setDefaulttitle("defaulttitle"+ i);
			manuscriptTemplate.setDefaultcontents("defaultcontents"+ i);
			manuscriptTemplate.setIssystemoriginal("issystemoriginal"+ i);	
			manuscriptTemplate.setAuthor("author"+i);
			manuscriptTemplateDao.addManuscriptTemplate(manuscriptTemplate);
		}
		List<ManuscriptTemplate> manuscriptTemplates = manuscriptTemplateDao.getManuscriptTemplateList("loginname");
		
		for(ManuscriptTemplate e:manuscriptTemplates){
			Log.i(TAG, e.toString());
		}
	}
	
	public void testAddManuscripts(){
		ManuscriptsDao manuscriptsDao = new ManuscriptsDao(this.getContext());
		
		for(int i=20;i<=23;i++){
			ManuscriptTemplate manuscriptTemplate = new ManuscriptTemplate();
			Manuscripts manuscripts = new Manuscripts();

			manuscriptTemplate.setLoginname("loginname");
			manuscripts.setCreateid("createid" + i);
			manuscripts.setReleid("releid" + i);			
			manuscripts.setNewsid("newsid" + i);
			manuscripts.setTitle("title" + i);
			manuscripts.setTitle3T("title3T" + i);
			manuscripts.setUsernameC("usernameC" + i);
			manuscripts.setUsernameE("usernameE" + i);
			manuscripts.setGroupnameC("groupnameC" + i);
			manuscripts.setGroupcode("groupcode" + i);
			manuscripts.setGroupnameE("groupnameE" + i);
			manuscripts.setNewstype("newstype" + i);
			manuscripts.setNewstypeID("newstypeID" + i);
			manuscriptTemplate.setComefromDept("comefromDept" + i);
			manuscriptTemplate.setComefromDeptID("comefromDeptID" + i);
			manuscriptTemplate.setProvtype("provtype" + i);
			manuscriptTemplate.setProvtypeID("provtypeID" + i);
			manuscriptTemplate.setDoctype("doctype" + i);
			manuscriptTemplate.setDoctypeID("doctypeID" + i);
			manuscriptTemplate.setKeywords("keywords" + i);
			manuscriptTemplate.setLanguage("language" + i);
			manuscriptTemplate.setLanguageID("languageID" + i);
			manuscriptTemplate.setPriority("priority" + i);
			manuscriptTemplate.setPriorityID("priorityID" + i);
			manuscriptTemplate.setSendarea("sendarea" + i);
			manuscriptTemplate.setHappenplace("happenplace" + i);
			manuscriptTemplate.setReportplace("reportplace" + i);
			manuscriptTemplate.setAddress("address" + i);
			manuscriptTemplate.setAddressID("addressID" + i);
			manuscripts.setComment("comment" + i);
			manuscriptTemplate.setIs3Tnews(XmcBool.True);
			manuscriptTemplate.setCreatetime("createtime" + i);
			manuscripts.setRejecttime("rejecttime" + i);
			manuscripts.setReletime("reletime" + i);
			manuscripts.setSenttime("senttime" + i);
			manuscripts.setRereletime("rereletime"+i);
			manuscriptTemplate.setReviewstatus("reviewstatus" + i);
			manuscriptTemplate.setRegion("region" + i);
			manuscriptTemplate.setRegionID("regionID" + i);
			manuscripts.setContents("contents" + i);
			manuscripts.setContents3T("contents3T" + i);
			manuscripts.setReceiveTime("receiveTime" + i);
			manuscripts.setNewsIDBackTime("newsIDBackTime" + i);
			manuscripts.setManuscriptsStatus(ManuscriptStatus.Editing);
			manuscripts.setM_id("m_id"+i);
			manuscripts.setLocation("location"+i);
			manuscripts.setManuscriptTemplate(manuscriptTemplate);
			
			manuscriptsDao.addManuscripts(manuscripts);
			
			
		}
		List<Manuscripts> manuscripts = manuscriptsDao.getManuscriptsList("loginname");
		
		for(Manuscripts e:manuscripts){
			Log.i(TAG1, e.toString());
		}
	}
	
	public void testGetManuscript(){
		ManuscriptsDao manuscriptsDao = new ManuscriptsDao(this.getContext());
		Manuscripts manuscripts = manuscriptsDao.getManuscripts("m_id2");
		Log.i(TAG1, manuscripts.toString());
	}
	
	
	public void testGetManuscriptTemplateList(){
		ManuscriptTemplateDao manuscriptTemplateDao = new ManuscriptTemplateDao(this.getContext());

		List<ManuscriptTemplate> manuscriptTemplates = manuscriptTemplateDao.getManuscriptTemplateList("loginname");
		
		for(ManuscriptTemplate e:manuscriptTemplates){
			Log.i(TAG, e.toString());
		}
	}
	
	public void testGetManuscriptTemplateByPage(){
		ManuscriptTemplateDao manuscriptTemplateDao = new ManuscriptTemplateDao(this.getContext());
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setPageSize(3);
		pager.setTotalNum(Integer.parseInt(manuscriptTemplateDao.getManuscriptTemplateCount()+""));
		
		List<ManuscriptTemplate> manuscriptTemplates = manuscriptTemplateDao.getManuscriptTemplateByPage(pager);

		for(ManuscriptTemplate e:manuscriptTemplates){
			Log.i(TAG, e.toString());
		}
	}
	
	public void testGetManuscriptsByPage(){
		ManuscriptsDao manuscriptsDao = new ManuscriptsDao(this.getContext());
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setPageSize(3);
		pager.setTotalNum(Integer.parseInt(manuscriptsDao.getManuscriptsCount()+""));
	
		List<Manuscripts> manuscriptses = manuscriptsDao.getManuscriptsByPage(pager, "", ManuscriptStatus.Editing, "", false);

		for(Manuscripts e:manuscriptses){
			Log.i(TAG1, e.toString());
		}
	}
	
	public void testDeleteManuscriptTemplate(){
		ManuscriptTemplateDao manuscriptTemplateDao = new ManuscriptTemplateDao(this.getContext());
		manuscriptTemplateDao.deleteManuscriptTemplate("mt_id0");
		List<ManuscriptTemplate> manuscriptTemplates = manuscriptTemplateDao.getManuscriptTemplateList("loginname");
		for(ManuscriptTemplate e:manuscriptTemplates){
			Log.i(TAG, e.toString());
		}
	}
	
	public void testDeleteManuscripts(){
		ManuscriptsDao manuscriptsDao = new ManuscriptsDao(this.getContext());
		manuscriptsDao.deleteManuscripts("m_id0");
		List<Manuscripts> manuscriptses = manuscriptsDao.getManuscriptsList("loginname");
		for(Manuscripts e:manuscriptses){
			Log.i(TAG1, e.toString());
		}
	}
	
	public void testUpdateManuscriptTemplate(){
		ManuscriptTemplateDao manuscriptTemplateDao = new ManuscriptTemplateDao(this.getContext());
		ManuscriptTemplate manuscriptTemplate = manuscriptTemplateDao.getManuscriptTemplateById("mt_id2");
		
		manuscriptTemplate.setName("updatename");
		manuscriptTemplate.setLoginname("updateloginname");			
		manuscriptTemplate.setComefromDept("updatecomefromDept");
		manuscriptTemplate.setComefromDeptID("updatecomefromDeptID");
		manuscriptTemplate.setRegion("updateregion");
		manuscriptTemplate.setRegionID("updateregionID");
		manuscriptTemplate.setDoctype("updatedoctype");
		manuscriptTemplate.setDoctypeID("updatedoctypeID");
		manuscriptTemplate.setProvtype("updateprovtype");
		manuscriptTemplate.setProvtypeID("updateprovtypeID");
		manuscriptTemplate.setKeywords("updatekeywords");
		manuscriptTemplate.setLanguage("updatelanguage");
		manuscriptTemplate.setLanguageID("updatelanguageID");
		manuscriptTemplate.setPriority("updatepriority");
		manuscriptTemplate.setPriorityID("updatepriorityID");
		manuscriptTemplate.setSendarea("updatesendarea");
		manuscriptTemplate.setHappenplace("updatehappenplace");
		manuscriptTemplate.setReportplace("updatereportplace");
		manuscriptTemplate.setAddress("updateaddress");
		manuscriptTemplate.setAddressID("updateaddressID");
		manuscriptTemplate.setIs3Tnews(XmcBool.True);
		manuscriptTemplate.setIsdefault(XmcBool.True);
		manuscriptTemplate.setCreatetime("updatecreatetime");
		manuscriptTemplate.setReviewstatus("updatereviewstatus");
		manuscriptTemplate.setDefaulttitle("updatedefaulttitle");
		manuscriptTemplate.setDefaultcontents("updatedefaultcontents");
		manuscriptTemplate.setIssystemoriginal("updateissystemoriginal");	
		manuscriptTemplate.setAuthor("updateauthor");
		manuscriptTemplateDao.updateManuscriptTemplate(manuscriptTemplate);
		
		Log.i(TAG, manuscriptTemplate.toString());
	}
	public void testUpdateManuscripts(){
		ManuscriptsDao manuscriptsDao = new ManuscriptsDao(this.getContext());
		Manuscripts manuscripts = manuscriptsDao.getManuscripts("m_id6");
		
		ManuscriptTemplate manuscriptTemplate = manuscripts.getManuscriptTemplate();

		manuscriptTemplate.setLoginname("123loginname");
		manuscripts.setCreateid("123createid" );
		manuscripts.setReleid("123releid" );			
		manuscripts.setNewsid("123newsid" );
		manuscripts.setTitle("123title" );
		manuscripts.setTitle3T("123title3T");
		manuscripts.setUsernameC("123usernameC" );
		manuscripts.setUsernameE("123usernameE" );
		manuscripts.setGroupnameC("123groupnameC" );
		manuscripts.setGroupcode("123groupcode" );
		manuscripts.setGroupnameE("123groupnameE" );
		manuscripts.setNewstype("123newstype" );
		manuscripts.setNewstypeID("123newstypeID");
		manuscriptTemplate.setComefromDept("123comefromDept");
		manuscriptTemplate.setComefromDeptID("123comefromDeptID");
		manuscriptTemplate.setProvtype("123provtype" );
		manuscriptTemplate.setProvtypeID("123provtypeID" );
		manuscriptTemplate.setDoctype("123doctype" );
		manuscriptTemplate.setDoctypeID("123doctypeID" );
		manuscriptTemplate.setKeywords("123keywords" );
		manuscriptTemplate.setLanguage("123language" );
		manuscriptTemplate.setLanguageID("123languageID" );
		manuscriptTemplate.setPriority("123priority" );
		manuscriptTemplate.setPriorityID("123priorityID" );
		manuscriptTemplate.setSendarea("123sendarea" );
		manuscriptTemplate.setHappenplace("123happenplace" );
		manuscriptTemplate.setReportplace("123reportplace" );
		manuscriptTemplate.setAddress("123address" );
		manuscriptTemplate.setAddressID("123addressID" );
		manuscripts.setComment("123comment" );
		manuscriptTemplate.setIs3Tnews(XmcBool.True );
		manuscriptTemplate.setCreatetime("123createtime" );
		manuscripts.setRejecttime("123rejecttime" );
		manuscripts.setReletime("123reletime" );
		manuscripts.setSenttime("123senttime" );
		manuscripts.setRereletime("123rereletime");
		manuscriptTemplate.setReviewstatus("123reviewstatus" );
		manuscriptTemplate.setRegion("123region" );
		manuscriptTemplate.setRegionID("123regionID");
		manuscripts.setContents("123contents" );
		manuscripts.setContents3T("123contents3T" );
		manuscripts.setReceiveTime("123receiveTime" );
		manuscripts.setNewsIDBackTime("123newsIDBackTime" );
		manuscripts.setManuscriptsStatus(ManuscriptStatus.Editing);
		manuscripts.setLocation("123location");
		manuscripts.setManuscriptTemplate(manuscriptTemplate);
		
		manuscriptsDao.updateManuscripts(manuscripts);
		
		Log.i(TAG1, manuscriptTemplate.toString());
	}
	
}
