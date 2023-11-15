package com.codingcube.simpleauth.validated.aspect;

import com.codingcube.simpleauth.exception.ValidateMethodException;
import com.codingcube.simpleauth.properties.ValidateProper;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import com.codingcube.simpleauth.util.NullTarget;
import com.codingcube.simpleauth.validated.annotation.SimpleValidate;
import com.codingcube.simpleauth.validated.domain.ReflectMethod;
import com.codingcube.simpleauth.validated.strategic.ValidateRejectedStratagem;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Component
public class AutoValidated {
    @Resource
    ApplicationContext application;

    final Map<String, ReflectMethod> reflectMethodMap = new HashMap<>();
    final Map<String, ReflectMethod> reflectParameterMap = new HashMap<>();

    @Around("@annotation(simpleValidate)")
    public Object validateMethod(ProceedingJoinPoint joinPoint, SimpleValidate simpleValidate) throws Throwable{
        String[] methodsString = simpleValidate.methods();
        if (methodsString.length == 0){
            methodsString = new String[1];
            methodsString[0] = "validate";
        }

        final SimpleValidate classAnnotation = joinPoint.getTarget().getClass().getAnnotation(SimpleValidate.class);
        final Class<?> validateObj = getValidateObjected(simpleValidate, classAnnotation);
        final Class<? extends ValidateRejectedStratagem> rejected = getValidateRejected(simpleValidate, classAnnotation);

        doPositiveMethodName(validateObj, methodsString, rejected, joinPoint);

        return joinPoint.proceed();
    }

    @Around("execution(* *.*(.., @com.codingcube.simpleauth.validated.annotation.SimpleValidate (*), ..))")
    public Object myAdvice(ProceedingJoinPoint joinPoint) throws Throwable{
        //初始化参数
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        SimpleValidate simpleValidate = method.getAnnotation(SimpleValidate.class);

        final SimpleValidate classAnnotation = joinPoint.getTarget().getClass().getAnnotation(SimpleValidate.class);
        Class<?> validateObj;
        if (simpleValidate == null ||(validateObj=simpleValidate.value()) == Object.class){
            //寻找类上的validateObj
            if (classAnnotation == null || (validateObj = classAnnotation.value()) == Object.class ){
                //全局ValidatedObjected
                validateObj = ValidateProper.getDefaultValidateObjectClazz();
            }
        }

        final Class<? extends ValidateRejectedStratagem> rejected = getValidateRejected(simpleValidate, classAnnotation);


        //查询参数是否携带SimpleValidate注解
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final Object[] args = joinPoint.getArgs();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            for (Annotation annotation : parameterAnnotation) {
                if (annotation.annotationType() == SimpleValidate.class){
                    final String[] methodsString = ((SimpleValidate) annotation).methods();
                    final Class<?> parameterValidateObj = ((SimpleValidate) annotation).value();
                    if (parameterValidateObj != Object.class){
                        validateObj = parameterValidateObj;
                    }
                    if (validateObj == Object.class){
                        throw new ValidateMethodException("Requires a validate class as the value parameter, which can be placed on parameter or class annotations");
                    }
                    //存在methodsString 执行positiveMethod
                    if (methodsString.length != 0){
                        doPositiveMethodName(validateObj, methodsString, rejected, joinPoint);
                        return joinPoint.proceed();
                    }
                    final Object targetParameter = args[i];
                    //不存在MethodsString 执行positiveParameter

                    doPositiveParameter(validateObj, rejected, targetParameter);
                }
            }
        }

