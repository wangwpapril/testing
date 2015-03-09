package com.cuc.miti.phone.xmc.logic;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.dao.KeywordsDao;
import com.cuc.miti.phone.xmc.domain.ComeFromAddress;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.Keywords;
import com.cuc.miti.phone.xmc.domain.Pager;

import android.content.Context;

public class KeywordsService {
	private Context context;
	private KeywordsDao keywordsDao;
	
	public KeywordsService(Context context){
		this.context = context;
		this.keywordsDao = new KeywordsDao(context);
		
	}
	
	public boolean addKeywords(Keywords keywords){
		return this.keywordsDao.addKeywords(keywords);
	}
	
	public boolean deleteKeywords(String name){
		return this.keywordsDao.deleteKeywords(name);
	}
	
	public boolean deleteAllKeywords(){
		return this.keywordsDao.deleteAllKeywords();
	}
	
	public boolean updateKeywords(Keywords keywords){
		return this.keywordsDao.updateKeywords(keywords);
	}
	
	public Keywords getKeywords(String name){
		return this.keywordsDao.getKeywords(name);
	}
	
	public List<Keywords> getKeywordsList(){
		return this.keywordsDao.getKeywordsList();
	}
	
	public List<Keywords> getKeywordsListByPage(Pager pager){
		return this.keywordsDao.getKeywordsByPage(pager);
	}
	
	public long getKeywordsCount(){
		return this.keywordsDao.getKeywordsCount();
	}
	
	/**
	 * ��ȡ����ѡ��Ļ���ݶ����б�
	 * @return
	 */
	public List<KeyValueData> getBaseDataForBind(String queryString){
		//TODO ��Ҫ��ȡ��ǰ�ֻ��ϵͳ���������ò�ͬ�����ֲ�ѯ��Ŀǰ��д�������ġ�
		List<Keywords> keywordsList = keywordsDao.getKeywordsList("All-Language",queryString);
		List<KeyValueData> keyValueDataList = null;
		KeyValueData kvDataInfo = null;
		if(keywordsList != null && keywordsList.size()>0){
			keyValueDataList = new ArrayList<KeyValueData>();
			for(Keywords k : keywordsList){
				kvDataInfo = new KeyValueData();
				kvDataInfo.setKey(k.getName());
				kvDataInfo.setValue(k.getCode());
				
				keyValueDataList.add(kvDataInfo);
			}
		}
		return keyValueDataList;
	}

}
