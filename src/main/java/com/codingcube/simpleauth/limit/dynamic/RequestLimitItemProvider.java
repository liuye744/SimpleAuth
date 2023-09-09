package com.codingcube.simpleauth.limit.dynamic;


import java.util.List;
/**
 * @author CodingCube<br>*
 * Provider of Dynamic Limit Data*
 */
public interface RequestLimitItemProvider {
    List<RequestLimitItem> getRequestLimitItem();
}
