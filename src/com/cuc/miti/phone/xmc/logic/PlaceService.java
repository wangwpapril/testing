package com.cuc.miti.phone.xmc.logic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.dao.PlaceDao;
import com.cuc.miti.phone.xmc.domain.CommonXMLData;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.NewsPriority;
import com.cuc.miti.phone.xmc.domain.NewsType;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Place;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;

import android.content.Context;

public class PlaceService {
	
	private Context context;
	
	private PlaceDao placeDao;
	
	public PlaceService(Context context)
	{
		this.context = context;
		this.placeDao=new PlaceDao(context);
	}
	
	public boolean addPlace(Place place)
	{
		return this.placeDao.addPlace(place);
	}
	
	public boolean multiInsert(List<Place> placeList )
	{
		return this.placeDao.multiInsert(placeList);
	}
	
	public boolean deletePlace(String name)
	{
		return this.placeDao.deletePlace(name);
	}
	
	
	public boolean updatePlace(Place place)
	{
		return this.placeDao.updatePlace(place);
	}
	
	public Place getPlace(String name)
	{
		return this.placeDao.getPlace(name);	
	}
	
	public List<Place> getPlaceList()
	{
		return this.placeDao.getPlaceList();
	}
	
	public long getPlaceCount()
	{
		return this.placeDao.getPlaceCount();
	}
	
	public List<Place> getPlaceByPage(Pager pager)
	{
		return this.placeDao.getPlaceByPage(pager);
	}
	
	public boolean deleteAllPlace(){
		return this.placeDao.deleteAllPlace();
	}
	
	/**
	 * ��ȡ�ص����List
	 * @param filePath �������xml�ļ�·��
	 * @return
	 * @throws IOException 
	 */
	
	public List<Place> GetPlaceFromXMLFile(String filePath) throws IOException{
		List<Place> pList = null;
		File file = new File(filePath);
		InputStream is = null;   
		try {     
			is = new BufferedInputStream(new FileInputStream(file));
			List<CommonXMLData> cxdList = XMLDataHandle.parser(is);
			
			if(cxdList != null){
				pList =  this.GetFromCommonList(cxdList);
			}
		}catch(Exception ex){
			Logger.e(ex);
		}finally{
			if (is != null) {
				is.close();
			} 
			
		}	
		return pList;
	}
	
	public List<Place> GetPlaceFromXMLFile(InputStream is) throws IOException{
		List<Place> pList = null;
		
		try {     
			List<CommonXMLData> cxdList = XMLDataHandle.parser(is);
			
			if(cxdList != null){
				pList =  this.GetFromCommonList(cxdList);
			}
		}catch(Exception ex){
			Logger.e(ex);
		}finally{
			if (is != null) {
				is.close();
			} 
			
		}	
		return pList;
	}
	
	
	private List<Place> GetFromCommonList(List<CommonXMLData> cxmList){
		
		List<Place> placeList=new ArrayList<Place>();
		
		for(CommonXMLData commonXMLData:cxmList)
		{
			
			List<KeyValueData> keyValueDatas=commonXMLData.getLanguageList();
			
			List<KeyValueData> descriptionList=commonXMLData.getDescriptionList();
			
			for(KeyValueData kv:keyValueDatas)
			{
				Place place=new Place();
				
				place.setCode(commonXMLData.getTopicId());
				place.setP_id(commonXMLData.getId());
				
				
				place.setLanguage(kv.getKey());
				place.setName(kv.getValue());
				
				for(KeyValueData k:descriptionList)
				{
					if(place.getLanguage().equals(k.getKey()))
						place.setDesc(k.getValue());
				}
				
				placeList.add(place);
				
			}
			
		}
		
		return placeList;
	}
	
	/**
	 * ��ȡ����ѡ��Ļ���ݶ����б�
	 * @return
	 */
	public List<KeyValueData> getBaseDataForBind(String queryString){
		//TODO ��Ҫ��ȡ��ǰ�ֻ��ϵͳ���������ò�ͬ�����ֲ�ѯ��Ŀǰ��д�������ġ�
		List<Place> placeList = placeDao.getPlaceList("zh-CN",queryString);
		List<KeyValueData> keyValueDataList = null;
		KeyValueData kvDataInfo = null;
		if(placeList != null && placeList.size()>0){
			keyValueDataList = new ArrayList<KeyValueData>();
			for(Place p : placeList){
				kvDataInfo = new KeyValueData();
				kvDataInfo.setKey(p.getName());
				kvDataInfo.setValue(p.getCode());
				
				keyValueDataList.add(kvDataInfo);
			}
		}
		return keyValueDataList;
	}
	
	
	
	
	
}
