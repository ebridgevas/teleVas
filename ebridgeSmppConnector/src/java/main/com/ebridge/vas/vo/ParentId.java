package com.ebridge.vas.vo;

/**
 * @author david@tekeshe.com
 */
public final class ParentId implements Comparable<ParentId> {

    private final String index;

    public ParentId(String index) {
        this.index = index;
    }

    public String index() {
        return index;
    }

    @Override
    public int compareTo(ParentId o) {
        return o.index.compareTo( this.index );
    }
}
