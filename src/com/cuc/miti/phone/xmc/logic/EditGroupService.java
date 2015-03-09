package com.cuc.miti.phone.xmc.logic;

import java.util.List;

import com.cuc.miti.phone.xmc.dao.EditGroupDao;
import com.cuc.miti.phone.xmc.domain.EditGroup;
import com.cuc.miti.phone.xmc.domain.Pager;

import android.content.Context;

public class EditGroupService {
	private Context context;
	private EditGroupDao editGroupDao;
	public EditGroupService(Context context){
		this.context = context;
		this.editGroupDao = new EditGroupDao(context);
	}
	
	public boolean addEditGroup(EditGroup editGroup){
		return this.editGroupDao.addEditGroup(editGroup);
	}
	public boolean deleteEditGroup(String name){
		return this.editGroupDao.deleteEditGroup(name);
	}
	public boolean deleteAllEditGroup(){
		return this.editGroupDao.deleteAllEditGroup();
	}
	public boolean updateEditGroup(EditGroup editGroup){
		return this.editGroupDao.updateEditGroup(editGroup);
	}
	public EditGroup getEditGroup(String name){
		return this.editGroupDao.getEditGroup(name);
	}
	public List<EditGroup> getEditGroupList(){
		return this.editGroupDao.getEditGroupList();
	}
	public List<EditGroup> getEditGroupByPage(Pager pager){
		return this.editGroupDao.getEditGroupByPage(pager);
	}
	public long getEditGroupCount(){
		return this.editGroupDao.getEditGroupCount();
	}
	

}
