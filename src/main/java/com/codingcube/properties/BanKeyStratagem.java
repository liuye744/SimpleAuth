package com.codingcube.properties;

public class BanKeyStratagem {
    public static String getBanKey(String recordItem, String sign){
        return recordItem + sign;
    }
}
