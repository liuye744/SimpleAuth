package com.codingcube.simpleauth.logging;

import com.codingcube.simpleauth.exception.LogException;
import com.codingcube.simpleauth.logging.jdk.Jdk14LoggingImpl;
import com.codingcube.simpleauth.logging.nologging.NoLoggingImpl;
import com.codingcube.simpleauth.logging.stdout.StdOutImpl;
import com.codingcube.simpleauth.properties.LogProper;
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
    private Constructor<? extends Log> validateLogConstructor;


    public LogFactory(LogProper logProper) {
        final String log = logProper.getLogImpl();
        logConstructor = getLogImpl(log);

        final String limitLogImpl = logProper.getLimitLogImpl();
        limitLogConstructor = getLogImpl(limitLogImpl);

        final String validatedLogImpl = logProper.getValidatedLogImpl();
        validateLogConstructor = getLogImpl(validatedLogImpl);

        getLog(this.getClass()).debug("Auth Logging initialized using "+logConstructor.getDeclaringClass()+" adapter.");
        getLimitLog(this.getClass()).debug("Limit Logging initialized using "+limitLogConstructor.getDeclaringClass()+" adapter.");
        getValidateLog(this.getClass()).debug("Validate Logging initialized using "+validateLogConstructor.getDeclaringClass()+" adapter.");

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
            throw new LogException("Error creating logger for limit function " + aClass.getName() + ".  Cause: " + t, t);
        }
    }

    public Log getValidateLog(Class<?> aClass) {
        try {
            return validateLogConstructor.newInstance(aClass.getName());
        } catch (Throwable t) {
            throw new LogException("Error creating logger for validate function " + aClass.getName() + ".  Cause: " + t, t);
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
            return implClass.getConstructor(String.class);
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

    public void setValidateLogConstructor(Constructor<? extends Log> validateLogConstructor) {
        this.validateLogConstructor = validateLogConstructor;
    }
}
