package com.codingcube.simpleauth.security.handler.session;

import com.codingcube.simpleauth.auth.handler.AutoAuthHandler;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.log.LogMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

@Component
public class ChangeSessionIdHandler extends AutoAuthHandler {
    private final Log log;

    public ChangeSessionIdHandler(LogFactory logFactory) {
        this.log = logFactory.getLog(this.getClass());
    }

    @Override
    public boolean isAuthor(HttpServletRequest request, String permission) {
        boolean hadSessionAlready = request.getSession(false) != null;
        if (hadSessionAlready) {
            HttpSession session = request.getSession();
            if (request.isRequestedSessionIdValid()) {
                String originalSessionId;
                String newSessionId;
                synchronized(WebUtils.getSessionMutex(session)) {
                    originalSessionId = session.getId();
                    session = this.applySessionFixation(request);
                    newSessionId = session.getId();
                }

                if (originalSessionId.equals(newSessionId)) {
                    log.warn("Your servlet container did not change the session ID when a new session was created. You will not be adequately protected against session-fixation attacks");
                } else {
                    log.debug("Changed session id from "+originalSessionId + " to " +newSessionId);
                }

            }

        }
        return true;
    }

    HttpSession applySessionFixation(HttpServletRequest request) {
        request.changeSessionId();
        return request.getSession();
    }

}
