package com.codingcube.util;

import com.codingcube.exception.PermissionsException;
import com.codingcube.handler.AutoAuthHandler;
import com.codingcube.handler.AutoAuthHandlerChain;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AuthHandlerUtil {
    public static void handlerChain(AutoAuthHandlerChain autoAuthHandlerChain, ApplicationContext applicationContext, HttpServletRequest request, String permissions){
        final List<Object> autoAuthServiceList = autoAuthHandlerChain.getAutoAuthServiceList();
        autoAuthServiceList.forEach(
                (item)->{
                    final AutoAuthHandler autoAuth;
                    if (item instanceof String ){
                        //item is BeanName
                        autoAuth = applicationContext.getBean((String) item, AutoAuthHandler.class);

                    }else {
                        //item is class of AutoAuthService
                        autoAuth = applicationContext.getBean((Class<? extends AutoAuthHandler>) item);
                    }

                    if (!autoAuth.isAuthor(request, permissions)){
                        //Permission not met
                        throw new PermissionsException("lack of permissions");
                    }
                }
        );

    }
}
