package com.codingcube.simpleauth.auth.strategic;

import com.codingcube.simpleauth.logging.logformat.LogAuthFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthRejectedStratagem {
    public abstract void doRejected(HttpServletRequest request, HttpServletResponse response, LogAuthFormat authFormat);
}