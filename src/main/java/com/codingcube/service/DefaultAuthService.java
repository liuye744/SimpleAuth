package com.codingcube.service;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * @author dhc
 */
@Component
public final class DefaultAuthService extends AutoAuthService{
    @Override
    public boolean isAuthor(HttpServletRequest request, String permission) {
        final ArrayList<String> userPermissions = getPermissions(request);
        if (userPermissions!=null){
            return userPermissions.contains(permission);
        }else {
            throw new InvalidParameterException("Missing permission list");
        }
    }
}