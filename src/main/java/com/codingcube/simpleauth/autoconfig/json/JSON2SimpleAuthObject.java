package com.codingcube.simpleauth.autoconfig.json;

import com.alibaba.fastjson.JSON;
import com.codingcube.simpleauth.autoconfig.Config2SimpleAuthObject;
import com.codingcube.simpleauth.autoconfig.domain.*;
import com.codingcube.simpleauth.autoconfig.execption.JSONParseException;
import com.codingcube.simpleauth.autoconfig.execption.XMLParseException;
import com.codingcube.simpleauth.autoconfig.factory.ConfigFactory;
import com.codingcube.simpleauth.autoconfig.json.utils.JSONUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class JSON2SimpleAuthObject implements Config2SimpleAuthObject {
    List<String> config;
    Map<String, Handler> handlerMap;
    Map<String, Limit> limitMap;
    Map<String, HandlerChain> handlerChainMap;
    Map<String, Paths> pathsMap;
    private final JSONObject jsonObject;

    public JSON2SimpleAuthObject(String path) {
        final InputStream resourceAsStream = JSON2SimpleAuthObject.class.getClassLoader().getResourceAsStream(path);
        if (resourceAsStream==null){
            throw new XMLParseException();
        }
        StringBuilder sb = new StringBuilder();
        try {
            byte[] read = new byte[1024];
            while (resourceAsStream.read(read) != -1){
                sb.append(new String(read));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.jsonObject = new JSONObject(sb.toString());
        } catch (JSONException e) {
            throw new JSONParseException(e);
        }

        this.handlerMap = new HashMap<>();
        this.limitMap = new HashMap<>();
        this.handlerChainMap = new HashMap<>();
        this.pathsMap = new HashMap<>();
    }

    @Override
    public void initAttr(SimpleAuthConfig simpleAuthConfig) {
        this.handlerMap = simpleAuthConfig.getHandlerMap();
        this.limitMap = simpleAuthConfig.getLimitMap();
        this.handlerChainMap = simpleAuthConfig.getHandlerChainMap();
        this.pathsMap = simpleAuthConfig.getPathsMap();
    }

    @Override
    public void initConfig() {
        config = JSONUtils.getStringList(jsonObject, "configs");
    }

    @Override
    public List<String> getConfig() {
        if (config == null){
            return new ArrayList<>();
        }
        return config;
    }

    @Override
    public void initPaths() {
        final JSONArray pathsJSONArray = JSONUtils.geJSONArray(jsonObject, "paths");
        if (pathsJSONArray != null){
            for (int i = 0; i < pathsJSONArray.length(); i++) {
                try {
                    final JSONObject jsonObject = pathsJSONArray.getJSONObject(i);
                    if (jsonObject != null ){
                        final Paths paths = JSON.parseObject(jsonObject.toString(), Paths.class);
                        if (paths.getId() == null){
                            throw new JSONParseException("The 'paths' tag requires an 'id' attribute.");
                        }
                        if (pathsMap.get(paths.getId()) != null){
                            throw new XMLParseException("The 'id'("+ paths.getId() +") attribute of the 'paths' tag is duplicated.");
                        }
                        pathsMap.put(paths.getId(), paths);
                    }
                } catch (JSONException e) {
                    throw new JSONParseException();
                }
            }
        }
    }

    @Override
    public void initHandler() {
        final JSONArray handlerJSONArray = JSONUtils.geJSONArray(jsonObject, "handler");
        if (handlerJSONArray != null){
            for (int i = 0; i < handlerJSONArray.length(); i++) {
                try {
                    final JSONObject jsonObject = handlerJSONArray.getJSONObject(i);
                    if (jsonObject != null ){
                        final Handler handler = JSON.parseObject(jsonObject.toString(), Handler.class);
                        if (handler.getClazz() == null){
                            throw new JSONParseException("handler needs \"class\"");
                        }
                        if (handler.getId() == null){
                            handler.setId(handler.getClazz());
                        }

                        if (handler.getPaths() != null){
                            //互通外部的PathsId 与 Paths 内部Id
                            if (handler.getPaths().getId() !=null
                                    && handler.getPathsId() == null){
                                handler.setPathsId(handler.getPaths().getId());
                            }
                            if (handler.getPaths().getId() == null
                                    && handler.getPathsId() != null){
                                handler.getPaths().setId(handler.getPathsId());
                            }
                            this.pathsMap.computeIfAbsent(handler.getPathsId(), k -> handler.getPaths());
                        }
                        this.handlerMap.put(handler.getId(), handler);
                    }
                } catch (JSONException e) {
                    throw new JSONParseException();
                }
            }
        }

    }

    @Override
    public void initHandlerChain() {
        final JSONArray limitJSONArray = JSONUtils.geJSONArray(jsonObject, "handlerChain");
        if (limitJSONArray != null){
            for (int i = 0; i < limitJSONArray.length(); i++) {
                try {
                    final JSONObject jsonObject = limitJSONArray.getJSONObject(i);
                    if (jsonObject != null ){
                        final HandlerChain handlerChain = JSON.parseObject(jsonObject.toString(), HandlerChain.class);
                        final String id = handlerChain.getId();
                        final List<Handler> handlerList = handlerChain.getHandlerList();
                        if (id == null || "".equals(id)){
                            //HandlerChain的默认策略为 所有其内部注册的HandlerId相加的MD5值
                            StringBuilder sb = new StringBuilder();
                            handlerList.forEach(handler -> sb.append(handler).append("$"));
                            handlerChain.setId(DigestUtils.md5DigestAsHex(sb.toString().getBytes(StandardCharsets.UTF_8)));
                        }
                        //缓存handler
                        for (int j=0; j< handlerList.size() ; j++) {
                            Handler handler = handlerList.get(j);
                            if (handler.getClazz() == null && handler.getId() == null) {
                                throw new JSONParseException("missing id or class");
                            }
                            if (handler.getId() == null) {
                                handler.setId(handler.getClazz());
                            }
                            if (this.handlerMap.get(handler.getId()) != null) {
                                handlerList.set(j, this.handlerMap.get(handler.getId()));
                            }else {
                                this.handlerMap.put(handler.getId(), handler);

                            }
                        }
                        //装配Path
                        if (handlerChain.getPaths() != null){
                            //互通外部的PathsId 与 Paths 内部Id
                            if (handlerChain.getPaths().getId() !=null
                                    && handlerChain.getPathId() == null){
                                handlerChain.setPathId(handlerChain.getPaths().getId());
                            }
                            if (handlerChain.getPaths().getId() == null
                                    && handlerChain.getPathId() != null){
                                handlerChain.getPaths().setId(handlerChain.getPathId());
                            }
                            this.pathsMap.computeIfAbsent(handlerChain.getPathId(), k -> handlerChain.getPaths());
                        }
                        this.handlerChainMap.put(handlerChain.getId(), handlerChain);
                    }
                } catch (JSONException e) {
                    throw new JSONParseException();
                }
            }
        }
    }

    @Override
    public void initLimit() {
        final JSONArray limitJSONArray = JSONUtils.geJSONArray(jsonObject, "limit");
        if (limitJSONArray != null){
            for (int i = 0; i < limitJSONArray.length(); i++) {
                try {
                    final JSONObject jsonObject = limitJSONArray.getJSONObject(i);
                    if (jsonObject != null ){
                        final Limit limit = JSON.parseObject(jsonObject.toString(), Limit.class);
                        if (limit.getId() == null){
                            limit.setId(UUID.randomUUID().toString());
                        }
                        //初始化策略
                        limit.setSignStrategic(limit.getSignStrategic());
                        limit.setItemStrategic(limit.getItemStrategic());
                        limit.setTokenLimit(limit.getTokenLimit());
                        limit.setEffectiveStrategic(limit.getEffectiveStrategic());
                        //装配Path
                        if (limit.getPaths() != null){
                            //互通外部的PathsId 与 Paths 内部Id
                            if (limit.getPaths().getId() !=null
                                    && limit.getPathId() == null){
                                limit.setPathId(limit.getPaths().getId());
                            }
                            if (limit.getPaths().getId() == null
                                    && limit.getPathId() != null){
                                limit.getPaths().setId(limit.getPathId());
                            }
                            this.pathsMap.computeIfAbsent(limit.getPathId(), k -> limit.getPaths());
                        }
                        this.limitMap.put(limit.getId(), limit);
                    }
                } catch (JSONException e) {
                    throw new JSONParseException();
                }
            }
        }
    }
}
