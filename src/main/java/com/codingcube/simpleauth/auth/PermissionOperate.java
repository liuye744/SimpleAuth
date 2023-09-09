package com.codingcube.simpleauth.auth;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author CodingCube<br>
 * PermissionOperate*
 */
public interface PermissionOperate {
    String PRINCIPAL = "$PRINCIPAL$";
    String PERMISSIONS = "$PERMISSIONS$";

    default <T> T getPrincipal(){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        return (T) request.getAttribute(PRINCIPAL);
    }

    default void setPrincipal(Object target){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        request.setAttribute(PRINCIPAL, target);
    }

    default ArrayList<String> getPermissions(){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        final ArrayList<String> attribute = (ArrayList<String>) request.getAttribute(PERMISSIONS);
        if (attribute == null){
            return new ArrayList<>();
        }
        return attribute;
    }

    default void setPermissions(ArrayList<String> target){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        request.setAttribute(PERMISSIONS, target);
    }

    default void setPermissions(String permission){
        final ArrayList<String> permissions = getPermissions();
        permissions.add(permission);
        setPermissions(permissions);
    }

    static <T> T principal(){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        return (T) request.getAttribute(PRINCIPAL);
    }
}
