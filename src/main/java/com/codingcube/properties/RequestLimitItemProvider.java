package com.codingcube.properties;


import com.codingcube.domain.RequestLimitItem;

import java.util.List;
/**
 * @author CodingCube<br>*
 * Provider of Dynamic Limit Data*
 */
public interface RequestLimitItemProvider {
    List<RequestLimitItem> getRequestLimitItem();
}
