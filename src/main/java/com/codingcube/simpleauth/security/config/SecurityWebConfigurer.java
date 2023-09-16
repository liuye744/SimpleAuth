package com.codingcube.simpleauth.security.config;

import com.codingcube.simpleauth.properties.FunctionProper;
import com.codingcube.simpleauth.security.interceptor.AddXFrameOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

@Configuration
public class SecurityWebConfigurer implements WebMvcConfigurer {
    @Resource
    private FunctionProper functionProper;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        final List<String> clickjackingPath = functionProper.getClickjackingPath();
        if (clickjackingPath.size() != 0){
            final String xFrameOptions = functionProper.getXFrameOptions();
            clickjackingPath.forEach(registry.addInterceptor(new AddXFrameOptions(xFrameOptions))::addPathPatterns);
        }
    }
}
