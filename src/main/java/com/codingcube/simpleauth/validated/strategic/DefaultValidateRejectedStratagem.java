package com.codingcube.simpleauth.validated.strategic;

import com.codingcube.simpleauth.exception.ValidateException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultValidateRejectedStratagem implements ValidateRejectedStratagem{
    @Override
    public void doRejected(HttpServletRequest request, HttpServletResponse response, Object validationObj) {
        throw new ValidateException(validationObj + " verification failed.");
    }
}
