package com.codingcube.simpleauth.logging.logformat;

import com.codingcube.simpleauth.auth.PermissionOperate;

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
        return "SimpleAuth auth => \r\n\turi: "+request.getRequestURI() +"\r\n"+
                "\thandlerName: "+handlerName+"\r\n"+
                "\tsource: "+source+"\r\n"+
                "\tRequired permission: "+permission+"\r\n"+
                "\tPermissions to carry: "+ request.getAttribute(PermissionOperate.PERMISSIONS) +"\r\n"+
                "\tPrincipal to carry: "+ request.getAttribute(PermissionOperate.PRINCIPAL) +"\r\n"+
                "\tPass or not: "+pass+"";
    }
}
