package com.cuc.miti.phone.xmc.net;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


public abstract class ListJsonParser<T> implements IJsonParser {

    public void parse(String json) {
        try {
            JSONObject jo = new JSONObject(json);
            List<T> results = doParse(jo);
            onParse(results, jo);
        } catch (JSONException e) {
//            Logg.e(e);
        }
    }

    public void onError(int status, String message){}

    protected abstract List<T> doParse(JSONObject jo) throws JSONException;

    public abstract void onParse(List<T> results, JSONObject jo);
}
