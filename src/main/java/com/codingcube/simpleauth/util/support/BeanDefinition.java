package com.codingcube.simpleauth.util.support;

public class BeanDefinition {
    /**
     * false 0
     * singleton 1
     * prototype 2
     * service 3
     */
    Integer type;

    public BeanDefinition(Integer type) {
        this.type = type;
    }

    public BeanDefinition(String type) {
        switch (type){
            case "singleton":
                this.type = 1;
                break;
            case "prototype":
                this.type = 2;
                break;
            case "request":
                this.type = 3;
                break;
            case "session":
                this.type = 4;
                break;
            case "false":
            default:
                this.type = 0;
                break;
        }
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
