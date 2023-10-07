package com.codingcube.simpleauth.autoconfig.xml;

import com.codingcube.simpleauth.auth.strategic.SignStrategic;
import com.codingcube.simpleauth.autoconfig.Config2SimpleAuthObject;
import com.codingcube.simpleauth.autoconfig.domain.*;
import com.codingcube.simpleauth.autoconfig.execption.XMLParseException;
import com.codingcube.simpleauth.limit.util.TokenLimit;
import org.apache.tomcat.util.security.MD5Encoder;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XML2SimpleAuthObject implements Config2SimpleAuthObject {
    private final Element rootElement;
    private List<String> configList;
    private Map<String, Paths> pathsMap;
    private Map<String, Handler> handlerMap;
    private Map<String, HandlerChain> handlerChainMap;
    private Map<String, Limit> limitMap;


    public XML2SimpleAuthObject(String path) {
        SAXBuilder saxBuilder = new SAXBuilder();

        final ClassLoader classLoader = XML2SimpleAuthObject.class.getClassLoader();
        final InputStream resourceAsStream = classLoader.getResourceAsStream(path);
        try {
            final Document build = saxBuilder.build(resourceAsStream);
            rootElement = build.getRootElement();
        } catch (JDOMException | IOException e) {
            throw new XMLParseException("Configuration file '"+path+"' initialization error", e);
        }
        initAttr();
    }

    private void initAttr(){
        this.configList = new ArrayList<>();
        this.pathsMap = new HashMap<>();
        this.handlerMap = new HashMap<>();
        this.limitMap = new HashMap<>();
        this.handlerChainMap = new HashMap<>();
    }

    public void initAttr(SimpleAuthConfig simpleAuthConfig) {
        this.pathsMap = simpleAuthConfig.getPathsMap();
        this.handlerMap = simpleAuthConfig.getHandlerMap();
        this.limitMap = simpleAuthConfig.getLimitMap();
        this.handlerChainMap = simpleAuthConfig.getHandlerChainMap();
    }

    @Override
    public void initConfig() {
        final Element configs = rootElement.getChild("configs");
        if (configs == null){
            return;
        }
        final List<Element> config = configs.getChildren("config");
        config.forEach(item->{
            final String value = item.getValue().trim();
            if (!"".equals(value)){
                configList.add(value);
            }
        });

    }

    @Override
    public List<String> getConfig() {
        return configList;
    }

    @Override
    public void initPaths() {
        final List<Element> pathsElementList = rootElement.getChildren("paths");
        if (pathsElementList == null){
            return;
        }
        //遍历所有的Paths
        pathsElementList.forEach(pathsElement -> {
            final Paths path = this.parsePaths(pathsElement);
            if (pathsMap.get(path.getId()) != null){
                throw new XMLParseException("The 'id'("+ path.getId() +") attribute of the 'paths' tag is duplicated.");
            }
            pathsMap.put(path.getId(), path);
        });
    }

    @Override
    public void initHandler() {
        final List<Element> handlerElementList = rootElement.getChildren("handler");

        handlerElementList.forEach(handlerElement -> {
            final Handler handler = assembleHandler(handlerElement);
            if (this.handlerMap.get(handler.getId()) != null){
                throw new XMLParseException("The 'id' or 'name' ("+ handler.getId() +") attribute of the 'handler' tag is duplicated.");
            }
            this.handlerMap.put(handler.getId(), handler);
        });
    }

    @Override
    public void initHandlerChain() {
        final List<Element> handlerChainElementList = rootElement.getChildren("handlerChain");
        handlerChainElementList.forEach(handlerChainElement -> {
            final Element list = handlerChainElement.getChild("list");
            if (list == null){
                throw new XMLParseException("Missing 'list' tag");
            }
            final List<Element> handlerElementList = list.getChildren("handler");
            if (handlerElementList.size() == 0){
                throw new XMLParseException("Missing 'handler' tag");
            }
            List<Handler> handlerList = new ArrayList<>();
            handlerElementList.forEach(
                    handlerElement -> handlerList.add(assembleHandlerChainHandler(handlerElement))
            );
            //初始化HandlerChain ID
            String id = handlerChainElement.getAttributeValue("id");
            if (id == null || "".equals(id)){
                id = handlerChainElement.getAttributeValue("name");
            }
            if (id == null || "".equals(id)){
                //HandlerChain的默认策略为 所有其内部注册的HandlerId相加的MD5值
                StringBuilder sb = new StringBuilder();
                handlerList.forEach(handler -> sb.append(handler).append("$"));
                id = MD5Encoder.encode(sb.toString().getBytes(StandardCharsets.UTF_8));
            }
            final HandlerChain handlerChain = new HandlerChain(id, handlerList);
            assemblePath(handlerChainElement, handlerChain);

            if (this.handlerChainMap.get(handlerChain.getId()) != null){
                throw new XMLParseException("handlerChain id is duplicated, or handlers in unnamed different handlerChain are all identical");
            }
            this.handlerChainMap.put(handlerChain.getId(), handlerChain);
        });
    }

    private Handler assembleHandlerChainHandler(Element element){
        final String id = getId(element);
        if (id == null){
            //ID为Class 缓存到handlerMap 查重报错
            final Handler handler = assembleHandler(element);
            this.handlerMap.putIfAbsent(handler.getId(), handler);
            return handler;
        }
        final Element clazz = element.getChild("class");
        if (clazz == null){
            return new Handler(id);
        }
        final Handler handler = assembleHandler(element);
        if (this.handlerMap.get(handler.getId()) != null) {
            throw new XMLParseException("The handler in handlerChain id '"+handler.getId()+"' duplicated");
        }else {
            this.handlerMap.put(handler.getId(), handler);
        }
        return handler;
    }

    @Override
    public void initLimit() {
        final List<Element> limitElementList = rootElement.getChildren("limit");

        limitElementList.forEach(limitElement -> {
            final Limit limit = assembleLimit(limitElement);
            if (this.limitMap.get(limit.getId()) != null){
                throw new XMLParseException("The 'id' or 'name' ("+ limit.getId() +") attribute of the 'handler' tag is duplicated.");
            }
            this.limitMap.put(limit.getId(), limit);
        });
    }

    @Override
    public void init() {
        Config2SimpleAuthObject.super.init();
    }

    public List<String> getConfigList() {
        return configList;
    }

    private Handler assembleHandler(Element element){
        final String clazz = this.getClazz(element);
        //初始化Id
        final String id = this.getId(element, clazz);

        //装配id 和class标签
        Handler handler = new Handler(id);
        handler.setClazz(clazz);

        //装配scope标签
        handler.setScope(this.parseStringValue(element.getChild("scope")));
        //装配path
        assemblePath(element, handler);
        return handler;
    }

    /**
     * 装配Paths*
     * @param element paths的父标签
     * @param obj 需要装配的对象
     */
    private void assemblePath(Element element, Object obj){
        final Class<?> objClazz = obj.getClass();
        //装配path标签
        final Element pathsElement = element.getChild("paths");
        if (pathsElement != null){
            final List<Element> path = pathsElement.getChildren("path");
            if (path.size() == 0){
                //只装配Id
                final Attribute pathIdAttribute = pathsElement.getAttribute("id");
                if (pathIdAttribute == null || "".equals(pathIdAttribute.getValue().trim())){
                    throw new XMLParseException("An empty 'paths' tag requires an 'id' attribute.");
                }
                try {
                    final Method setPathId = objClazz.getMethod("setPathId", String.class);
                    setPathId.invoke(obj, pathIdAttribute.getValue().trim());
                } catch (NoSuchMethodException| InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }else {
                //装配Path
                final Paths paths = parsePathNoId(pathsElement);

                try {
                    final Method setPaths = objClazz.getMethod("setPaths", Paths.class);
                    setPaths.invoke(obj, paths);
                } catch (NoSuchMethodException| InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }

                //查询是否注册Paths
                if (paths.getId() != null){
                    this.pathsMap.put(paths.getId(), paths);
                }
            }
        }
    }

    private Limit assembleLimit(Element element){
        //初始化Id
        final String id = this.getId(element, "");
        Limit limit = new Limit(id);

        //Integer times;
        limit.setTimes(this.parseIntegerValue(element.getChild("times")));
        //Integer seconds;
        limit.setSeconds(this.parseIntegerValue(element.getChild("seconds")));
        //Integer ban;
        limit.setBan(this.parseIntegerValue(element.getChild("ban")));
        //String item;
        limit.setItemStrategic(this.parseStringValue(element.getChild("itemStrategic")));
        //String signStrategic;
        limit.setSignStrategic(this.parseStringValue(element.getChild("signStrategic")));
        //String effectiveStrategic;
        limit.setEffectiveStrategic(this.parseStringValue(element.getChild("effectiveStrategic")));
        //String tokenLimit;
        limit.setTokenLimit(this.parseStringValue(element.getChild("tokenLimit")));
        //Paths
        assemblePath(element, limit);
        return limit;
    }

    /**
     * * 获取id(属性可以为id或者name)，都没有则默认为class标签内容
     */
    private String getId(Element element, String defaultValue) {
        Attribute idAttribute = element.getAttribute("id");
        if (idAttribute == null){
            idAttribute = element.getAttribute("name");
        }
        if (idAttribute == null){
            return defaultValue;
        }else {
            return idAttribute.getValue();
        }
    }
    private String getId(Element element) {
        Attribute idAttribute = element.getAttribute("id");
        if (idAttribute == null){
            idAttribute = element.getAttribute("name");
        }
        return idAttribute == null ? null:idAttribute.getValue();
    }

    /**
     * * parse整数值
     */
    private Integer parseIntegerValue(Element element){
        if (element == null){
            return null;
        }
        return Integer.parseInt(element.getValue().trim());
    }

    /**
     * * parse String值
     */
    private String parseStringValue(Element element){
        if (element == null){
            return null;
        }
        return element.getValue().trim();
    }

    /**
     * 获取Class标签*
     */
    private String getClazz(Element element){
        //检查&初始化 必要的标签
        final Element clazzElement = element.getChild("class");
        if (clazzElement == null || "".equals(clazzElement.getValue().trim())){
            throw new XMLParseException("The 'handler' tag requires an 'class' tag.");
        }
        return clazzElement.getValue().trim();
    }

    /**
     * parsePaths标签（必须带有id属性）*
     */
    private Paths parsePaths(Element pathsElement){
        final Paths paths = parsePathNoId(pathsElement);
        if (paths.getId() == null){
            throw new XMLParseException("The 'paths' tag requires an 'id' attribute.");
        }
        return paths;
    }

    /**
     * parse可以没有id的Paths标签*
     */
    private Paths parsePathNoId(Element pathsElement){
        final Attribute idAttribute = pathsElement.getAttribute("id");
        String id = null;
        if (idAttribute != null){
            id = pathsElement.getAttribute("id").getValue();
        }
        final Paths paths = new Paths(id);

        final Element permissionElement = pathsElement.getChild("permission");
        if (permissionElement != null){
            paths.setPermission(permissionElement.getValue().trim());
        }

        final List<Element> pathElementList = pathsElement.getChildren("path");
        List<String> pathList = new ArrayList<>();
        pathElementList.forEach(path -> pathList.add(path.getValue().trim()));
        paths.setPath(pathList);
        return paths;
    }
}
