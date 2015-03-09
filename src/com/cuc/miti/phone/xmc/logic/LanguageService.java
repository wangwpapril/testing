package com.cuc.miti.phone.xmc.logic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.cuc.miti.phone.xmc.dao.LanguageDao;
import com.cuc.miti.phone.xmc.domain.CommonXMLData;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.Keywords;
import com.cuc.miti.phone.xmc.domain.Language;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;

public class LanguageService {
	
	private Context context;
	
	private LanguageDao languageDao;
	
	
	public LanguageService(Context context){
		this.context = context;
		this.languageDao = new LanguageDao(context);
	}
	
	
	public boolean addLanguage(Language language)
	{
		return languageDao.addLanguage(language);
		
	}
	
	public boolean multiInsert(List<Language> languageList)
	{
		return languageDao.multiInsert(languageList);
	}
	
	public boolean deleteLanguage(String name)
	{
		return languageDao.deleteLanguage(name);
	}
	
	public boolean updateLanguage(Language language)
	{
		return languageDao.updateLanguage(language);
	}

	public Language getLanguage(String name)
	{
		return languageDao.getLanguage(name);
	}
	
	
	public List<Language> getLanguageList()
	{
		return languageDao.getLanguageList();
	}
	
	
	public long getLanguageCount()
	{
		return languageDao.getLanguageCount();
	}
	
	public List<Language> getLanguageByPage(Pager pager)
	{
		return languageDao.getLanguageByPage(pager);
	}
	
	public boolean deleteAllLanguage(){
		return this.languageDao.deleteAllLanguage();
	}
	
	
	/**
	 * ��ȡ������Ͷ���List
	 * @param filePath �������xml�ļ�·��
	 * @return
	 * @throws IOException 
	 */
	
	public List<Language> GetLanguageFromXMLFile(String filePath) throws IOException{
		List<Language> ntList = null;
		File file = new File(filePath);
		InputStream is = null;   
		try {     
			is = new BufferedInputStream(new FileInputStream(file));
			List<CommonXMLData> cxdList = XMLDataHandle.parser(is);
			
			if(cxdList != null){
				ntList =  this.GetFromCommonList(cxdList);
			}
		}catch(Exception ex){
			Logger.e(ex);
		}finally{
			if (is != null) {
				is.close();
			} 
			
		}	
		return ntList;
	}
	
	public List<Language> GetLanguageFromXMLFile(InputStream is) throws IOException{
		List<Language> ntList = null;
		
		try {     
			List<CommonXMLData> cxdList = XMLDataHandle.parser(is);
			
			if(cxdList != null){
				ntList =  this.GetFromCommonList(cxdList);
			}
		}catch(Exception ex){
			Logger.e(ex);
		}finally{
			if (is != null) {
				is.close();
			} 
			
		}	
		return ntList;
	}
	
	
	private List<Language> GetFromCommonList(List<CommonXMLData> cxmList){
		
		List<Language> languageList=new ArrayList<Language>();
		
		for(CommonXMLData commonXMLData:cxmList)
		{
			
			List<KeyValueData> keyValueDatas=commonXMLData.getLanguageList();
			
			
			for(KeyValueData kv:keyValueDatas)
			{
				Language language=new Language();
				
				language.setCode(commonXMLData.getTopicId());
				language.setL_id(commonXMLData.getId());
				
				
				language.setLanguage(kv.getKey());
				language.setName(kv.getValue());
				
				languageList.add(language);
				
			}
			
		}
		
		return languageList;
	}
	
	
	/**
	 * ��ȡ����ѡ��Ļ���ݶ����б�
	 * @return
	 */
	public List<KeyValueData> getBaseDataForBind(){
		//TODO ��Ҫ��ȡ��ǰ�ֻ��ϵͳ���������ò�ͬ�����ֲ�ѯ��Ŀǰ��д�������ġ�
		List<Language> languageList = languageDao.getLanguageList("zh-CN");
		List<KeyValueData> keyValueDataList = null;
		KeyValueData kvDataInfo = null;
		if(languageList != null && languageList.size()>0){
			keyValueDataList = new ArrayList<KeyValueData>();
			for(Language l : languageList){
				kvDataInfo = new KeyValueData();
				kvDataInfo.setKey(l.getName());
				kvDataInfo.setValue(l.getCode());
				
				keyValueDataList.add(kvDataInfo);
			}
		}
		return keyValueDataList;
	}
	
	

}
