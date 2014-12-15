package com.ebridge.vas.dto;

/**
 * @author david@tekeshe.com
 */
public abstract class AbstractNode implements Node {

    private final String node;

    public AbstractNode(String node) {
        this.node = node;
    }

    @Override
    public String node() {
        return node;
    }
}
