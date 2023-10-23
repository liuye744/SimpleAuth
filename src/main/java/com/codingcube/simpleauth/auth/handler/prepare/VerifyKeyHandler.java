package com.codingcube.simpleauth.auth.handler.prepare;

import com.codingcube.simpleauth.auth.handler.AutoAuthHandler;
import com.codingcube.simpleauth.properties.AuthProper;

import javax.servlet.http.HttpServletRequest;

public class VerifyKeyHandler extends AutoAuthHandler {
    @Override
    public boolean isAuthor(HttpServletRequest request, String permission) {
        return AuthProper.getVerifyValueStatic().equals(
                request.getParameter(AuthProper.getVerifyKeyStatic())
        );
    }
}
