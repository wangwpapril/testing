package com.cuc.miti.phone.xmc.net;

import org.json.JSONException;
import org.json.JSONObject;


public abstract class JsonParser<T> implements IJsonParser {

    public void parse(String json) {
        try {
            JSONObject jo = new JSONObject(json);
            T result = doParse(jo);
            onParse(result, jo);
        } catch (JSONException e) {
 //           Logg.e(e);
        }
    }

    public void onError(int status, String message){}

    protected abstract T doParse(JSONObject jo) throws JSONException;

    public abstract void onParse(T result, JSONObject jo);
}
