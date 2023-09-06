package com.codingcube.properties;

import com.codingcube.domain.RequestAuthItem;
import java.util.List;

/**
 * @author CodingCube<br>*
 * Provider of Dynamic Permission Control Data*
 */
public interface RequestAuthItemProvider {
    List<RequestAuthItem> getRequestAuthItem();
}
