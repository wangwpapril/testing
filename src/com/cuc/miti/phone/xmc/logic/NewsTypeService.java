package com.cuc.miti.phone.xmc.logic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.dao.NewsTypeDao;
import com.cuc.miti.phone.xmc.domain.CommonXMLData;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.NewsCategory;
import com.cuc.miti.phone.xmc.domain.NewsPriority;
import com.cuc.miti.phone.xmc.domain.NewsType;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;

import android.content.Context;

public class NewsTypeService {
	
	private Context context;
	
	private NewsTypeDao newsTypeDao;
	
	
	public NewsTypeService(Context context){
		this.context = context;
		this.newsTypeDao = new NewsTypeDao(context);
	}
	
	
	public boolean addNewsType(NewsType newsType)
	{
		return newsTypeDao.addNewsType(newsType);
		
	}
	
	public boolean multiInsert(List<NewsType> newsTypeList)
	{
		return newsTypeDao.multiInsert(newsTypeList);
	}
	
	public boolean deleteNewsType(String name)
	{
		return newsTypeDao.deleteNewsType(name);
	}
	
	public boolean updateNewsType(NewsType newsType)
	{
		return newsTypeDao.updateNewsType(newsType);
	}

	public NewsType getNewsType(String name)
	{
		return newsTypeDao.getNewsType(name);
	}
	
	
	public List<NewsType> getNewsTypeList()
	{
		return newsTypeDao.getNewsTypeList();
	}
	
	
	public long getNewsTypeCount()
	{
		return newsTypeDao.getNewsTypeCount();
	}
	
	public List<NewsType> getNewsTypeByPage(Pager pager)
	{
		return newsTypeDao.getNewsTypeByPage(pager);
	}
	
	public boolean deleteAllNewsType(){
		return this.newsTypeDao.deleteAllNewsType();
	}
	
	
	
	/**
	 * ��ȡ������Ͷ���List
	 * @param filePath �������xml�ļ�·��
	 * @return
	 * @throws IOException 
	 */
	
	public List<NewsType> GetNewsTypeFromXMLFile(String filePath) throws IOException{
		List<NewsType> ntList = null;
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
	
	public List<NewsType> GetNewsTypeFromXMLFile(InputStream is) throws IOException{
		List<NewsType> ntList = null;
		
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
	
	
	private List<NewsType> GetFromCommonList(List<CommonXMLData> cxmList){
		
		List<NewsType> newsTypeList=new ArrayList<NewsType>();
		
		for(CommonXMLData commonXMLData:cxmList)
		{
			
			List<KeyValueData> keyValueDatas=commonXMLData.getLanguageList();
			
			
			for(KeyValueData kv:keyValueDatas)
			{
				NewsType newsType=new NewsType();
				
				newsType.setCode(commonXMLData.getTopicId());
				newsType.setNt_id(commonXMLData.getId());
				
				
				newsType.setLanguage(kv.getKey());
				newsType.setName(kv.getValue());
				
				newsTypeList.add(newsType);
				
			}
			
		}
		
		return newsTypeList;
	}


	/**
	 * ��ȡ����ѡ��Ļ���ݶ����б�
	 * @return
	 */
	public List<KeyValueData> getBaseDataForBind(){
		//TODO ��Ҫ��ȡ��ǰ�ֻ��ϵͳ���������ò�ͬ�����ֲ�ѯ��Ŀǰ��д�������ġ�
		List<NewsType> newsTypeList = newsTypeDao.getNewsTypeList("zh-CN");
		List<KeyValueData> keyValueDataList = null;
		KeyValueData kvDataInfo = null;
		if(newsTypeList != null && newsTypeList.size()>0){
			keyValueDataList = new ArrayList<KeyValueData>();
			for(NewsType nt : newsTypeList){
				kvDataInfo = new KeyValueData();
				kvDataInfo.setKey(nt.getName());
				kvDataInfo.setValue(nt.getCode());
				
				keyValueDataList.add(kvDataInfo);
			}
		}
		return keyValueDataList;
	}
	
	
}
