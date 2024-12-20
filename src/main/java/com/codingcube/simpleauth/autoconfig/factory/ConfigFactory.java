package com.codingcube.simpleauth.autoconfig.factory;

import com.codingcube.simpleauth.autoconfig.Config2SimpleAuthObject;
import com.codingcube.simpleauth.autoconfig.domain.*;
import com.codingcube.simpleauth.autoconfig.execption.ConfigurationParseException;
import com.codingcube.simpleauth.properties.AuthProper;
import com.codingcube.simpleauth.properties.LimitProper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CodingCube
 */
public class ConfigFactory {
    Class<? extends Config2SimpleAuthObject> config2SimpleAuthObjectClazz;
    SimpleAuthConfig simpleAuthConfig;
    List<String> currentConfigList;

    public ConfigFactory(Class<? extends Config2SimpleAuthObject> config2SimpleAuthObjectClazz) {
        this.config2SimpleAuthObjectClazz = config2SimpleAuthObjectClazz;
        simpleAuthConfig = new SimpleAuthConfig(new HashMap<>(),new HashMap<>(),new HashMap<>(), new HashMap<>());
    }

    public ConfigFactory(Class<? extends Config2SimpleAuthObject> config2SimpleAuthObjectClazz, SimpleAuthConfig simpleAuthConfig) {
        this.config2SimpleAuthObjectClazz = config2SimpleAuthObjectClazz;
        this.simpleAuthConfig = simpleAuthConfig;
    }

    public SimpleAuthConfig getConfig(String path){
        //初始化一级缓存，缓存到 simpleAuthConfig
        List<String> configPathList = new ArrayList<>();
        configPathList.add(path);
        int processedNum = 0;
        while (configPathList.size() > processedNum){
            initConfig(configPathList.get(processedNum));
            processedNum++;
            currentConfigList.forEach(item-> {
                if (!item.contains(".xml") && !item.contains(".json")){
                    findAndAdd(configPathList,item+".xml");
                }else {
                    findAndAdd(configPathList,item);
                }

            });
        }
        //装配
        this.assembleConfig();
        return simpleAuthConfig;
    }

    private void initConfig(String path){
        final Config2SimpleAuthObject config2SimpleAuthObject;
        try {
            config2SimpleAuthObject = this.config2SimpleAuthObjectClazz.getConstructor(String.class).newInstance(path);
            config2SimpleAuthObject.initAttr(simpleAuthConfig);
            config2SimpleAuthObject.init();
            currentConfigList = config2SimpleAuthObject.getConfig();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ConfigurationParseException("Parser initialization error. Requires a constructor with only a String path.", e);
        }
    }

    /**
     * 如果存在则不添加*
     */
    private void findAndAdd(List<String> configPathList, String configPath){
        for (final String s : configPathList) {
            if (s.equals(configPath)) {
                return;
            }
        }
        configPathList.add(configPath);
    }

    private void assembleConfig(){
        final Map<String, Handler> handlerMap = simpleAuthConfig.getHandlerMap();
        final Map<String, Limit> limitMap = simpleAuthConfig.getLimitMap();
        final Map<String, Paths> pathsMap = simpleAuthConfig.getPathsMap();
        final Map<String, HandlerChain> handlerChainMap = simpleAuthConfig.getHandlerChainMap();
        handlerMap.forEach((key, value) ->{
            //装配Paths
            final String pathId = value.getPathId();
            if (pathId != null){
                value.setPaths(pathsMap.get(pathId));
            }
            //装配Clazz
            value.setHandlerClass(this.getClassForName(value.getClazz()));
            //装配rejected
            if (value.getRejected() != null){
                value.setRejectedClass(this.getClassForName(value.getRejected()));
            }else {
                value.setRejectedClass(AuthProper.getDefaultRejectedClazz());
            }
            //检查scope
            if (value.getScope() == null){
                value.setScope("SINGLETON");
            }
        });
        limitMap.forEach((key, value) ->{
            //装配Paths
            final String pathId = value.getPathId();
            if (pathId != null){
                value.setPaths(pathsMap.get(pathId));
            }
            //装配signStrategicClass
            if (value.getSignStrategicClass() == null){
                value.setSignStrategicClass(getClassForName(value.getSignStrategic()));
            }
            //装配EffectiveStrategicClass
            if (value.getEffectiveStrategicClass() == null){
                value.setEffectiveStrategicClass(getClassForName(value.getEffectiveStrategic()));
            }
            //装配TokenLimitClass
            if (value.getTokenLimitClass() == null){
                value.setTokenLimitClass(getClassForName(value.getTokenLimit()));
            }
            //装配Rejected
            if (value.getRejectedClass() != null){
                value.setRejectedClass(getClassForName(value.getRejected()));
            }else {
                value.setRejectedClass(LimitProper.getDefaultRejectedClazz());
            }
        });
        handlerChainMap.forEach(
            (key, value) -> {
                //装配Handler
                final List<Handler> handlerList = value.getHandlerList();
                for (int i = 0; i < handlerList.size(); i++) {
                    Handler handler  =  handlerList.get(i);
                    final String clazz = handler.getClazz();
                    if (clazz == null || "".equals(clazz)){
                        final Handler findHandler = handlerMap.get(handler.getId());
                        if (findHandler == null){
                            throw new ConfigurationParseException("Handler with id '"+handler.getId()+"'not found in the handlerChain configuration");
                        }
                        handlerList.set(i, findHandler);
                    }
                }
                //装配rejected
                if (value.getRejected() != null){
                    value.setRejectedClass(this.getClassForName(value.getRejected()));
                }else {
                    value.setRejectedClass(AuthProper.getDefaultRejectedClazz());
                }
                //装配Paths
                final String pathId = value.getPathId();
                if (pathId != null){
                    value.setPaths(pathsMap.get(pathId));
                }
            }
        );

    }

    private <T> T getClassForName(String className){
        try {
            if (className == null){
                return null;
            }else {
                return (T)Class.forName(className);
            }
        } catch (ClassNotFoundException e) {
            throw new ConfigurationParseException("class:"+className+" not find", e);
        } catch (ClassCastException e) {
            return null;
        }
    }
}
