package com.codingcube.simpleauth.validated.strategic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ValidateRejectedStratagem {
    void doRejected(HttpServletRequest request, HttpServletResponse response, Object validationObj);
}
