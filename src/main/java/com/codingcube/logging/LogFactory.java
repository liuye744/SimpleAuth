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
    private Constructor<? extends Log> limitLogConstructor;


    public LogFactory(LogProper logProper) {
        final String log = logProper.getLogImpl();
        logConstructor = getLogImpl(log);

        final String limitLogImpl = logProper.getLimitLogImpl();
        limitLogConstructor = getLogImpl(limitLogImpl);

        LogProper.setStaticShowOptList(logProper.getShowOptList());

        getLog(this.getClass()).debug("Auth Logging initialized using "+logConstructor.getDeclaringClass()+" adapter.");
        getLimitLog(this.getClass()).debug("Limit Logging initialized using "+limitLogConstructor.getDeclaringClass()+" adapter.");
    }

    public Constructor<? extends Log> getLogImpl(String logImplString){
        if ("NoLogging".equalsIgnoreCase(logImplString)){
            return getImplementation(NoLoggingImpl.class);
        }else if ("StdOut".equalsIgnoreCase(logImplString)){
            return getImplementation(StdOutImpl.class);
        }else if ("jdk".equalsIgnoreCase(logImplString)){
            return getImplementation(Jdk14LoggingImpl.class);
        }else {
            try {
                final Class<? extends Log> aClass = (Class<? extends Log>) Class.forName(logImplString);
                return getImplementation(aClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return getImplementation(StdOutImpl.class);
            }
        }
    }

    public Log getLog(Class<?> aClass) {
        return getLog(aClass.getName());
    }

    public Log getLimitLog(Class<?> aClass) {
        try {
            return limitLogConstructor.newInstance(aClass.getName());
        } catch (Throwable t) {
            throw new LogException("Error creating logger for logger " + aClass.getName() + ".  Cause: " + t, t);
        }
    }

    public Log getLog(String logger) {
        try {
            return logConstructor.newInstance(logger);
        } catch (Throwable t) {
            throw new LogException("Error creating logger for logger " + logger + ".  Cause: " + t, t);
        }
    }

    private Constructor<? extends Log> getImplementation(Class<? extends Log> implClass) {
        try {
            return implClass.getConstructor(new Class[] { String.class });
        } catch (Throwable t) {
            throw new LogException("Error setting Log implementation. Needs to have a constructor that takes a String as a parameter.");
        }
    }

    public void setLogConstructor(Constructor<? extends Log> logConstructor) {
        this.logConstructor = logConstructor;
    }

    public void setLimitLogConstructor(Constructor<? extends Log> limitLogConstructor) {
        this.limitLogConstructor = limitLogConstructor;
    }
}
