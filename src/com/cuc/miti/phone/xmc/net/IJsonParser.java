package com.cuc.miti.phone.xmc.net;

public interface IJsonParser {
    public void parse(String json);
    public void onError(int status, String message);
}
