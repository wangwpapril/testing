package com.cuc.miti.phone.xmc.logic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xml.sax.Parser;

import android.content.Context;
import android.content.res.AssetManager;

import com.cuc.miti.phone.xmc.dao.NewsCategoryDao;
import com.cuc.miti.phone.xmc.domain.CommonXMLData;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.Keywords;
import com.cuc.miti.phone.xmc.domain.NewsCategory;
import com.cuc.miti.phone.xmc.domain.NewsType;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.TreeNode;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;
import com.cuc.miti.phone.xmc.R;

public class NewsCategoryService {

	private NewsCategoryDao newsCategoryDao;
	private Context context;
	
	public NewsCategoryService(Context context){
		this.context = context;
		this.newsCategoryDao = new NewsCategoryDao(context);
	}
	
	
	public boolean addNewsCategory(NewsCategory newsCategory)
	{
		return newsCategoryDao.addNewsCategory(newsCategory);
	}
	
	public boolean multiInsert(List<NewsCategory> newsCategoryList)
	{
		return newsCategoryDao.multiInsert(newsCategoryList);
	}
	
	public boolean deleteNewsCategory(String name)
	{
		return newsCategoryDao.deleteNewsCategory(name);
	}
	
	public boolean updateNewsCategory(NewsCategory newsCategory)
	{
		return newsCategoryDao.updateNewsCategory(newsCategory);
	}
	
	public NewsCategory getNewsCategory(String name)
	{
		return newsCategoryDao.getNewsCategory(name);
	}
	
	public List<NewsCategory> getNewsCategoryList()
	{
		return newsCategoryDao.getNewsCategoryList();
	}
	
	public long getNewsCategoryCount()
	{
		return newsCategoryDao.getNewsCategoryCount();
	}
	
	public List<NewsCategory> getNewsCategoryByPage(Pager pager)
	{
		return newsCategoryDao.getNewsCategoryByPage(pager);
	}
	
	public boolean deteteAllNewsCategory(){
		return this.newsCategoryDao.deleteAllNewsCategory();
	}
	
	
	/**
	 * ��ȡ���ŷ������List
	 * @param filePath �������xml�ļ�·��
	 * @return
	 * @throws IOException 
	 */
	public List<NewsCategory> GetNewsCategoryDataFromXMLFile(String filePath) throws IOException{
		List<NewsCategory> ncList = null;
		File file = new File(filePath);
		InputStream is = null;   
		try {     
			is = new BufferedInputStream(new FileInputStream(file));
			List<CommonXMLData> cxdList = XMLDataHandle.parser(is);
			
			if(cxdList != null){
				ncList =  this.GetFromCommonList(cxdList);
			}
		}catch(Exception ex){
			Logger.e(ex);
		}finally{
			if (is != null) {
				is.close();
			} 
			
		}
		return ncList;
	}
	
	public List<NewsCategory> GetNewsCategoryDataFromXMLFile(InputStream is) throws IOException{
		List<NewsCategory> ncList = null;
		try {     
			
			List<CommonXMLData> cxdList = XMLDataHandle.parser(is);
			
			if(cxdList != null){
				ncList =  this.GetFromCommonList(cxdList);
			}
		}catch(Exception ex){
			Logger.e(ex);
		}finally{
			if (is != null) {
				is.close();
			} 
			
		}
		return ncList;
	}
	
	private List<NewsCategory> GetFromCommonList(List<CommonXMLData> cxmList){
		
		List<NewsCategory> newsCategoryList=new ArrayList<NewsCategory>();
		
		for(CommonXMLData commonXMLData:cxmList)
		{
			
			List<KeyValueData> keyValueDatas=commonXMLData.getLanguageList();
			
			List<KeyValueData> descriptionList=commonXMLData.getDescriptionList();
			
			for(KeyValueData kv:keyValueDatas)
			{
				NewsCategory newsCategory=new NewsCategory();
				
				newsCategory.setCode(commonXMLData.getTopicId());
				newsCategory.setNc_id(commonXMLData.getId());
				

				newsCategory.setLanguage(kv.getKey());
				newsCategory.setName(kv.getValue());
				
				for(KeyValueData k:descriptionList)
				{
					if(newsCategory.getLanguage().equals(k.getKey()))
						newsCategory.setDesc(k.getValue());
				}
				
				newsCategoryList.add(newsCategory);
				
				
			}
			
		}
		
		return newsCategoryList;
		
	}
	
	/**
	 * �����µ�XML�ļ��е���ݵ�����ݿ�(��Ҫȫ����գ�Ȼ�������)
	 * @return
	 */
	public boolean DataImporToDB(){
		return false;
	}
	
	/**
	 * ��ȡ����ѡ��Ļ���ݶ����б�(�ݹ飬�������������С���)
	 * @return
	 */
	public TreeNode getBaseDataForBind(){
		//TODO ��Ҫ��ȡ��ǰ�ֻ��ϵͳ���������ò�ͬ�����ֲ�ѯ��Ŀǰ��д�������ġ�
		List<NewsCategory> newsCategoryList = null;
		
		TreeNode rootNode = new TreeNode("����", "0");				//���ø�ڵ�
		rootNode.setIcon(R.drawable.icon_department);					//����ͼ��
			
		rootNode = this.getNode("0" ,rootNode);
		
		return rootNode;
	}
	
	/**
	 * �ݹ��ȡ��
	 * @param code ���ڵ�id 
	 * @return
	 */
	public TreeNode getNode(String code,TreeNode parentNode){ 
		List<NewsCategory> ncList = this.getNewCategoryList(code);
		TreeNode currentNode;
		if(ncList !=null && ncList.size()!=0){//�ж��Ƿ���Ҫ��������ˣ�˵��û���ӽڵ���
			for(int i=0;i<ncList.size();i++){ 
				currentNode = new TreeNode(ncList.get(i).getName(),ncList.get(i).getCode());
				currentNode.setIcon(R.drawable.icon_department);									//����ͼ��
				currentNode.setParent(parentNode);															//�����ýڵ�ĸ��ڵ�
				parentNode.add(currentNode);																	//���ӽڵ���ӵ����ڵ���
				getNode(ncList.get(i).getCode(),currentNode); 										    //��ݵ�ǰid��ѯ�ӽڵ�  
		    }
		 }	
		return parentNode;
	}
	
	/**
	 * ��ݴ���Ľڵ�����ȡ�ýڵ��һ���ӽڵ㣬������TreeNode����
	 * @param code ��Ҫ��ȡ�ӽڵ��б�Ľڵ�code
	 * @return ����ڵ���ӽڵ��б�
	 */
	public List<TreeNode> getBaseDataForBind(String code){
		List<NewsCategory> ncList = null;
		List<TreeNode> tnList = null;
		TreeNode node= null;
		if(code != null){
			ncList = getNewCategoryList(code);
		}
		
		if(ncList !=null && ncList.size()>0){
			tnList = new ArrayList<TreeNode>();
			
			for(NewsCategory nc : ncList){
				node = new TreeNode(nc.getName(),nc.getCode());
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
	private List<NewsCategory> getNewCategoryList(String parentCode){
		if(parentCode.length()==1){
			return newsCategoryDao.getNewsCategoryList("zh-CN", "2");
		}
		else if(parentCode.length()>=2){
			return newsCategoryDao.getNewsCategoryList("zh-CN", parentCode, String.valueOf((parentCode.length()+3)));
		}
		else{return null;}	
	}
}
