package com.codingcube.handler;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * @author dhc
 */
@Component
public final class DefaultAuthHandler extends AutoAuthHandler {
    @Override
    public boolean isAuthor(HttpServletRequest request, String permission) {
        final ArrayList<String> userPermissions = getPermissions();
        if (userPermissions!=null){
            return userPermissions.contains(permission);
        }else {
            throw new InvalidParameterException("Missing permission list");
        }
    }
}