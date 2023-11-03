package com.codingcube.simpleauth.limit.strategic;

import com.codingcube.simpleauth.logging.logformat.LogLimitFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RejectedStratagem {
    void doRejected(HttpServletRequest request, HttpServletResponse response, LogLimitFormat limitFormat);
}
