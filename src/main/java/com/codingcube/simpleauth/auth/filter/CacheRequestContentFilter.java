package com.codingcube.simpleauth.auth.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class CacheRequestContentFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (request instanceof HttpServletRequest) {
            String contentType = request.getContentType();
            if (contentType == null || !contentType.contains("multipart/form-data")) {
                request = new CacheBodyHttpServletRequest((HttpServletRequest) request);
            }
        }
        chain.doFilter(request, response);
    }
}