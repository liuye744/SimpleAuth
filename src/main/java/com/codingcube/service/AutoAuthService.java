package com.codingcube.service;


import com.codingcube.implement.PermissionOperate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @author dhc
 */
@Component
public abstract class AutoAuthService implements PermissionOperate {

    /**
     * 判断是否符合权限 *
     * @param request HttpServletRequest
     * @return true符合 false不符合
     */
    public abstract boolean isAuthor(HttpServletRequest request, String permission);

}