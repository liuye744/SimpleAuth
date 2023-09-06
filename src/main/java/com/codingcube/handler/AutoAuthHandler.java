package com.codingcube.handler;


import com.codingcube.implement.PermissionOperate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CodingCube<br>
 * Authentication Handler*
 */
public abstract class AutoAuthHandler implements PermissionOperate {

    /**
     * Judge whether the permission is met. *
     * @param request HttpServletRequest
     * @return True passes false, but not true.
     */
    public abstract boolean isAuthor(HttpServletRequest request, String permission);

}