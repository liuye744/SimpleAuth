package com.codingcube.properties;

/**
 * @author CodingCube<br>*
 * Ban Flag Generation Strategy*
 */
public class BanKeyStratagem {
    public static String getBanKey(String recordItem, String sign){
        return recordItem + sign;
    }
}
