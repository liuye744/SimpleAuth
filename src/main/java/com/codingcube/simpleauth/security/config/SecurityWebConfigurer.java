package com.codingcube.simpleauth.security.config;

import com.codingcube.simpleauth.properties.SecurityProper;
import com.codingcube.simpleauth.security.interceptor.AddXFrameOptionsInterceptor;
import com.codingcube.simpleauth.security.interceptor.ContentSecurityPolicyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

@Configuration
public class SecurityWebConfigurer implements WebMvcConfigurer {
    @Resource
    private SecurityProper securityProper;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //set Header 'X-Frame-Options'
        final List<String> clickjackingPath = securityProper.getXFrameOptionsPath();
        if (clickjackingPath.size() != 0){
            final String xFrameOptions = securityProper.getXFrameOptions();
            final InterceptorRegistration interceptorRegistration = registry.addInterceptor(new AddXFrameOptionsInterceptor(xFrameOptions));
            clickjackingPath.forEach(interceptorRegistration::addPathPatterns);

            final List<String> xFrameOptionsExcludePath = securityProper.getXFrameOptionsExcludePath();
            xFrameOptionsExcludePath.forEach(interceptorRegistration::excludePathPatterns);

        }

        //set Header 'Content-Security-Policy'
        final List<String> contentSecurityPolicyPath = securityProper.getContentSecurityPolicyPath();
        if (contentSecurityPolicyPath.size() != 0){
            final String contentSecurityPolicy = securityProper.getContentSecurityPolicy();
            final InterceptorRegistration interceptorRegistration = registry.addInterceptor(new ContentSecurityPolicyInterceptor(contentSecurityPolicy));
            contentSecurityPolicyPath.forEach(interceptorRegistration::addPathPatterns);

            final List<String> contentSecurityPolicyExcludePath = securityProper.getContentSecurityPolicyExcludePath();
            contentSecurityPolicyExcludePath.forEach(interceptorRegistration::excludePathPatterns);
        }
    }
}
