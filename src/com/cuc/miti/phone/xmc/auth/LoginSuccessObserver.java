package com.cuc.miti.phone.xmc.auth;

import java.util.ArrayList;
import java.util.List;

public class LoginSuccessObserver implements LoginObserver {
    private List<LoginExecutor> executorList = new ArrayList<LoginExecutor>();
    @Override
    public void addExecutors(LoginExecutor executor) {
        executorList.add(executor);
    }

    @Override
    public void removeObserver(LoginObserver observer) {
        executorList.remove(observer);
    }

    @Override
    public void removeAll() {
        executorList.clear();
    }

    @Override
    public void notifyExecutors() {
        for (LoginExecutor executor:executorList){
            executor.update();
        }
    }
}

