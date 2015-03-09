package com.cuc.miti.phone.xmc.logic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.dao.ComeFromAddressDao;
import com.cuc.miti.phone.xmc.domain.ComeFromAddress;
import com.cuc.miti.phone.xmc.domain.CommonXMLData;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;

import android.content.Context;
import android.util.Log;

public class ComeFromAddressService {
	
	private Context context;
	
	private ComeFromAddressDao comeFromAddressDao;
	
	public ComeFromAddressService(Context context)
	{
		this.context = context;
		this.comeFromAddressDao=new ComeFromAddressDao(context);
	}
	
	public boolean addComeFromAddress(ComeFromAddress comeFromAddress)
	{
		return this.comeFromAddressDao.addComeFromAddress(comeFromAddress);
	}
	
	public boolean multiInsert(List<ComeFromAddress> comeFromAddressList)
	{
		return this.comeFromAddressDao.multiInsert(comeFromAddressList);
	}
	
	public boolean deleteComeFromAddress(String name)
	{
		return this.comeFromAddressDao.deleteComeFromAddress(name);
	}
	
	
	public boolean updateComeFromAddress(ComeFromAddress comeFromAddress)
	{
		return this.comeFromAddressDao.updateComeFromAddress(comeFromAddress);
	}
	
	public ComeFromAddress getComeFromAddress(String name)
	{
		return this.comeFromAddressDao.getComeFromAddress(name);	
	}
	
	public List<ComeFromAddress> getComeFromAddressList()
	{
		return this.comeFromAddressDao.getComeFromAddressList();
	}
	
	public long getComeFromAddressCount()
	{
		return this.comeFromAddressDao.getComeFromAddressCount();
	}
	
	public List<ComeFromAddress> getComeFromAddressByPage(Pager pager)
	{
		return this.comeFromAddressDao.getComeFromAddressByPage(pager);
	}
	
	public boolean deleteAllComeFromAddress(){
		return this.comeFromAddressDao.deleteAllComeFromAddress();
	}
	
	/**
	 * ��ȡ�ص����List
	 * @param filePath �������xml�ļ�·��
	 * @return
	 * @throws IOException 
	 */
	
	public List<ComeFromAddress> GetComeFromAddressFromXMLFile(String filePath) throws IOException{
		List<ComeFromAddress> pList = null;
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
	
	public List<ComeFromAddress> GetComeFromAddressFromXMLFile(InputStream is) throws IOException{
		List<ComeFromAddress> pList = null;
		
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
	
	
	private List<ComeFromAddress> GetFromCommonList(List<CommonXMLData> cxmList){
		
		List<ComeFromAddress> comeFromAddressList=new ArrayList<ComeFromAddress>();
		
		for(CommonXMLData commonXMLData:cxmList)
		{
			
			List<KeyValueData> keyValueDatas=commonXMLData.getLanguageList();
			
			List<KeyValueData> descriptionList=commonXMLData.getDescriptionList();
			
			for(KeyValueData kv:keyValueDatas)
			{
				if(commonXMLData.getTopicId() =="Xxjinm"){
					Log.d("XMLtest", "Xxjinm");
				}
				ComeFromAddress comeFromAddress=new ComeFromAddress();
				
				comeFromAddress.setCode(commonXMLData.getTopicId());
				comeFromAddress.setCa_id(commonXMLData.getId());
				
				
				comeFromAddress.setLanguage(kv.getKey());
				comeFromAddress.setName(kv.getValue());
				
				for(KeyValueData k:descriptionList)
				{
				//	if(comeFromAddress.getLanguage().equals(k.getLanguage()))
						comeFromAddress.setDesc(k.getValue());
				}
				
				comeFromAddressList.add(comeFromAddress);
				
			}
			
		}
		
		return comeFromAddressList;
	}
	
	/**
	 * ��ȡ����ѡ��Ļ���ݶ����б�
	 * @return
	 */
	public List<KeyValueData> getBaseDataForBind(String queryString){
		//TODO ��Ҫ��ȡ��ǰ�ֻ��ϵͳ���������ò�ͬ�����ֲ�ѯ��Ŀǰ��д�������ġ�
		List<ComeFromAddress> comeFromAddressList = comeFromAddressDao.getComeFromAddressList("zh-CN",queryString);
		List<KeyValueData> keyValueDataList = null;
		KeyValueData kvDataInfo = null;
		if(comeFromAddressList != null && comeFromAddressList.size()>0){
			keyValueDataList = new ArrayList<KeyValueData>();
			for(ComeFromAddress cfaItem : comeFromAddressList){
				kvDataInfo = new KeyValueData();
				kvDataInfo.setKey(cfaItem.getName());
				kvDataInfo.setValue(cfaItem.getCode());
				
				keyValueDataList.add(kvDataInfo);
			}
		}
		return keyValueDataList;
	}
}
