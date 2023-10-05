package com.codingcube.simpleauth.autoconfig.factory;

import com.codingcube.simpleauth.autoconfig.Config2SimpleAuthObject;
import com.codingcube.simpleauth.autoconfig.domain.Handler;
import com.codingcube.simpleauth.autoconfig.domain.Limit;
import com.codingcube.simpleauth.autoconfig.domain.Paths;
import com.codingcube.simpleauth.autoconfig.domain.SimpleAuthConfig;
import com.codingcube.simpleauth.autoconfig.execption.XMLParseException;
import com.codingcube.simpleauth.autoconfig.xml.XML2SimpleAuthObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigFactory {
    Class<? extends Config2SimpleAuthObject> config2SimpleAuthObjectClazz;
    SimpleAuthConfig simpleAuthConfig;
    List<String> currentConfigList;

    public ConfigFactory(Class<? extends Config2SimpleAuthObject> config2SimpleAuthObjectClazz) {
        this.config2SimpleAuthObjectClazz = config2SimpleAuthObjectClazz;
        simpleAuthConfig = new SimpleAuthConfig(new HashMap<>(),new HashMap<>(),new HashMap<>());
    }

    public ConfigFactory(Class<? extends Config2SimpleAuthObject> config2SimpleAuthObjectClazz, SimpleAuthConfig simpleAuthConfig) {
        this.config2SimpleAuthObjectClazz = config2SimpleAuthObjectClazz;
        this.simpleAuthConfig = simpleAuthConfig;
    }

    SimpleAuthConfig getConfig(String path){
        //初始化一级缓存，缓存到 simpleAuthConfig
        List<String> configPathList = new ArrayList<>();
        configPathList.add(path);
        int processedNum = 0;
        while (configPathList.size() > processedNum){
            initConfig(configPathList.get(processedNum));
            processedNum++;
            currentConfigList.forEach(item-> {
                if (!item.contains(".xml")){
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
            throw new XMLParseException("Requires a constructor with only a String path.");
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
        handlerMap.forEach((key, value) ->{
            final String pathId = value.getPathId();
            if (pathId != null){
                value.setPaths(pathsMap.get(pathId));
            }
        });
        limitMap.forEach((key, value) ->{
            final String pathId = value.getPathId();
            if (pathId != null){
                value.setPaths(pathsMap.get(pathId));
            }
        });
    }
}
