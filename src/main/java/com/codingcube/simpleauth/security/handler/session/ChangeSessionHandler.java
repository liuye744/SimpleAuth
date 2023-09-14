package com.codingcube.simpleauth.security.handler.session;

import com.codingcube.simpleauth.auth.handler.AutoAuthHandler;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class ChangeSessionHandler extends AutoAuthHandler {
    private final Log log;

    public ChangeSessionHandler(LogFactory logFactory) {
        this.log = logFactory.getLog(this.getClass());
    }

    @Override
    public boolean isAuthor(HttpServletRequest request, String permission) {
        final HttpSession session = request.getSession(false);
        if (session != null){
            final String sessionId = session.getId();
            session.invalidate();
            final HttpSession newSession = request.getSession(true);
            log.debug("Changed session id from "+sessionId + " to " + newSession.getId());
        }
        return true;
    }
}
