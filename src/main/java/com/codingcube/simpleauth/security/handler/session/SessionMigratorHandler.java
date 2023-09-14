package com.codingcube.simpleauth.security.handler.session;

import com.codingcube.simpleauth.auth.handler.AutoAuthHandler;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Component
public class SessionMigratorHandler extends AutoAuthHandler {
    private final Log log;

    public SessionMigratorHandler(LogFactory logFactory) {
        this.log = logFactory.getLog(this.getClass());
    }

    @Override
    public boolean isAuthor(HttpServletRequest request, String permission) {
        HttpSession originalSession = request.getSession(false);

        if (originalSession != null) {
            final String originalSessionId = originalSession.getId();
            Enumeration<String> attributeNames = originalSession.getAttributeNames();
            List<Map.Entry<String, Object>> entryList = new ArrayList<>();

            // copy attribute to entrySet
            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                Object attributeValue = originalSession.getAttribute(attributeName);

                entryList.add(new AbstractMap.SimpleEntry<>(attributeName, attributeValue));
            }

            // destroy original Session
            originalSession.invalidate();
            HttpSession newSession = request.getSession(true);
            for (Map.Entry<String, Object> entry : entryList) {
                newSession.setAttribute(entry.getKey(), entry.getValue());
            }
            log.debug("Changed session id from "+originalSessionId + " to " + newSession.getId());
        }
        return true;
    }
}
