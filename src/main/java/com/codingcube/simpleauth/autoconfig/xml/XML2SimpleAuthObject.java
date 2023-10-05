package com.codingcube.simpleauth.autoconfig.xml;

import com.codingcube.simpleauth.autoconfig.Config2SimpleAuthObject;
import com.codingcube.simpleauth.autoconfig.domain.Handler;
import com.codingcube.simpleauth.autoconfig.domain.Limit;
import com.codingcube.simpleauth.autoconfig.domain.Paths;
import com.codingcube.simpleauth.autoconfig.execption.XMLParseException;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.xml.crypto.dsig.XMLSignatureException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XML2SimpleAuthObject implements Config2SimpleAuthObject {
    Element rootElement;
    List<String> configList;
    Map<String, Paths> pathsMap;
    Map<String, Handler> handlerMap;
    Map<String, Limit> limitMap;


    public XML2SimpleAuthObject(String path) throws IOException, JDOMException {
        SAXBuilder saxBuilder = new SAXBuilder();

        final ClassLoader classLoader = this.getClass().getClassLoader();
        final InputStream resourceAsStream = classLoader.getResourceAsStream(path);
        rootElement = saxBuilder.build(resourceAsStream).getRootElement();
        initAttr();
    }

    private void initAttr(){
        this.configList = new ArrayList<>();
        this.pathsMap = new HashMap<>();
        this.handlerMap = new HashMap<>();
        this.limitMap = new HashMap<>();
    }

    public void initAttr(List<String> configList, Map<String, Paths> pathsMap, Map<String, Handler> handlerMap, Map<String, Limit> limitMap) {
        this.configList = configList;
        this.pathsMap = pathsMap;
        this.handlerMap = handlerMap;
        this.limitMap = limitMap;
    }

    @Override
    public List<String> initConfig() {
        final Element configs = rootElement.getChild("configs");
        if (configs == null){
            return null;
        }
        final List<Element> config = configs.getChildren("config");
        config.forEach(item->{
            final String value = item.getValue().trim();
            if (!"".equals(value)){
                configList.add(value);
            }
        });
        return configList;

    }

    @Override
    public Map<String, Paths> initPaths() {
        final List<Element> pathsElementList = rootElement.getChildren("paths");
        if (pathsElementList == null){
            return null;
        }
        //遍历所有的Paths
        pathsElementList.forEach(pathsElement -> {
            final Paths path = this.parsePaths(pathsElement);
            if (pathsMap.get(path.getId()) != null){
                throw new XMLParseException("The 'id'("+ path.getId() +") attribute of the 'paths' tag is duplicated.");
            }
            pathsMap.put(path.getId(), path);
        });
        return pathsMap;
    }

    @Override
    public Map<String, Handler> initHandler() {
        final List<Element> handlerElementList = rootElement.getChildren("handler");

        handlerElementList.forEach(handlerElement -> {
            try {
                final Handler handler = assemble(handlerElement, Handler.class);
                if (this.handlerMap.get(handler.getId()) != null){
                    throw new XMLParseException("The 'id' or 'name' ("+ handler.getId() +") attribute of the 'handler' tag is duplicated.");
                }
                this.handlerMap.put(handler.getId(), handler);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return this.handlerMap;
    }

    @Override
    public Map<String, Limit> initLimit() {
        final List<Element> limitElementList = rootElement.getChildren("limit");

        limitElementList.forEach(limitElement -> {
            try {
                final Limit limit = assemble(limitElement, Limit.class);
                if (this.limitMap.get(limit.getId()) != null){
                    throw new XMLParseException("The 'id' or 'name' ("+ limit.getId() +") attribute of the 'handler' tag is duplicated.");
                }
                this.limitMap.put(limit.getId(), limit);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return this.limitMap;
    }

    public <T> T assemble(Element element, Class<? extends T> t) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final String clazz = this.getClazz(element);
        //初始化Id
        final String id = this.getId(element, clazz);

        //装配id 和class标签
        final Constructor<?> constructor = t.getConstructor(String.class);
        final Object o = constructor.newInstance(id);

        final Method setClazz = t.getMethod("setClazz", String.class);
        setClazz.invoke(o, clazz);


        //装配scope标签
        final Element scopeElement = element.getChild("scope");
        if (scopeElement != null){
            final Method setScope = t.getMethod("setScope", String.class);
            setScope.invoke(o, scopeElement.getValue());
        }

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

                final Method setPathId = t.getMethod("setPathId", String.class);
                setPathId.invoke(o, pathIdAttribute.getValue().trim());
            }else {
                //装配Path
                final Paths paths = parsePathNoId(pathsElement);

                final Method setPaths = t.getMethod("setPaths", Paths.class);
                setPaths.invoke(o, paths);

                //查询是否注册Paths
                if (paths.getId() != null){
                    final Method setPathId = t.getMethod("setPathId", String.class);
                    setPathId.invoke(o, paths.getId());
                    this.pathsMap.put(paths.getId(), paths);
                }
            }
        }
        return (T) o;
    }

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

    private String getClazz(Element element){
        //检查&初始化 必要的标签
        final Element clazzElement = element.getChild("class");
        if (clazzElement == null || "".equals(clazzElement.getValue().trim())){
            throw new XMLParseException("The 'paths' tag requires an 'class' tag.");
        }
        return clazzElement.getValue().trim();
    }

    private Paths parsePaths(Element pathsElement){
        final Paths paths = parsePathNoId(pathsElement);
        if (paths.getId() == null){
            throw new XMLParseException("The 'paths' tag requires an 'id' attribute.");
        }
        return paths;
    }

    private Paths parsePathNoId(Element pathsElement){
        final Attribute idAttribute = pathsElement.getAttribute("id");
        String id = null;
        if (idAttribute != null){
            id = pathsElement.getAttribute("id").getValue();
        }

        final List<Element> pathElementList = pathsElement.getChildren("path");
        List<String> pathList = new ArrayList<>();
        pathElementList.forEach(path -> pathList.add(path.getValue().trim()));
        return new Paths(id, pathList);
    }
}
