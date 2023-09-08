package com.codingcube.logging;

import com.codingcube.implement.PermissionOperate;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CodingCube <br>
 * Format Auth Log
 */
public class LogAuthFormat {
    private final HttpServletRequest request;
    private final String source;
    private final boolean pass;
    private final String handlerName;
    private final String permission;

    public LogAuthFormat(HttpServletRequest request, String source, boolean pass, String handlerName, String permission) {
        this.request = request;
        this.source = source;
        this.pass = pass;
        this.handlerName = handlerName;
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "SimpleAuth run => \n\turi: "+request.getRequestURI() +"\n"+
                "\thandlerName: "+handlerName+"\n"+
                "\tsource: "+source+"\n"+
                "\tRequired permission: "+permission+"\n"+
                "\tPermissions to carry: "+ request.getAttribute(PermissionOperate.PERMISSIONS) +"\n"+
                "\tPrincipal to carry: "+ request.getAttribute(PermissionOperate.PRINCIPAL) +"\n"+
                "\tPass or not: "+pass+"";
    }
}
