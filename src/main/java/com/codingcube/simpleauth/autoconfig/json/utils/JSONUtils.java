package com.codingcube.simpleauth.autoconfig.json.utils;


import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    public static List<String> getStringList(JSONObject jsonObject, String key){
        List<String> list = new ArrayList<>(jsonObject.length());
        try {
            final JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add((String) jsonArray.get(i));
            }
        } catch (JSONException e) {
            return null;
        }

        return list;
    }
    public static JSONObject geJSONObject(JSONObject jsonObject, String key){
        try {
            return jsonObject.getJSONObject(key);
        } catch (JSONException e) {
            return null;
        }
    }

    public static JSONArray geJSONArray(JSONObject jsonObject, String key){
        try {
            return jsonObject.getJSONArray(key);
        } catch (JSONException e) {
            return null;
        }
    }
}
