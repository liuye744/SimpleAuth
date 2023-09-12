package com.codingcube.simpleauth.auth.handler;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CodingCube<br>
 * Default Authentication Handler Class*
 */
@Component
public final class DefaultAuthHandler extends AutoAuthHandler {
    @Override
    public boolean isAuthor(HttpServletRequest request, String permission) {
        final List<String> userPermissions = getPermissions();
        if (userPermissions!=null){
            return userPermissions.contains(permission);
        }else {
            throw new InvalidParameterException("Missing permission list");
        }
    }
}