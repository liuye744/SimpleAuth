package com.codingcube.logging;

import com.codingcube.logging.jdk.Jdk14LoggingImpl;
import com.codingcube.logging.nologging.NoLoggingImpl;
import com.codingcube.logging.stdout.StdOutImpl;
import com.codingcube.properties.LogProper;
import org.springframework.stereotype.Component;
import java.lang.reflect.Constructor;

/**
 * @author CodingCube<br>
 * logFactory
 */
@Component
public class LogFactory {

    private Constructor<? extends Log> logConstructor;


    public LogFactory(LogProper logProper) {
        final String log = logProper.getLogImpl();
        if ("NoLogging".equals(log)){
            setImplementation(NoLoggingImpl.class);
        }else if ("StdOut".equals(log)){
            setImplementation(StdOutImpl.class);
        }else if ("jdk".equals(log)){
            setImplementation(Jdk14LoggingImpl.class);
        }else {
            try {
                final Class<? extends Log> aClass = (Class<? extends Log>) Class.forName(log);
                setImplementation(aClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Log getLog(Class<?> aClass) {
        return getLog(aClass.getName());
    }

    public Log getLog(String logger) {
        try {
            return logConstructor.newInstance(new Object[] { logger });
        } catch (Throwable t) {
            throw new LogException("Error creating logger for logger " + logger + ".  Cause: " + t, t);
        }
    }

    private void setImplementation(Class<? extends Log> implClass) {
        try {
            Constructor<? extends Log> candidate = implClass.getConstructor(new Class[] { String.class });
            Log log = candidate.newInstance(new Object[] { LogFactory.class.getName() });
            log.debug("Logging initialized using '" + implClass + "' adapter.");
             logConstructor = candidate;
        } catch (Throwable t) {
            throw new LogException("Error setting Log implementation. Needs to have a constructor that takes a String as a parameter.");
        }
    }
}
