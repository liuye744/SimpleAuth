package com.codingcube.properties;

import com.codingcube.domain.RequestAuthItem;

import java.util.List;

public interface RequestAuthItemProvider {
    List<RequestAuthItem> getRequestAuthItem();
}
