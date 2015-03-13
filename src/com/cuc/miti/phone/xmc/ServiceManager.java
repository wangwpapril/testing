package com.cuc.miti.phone.xmc;

import android.content.Context;



import com.cuc.miti.phone.xmc.net.NetworkService;
import com.cuc.miti.phone.xmc.store.DatabaseManager;

public class ServiceManager {
    static Context context;

    static NetworkService networkService;
    static DatabaseManager databaseManager;
//    static DataService dataService;

    public static void init(Context ctx) {
        context = ctx;
//        dataService = new DataService();
        networkService = new NetworkService();
        databaseManager = new DatabaseManager(ctx);
    }

    public static NetworkService getNetworkService() {
        return networkService;
    }
    
    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    
/*    public static DataService getDataService() {
        return dataService;
    }
 */   
}
