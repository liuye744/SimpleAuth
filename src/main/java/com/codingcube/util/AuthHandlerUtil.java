package com.codingcube.util;

import com.codingcube.exception.PermissionsException;
import com.codingcube.exception.TargetNotFoundException;
import com.codingcube.handler.AutoAuthHandler;
import com.codingcube.handler.AutoAuthHandlerChain;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author CodingCube<br>*
 * Utility Class for Handling Handlers*
 */
public class AuthHandlerUtil {
    public static void handlerChain(AutoAuthHandlerChain autoAuthHandlerChain, ApplicationContext applicationContext, HttpServletRequest request, String permissions){
        final List<Object> autoAuthServiceList = autoAuthHandlerChain.getAutoAuthServiceList();
        autoAuthServiceList.forEach(
                (item)->{
                    final AutoAuthHandler autoAuth;
                    if (item instanceof String ){
                        //item is BeanName
                        autoAuth = applicationContext.getBean((String) item, AutoAuthHandler.class);

                    }else if (item instanceof Class){
                        //item is class of AutoAuthService
                        autoAuth = applicationContext.getBean((Class<? extends AutoAuthHandler>) item);
                    }else {
                        throw new TargetNotFoundException("handlerChain error. The value can only be String or Class<? extends AutoAuthHandler>");
                    }

                    if (!autoAuth.isAuthor(request, permissions)){
                        //Permission not met
                        throw new PermissionsException("lack of permissions");
                    }
                }
        );

    }
}