        return joinPoint.proceed();
    }

    private Class<?> getValidateObjected(SimpleValidate simpleValidate,SimpleValidate fatherSimpleValidate){
        Class<?> validateObj = simpleValidate.value();
        if (validateObj == Object.class){
            //寻找类上的validateObj
            if (fatherSimpleValidate == null || (validateObj = fatherSimpleValidate.value()) == Object.class ){
                //全局ValidatedObjected
                if ((validateObj = ValidateProper.getDefaultValidateObjectClazz()) == Object.class){
                    throw new ValidateMethodException("Requires a validate class as the value parameter, which can be placed on method or class annotations");
                }
            }
        }
        return validateObj;
    }

    private Class<? extends ValidateRejectedStratagem> getValidateRejected(SimpleValidate simpleValidate,SimpleValidate fatherSimpleValidate){
        Class<? extends ValidateRejectedStratagem> rejected;
        if (simpleValidate==null || (rejected=simpleValidate.rejected()) == NullTarget.class){
            if (fatherSimpleValidate == null || (rejected = fatherSimpleValidate.rejected()) == NullTarget.class ){
                rejected = ValidateProper.getDefaultRejectedClazz();
            }
        }
        return rejected;
    }

    /**
     * * 存在确定的方法名时执行
     * @param validateObj 验证类
     * @param methodsString 方法名
     * @param rejected 拒绝策略
     * @param joinPoint 方法切点
     */
    private void doPositiveMethodName(Class<?> validateObj, String[] methodsString, Class<? extends ValidateRejectedStratagem> rejected, ProceedingJoinPoint joinPoint) throws Throwable {
        for (String methodName : methodsString) {
            final String key = key(validateObj, methodName);
            ReflectMethod reflectMethod = reflectMethodMap.get(key);
            if (reflectMethod == null){
                initReflectMethodCache(key, validateObj, methodName);
                reflectMethod = reflectMethodMap.get(key);
            }

            if(reflectMethod == null){
                throw new ValidateMethodException("No matching method found for "+ methodName +" in the class "+validateObj);
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
    }

    /**
     * 存在确定的参数时执行*
     * @param validateObj 实力类型
     * @param rejected 拒绝策略
     * @param parameter 参数
     */
    private void doPositiveParameter(Class<?> validateObj, Class<? extends ValidateRejectedStratagem> rejected, Object parameter) throws Throwable {
        //判断并初始化缓存
        final String parameterKey = parameterKey(validateObj, parameter.getClass());
        ReflectMethod reflectMethod = reflectParameterMap.get(parameterKey);
        if (reflectMethod == null){
            initReflectParameterCache(parameterKey, validateObj, parameter.getClass());
            reflectMethod = reflectParameterMap.get(parameterKey);
        }
        if(reflectMethod == null){
            throw new ValidateMethodException("No method found with only one parameter of type " + parameter.getClass()+" in "+ validateObj);
        }
        while (reflectMethod != null){
            final Method method = reflectMethod.getMethod();
            final Object instance = reflectMethod.getInstance();

            try{
                final Object validatedResult = method.invoke(instance, parameter);
                if (validatedResult instanceof Boolean){
                    if (!(Boolean) validatedResult){
                        //验证失败
                        final ValidateRejectedStratagem rejectedInstance = AuthHandlerUtil.getBean(application, rejected);
                        final ServletRequestAttributes attributes = (ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
                        rejectedInstance.doRejected(attributes.getRequest(), attributes.getResponse(), parameter);
                    }
                }else {
                    throw new ValidateMethodException("he return value type of "+ method.getName() +" should be Boolean.");
                }

            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
            reflectMethod = reflectMethod.getNext();
        }
    }


    /**
     * 初始化确定方法名的反射缓存*
     * @param key 缓存key
     * @param validateObj 缓存实例类型
     * @param methodName 方法名
     */
    private void initReflectMethodCache(String key, Class<?> validateObj, String methodName){
        synchronized (getReflectMethodCacheMapMutex()){
            //初始化
            if(reflectMethodMap.get(key) == null){
                final Method[] methods = validateObj.getMethods();
                final Object bean = AuthHandlerUtil.getBean(application, validateObj);
                for (Method method : methods) {
                    if (method.getParameterCount() == 1 && method.getName().equals(methodName)){
                        updateReflectMap(key, bean, method, reflectMethodMap);
                    }
                }
            }
        }

    }

    /**
     * 初始化确定参数类型的反射缓存*
     * @param key 缓存key
     * @param validateObj 缓存实例类型
     * @param parameter 缓存参数类型(仅缓存存在一个实例的对象)
     */
    private void initReflectParameterCache(String key, Class<?> validateObj, Class<?> parameter){
        synchronized (getReflectParameterCacheMapMutex()){
            if(reflectParameterMap.get(key) == null){
                final Method[] methods = validateObj.getMethods();
                final Object bean = AuthHandlerUtil.getBean(application, validateObj);
                for (Method method : methods) {
                    if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == parameter){
                        updateReflectMap(key, bean, method, reflectParameterMap);
                    }
                }
            }
        }
    }

    /**
     * 更新反射缓存
     * @param key 缓存key
     * @param bean 应用实例
     * @param method 反射方法
     * @param reflectMap 缓存Map
     */
    private void updateReflectMap(String key, Object bean, Method method, Map<String, ReflectMethod> reflectMap) {
        method.setAccessible(true);
        final ReflectMethod newReflectMethod = new ReflectMethod(bean, method, method.getParameterTypes()[0]);
        if (reflectMap.get(key) == null){
            reflectMap.put(key, newReflectMethod);
        }else {
            ReflectMethod mapReflectMethod = reflectMap.get(key);
            while (mapReflectMethod.getNext() != null){
                mapReflectMethod = mapReflectMethod.getNext();
            }
            mapReflectMethod.setNext(newReflectMethod);
        }
    }

    private String key(Class<?> validateObjClazz, String methodName){
        return validateObjClazz.getName() + "$" + methodName;
    }

    private String parameterKey(Class<?> validateObjClazz, Class<?> parameter){
        return validateObjClazz.getName() + "$" + parameter.getName();
    }

    private Object getReflectMethodCacheMapMutex(){
        return reflectMethodMap;
    }
    private Object getReflectParameterCacheMapMutex(){
        return reflectParameterMap;
    }
}
