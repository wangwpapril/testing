package com.cuc.miti.phone.xmc.dao.test;

import java.util.List;

import com.cuc.miti.phone.xmc.dao.AccessoriesDao;
import com.cuc.miti.phone.xmc.dao.ComeFromAddressDao;
import com.cuc.miti.phone.xmc.dao.EditGroupDao;
import com.cuc.miti.phone.xmc.dao.KeywordsDao;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.ComeFromAddress;
import com.cuc.miti.phone.xmc.domain.EditGroup;
import com.cuc.miti.phone.xmc.domain.Keywords;
import com.cuc.miti.phone.xmc.domain.Pager;

import android.test.AndroidTestCase;
import android.util.Log;

public class AccessoriesDaoTest extends AndroidTestCase  {
	private static final String TAG = "Accessories";
	private static final String TAG1 = "ComeFromAddress";
	private static final String TAG2 = "EditGroup";
	private static final String TAG3 = "Keywords";
	
	//����Accessories
	public void testAddAccessoriesDao(){
		AccessoriesDao accessoriesDao = new AccessoriesDao(this.getContext());
		
		for(int i =0 ;i<10; i++){
			Accessories accessories = new Accessories();
			accessories.setM_id("m_id"+i);
			accessories.setCreatetime("createtime"+i);
			accessories.setDesc("desc"+i);
			accessories.setInfo("info"+i);
			accessories.setOriginalName("originalName"+i);
			accessories.setSize("size"+i);
			accessories.setTitle("title"+i);
			accessories.setType("type"+i);
			accessories.setUrl("url"+i);
			accessoriesDao.addAccessories(accessories);
		}
		List<Accessories> accessoriesList = accessoriesDao.getAccessoriesList();
		for(Accessories a : accessoriesList){
			Log.i(TAG, a.toString());
		}
	}
	
	public void testDeleteAccessoriesDao(){
		AccessoriesDao accessoriesDao = new AccessoriesDao(this.getContext());		

		Accessories accessories = accessoriesDao.getAccessories("originalName3");
		
		Log.i(TAG, accessories.toString());
		
		accessoriesDao.deleteAccessories("originalName1");

		Log.i(TAG, accessoriesDao.getAccessoriesCount()+"");
		List<Accessories> accessoriesList = accessoriesDao.getAccessoriesList();
		for(Accessories a : accessoriesList){
			Log.i(TAG, a.toString());
		}
	}
	public void testUpdateAccessoriesDao(){
		AccessoriesDao accessoriesDao = new AccessoriesDao(this.getContext());	
		Accessories accessories = accessoriesDao.getAccessories("originalName1");		
	    accessories.setM_id("m1");
		accessories.setCreatetime("c1");
		accessories.setDesc("d1");
		accessories.setInfo("i1");
		accessories.setTitle("t1");
		accessories.setSize("s1");
		accessories.setType("t1");
		accessories.setUrl("u1");
		
		accessoriesDao.updateAccessories(accessories);
		Log.i(TAG, accessories.toString());
	}
	public void testGetAccessoriesByPage(){
		AccessoriesDao accessoriesDao = new AccessoriesDao(this.getContext());
		Pager pager = new Pager();
		pager.setCurrentPage(3);
		pager.setPageSize(3);
		pager.setTotalNum(Integer.parseInt(accessoriesDao.getAccessoriesCount()+""));
		Log.i(TAG, accessoriesDao.getAccessoriesCount()+"");
		
		List<Accessories> accessoriesByPageList = accessoriesDao.getAccessoriesByPage(pager);
		
		for(Accessories a : accessoriesByPageList){
		Log.i(TAG,a.toString());
		}		
	}
	
	public void testDeleteAllAccessoried(){
		AccessoriesDao accessoriesDao = new AccessoriesDao(this.getContext());
		accessoriesDao.deleteAllAccessories();
		List<Accessories> accessoriesList = accessoriesDao.getAccessoriesList();
		for(Accessories a : accessoriesList){
			Log.i(TAG, a.toString());
		}
	}
	
//	public void testGetAccessoriesById(){
//		AccessoriesDao accessoriesDao = new AccessoriesDao(this.getContext());
//		Accessories accessories = accessoriesDao.getAccessoriesById(22);
//		Log.i(TAG, accessories.toString());
//	}
//	
	//����ComeFromAddressDao
	public void testAddComeFromAddressDao(){	
		ComeFromAddressDao comeFromAddressDao = new ComeFromAddressDao(this.getContext());
		for(int i = 16; i<19 ;i++){
			ComeFromAddress comeFromAddress = new ComeFromAddress();
			comeFromAddress.setCa_id("caId"+i);
			comeFromAddress.setCode("code"+i);
			comeFromAddress.setDesc("desc"+i);
			comeFromAddress.setLanguage("language"+i);
			comeFromAddress.setName("name"+i);
			comeFromAddressDao.addComeFromAddress(comeFromAddress);
		}
		List<ComeFromAddress> comeFromAddressList = comeFromAddressDao.getComeFromAddressList();
		for(ComeFromAddress c :comeFromAddressList){
			Log.i(TAG1 , c.toString());
		}
	}
	
