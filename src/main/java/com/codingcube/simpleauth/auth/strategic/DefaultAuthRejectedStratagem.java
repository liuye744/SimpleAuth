package com.codingcube.simpleauth.auth.strategic;

import com.codingcube.simpleauth.exception.PermissionsException;
import com.codingcube.simpleauth.logging.logformat.LogAuthFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultAuthRejectedStratagem implements AuthRejectedStratagem {
    @Override
    public void doRejected(HttpServletRequest request, HttpServletResponse response, LogAuthFormat authFormat) {
        throw new PermissionsException("lack of permissions");
    }
}
