package com.codingcube.filter;


import com.codingcube.exception.PermissionsException;
import com.codingcube.implement.PermissionOperate;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter
@Component
public abstract class AuthPermissions implements Filter, PermissionOperate {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (!setPermission(servletRequest, servletResponse)){
            //No access
            throw new PermissionsException("No access");
        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    abstract boolean setPermission(ServletRequest servletRequest, ServletResponse servletResponse);
}
