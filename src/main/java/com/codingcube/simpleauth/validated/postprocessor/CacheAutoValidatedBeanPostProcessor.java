package com.codingcube.simpleauth.validated.postprocessor;

import com.codingcube.simpleauth.properties.ValidateProper;
import com.codingcube.simpleauth.validated.annotation.SimpleValidate;
import com.codingcube.simpleauth.validated.aspect.AutoValidated;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 在使用前缓存信息的工具 *
 * @author CodingCube*
 */
@Component
public class CacheAutoValidatedBeanPostProcessor implements BeanPostProcessor {
    @Resource
    private AutoValidated autoValidated;
    @Resource
    private ValidateProper validateProper;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        final SimpleValidate classAnnotation = bean.getClass().getAnnotation(SimpleValidate.class);
        final Class<?> classValidateObj = classAnnotation==null ? Object.class:classAnnotation.value();

        final Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            final Class<?>[] parameterTypes = method.getParameterTypes();
            final SimpleValidate methodAnnotation = method.getAnnotation(SimpleValidate.class);

            final Class<?> methodValidateObj = methodAnnotation==null ? Object.class:methodAnnotation.value();

            final Annotation[][] allAnnotation = method.getParameterAnnotations();
            for (int i = 0; i < allAnnotation.length; i++) {
                Annotation[] allParamAnnotation = allAnnotation[i];
                for (Annotation paramAnnotation : allParamAnnotation) {
                    if (paramAnnotation.annotationType() == SimpleValidate.class){
                        final SimpleValidate paramValidateAnnotation = (SimpleValidate) paramAnnotation;
                        final Class<?> paramValidateObj = paramValidateAnnotation.value();
                        final String[] methodsString = paramValidateAnnotation.methods();

                        final Class<?> validateObj = getValidateObj(Object.class,
                                ValidateProper.getDefaultValidateObjectClazz(),
                                paramValidateObj,
                                methodValidateObj,
                                classValidateObj);
                        if (methodsString.length == 0){
                            final String key = autoValidated.parameterKey(validateObj, parameterTypes[i]);
                            autoValidated.initReflectParameterCache(key, validateObj, parameterTypes[i]);
                        }else {
                            for (String methodName : methodsString) {
                                autoValidated.initReflectMethodCache(autoValidated.key(validateObj, methodName),
                                        validateObj, methodName);
                            }
                        }

                    }
                }
            }
            if (methodAnnotation != null){
                String[] methodsString = methodAnnotation.methods();
                if (methodsString.length == 0){
                    methodsString = new String[1];
                    methodsString[0] = "validate";
                }
                final Class<?> validateObj = getValidateObj(Object.class,
                        ValidateProper.getDefaultValidateObjectClazz(),
                        methodValidateObj,
                        classValidateObj);

                for (String methodName : methodsString) {
                    autoValidated.initReflectMethodCache(autoValidated.key(validateObj, methodName),
                            validateObj, methodName);
                }
            }
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    private Class<?> getValidateObj(Class<?> invalidValue, Class<?> defaultValue ,Class<?> ...valueList){
        for (Class<?> value : valueList) {
            if (value != invalidValue){
                return value;
            }
        }
        return defaultValue;
    }
}
