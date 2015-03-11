package com.cuc.miti.phone.xmc.store.beans;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.models.Area;
import com.cuc.miti.phone.xmc.models.ModelParser;
import com.cuc.miti.phone.xmc.store.Bean;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class AreaTable extends Bean {

	private static final String TABLE_NAME = "areaTable";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PARENT_ID = "parentId";

    private SQLiteDatabase dataBase;
    private static AreaTable instance;
    
    private AreaTable() {
    	if (dataBase == null) {
            dataBase = db.getDb();
        }
    }

    public static AreaTable getInstance() {
        if (instance == null) {
            instance = new AreaTable();
        }

        return instance;
    }

    @Override
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + " ("
                + ID + " TEXT, "
                + PARENT_ID + " TEXT, "
                + NAME + " TEXT);";
        db.execSql(sql);
    }

    public void init() {
        if (getCount() == 0) {
        	saveList(parseAreaList());
        }
    }

    private List<Area> parseAreaList() {
        String json = null;
        try {
            InputStream is = IngleApplication.getInstance().getAssets().open("data/city_area.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
 //           Logg.d("access file txt get wrong");
            ex.printStackTrace();
        }

        return ModelParser.parseAsJSONObject(json, ModelParser.PARSE_AREAS, ModelParser.AREAS);
    }

    public void saveList(List<Area> list) {
        try {
            dataBase.beginTransaction();
            String sql = "INSERT INTO " + TABLE_NAME + " (" + ID + ", " + PARENT_ID + ", "  + NAME
                    + ") values (?, ?, ?)";
            for (Area area : list) {
                dataBase.execSQL(sql, new Object[]{area.id, area.parentId, area.name});
            }
            dataBase.setTransactionSuccessful();
        } finally {
            dataBase.endTransaction();
        }
    }

    public Area getAreaById(String id) {
        Cursor cursor = dataBase.query(TABLE_NAME, null, ID + " = ?", new String[]{"" + id}, null, null, null);
        List<Area> list = getAreaList(cursor);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public Area getParentAreaBySubId(String id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID
                   + " = (SELECT " + PARENT_ID + " FROM " + TABLE_NAME + " WHERE " + ID + " = ?)";
        Cursor cursor = dataBase.rawQuery(sql, new String[]{id});
        List<Area> list = getAreaList(cursor);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public Area getAreaByName(String name) {
        Cursor cursor = dataBase.query(TABLE_NAME, null, NAME + " = ?", new String[]{name}, null, null, null);
        List<Area> list = getAreaList(cursor);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<Area> getParentAreaList() {
        Cursor cursor = dataBase.query(TABLE_NAME, null, PARENT_ID + " =?", new String[]{"999999"}, null, null, null);
        return getAreaList(cursor);
    }

    public List<Area> getSubAreaListByParentId(String parentId) {
        Cursor cursor = dataBase.query(TABLE_NAME, null, PARENT_ID + " = ?", new String[]{"" + parentId}, null, null, null);
        return getAreaList(cursor);
    }

    public List<Area> getAllArea() {
        Cursor cursor = dataBase.query(TABLE_NAME, null, null, null, null, null, null);
        return getAreaList(cursor);
    }

    private List<Area> getAreaList(Cursor cursor) {
        if (cursor == null || !cursor.moveToFirst()) {
            if (cursor != null) {
                cursor.close();
            }
            return null;
        }

        List<Area> result = new ArrayList<Area>();
        try {
            do {
            	Area area = new Area();
            	area.id = cursor.getString(0);
            	area.parentId = cursor.getString(1);
                area.name = cursor.getString(2);
                result.add(area);
            } while (cursor.moveToNext());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        return result;
    }

    public int getCount() {
    	int count = 0;
    	Cursor cursor = null;
        String sql = "SELECT count(1) FROM " + TABLE_NAME;
        try {
        	cursor = dataBase.rawQuery(sql, null);
            cursor.moveToFirst();
            count = cursor.getInt(0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        return count;
    }

}
