package com.cuc.miti.phone.xmc.auth;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class LoginForServerDataObserver implements LoginExecutor {

    private ExecutorService mExecutorService;

    public LoginForServerDataObserver() {
        mExecutorService = Executors.newSingleThreadExecutor();
    }
    
    @Override
    public void update() {
        getFavoriteDealIds();
//        getUserIntegral();
    }

    //获取我收藏的Deal ids
    public void getFavoriteDealIds() {

        FutureTask<Integer> mTask = new FutureTask<Integer>(new FavoriteDealTask());
        mExecutorService.execute(mTask);
    }

    //获取我收藏的用户积分数量
    public void getUserIntegral() {

    }

    private class FavoriteDealTask implements Callable<Integer> {
        
        @Override
        public Integer call() throws Exception {
            
            // String result =  ServiceManager.getNetworkService().getSync(LeyouApi.LEYOU_WEIBO_SN);

            // JSONObject obj = new JSONObject(result);
            // JSONArray array = obj.getJSONArray("myDealIds");

            return 0;
        }
    }

}
