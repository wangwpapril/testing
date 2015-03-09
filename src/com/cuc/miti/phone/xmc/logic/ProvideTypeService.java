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
import com.cuc.miti.phone.xmc.dao.ProvideTypeDao;
import com.cuc.miti.phone.xmc.domain.CommonXMLData;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.NewsCategory;
import com.cuc.miti.phone.xmc.domain.NewsPriority;
import com.cuc.miti.phone.xmc.domain.NewsType;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Place;
import com.cuc.miti.phone.xmc.domain.ProvideType;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;

import android.content.Context;

public class ProvideTypeService {
	
	private Context context;
	
	private ProvideTypeDao provideTypeDao;
	
	
	public ProvideTypeService(Context context){
		this.context = context;
		this.provideTypeDao = new ProvideTypeDao(context);
	}
	
	
	public boolean addProvideType(ProvideType provideType)
	{
		return provideTypeDao.addProvideType(provideType);
		
	}
	
	public boolean multiInsert(List<ProvideType> provideTypeList)
	{
		return provideTypeDao.multiInsert(provideTypeList);
	}
	
	public boolean deleteProvideType(String name)
	{
		return provideTypeDao.deleteProvideType(name);
	}
	
	public boolean updateProvideType(ProvideType provideType)
	{
		return provideTypeDao.updateProvideType(provideType);
	}

	public ProvideType getProvideType(String name)
	{
		return provideTypeDao.getProvideType(name);
	}
	
	
	public List<ProvideType> getProvideTypeList()
	{
		return provideTypeDao.getProvideTypeList();
	}
	
	
	public long getProvideTypeCount()
	{
		return provideTypeDao.getProvideTypeCount();
	}
	
	public List<ProvideType> getProvideTypeByPage(Pager pager)
	{
		return provideTypeDao.getProvideTypeByPage(pager);
	}
	
	public boolean deleteAllProvideType(){
		return this.deleteAllProvideType();
	}
	
	
	/**
	 * ��ȡ������Ͷ���List
	 * @param filePath �������xml�ļ�·��
	 * @return
	 * @throws IOException 
	 */
	
	public List<ProvideType> GetProvideTypeFromXMLFile(String filePath) throws IOException{
		List<ProvideType> ntList = null;
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
	
	public List<ProvideType> GetProvideTypeFromXMLFile(InputStream is) throws IOException{
		List<ProvideType> ntList = null;
		
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
	
	
	private List<ProvideType> GetFromCommonList(List<CommonXMLData> cxmList){
		
		List<ProvideType> provideTypeList=new ArrayList<ProvideType>();
		
		for(CommonXMLData commonXMLData:cxmList)
		{
			
			List<KeyValueData> keyValueDatas=commonXMLData.getLanguageList();
			
			
			for(KeyValueData kv:keyValueDatas)
			{
				ProvideType provideType=new ProvideType();
				
				provideType.setCode(commonXMLData.getTopicId());
				provideType.setPt_id(commonXMLData.getId());
				
				
				provideType.setLanguage(kv.getKey());
				provideType.setName(kv.getValue());
				
				provideTypeList.add(provideType);
				
			}
			
		}
		
		return provideTypeList;
	}
	
	/**
	 * ��ȡ����ѡ��Ļ���ݶ����б�
	 * @return
	 */
	public List<KeyValueData> getBaseDataForBind(){
		//TODO ��Ҫ��ȡ��ǰ�ֻ��ϵͳ���������ò�ͬ�����ֲ�ѯ��Ŀǰ��д�������ġ�
		List<ProvideType> provideTypeList = provideTypeDao.getProvideTypeList("zh-CN");
		List<KeyValueData> keyValueDataList = null;
		KeyValueData kvDataInfo = null;
		if(provideTypeList != null && provideTypeList.size()>0){
			keyValueDataList = new ArrayList<KeyValueData>();
			for(ProvideType pt : provideTypeList){
				kvDataInfo = new KeyValueData();
				kvDataInfo.setKey(pt.getName());
				kvDataInfo.setValue(pt.getCode());
				
				keyValueDataList.add(kvDataInfo);
			}
		}
		return keyValueDataList;
	}
	
	
}
