package com.codingcube.simpleauth.logging.logformat;

import com.codingcube.simpleauth.util.AuthHandlerUtil;

public class LogValidatedFormat {
    private final Class<?> validateObjClass;
    private final Class<?> validateTargetClass;
    private final String validateTarget;
    private final Boolean passOrNot;

    public LogValidatedFormat(Class<?> validateObjClass, Class<?> validateTargetClass, String validateTarget, Boolean passOrNot) {
        this.validateObjClass = validateObjClass;
        this.validateTargetClass = validateTargetClass;
        this.validateTarget = validateTarget;
        this.passOrNot = passOrNot;
    }

    public Class<?> getValidateTargetClass() {
        return validateTargetClass;
    }

    public Class<?> getValidateObjClass() {
        return validateObjClass;
    }

    public Boolean getPassOrNot() {
        return passOrNot;
    }

    public String getValidateTarget() {
        return validateTarget;
    }

    @Override
    public String toString() {
        return "SimpleAuth validate => \r\n" +
                "\tvalidateObj: " +  validateObjClass + "\r\n" +
                "\tvalidateTarget: " + validateTargetClass + "\r\n" +
                "\tpass or not: " + passOrNot + "\r\n";
    }
}
