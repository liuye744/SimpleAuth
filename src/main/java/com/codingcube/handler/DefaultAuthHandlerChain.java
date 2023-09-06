package com.codingcube.handler;

import org.springframework.stereotype.Component;

/**
 * @author CodingCube<br>
 * Default Authentication Handler Chain Class*
 */
public final class DefaultAuthHandlerChain extends AutoAuthHandlerChain {
    @Override
    public void addChain() {
        //The default is empty.
    }
}
