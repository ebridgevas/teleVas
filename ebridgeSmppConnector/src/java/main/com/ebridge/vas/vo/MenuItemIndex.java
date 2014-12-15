package com.ebridge.vas.vo;

/**
 * @author david@tekeshe.com
 */
public final class MenuItemIndex implements Comparable<MenuItemIndex> {

    private final String index;

    public MenuItemIndex(String index) {
        this.index = index;
    }

    public String index() {
        return index;
    }

    @Override
    public int compareTo(MenuItemIndex o) {
        return o.index.compareTo( this.index );
    }
}
