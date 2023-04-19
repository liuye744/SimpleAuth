package com.codingcube.implement;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public interface PermissionOperate {
    String PRINCIPAL = "$PRINCIPAL$";
    String PERMISSIONS = "$PERMISSIONS$";

    default <T> T getPrincipal(ServletRequest request){
        return (T) request.getAttribute(PRINCIPAL);
    }

    default void setPrincipal(ServletRequest request,Object target){
        request.setAttribute(PRINCIPAL, target);
    }

    default ArrayList<String> getPermissions(HttpServletRequest request){
        return (ArrayList<String>) request.getAttribute(PERMISSIONS);
    }

    default void setPermissions(HttpServletRequest request,ArrayList<String> target){
        request.setAttribute(PERMISSIONS, target);
    }
}
