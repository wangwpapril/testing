package com.cuc.miti.phone.xmc.logic;

import java.util.List;

import com.cuc.miti.phone.xmc.dao.SalaryEarnerSendToAddressDao;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.SalaryEarnerSendToAddress;

import android.content.Context;

public class SalaryEarnerSendToAddressService {
	private Context context;
	private SalaryEarnerSendToAddressDao salaryEarnerSendToAddressDao;
	public SalaryEarnerSendToAddressService(Context context){
		this.context = context;
		this.salaryEarnerSendToAddressDao = new SalaryEarnerSendToAddressDao(context);
	}
	
	public boolean addSalaryEarnerSendToAddress(SalaryEarnerSendToAddress salaryEarnerSendToAddress){
		return salaryEarnerSendToAddressDao.addSalaryEarnerSendToAddress(salaryEarnerSendToAddress);
	}
	public boolean deleteSalaryEarnerSendToAddress(String name){
		return salaryEarnerSendToAddressDao.deleteSalaryEarnerSendToAddress(name);
	}
	public boolean deleteAllSalaryEarnerSendToAddress(){
		return salaryEarnerSendToAddressDao.deleteAllSalaryEarnerSendToAddress();
	}
	public boolean updateSalaryEarnerSendToAddress(SalaryEarnerSendToAddress salaryEarnerSendToAddress){
		return salaryEarnerSendToAddressDao.updateSalaryEarnerSendToAddress(salaryEarnerSendToAddress);
	}
	public SalaryEarnerSendToAddress getSalaryEarnerSendToAddress(String name){
		return salaryEarnerSendToAddressDao.getSalaryEarnerSendToAddress(name);
	}
	public List<SalaryEarnerSendToAddress> getSalaryEarnerSendToAddressList(){
		return salaryEarnerSendToAddressDao.getSalaryEarnerSendToAddressList();
	}
	public List<SalaryEarnerSendToAddress> getSalaryEarnerSendToAddressByPage(Pager pager){
		return salaryEarnerSendToAddressDao.getSalaryEarnerSendToAddressByPage(pager);
	}
	public long getSalaryEarnerSendToAddressCount(){
		return salaryEarnerSendToAddressDao.getSalaryEarnerSendToAddressCount();
	}
	public SalaryEarnerSendToAddress getSalaryEarnerSendToAddressById(int id){
		return salaryEarnerSendToAddressDao.getSalaryEarnerSendToAddressById(id);
	}

}
