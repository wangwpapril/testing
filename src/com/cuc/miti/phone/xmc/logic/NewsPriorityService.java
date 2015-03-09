package com.cuc.miti.phone.xmc.logic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.dao.NewsPriorityDao;
import com.cuc.miti.phone.xmc.dao.NewsTypeDao;
import com.cuc.miti.phone.xmc.domain.CommonXMLData;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.Language;
import com.cuc.miti.phone.xmc.domain.NewsCategory;
import com.cuc.miti.phone.xmc.domain.NewsPriority;
import com.cuc.miti.phone.xmc.domain.NewsType;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;

import android.content.Context;

public class NewsPriorityService {
	
	private Context context;
	
	private NewsPriorityDao newsPriorityDao;
	
	
	public NewsPriorityService(Context context){
		this.context = context;
		this.newsPriorityDao = new NewsPriorityDao(context);
	}
	
	
	public boolean addNewsPriority(NewsPriority newsPriority)
	{
		return newsPriorityDao.addNewsPriority(newsPriority);
		
	}
	
	public boolean multiInsert(List<NewsPriority> newsPriorityList)
	{
		return newsPriorityDao.multiInsert(newsPriorityList);
	}
	
	public boolean deleteNewsPriority(String name)
	{
		return newsPriorityDao.deleteNewsPriority(name);
	}
	
	public boolean updateNewsPriority(NewsPriority newsPriority)
	{
		return newsPriorityDao.updateNewsPriority(newsPriority);
	}

	public NewsPriority getNewsPriority(String name)
	{
		return newsPriorityDao.getNewsPriority(name);
	}
	
	
	public List<NewsPriority> getNewsPriorityList()
	{
		return newsPriorityDao.getNewsPriorityList();
	}
	
	
	public long getNewsPriorityCount()
	{
		return newsPriorityDao.getNewsPriorityCount();
	}
	
	public List<NewsPriority> getNewsPriorityByPage(Pager pager)
	{
		return newsPriorityDao.getNewsPriorityByPage(pager);
	}
	
	public boolean deleteAllNewsPriority(){
		return this.newsPriorityDao.deleteAllNewsPriority();
	}
	
	
	
	/**
	 * ��ȡ������Ͷ���List
	 * @param filePath �������xml�ļ�·��
	 * @return
	 * @throws IOException 
	 */
	
	public List<NewsPriority> GetNewsPriorityFromXMLFile(String filePath) throws IOException{
		List<NewsPriority> ntList = null;
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
	
	public List<NewsPriority> GetNewsPriorityFromXMLFile(InputStream is) throws IOException{
		List<NewsPriority> ntList = null;
		
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
	
	
	private List<NewsPriority> GetFromCommonList(List<CommonXMLData> cxmList){
		
		List<NewsPriority> newsPriorityList=new ArrayList<NewsPriority>();
		
		for(CommonXMLData commonXMLData:cxmList)
		{
			
			List<KeyValueData> keyValueDatas=commonXMLData.getLanguageList();
			
			
			for(KeyValueData kv:keyValueDatas)
			{
				NewsPriority newsPriority=new NewsPriority();
				
				newsPriority.setCode(commonXMLData.getTopicId());
				newsPriority.setNp_id(commonXMLData.getId());
				
				
				newsPriority.setLanguage(kv.getKey());
				newsPriority.setName(kv.getValue());
				
				newsPriorityList.add(newsPriority);
				
			}
			
		}
		
		return newsPriorityList;
	}
	
	/**
	 * ��ȡ����ѡ��Ļ���ݶ����б�
	 * @return
	 */
	public List<KeyValueData> getBaseDataForBind(){
		//TODO ��Ҫ��ȡ��ǰ�ֻ��ϵͳ���������ò�ͬ�����ֲ�ѯ��Ŀǰ��д�������ġ�
		List<NewsPriority> newsPriorityList = newsPriorityDao.getNewsPriorityList("zh-CN");
		List<KeyValueData> keyValueDataList = null;
		KeyValueData kvDataInfo = null;
		if(newsPriorityList != null && newsPriorityList.size()>0){
			keyValueDataList = new ArrayList<KeyValueData>();
			for(NewsPriority np : newsPriorityList){
				kvDataInfo = new KeyValueData();
				kvDataInfo.setKey(np.getName());
				kvDataInfo.setValue(np.getCode());
				
				keyValueDataList.add(kvDataInfo);
			}
		}
		return keyValueDataList;
	}
	
	
}
