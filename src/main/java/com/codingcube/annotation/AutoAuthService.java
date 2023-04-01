package com.codingcube.annotation;


import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @author dhc
 */
@Component
public abstract class AutoAuthService {

    final static String PRINCIPAL = "$PRINCIPAL$";
    final static String PERMISSIONS = "$PERMISSIONS$";

    /**
     * 判断是否符合权限 *
     * @param request HttpServletRequest
     * @return true符合 false不符合
     */
    public abstract boolean isAuthor(HttpServletRequest request, String permission);

    public static  <T> T getPrincipal(HttpServletRequest request){
        return (T) request.getAttribute(PRINCIPAL);
    }

    public static void setPrincipal(HttpServletRequest request,Object target){
        request.setAttribute(PRINCIPAL, target);
    }

    public static  ArrayList<String> getPermissions(HttpServletRequest request){
        return (ArrayList<String>) request.getAttribute(PERMISSIONS);
    }

    public static void setPermissions(HttpServletRequest request,ArrayList<String> target){
        request.setAttribute(PERMISSIONS, target);
    }
}