package com.cuc.miti.phone.xmc.store;


import com.cuc.miti.phone.xmc.ServiceManager;


public abstract class Bean {
    protected Database db;

    public Bean() {
        db = ServiceManager.getDatabaseManager().openDatabase(getDatabaseName());
        createTable();
    }

    public String getDatabaseName() {
        return "ingle_an";
    }

    public Database getDatabase() {
        return db;
    }

    /**
     * Abstract create table method
     */
    public abstract void createTable();
}
