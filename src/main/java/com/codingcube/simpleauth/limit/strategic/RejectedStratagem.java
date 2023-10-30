package com.codingcube.simpleauth.limit.strategic;

import com.codingcube.simpleauth.limit.dynamic.RequestLimitItem;
import com.codingcube.simpleauth.logging.logformat.LogLimitFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class RejectedStratagem {
    public abstract void doRejected(HttpServletRequest request, HttpServletResponse response, LogLimitFormat limitFormat);
}
