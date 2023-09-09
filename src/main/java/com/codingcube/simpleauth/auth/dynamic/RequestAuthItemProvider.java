package com.codingcube.simpleauth.auth.dynamic;

import java.util.List;

/**
 * @author CodingCube<br>*
 * Provider of Dynamic Permission Control Data*
 */
public interface RequestAuthItemProvider {
    List<RequestAuthItem> getRequestAuthItem();
}
