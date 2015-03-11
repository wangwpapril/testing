package com.cuc.miti.phone.xmc.store.beans;

import java.util.List;

import com.cuc.miti.phone.xmc.models.User;
import com.cuc.miti.phone.xmc.store.Bean;

import android.database.Cursor;


public class UserTable extends Bean {

	public static final String TB_NAME = "user";

    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String USER_PASSWORD = "user_pwd";
    private static final String USER_TOKEN = "user_token";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String IS_LOGIN = "is_login";
    private static final String IS_AUTO_LOGIN = "is_auto_login";

    private static UserTable mInstance;
    public static UserTable getInstance(){
        if(mInstance == null){
            mInstance = new UserTable();
        }
        return mInstance;
    }

    @Override
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TB_NAME
                + " (" + USER_ID + " TEXT"           + ","
                       + USER_NAME + " TEXT"         + ","
                       + USER_PASSWORD + " TEXT"     + ","
                       + USER_TOKEN + " TEXT"        + ","
                       + PHONE_NUMBER + " TEXT"      + ","
                       + IS_LOGIN + " INTEGER"     + ","
                       + IS_AUTO_LOGIN + " INTEGER)";
        getDatabase().execSql(sql);
    }

    /**
     * you can get user by calling the method ,
     * if null returned,it's not login, otherwise,
     * you need to judge whether the user is login by the method of isLogin() of user；
     * @return
     */
    public User getUser() {
        String sql = "SELECT * FROM " + TB_NAME + " WHERE " + IS_LOGIN  +" = 1";
        Cursor cursor = null;
        User user = null;
        try {
            cursor = db.getDb().rawQuery(sql, new String[]{});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                user = new User();
                user.id = cursor.getString(cursor.getColumnIndex(USER_ID));
                user.name = cursor.getString(cursor.getColumnIndex(USER_NAME));
                user.password = cursor.getString(cursor.getColumnIndex(USER_PASSWORD));
                user.accessToken = cursor.getString(cursor.getColumnIndex(USER_TOKEN));
                user.phoneNumber = cursor.getString(cursor.getColumnIndex(PHONE_NUMBER));
                //user.isLogin = cursor.getInt(cursor.getColumnIndex(IS_LOGIN)) == 1;
                user.isLogin = true;
                user.isAutoLogin = cursor.getInt(cursor.getColumnIndex(IS_AUTO_LOGIN)) == 1;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return user;
    }
    /**
     * @author xudsh
     * @return
     */
    public String getLoginUserId(){
    	String userId = "";
    	Cursor cursor = null;
        String sql = "SELECT " + USER_ID + " FROM " + TB_NAME + " WHERE " + IS_LOGIN  +" = 1";
        try {
        	cursor = db.getDb().rawQuery(sql, null);
            cursor.moveToFirst();
            userId = cursor.getString(cursor.getColumnIndex(USER_ID));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        return userId;
    }

    /**
     * when you login success,you need call the method of saveUser().
     *
     * @param user
     */
    public void saveUser(User user) {
        User loginUser = getUser();
        if (loginUser != null) {
            deleteData();
        }
        if (user != null) {
            String sql = "insert into " + TB_NAME
                    + " values(" + user.id                        + ","
                                 + "'" + user.name        + "'"    + ","
                                 + "'" + user.password    + "'"    + ","
                                 + "'" + user.accessToken + "'"    + ","
                                 + "'" + user.phoneNumber + "'"    + ","
                                 + (user.isLogin ? 1 : 0 )            + ","
                                 + "1"                                  + ")";
            db.execSql(sql);
        }
    }

    /**
     * when you logout,you must call the method.
     *@param flag 退出是否成功
     */
    public void update(boolean flag) {
        String sql = "UPDATE " + TB_NAME
                + " SET " + IS_LOGIN + " = " + (flag ? 0 : 1);
        db.execSql(sql);
    }

    public void updateAutoLogin(boolean isAuto){
        String sql = "UPDATE " + TB_NAME
                + " SET " + IS_AUTO_LOGIN + " = " + (isAuto ? 1 : 0);
        db.execSql(sql);
    }

    // clear data of table
    public void deleteData() {
        String sql = "DELETE FROM " + TB_NAME;
        db.execSql(sql);
    }
    
}
