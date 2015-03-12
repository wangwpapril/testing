package com.cuc.miti.phone.xmc.store.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.cuc.miti.phone.xmc.models.Address;
import com.cuc.miti.phone.xmc.models.Invoice;
import com.cuc.miti.phone.xmc.models.PayType;
import com.cuc.miti.phone.xmc.models.SendType;
import com.cuc.miti.phone.xmc.store.Bean;
import com.cuc.miti.phone.xmc.utils.StringUtil;

import android.database.Cursor;
import android.util.Base64;


/*
 * 此表只有一条记录
 */
public class UserTable1 extends Bean {

	public static final String TABLE_NAME = "settlement_tab";

	private static final String USER_ID = "user_id";
    private static final String SAVE_ADDR = "save_address";
    private static final String SAVE_SEND = "save_send";
    private static final String SAVE_PAY = "save_pay";
    private static final String INVOCE = "invoce";
    private static final String REMARK = "remark";
    

    private static UserTable1 instance;

    public static UserTable1 getInstance() {
        if (null == instance) {
            instance = new UserTable1();
        }

        return instance;
    }

    @Override
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
        		+ USER_ID + " TEXT, "
                + SAVE_ADDR + " TEXT, "
                + SAVE_SEND + " TEXT, "
                + SAVE_PAY + " TEXT, "
                + INVOCE + " TEXT, "
                + REMARK + " TEXT);";
        db.execSql(sql);
    }

    public boolean saveAddr(Address address) {
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bao);
            oos.writeObject(address);
            oos.flush();
            oos.close();
            bao.close();

            String dealStream = Base64.encodeToString(bao.toByteArray(), Base64.DEFAULT);
            
            //存到当前登录用户的记录里
            String sql;
            String userId = UserTable.getInstance().getLoginUserId();
            if (getCount() > 0) {
            	sql = "UPDATE " + TABLE_NAME + " SET " + SAVE_ADDR + " = ? "  + " WHERE " + USER_ID  +" = " + userId ;
            	return db.execSql(sql, dealStream);
            } else {
            	sql = StringUtil.simpleFormat("replace into %s (%s,%s) values (?,?)", TABLE_NAME, SAVE_ADDR,USER_ID);
            	return db.execSql(sql, dealStream,userId);
            }

            
        } catch (Exception e) {
 //           Logg.e(e);
        }

        return false;
    }
    
    public boolean saveSend(SendType sendType) {
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bao);
            oos.writeObject(sendType);
            oos.flush();
            oos.close();
            bao.close();

            String dealStream = Base64.encodeToString(bao.toByteArray(), Base64.DEFAULT);
            
            String sql;
            String userId = UserTable.getInstance().getLoginUserId();
            if (getCount() > 0) {
            	sql = "UPDATE " + TABLE_NAME + " SET " + SAVE_SEND + " = ?"  + " WHERE " + USER_ID  +" = " + userId ;
            	return db.execSql(sql, dealStream);
            } else {
            	sql = StringUtil.simpleFormat("replace into %s (%s,%s) values (?,?)", TABLE_NAME, SAVE_SEND,USER_ID);
            	return db.execSql(sql, dealStream,userId);
            }

            
        } catch (Exception e) {
 //           Logg.e(e);
        }

        return false;
    }
    
    public boolean savePay(PayType payType) {
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bao);
            oos.writeObject(payType);
            oos.flush();
            oos.close();
            bao.close();

            String dealStream = Base64.encodeToString(bao.toByteArray(), Base64.DEFAULT);
            
            String sql;
            String userId = UserTable.getInstance().getLoginUserId();
            if (getCount() > 0) {
            	sql = "UPDATE " + TABLE_NAME + " SET " + SAVE_PAY + " = ?"  + " WHERE " + USER_ID  +" = " + userId ;
                return db.execSql(sql, dealStream);
            } else {
            	sql = StringUtil.simpleFormat("replace into %s (%s,%s) values (?,?)", TABLE_NAME, SAVE_PAY,USER_ID);
            	return db.execSql(sql, dealStream,userId);
            }

           
        } catch (Exception e) {
//            Logg.e(e);
        }

        return false;
    }
    
    public boolean saveInvoce(Invoice invoice) {
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bao);
            oos.writeObject(invoice);
            oos.flush();
            oos.close();
            bao.close();

            String dealStream = Base64.encodeToString(bao.toByteArray(), Base64.DEFAULT);
            
            String sql;
            String userId = UserTable.getInstance().getLoginUserId();
            if (getCount() > 0) {
            	sql = "UPDATE " + TABLE_NAME + " SET " + INVOCE + " = ?"  + 
                    " WHERE " + USER_ID  +" = " + userId;
            	return db.execSql(sql, dealStream);
            } else {
            	sql = StringUtil.simpleFormat("replace into %s (%s,%s) values (?,?)", TABLE_NAME, INVOCE,USER_ID);
            	return db.execSql(sql, dealStream,userId);
            }

        } catch (Exception e) {
 //           Logg.e(e);
        }

        return false;
    }
    
    public boolean saveRemark(String remark) {
        try {
            String sql;
            String userId = UserTable.getInstance().getLoginUserId();
            if (getCount() > 0) {
            	sql = "UPDATE " + TABLE_NAME + " SET " + REMARK + " = ?" + 
                  " WHERE " + USER_ID  +" = " + userId;
            	return db.execSql(sql, remark);
            } else {
            	sql = StringUtil.simpleFormat("replace into %s (%s,%s) values (?,?)", TABLE_NAME, REMARK,USER_ID);
            	return db.execSql(sql, remark,userId);
            }

            
        } catch (Exception e) {
//            Logg.e(e);
        }

        return false;
    }
    
    public synchronized Invoice getInvoice() {
        String sql = "SELECT " + INVOCE + " FROM " + TABLE_NAME + " WHERE " + USER_ID  +" = " + UserTable.getInstance().getLoginUserId();
        Cursor cursor = db.getDb().rawQuery(sql, null);

        return paserInvoice(cursor);
    }

    private Invoice paserInvoice(Cursor cursor) {
    	Invoice invoice = null;
        if (cursor == null || !cursor.moveToFirst()) {
            if (cursor != null) {
                cursor.close();
            }
            return invoice;
        }

        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            String data = cursor.getString(0);
            bis = new ByteArrayInputStream(Base64.decode(data, Base64.DEFAULT));
            ois = new ObjectInputStream(bis);
            Object object = ois.readObject();

            return (Invoice) object;
        } catch (Exception e) {
 //           Logg.e(e);
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
  //              Logg.e(e);
            }
            cursor.close();
        }

        return invoice;
    }
    
    public synchronized String getRemark() {
        String sql = "SELECT " + REMARK + " FROM " + TABLE_NAME  + 
        		" WHERE " + USER_ID  +" = " + UserTable.getInstance().getLoginUserId() ;
        Cursor cursor = db.getDb().rawQuery(sql, null);
        
        String result = null;
        
        if (cursor == null || !cursor.moveToFirst()) {
            if (cursor != null) {
                cursor.close();
            }
            return result;
        }
        
        result = cursor.getString(0);
        cursor.close();

        return result;
    }

    public synchronized Address getAddress() {
        String sql = "SELECT " + SAVE_ADDR + " FROM " + TABLE_NAME + " WHERE " + USER_ID  +" = " + UserTable.getInstance().getLoginUserId() ;
        Cursor cursor = db.getDb().rawQuery(sql, null);

        return paserAddress(cursor);
    }

    private Address paserAddress(Cursor cursor) {
    	Address address = null;
        if (cursor == null || !cursor.moveToFirst()) {
            if (cursor != null) {
                cursor.close();
            }
            return address;
        }

        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            String data = cursor.getString(0);
            bis = new ByteArrayInputStream(Base64.decode(data, Base64.DEFAULT));
            ois = new ObjectInputStream(bis);
            Object object = ois.readObject();

            return (Address) object;
        } catch (Exception e) {
   //         Logg.e(e);
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
 //               Logg.e(e);
            }
            cursor.close();
        }

        return address;
    }
    
    public synchronized SendType getSendType() {
        String sql = "SELECT " + SAVE_SEND + " FROM " + TABLE_NAME  + " WHERE " + USER_ID  +" = " + UserTable.getInstance().getLoginUserId();
        Cursor cursor = db.getDb().rawQuery(sql, null);

        return paserSendType(cursor);
    }

    private SendType paserSendType(Cursor cursor) {
    	SendType send = null;
        if (cursor == null || !cursor.moveToFirst()) {
            if (cursor != null) {
                cursor.close();
            }
            return send;
        }

        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            String data = cursor.getString(0);
            bis = new ByteArrayInputStream(Base64.decode(data, Base64.DEFAULT));
            ois = new ObjectInputStream(bis);
            Object object = ois.readObject();

            return (SendType) object;
        } catch (Exception e) {
 //           Logg.e(e);
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
  //              Logg.e(e);
            }
            cursor.close();
        }

        return send;
    }
    
    public synchronized PayType getPayType() {
        String sql = "SELECT " + SAVE_PAY + " FROM " + TABLE_NAME  + " WHERE " + USER_ID  +" = " + UserTable.getInstance().getLoginUserId();
        Cursor cursor = db.getDb().rawQuery(sql, null);

        return paserPayType(cursor);
    }

    private PayType paserPayType(Cursor cursor) {
    	PayType pay = null;
        if (cursor == null || !cursor.moveToFirst()) {
            if (cursor != null) {
                cursor.close();
            }
            return pay;
        }

        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            String data = cursor.getString(0);
            bis = new ByteArrayInputStream(Base64.decode(data, Base64.DEFAULT));
            ois = new ObjectInputStream(bis);
            Object object = ois.readObject();

            return (PayType) object;
        } catch (Exception e) {
 //           Logg.e(e);
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
  //              Logg.e(e);
            }
            cursor.close();
        }

        return pay;
    }

    public int getCount() {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME;
        Cursor cursor = db.getDb().rawQuery(sql, null);
        cursor.moveToFirst();
        int n = cursor.getInt(0);
        cursor.close();
        return n;
    }
    
    public boolean remove() {
        String sql = "DELETE FROM " + TABLE_NAME;
        return db.execSql(sql);
    }

}
