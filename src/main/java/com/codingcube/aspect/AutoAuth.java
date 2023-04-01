package com.codingcube.aspect;

import com.codingcube.annotation.AutoAuthService;
import com.codingcube.annotation.IsAuthor;
import com.codingcube.exception.PermissionsException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
public class AutoAuth {
    @Resource
    private ApplicationContext applicationContext;

    @Around("@within(isAuthor)")
    public Object isAuthorClass(ProceedingJoinPoint joinPoint, IsAuthor isAuthor){
        final Class<? extends AutoAuthService>[] autoAuthServices = isAuthor.authentication();
        final String permissions = isAuthor.value();

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        //鉴权
        Arrays.stream(autoAuthServices).forEach(
            (item)->{
                final AutoAuthService autoAuthService = applicationContext.getBean(item);
                if (!autoAuthService.isAuthor(request, permissions)){
                    //权限不足
                    throw new PermissionsException("lack of permissions");
                }
            }
        );
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new RuntimeException();
    }
    @Around("@annotation(isAuthor)")
    public Object isAuthormethod(ProceedingJoinPoint joinPoint, IsAuthor isAuthor){
        return isAuthorClass(joinPoint, isAuthor);
    }

}