package com.cuc.miti.phone.xmc.logic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.cuc.miti.phone.xmc.dao.RegionDao;
import com.cuc.miti.phone.xmc.domain.CommonXMLData;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.NewsCategory;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Region;
import com.cuc.miti.phone.xmc.domain.TreeNode;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;
import com.cuc.miti.phone.xmc.R;

public class RegionService {
	
private Context context;
	
	private RegionDao regionDao;
	
	
	public RegionService(Context context){
		this.context = context;
		this.regionDao = new RegionDao(context);
	}
	
	
	public boolean addRegion(Region region)
	{
		return regionDao.addRegion(region);
		
	}
	
	public boolean multiInsert(List<Region> regionList)
	{
		return regionDao.multiInsert(regionList);
	}
	
	public boolean deleteRegion(String name)
	{
		return regionDao.deleteRegion(name);
	}
	
	public boolean updateRegion(Region region)
	{
		return regionDao.updateRegion(region);
	}

	public Region getRegion(String name)
	{
		return regionDao.getRegion(name);
	}
	
	
	public List<Region> getRegionList()
	{
		return regionDao.getRegionList();
	}
	
	
	public long getRegionCount()
	{
		return regionDao.getRegionCount();
	}
	
	public List<Region> getRegionByPage(Pager pager)
	{
		return regionDao.getRegionByPage(pager);
	}
	
	public boolean deleteAllRegion(){
		return this.regionDao.deleteAllRegion();
	}
	
	
	
	/**
	 * ��ȡ������Ͷ���List
	 * @param filePath �������xml�ļ�·��
	 * @return
	 * @throws IOException 
	 */
	
	public List<Region> GetRegionFromXMLFile(String filePath) throws IOException{
		List<Region> ntList = null;
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
	
	public List<Region> GetRegionFromXMLFile(InputStream is) throws IOException{
		List<Region> ntList = null;
		
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
	
	
	private List<Region> GetFromCommonList(List<CommonXMLData> cxmList){
		
		List<Region> regionList=new ArrayList<Region>();
		
		for(CommonXMLData commonXMLData:cxmList)
		{
			
			List<KeyValueData> keyValueDatas=commonXMLData.getLanguageList();
			
			
			for(KeyValueData kv:keyValueDatas)
			{
				Region region=new Region();
				
				region.setCode(commonXMLData.getTopicId());
				region.setR_id(commonXMLData.getId());
				
				
				region.setLanguage(kv.getKey());
				region.setName(kv.getValue());
				
				regionList.add(region);
				
			}
			
		}
		
		return regionList;
	}
	
	/**
	 * ��ݴ���Ľڵ�����ȡ�ýڵ��һ���ӽڵ㣬������TreeNode����
	 * @param code ��Ҫ��ȡ�ӽڵ��б�Ľڵ�code
	 * @return ����ڵ���ӽڵ��б�
	 */
	public List<TreeNode> getBaseDataForBind(String code){
		List<Region> rList = null;
		List<TreeNode> tnList = null;
		TreeNode node= null;
		if(code != null){
			rList = getRegionList(code);
		}
		
		if(rList !=null && rList.size()>0){
			tnList = new ArrayList<TreeNode>();
			
			for(Region r : rList){
				node = new TreeNode(r.getName(),r.getCode());
				node.setIcon(R.drawable.icon_department);					//����ͼ��
				
				tnList.add(node);
			}
		}
		return tnList;
	}
	/**
	 * ��ѯ���и�code��Ӧ�ڵ���ӽڵ�
	 * @param parentCode ���ڵ�codeֵ
	 * @return
	 */
	private List<Region> getRegionList(String parentCode){
		if(parentCode.length()==1){
			return regionDao.getRegionList("zh-CN", "2");
		}
		else if(parentCode.length()>=2){
			return regionDao.getRegionList("zh-CN", parentCode, String.valueOf((parentCode.length()+3)));
		}
		else{return null;}	
	}
	
	

}
