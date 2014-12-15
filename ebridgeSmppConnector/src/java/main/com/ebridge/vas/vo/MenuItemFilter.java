package com.ebridge.vas.vo;

/**
 * @author david@tekeshe.com
 */
public final class MenuItemFilter implements Comparable<MenuItemFilter> {

    private final String index;

    public MenuItemFilter(String index) {
        this.index = index;
    }

    public String index() {
        return index;
    }

    @Override
    public int compareTo(MenuItemFilter o) {
        return o.index.compareTo( this.index );
    }
}
