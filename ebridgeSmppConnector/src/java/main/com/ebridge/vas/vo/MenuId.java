package com.ebridge.vas.vo;

/**
 * @author david@tekeshe.com
 */
public final class MenuId implements Comparable<MenuId> {

    private final String index;

    public MenuId(String index) {
        this.index = index;
    }

    public String index() {
        return index;
    }

    @Override
    public int compareTo(MenuId o) {
        return o.index.compareTo( this.index );
    }
}
