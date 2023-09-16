package com.codingcube.simpleauth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "simple-auth.security")
public class SecurityProper {

    private List<String> XFrameOptionsPath = new ArrayList<>();
    private String XFrameOptions = "SAMEORIGIN";

    private List<String> contentSecurityPolicyPath = new ArrayList<>();
    private String ContentSecurityPolicy = "default-src 'self'";

    public List<String> getXFrameOptionsPath() {
        return XFrameOptionsPath;
    }

    public void setXFrameOptionsPath(List<String> XFrameOptionsPath) {
        this.XFrameOptionsPath = XFrameOptionsPath;
    }

    public String getXFrameOptions() {
        return XFrameOptions;
    }

    public void setXFrameOptions(String XFrameOptions) {
        this.XFrameOptions = XFrameOptions;
    }

    public List<String> getContentSecurityPolicyPath() {
        return contentSecurityPolicyPath;
    }

    public void setContentSecurityPolicyPath(List<String> contentSecurityPolicyPath) {
        this.contentSecurityPolicyPath = contentSecurityPolicyPath;
    }

    public String getContentSecurityPolicy() {
        return ContentSecurityPolicy;
    }

    public void setContentSecurityPolicy(String contentSecurityPolicy) {
        ContentSecurityPolicy = contentSecurityPolicy;
    }
}
