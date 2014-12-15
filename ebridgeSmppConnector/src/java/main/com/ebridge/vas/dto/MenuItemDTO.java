package com.ebridge.vas.dto;

import com.ebridge.vas.processors.ServiceCommandProcessor;
import com.ebridge.vas.vo.*;

/**
 * A wrapper class for this json string:
 *   { "menuId" : "1008",
 *     "parent" : "144",
 *     "itemIndex" : "1",
 *     "menuItemType" : "1",
 *     "menuNarration" : "$1.00 20 MB Data Bundle",
 *     "itemProcessor" : "DataBundlePurchaseProcessor"}
 *
 *     @author david@tekeshe.com
 */
public class MenuItemDTO implements Comparable<MenuItemDTO>{

    private String menuId;
    private String parentId;
    private String itemIndex;
    private MenuItemType menuItemType;
    private String menuNarration;
    private String itemFilter;
    private String itemProcessorName;
    private ServiceCommandProcessor serviceCommandProcessor;

    public MenuItemDTO(){

    }

    public MenuItemDTO(String menuId){
        this.menuId = menuId;
    }

    public MenuItemDTO(
            String menuId,
            String parentId,
            String itemIndex,
            MenuItemType menuItemType,
            String menuNarration,
            String itemFilter,
            String itemProcessorName) {
        this.menuId = menuId;
        this.parentId = parentId;
        this.itemIndex = itemIndex;
        this.menuItemType = menuItemType;
        this.menuNarration = menuNarration;
        this.itemFilter = itemFilter;
        this.itemProcessorName = itemProcessorName;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(String itemIndex) {
        this.itemIndex = itemIndex;
    }

    public MenuItemType getMenuItemType() {
        return menuItemType;
    }

    public void setMenuItemType(MenuItemType menuItemType) {
        this.menuItemType = menuItemType;
    }

    public String getMenuNarration() {
        return menuNarration;
    }

    public void setMenuNarration(String menuNarration) {
        this.menuNarration = menuNarration;
    }

    public String getItemFilter() {
        return itemFilter;
    }

    public void setItemFilter(String itemFilter) {
        this.itemFilter = itemFilter;
    }

    public String getItemProcessorName() {
        return itemProcessorName;
    }

    public void setItemProcessorName(String itemProcessorName) {
        this.itemProcessorName = itemProcessorName;
    }

    public ServiceCommandProcessor getServiceCommandProcessor() {
        return serviceCommandProcessor;
    }

    public void setServiceCommandProcessor(ServiceCommandProcessor serviceCommandProcessor) {
        this.serviceCommandProcessor = serviceCommandProcessor;
    }

    @Override
    public int compareTo(MenuItemDTO o) {
        return this.getMenuId().compareTo(o.getMenuId());
    }
}
