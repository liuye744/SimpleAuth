package com.codingcube.simpleauth.validated.aspect;

import com.codingcube.simpleauth.exception.ValidateMethodException;
import com.codingcube.simpleauth.properties.ValidateProper;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import com.codingcube.simpleauth.util.NullTarget;
import com.codingcube.simpleauth.validated.annotation.SimpleValidate;
import com.codingcube.simpleauth.validated.domain.ReflectMethod;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
public class AutoValidated {
    @Resource
    ApplicationContext application;

    final Map<String, ReflectMethod> reflectMethodMap = new HashMap<>();

    @Around("@annotation(simpleValidate)")
    public Object validateMethod(ProceedingJoinPoint joinPoint, SimpleValidate simpleValidate) throws Throwable{
        final String[] methodsString = simpleValidate.methods();
        Class<?> validateObj = simpleValidate.value();
        Class<? extends ValidateRejectedStratagem> rejected = simpleValidate.rejected();
        final SimpleValidate classAnnotation = joinPoint.getTarget().getClass().getAnnotation(SimpleValidate.class);
        if (validateObj == Object.class){
            //寻找类上的validateObj
            if (classAnnotation == null || (validateObj = classAnnotation.value()) == Object.class ){
                //全局ValidatedObjected
                if ((validateObj = ValidateProper.getDefaultValidateObjectClazz()) == Object.class){
                    throw new ValidateMethodException("Requires a validate class as the value parameter, which can be placed on method or class annotations");
                }
            }
        }
        if (rejected == NullTarget.class){
            if (classAnnotation == null || (rejected = classAnnotation.rejected()) == NullTarget.class ){
                rejected = ValidateProper.getDefaultRejectedClazz();
            }
        }

        for (String methodName : methodsString) {
            final String key = key(validateObj, methodName);
            ReflectMethod reflectMethod = reflectMethodMap.get(key);
            if (reflectMethod == null){
                initReflectMethodCache(key, validateObj, methodName);
                reflectMethod = reflectMethodMap.get(key);
            }

            while (reflectMethod != null){
                final Method method = reflectMethod.getMethod();
                final Object validateBean = reflectMethod.getInstance();
                final Class<?> param = reflectMethod.getParam();


                final Object[] args = joinPoint.getArgs();
                for (int i = 0; i < args.length; i++) {
                    Object target = args[i];
                    if (param == target.getClass()) {
                        //成功验证一个后跳出循环
                        i = Integer.MAX_VALUE - 1;
                        try {
                            final Object validatedResult = method.invoke(validateBean, target);
                            if (validatedResult instanceof Boolean){
                                if (!(Boolean) validatedResult){
                                    //验证失败
                                    final ValidateRejectedStratagem rejectedInstance = AuthHandlerUtil.getBean(application, rejected);
                                    final ServletRequestAttributes attributes = (ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
                                    rejectedInstance.doRejected(attributes.getRequest(), attributes.getResponse(), target);
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
                reflectMethod = reflectMethod.getNext();
            }
        }

        return joinPoint.proceed();
    }

    private void initReflectMethodCache(String key, Class<?> validateObj, String methodName){
        synchronized (getReflectMethodCacheMapMutex()){
            //初始化
            if(reflectMethodMap.get(key) == null){
                final Method[] methods = validateObj.getMethods();
                final Object bean = AuthHandlerUtil.getBean(application, validateObj);
                for (Method method : methods) {
                    if (method.getParameterCount() == 1 && method.getName().equals(methodName)){
                        method.setAccessible(true);
                        final ReflectMethod newReflectMethod = new ReflectMethod(bean, method, method.getParameterTypes()[0]);
                        if (reflectMethodMap.get(key) == null){
                            reflectMethodMap.put(key, newReflectMethod);
                        }else {
                            ReflectMethod mapReflectMethod = reflectMethodMap.get(key);
                            while (mapReflectMethod.getNext() != null){
                                mapReflectMethod = mapReflectMethod.getNext();
                            }
                            mapReflectMethod.setNext(newReflectMethod);
                        }
                    }
                }
            }
        }

    }

    private String key(Class<?> validateObjClazz, String methodName){
        return validateObjClazz.getName() + "$" + methodName;
    }

    private Object getReflectMethodCacheMapMutex(){
        return reflectMethodMap;
    }
}
