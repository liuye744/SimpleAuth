package com.codingcube.simpleauth.autoconfig;

import com.codingcube.simpleauth.autoconfig.domain.Handler;
import com.codingcube.simpleauth.autoconfig.domain.Limit;
import com.codingcube.simpleauth.autoconfig.domain.Paths;

import java.util.List;
import java.util.Map;

public interface Config2SimpleAuthObject {
    List<String> initConfig();
    Map<String, Paths> initPaths();
    Map<String, Handler> initHandler();
    Map<String, Limit> initLimit();
}
