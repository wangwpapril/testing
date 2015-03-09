package com.cuc.miti.phone.xmc.logic;

import java.util.List;

import com.cuc.miti.phone.xmc.adapter.FlashMAccessoryAdapter;
import com.cuc.miti.phone.xmc.dao.AccessoriesDao;
import com.cuc.miti.phone.xmc.dao.ManuscriptTemplateDao;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.Pager;

import android.content.Context;

public class AccessoriesService {
	private Context context;
	private AccessoriesDao accessoriesDao;
	
	
	public AccessoriesService(Context context) {
		this.context = context;
		this.accessoriesDao = new AccessoriesDao(context);
	}



	public boolean addAccessories(Accessories accessories){		
		return accessoriesDao.addAccessories(accessories);		
	}
	
	public boolean deleteAccessories(String name){
		return this.accessoriesDao.deleteAccessories(name);
	}
	
	public boolean deleteAllAccessories(){
		return this.accessoriesDao.deleteAllAccessories();
	}
	
	public boolean deleteAccessoriesByID(String id){
		return this.accessoriesDao.deleteAccessoriesByID(id);
	}
	
	public boolean deleteAccessoriesByMID(String m_id){
		return this.accessoriesDao.deleteAccessoriesByID(m_id);
	}
	
	public boolean updateAccessories(Accessories accessories){
		return this.accessoriesDao.updateAccessories(accessories);
	}
	
	public Accessories getAccessories(String a_id){
		return this.accessoriesDao.getAccessories(a_id);
	}
	public List<Accessories> getAccessoriesList(){
		return this.accessoriesDao.getAccessoriesList();
	}
	public List<Accessories> getAccessoriesByPage(Pager pager){
		return this.accessoriesDao.getAccessoriesByPage(pager);
	}
	public List<Accessories> getAccessoriesListByMID(String m_id){
		return this.accessoriesDao.getAccessoriesListByMID(m_id);
	}
}
