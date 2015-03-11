package com.cuc.miti.phone.xmc.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.cuc.miti.phone.xmc.models.Address;
import com.cuc.miti.phone.xmc.models.Advertise;
import com.cuc.miti.phone.xmc.models.Area;
import com.cuc.miti.phone.xmc.models.BigCategory;
import com.cuc.miti.phone.xmc.models.CartProductDetail;
import com.cuc.miti.phone.xmc.models.Category;
import com.cuc.miti.phone.xmc.models.Order;
import com.cuc.miti.phone.xmc.models.PayType;
import com.cuc.miti.phone.xmc.models.Product;
import com.cuc.miti.phone.xmc.models.SendType;
import com.cuc.miti.phone.xmc.models.Store;

public class ModelParser {

    public static final String B_CATEGORYS = "bCategorys";
    public static final String S_CATEGORYS = "sCategorys ";
    public static final String PRODUCTS = "products";
    public static final String ORDERS = "orders";
    public static final String COMMENTS = "comments";
    public static final String STORES = "stores";
    public static final String ADDRESS = "address";
    public static final String AREAS = "areas";
    public static final String SENDTYPE = "shiptypelist";
    public static final String PAYTYPE = "paytypelist";
    public static final String BANNER = "banner";
    
    public static final int PARSE_BIG_CATEGORY = 97;
    public static final int PARSE_SUB_CATEGORY = 98;
    public static final int PARSE_PRODUCTS = 99;
    public static final int PARSE_ORDERS = 100;
    public static final int PARSE_COMMENTS = 101;
    public static final int PARSE_STORES = 102;
    public static final int PARSE_CART_PRODUCTS = 103;
    public static final int PARSE_ADDRESS = 104;
    public static final int PARSE_AREAS = 105;
    public static final int PARSE_SENDTYPE = 106;
    public static final int PARSE_PAYTYPE = 107;
    
    public static final int PARSE_RECOMS = 108;
    public static final int PARSE_PROMOTIONS = 109;
    public static final int PARSE_DEALS = 110;
    public static final int PARSE_FIND_DISCOUNT = 111;
    public static final int PARSE_BANNER = 112;


    public static <T> T parseAsJSONObject(String response, int parseType, String parseKey) {
        if (TextUtils.isEmpty(response) || TextUtils.isEmpty(parseKey)) return null;

        ArrayList<Object> objects = new ArrayList<Object>();
        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray array = jsonObj.getJSONArray(parseKey);

            if (array != null) {
                int length = array.length();
                Object obj = null;

                for (int i = 0; i < length; i ++) {
                    jsonObj = array.getJSONObject(i);
                    if (jsonObj == null) {
                        continue;
                    }

                    switch (parseType) {
                        case PARSE_BIG_CATEGORY:
                        	obj = new BigCategory(jsonObj);
                        	break;
                        	
                        case PARSE_SUB_CATEGORY:
                        	obj = new Category(jsonObj);
                        	break;
                        	
                        case PARSE_PRODUCTS:
                        	obj = new Product(jsonObj);
                        	break;
                        	
                        case PARSE_ORDERS:
                        	obj = new Order(jsonObj);
                        	break;
                        	
                        case PARSE_STORES:
                        	obj = new Store(jsonObj);
                        	break;
                        	
                        case PARSE_CART_PRODUCTS:
                        	obj = new CartProductDetail(jsonObj);
                        	break;
                        	
                        case PARSE_AREAS:
                        	obj = new Area(jsonObj);
                        	break;
                        	
                        case PARSE_ADDRESS:
                        	obj = new Address(jsonObj);
                        	break;
                        	
                        case PARSE_SENDTYPE:
                        	obj = new SendType(jsonObj);
                        	break;
                        	
                        case PARSE_PAYTYPE:
                        	obj = new PayType(jsonObj);
                    		break;
                    		
                        case PARSE_BANNER:
                        	obj = new Advertise(jsonObj);
                        	break;
                        	
                        default:
                            break;
                    }

                    if (obj != null) {
                        objects.add(obj);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
 //           Logg.e(ex);
        }

        return (T) objects;
    }

    public static <T> T parseAsJSONArray(String response, int parseType) {
        ArrayList<Object> objects = new ArrayList<Object>();
        
        try {
	        JSONArray jsonArray = new JSONArray(response);
	
	        if (jsonArray != null) {
	            int length = jsonArray.length();
	            JSONObject jsonObject;
	            Object obj = null;
	            for (int i = 0; i < length; i++) {
                
                    jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject == null) {
                        continue;
                    }

                    switch (parseType) {
                    case PARSE_SENDTYPE:
                    	obj = new SendType(jsonObject);
                    	break;
                    	
                        default:
                            break;
                    }

                    if (obj != null) {
                        objects.add(obj);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
  //          Logg.e(e);
        }

        return (T) objects;
    }

}
