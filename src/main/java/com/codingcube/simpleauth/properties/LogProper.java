package com.codingcube.simpleauth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "simple-auth.log")
public class LogProper {
    private String logImpl = "NoLogging";
    private String limitLogImpl = "NoLogging";
    private Boolean showOptList = false;
    private static Boolean staticShowOptList = false;
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private static String dateFormatStatic = "yyyy-MM-dd HH:mm:ss";

    public String getLogImpl() {
        return logImpl;
    }

    public void setLogImpl(String logImpl) {
        this.logImpl = logImpl;
    }

    public String getLimitLogImpl() {
        return limitLogImpl;
    }

    public void setLimitLogImpl(String limitLogImpl) {
        this.limitLogImpl = limitLogImpl;
    }

    public Boolean getShowOptList() {
        return showOptList;
    }

    public void setShowOptList(Boolean showOptList) {
        this.showOptList = showOptList;
    }

    public static Boolean getStaticShowOptList() {
        return staticShowOptList;
    }

    public static void setStaticShowOptList(Boolean staticShowOptList) {
        LogProper.staticShowOptList = staticShowOptList;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public static String getDateFormatStatic() {
        return dateFormatStatic;
    }

    public static void setDateFormatStatic(String dateFormatStatic) {
        LogProper.dateFormatStatic = dateFormatStatic;
    }
}
