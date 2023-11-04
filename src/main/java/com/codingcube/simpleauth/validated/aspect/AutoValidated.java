package com.codingcube.simpleauth.validated.aspect;

import com.codingcube.simpleauth.exception.ValidateException;
import com.codingcube.simpleauth.exception.ValidateMethodException;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import com.codingcube.simpleauth.validated.annotation.SimpleValidate;
import com.codingcube.simpleauth.validated.strategic.ValidateRejectedStratagem;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
public class AutoValidated {
    @Resource
    ApplicationContext application;

    @Around("@annotation(simpleValidate)")
    public Object validateMethod(ProceedingJoinPoint joinPoint, SimpleValidate simpleValidate) throws Throwable{
        final String[] methodsString = simpleValidate.methods();
        Class<?> validateObj = simpleValidate.value();
        final Class<? extends ValidateRejectedStratagem> rejected = simpleValidate.rejected();
        if (validateObj == Object.class){
            //寻找类上的validateObj
            final SimpleValidate classAnnotation = joinPoint.getTarget().getClass().getAnnotation(SimpleValidate.class);
            if (classAnnotation == null || (validateObj = classAnnotation.value()) == Object.class){
                throw new ValidateMethodException("Requires a validate class as the value parameter, which can be placed on method or class annotations");
            }
        }
        final Method[] methods = validateObj.getMethods();

        for (Method method : methods) {
            if (method.getParameterCount() != 1) {
                continue;
            }
            for (String methodName : methodsString) {
                validate(method, methodName, validateObj, rejected, joinPoint);
            }
        }

        return joinPoint.proceed();
    }

    private void validate(Method method, String methodName, Class<?> validateObj, final Class<? extends ValidateRejectedStratagem> rejectedClazz, ProceedingJoinPoint joinPoint) throws Throwable {
        if (method.getName().equals(methodName)) {
            final Object[] args = joinPoint.getArgs();
            for (Object target : args) {
                if (method.getParameterTypes()[0] == target.getClass()) {
                    final Object validateBean = AuthHandlerUtil.getBean(application, validateObj);
                    try {
                        final Object validatedResult = method.invoke(validateBean, target);
                        if (validatedResult instanceof Boolean){
                            if (!(Boolean) validatedResult){
                                //验证失败
                                final ValidateRejectedStratagem rejected = AuthHandlerUtil.getBean(application, rejectedClazz);
                                final ServletRequestAttributes attributes = (ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
                                rejected.doRejected(attributes.getRequest(), attributes.getResponse(), target);
                                return;
                            }
                        }else {
                            throw new ValidateMethodException("he return value type of "+ methodName +" should be Boolean.");
                        }
                    } catch (IllegalAccessException e) {
                        throw new ValidateMethodException(methodName + " illegal access " + validateObj, e);
                    }catch (InvocationTargetException e) {
                        throw e.getCause();
                    }
                }
            }
        }

    }
}
