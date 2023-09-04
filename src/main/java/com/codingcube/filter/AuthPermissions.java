package com.codingcube.filter;


import com.codingcube.exception.PermissionsException;
import com.codingcube.implement.PermissionOperate;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthPermissions implements Filter, PermissionOperate {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (!attachPermission((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse)){
            //No access
            throw new PermissionsException("lack of permissions");
        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    public abstract boolean attachPermission(HttpServletRequest servletRequest, HttpServletResponse servletResponse);
}
