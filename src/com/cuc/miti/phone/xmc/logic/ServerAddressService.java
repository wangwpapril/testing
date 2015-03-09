package com.cuc.miti.phone.xmc.logic;

import java.util.List;

import com.cuc.miti.phone.xmc.dao.ServerAddressDao;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.ServerAddress;

import android.content.Context;

public class ServerAddressService {
	private Context context;
	private ServerAddressDao serverAddressDao;
	
	public ServerAddressService(Context context){
		this.context = context;
		this.serverAddressDao = new ServerAddressDao(context);
	}
	
	public boolean addServerAddress(ServerAddress serverAddress){
		return serverAddressDao.addServerAddress(serverAddress);
	}
	public boolean deleteServerAddress(String name){
		return serverAddressDao.deleteServerAddress(name);
	}
	public boolean deleteAllServerAddress(){
		return serverAddressDao.deleteAllServerAddress();
	}
	public boolean updateServerAddress(ServerAddress serverAddress){
		return serverAddressDao.updateServerAddress(serverAddress);
	}
	public ServerAddress getServerAddress(String name){
		return serverAddressDao.getServerAddress(name);
	}
	public List<ServerAddress> getServerAddressList(){
		return serverAddressDao.getServerAddressList();
	}
	public List<ServerAddress> getServerAddressByPage(Pager pager){
		return serverAddressDao.getServerAddressByPage(pager);
	}
	public long getServerAddressCount(){
		return serverAddressDao.getServerAddressCount();
	}
	public ServerAddress getServerAddressById(int id){
		return serverAddressDao.getServerAddressById(id);
	}

}
