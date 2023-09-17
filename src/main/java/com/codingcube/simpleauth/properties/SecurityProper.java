package com.codingcube.simpleauth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "simple-auth.security")
public class SecurityProper {

    private List<String> XFrameOptionsPath = new ArrayList<>();
    private List<String> XFrameOptionsExcludePath = new ArrayList<>();
    private String XFrameOptions = "SAMEORIGIN";

    private List<String> contentSecurityPolicyPath = new ArrayList<>();
    private List<String> contentSecurityPolicyExcludePath = new ArrayList<>();
    private String ContentSecurityPolicy = "default-src 'self'";

    public List<String> getXFrameOptionsPath() {
        return XFrameOptionsPath;
    }

    public List<String> getContentSecurityPolicyExcludePath() {
        return contentSecurityPolicyExcludePath;
    }

    public void setContentSecurityPolicyExcludePath(List<String> contentSecurityPolicyExcludePath) {
        this.contentSecurityPolicyExcludePath = contentSecurityPolicyExcludePath;
    }

    public List<String> getXFrameOptionsExcludePath() {
        return XFrameOptionsExcludePath;
    }

    public void setXFrameOptionsExcludePath(List<String> XFrameOptionsExcludePath) {
        this.XFrameOptionsExcludePath = XFrameOptionsExcludePath;
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