	public void testDeleteComeFromAddressDao(){
		ComeFromAddressDao comeFromAddressDao = new ComeFromAddressDao(this.getContext());
		comeFromAddressDao.deleteComeFromAddress("name2");
	}
	public void testUpdateComeFromAddressDao(){
		ComeFromAddressDao comeFromAddressDao = new ComeFromAddressDao(this.getContext());
		ComeFromAddress comeFromAddress = comeFromAddressDao.getComeFromAddress("name5");
		comeFromAddress.setCa_id("c");
		comeFromAddress.setCode("c");
		comeFromAddress.setDesc("d");
		comeFromAddress.setLanguage("l");
		comeFromAddressDao.updateComeFromAddress(comeFromAddress);
		Log.i(TAG1,comeFromAddress.toString());		
	}
	public void testGetComeFromAddressDao(){
		ComeFromAddressDao comeFromAddressDao = new ComeFromAddressDao(this.getContext());
		Pager pager = new Pager();
		pager.setCurrentPage(2);
		pager.setPageSize(3);
		pager.setTotalNum(Integer.parseInt(comeFromAddressDao.getComeFromAddressCount()+""));
		Log.i(TAG1, comeFromAddressDao.getComeFromAddressCount()+"");
		List<ComeFromAddress> comeFromAddressList =comeFromAddressDao.getComeFromAddressByPage(pager);
		for(ComeFromAddress c :comeFromAddressList){
			Log.i(TAG1, c.toString());
		}		
	}
	
	//����EditGroup
	public void testAddEditGroupDao(){	
		EditGroupDao editGroupDao = new EditGroupDao(this.getContext());
		for(int i = 16; i<17 ;i++){
			EditGroup editGroup = new EditGroup();
			editGroup.setEg_id("egId"+i);
			editGroup.setCode("code"+i);
			editGroup.setLanguage("language"+i);
			editGroup.setName("name"+i);
			editGroupDao.addEditGroup(editGroup);
		}
		List<EditGroup> editGroupList = editGroupDao.getEditGroupList();
		for(EditGroup e :editGroupList){
			Log.i(TAG2 , e.toString());
		}
	}
	
	public void testDeleteEditGroupDao(){
		EditGroupDao editGroupDao = new EditGroupDao(this.getContext());
		editGroupDao.deleteEditGroup("name11");
	}
	public void testUpdateEditGroupDao(){
		EditGroupDao editGroupDao = new EditGroupDao(this.getContext());
		EditGroup editGroup = editGroupDao.getEditGroup("name12");
		editGroup.setEg_id("e");
		editGroup.setCode("c");;
		editGroup.setLanguage("l");
		editGroupDao.updateEditGroup(editGroup);
		Log.i(TAG2,editGroup.toString());		
	}
	public void testGetEditGroupByPageDao(){
		EditGroupDao editGroupDao = new EditGroupDao(this.getContext());
		Pager pager = new Pager();
		pager.setCurrentPage(2);
		pager.setPageSize(3);
		pager.setTotalNum(Integer.parseInt(editGroupDao.getEditGroupCount()+""));
		Log.i(TAG2, editGroupDao.getEditGroupCount()+"");
		List<EditGroup> editGroupList =editGroupDao.getEditGroupByPage(pager);
		for(EditGroup e :editGroupList){
			Log.i(TAG2, e.toString());
		}		
	}
	
	//����Keywords
	public void testAddKeywordsDao(){	
		KeywordsDao keywordsDao = new KeywordsDao(this.getContext());
		for(int i = 10; i<15 ;i++){
			Keywords keywords = new Keywords();
			keywords.setK_id("kId"+i);
			keywords.setCode("code"+i);
			keywords.setLanguage("language"+i);
			keywords.setName("name"+i);
			keywordsDao.addKeywords(keywords);
		}
		List<Keywords> keywordsList = keywordsDao.getKeywordsList();
		for(Keywords e :keywordsList){
			Log.i(TAG3 , e.toString());
		}
	}
	
	public void testDeleteKeywordsDao(){
		KeywordsDao keywordsDao = new KeywordsDao(this.getContext());
		keywordsDao.deleteKeywords("name2");
		List<Keywords> keywordsList = keywordsDao.getKeywordsList();
		for(Keywords e :keywordsList){
			Log.i(TAG3 , e.toString());
		}
	}
	public void testUpdateKeywordsDao(){
		KeywordsDao keywordsDao = new KeywordsDao(this.getContext());
		Keywords keywords = keywordsDao.getKeywords("name3");
		keywords.setK_id("k");
		keywords.setCode("c");;
		keywords.setLanguage("l");
		keywordsDao.updateKeywords(keywords);
		Log.i(TAG3,keywords.toString());		
	}
	public void testGetKeywordsByPageDao(){
		KeywordsDao keywordsDao = new KeywordsDao(this.getContext());
		Pager pager = new Pager();
		pager.setCurrentPage(2);
		pager.setPageSize(3);
		pager.setTotalNum(Integer.parseInt(keywordsDao.getKeywordsCount()+""));
		Log.i(TAG3, keywordsDao.getKeywordsCount()+"");
		List<Keywords> keywordsList =keywordsDao.getKeywordsByPage(pager);
		for(Keywords e :keywordsList){
			Log.i(TAG3, e.toString());
		}		
	}
}
