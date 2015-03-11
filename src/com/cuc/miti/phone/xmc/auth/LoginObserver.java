package com.cuc.miti.phone.xmc.auth;

public interface LoginObserver {

	public void addExecutors(LoginExecutor observer);
    public void removeObserver(LoginObserver observer);
    public void removeAll();
    public void notifyExecutors();
    
}
