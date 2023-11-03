package com.codingcube.simpleauth.auth.strategic;

import com.codingcube.simpleauth.logging.logformat.LogAuthFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthRejectedStratagem {
    void doRejected(HttpServletRequest request, HttpServletResponse response, LogAuthFormat authFormat);
}