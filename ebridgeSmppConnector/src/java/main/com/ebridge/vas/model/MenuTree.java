//package com.ebridge.vas.model;
//
//import static com.ebridge.vas.util.StringUtils.fromFile;
////import static com.ebridge.vas.util.MenuUtils.nodeType;
//
//import com.ebridge.vas.dto.*;
//import com.ebridge.vas.kernel.EBridgeVasKernel;
//import com.ebridge.vas.processors.ServiceCommandProcessor;
//import com.ebridge.vas.util.MenuItemNotFoundException;
//import com.ebridge.vas.util.NodeTypeNotFoundException;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import java.lang.reflect.Constructor;
//import java.util.*;
//
///**
//* @author david@tekeshe.com
//*
//* Instantiate menu node from file: menu.config.
//*
//* TODO improve this data structure
//*/
//public class MenuTree {
//
//    private List<MenuItemDTO> menuItemList;
//    private Map<String, MenuItemDTO> itemMap;
//    private Map<String, Set<MenuItemDTO>> childMap;
//    private Map<String, ServiceCommandProcessor> leafInstanceMap;
//    private Map<String, ServiceCommandProcessor> nodeInstanceMap;
//
//    public MenuTree(  String configFilename, ConfigDTO vasConfigDTO ) {
//
//        /* Load menu model into memory from json file. */
//        menuItemList
//                = new Gson()
//                    .fromJson( fromFile( configFilename ),
//                            new TypeToken<List<MenuItemDTO>>() {
//                            }.getType()
//                    );
//        itemMap = itemMap( menuItemList );
//        childMap = childMap( menuItemList );
//        leafInstanceMap( menuItemList, vasConfigDTO );
//        nodeInstanceMap( menuItemList, vasConfigDTO );
//    }
//
//    public MenuItemDTO menuItem( String nodeId, String payload ) throws MenuItemNotFoundException {
//
//        return itemMap.get( nodeId );
//    }
//
//    public MenuItemDTO children(String unqualifiedNode, String payload ) throws NodeTypeNotFoundException,
//                                                                                MenuItemNotFoundException {
//
//        String menuItemType = itemMap.get(unqualifiedNode).getMenuItemType();
//
//        if ( "unfiltered-node".equals( menuItemType ) ) {
//            return children(new UnfilteredNode(unqualifiedNode), payload);
//        }
//
//        if ( "filtered-node".equals( menuItemType ) ) {
//            return children(new FilteredNode(unqualifiedNode), payload);
//        }
//
//        throw new NodeTypeNotFoundException(
//                "Node type : " + menuItemType + ", for node : " + unqualifiedNode + " doesn't exist");
//    }
//
//    public MenuItemDTO children(UnfilteredNode node, String itemIndex ) throws MenuItemNotFoundException {
//
//        if ( "0".equals(itemIndex) ) throw new MenuItemNotFoundException("Invalid selection.");
//
//        return childrenFor(node.node()).get( itemIndex );
//    }
//
//    public MenuItemDTO children(FilteredNode node, String filter ) throws MenuItemNotFoundException {
//
//        String menu = null;
//
//        for ( MenuItemDTO item : childMap.get(node.node()) ) {
//
//            if (item.getItemFilter().equalsIgnoreCase( filter ) )
//                return item;
//        }
//
//        throw new MenuItemNotFoundException( "Filter " + filter + " not found under node : " + node.node() );
//    }
//
//    /**
//     * Creates a set of menu items for a node.
//     * A menu is a defined as a parent and its children.
//     *
//     * @return set of menu items
//     */
//    public String menu( String node ) {
//        String menu = null;
//
//        for ( MenuItemDTO item : childMap.get( node ) ) {
//
//            if ( menu == null ) menu = item.getMenuNarration() + "\n";
//            else menu += (item.getItemIndex() + ". " + item.getMenuNarration().trim() + "\n");
//        }
//
//        return menu;
//    }
//
//    /**
//     * Creates a item map from menu list.
//     *
//     * @param list
//     * @return  item map
//     */
//    protected Map<String, MenuItemDTO> itemMap(List<MenuItemDTO> list) {
//
//        Map<String, MenuItemDTO> map = new HashMap<>();
//
//        for ( MenuItemDTO item : list ) {
//            map.put( item.getMenuId(), item );
//        }
//
//        return map;
//    }
//
//    /**
//     * A helper method for arranging child info for use by getChildrenFor service method.
//     * @param menuList
//     * @return
//     */
//    protected Map<String, Set<MenuItemDTO>> childMap(List<MenuItemDTO> menuList) {
//
//        Map<String, Set<MenuItemDTO>> parents = new HashMap<>();
//
//        for ( MenuItemDTO item : menuList ) {
//
//            if ( item.getParent().isEmpty() ) continue;
//
//            for ( String parentId : item.getParent().split(",") ) {
//                add(parents, item,  parentId );
//            }
//        }
//
//        return parents;
//    }
//
//    protected void add( Map<String, Set<MenuItemDTO>> parents,
//                        MenuItemDTO item,
//                        String parentId ) {
//
//        Set<MenuItemDTO> children = parents.get( parentId );
//
//        if (children == null) {
//            children = new TreeSet<>();
//            parents.put(parentId, children);
//        }
//
//        children.add( item );
//    }
//
//    /**
//     * A service method that returns a list of children belonging to a give parent.
//     */
//    public Map<String, MenuItemDTO> childrenFor( String node ) {
//
//        Map<String, MenuItemDTO> items = new HashMap<String, MenuItemDTO>();
//
//        for ( MenuItemDTO item : childMap.get( node ) ) {
//            items.put(item.getItemIndex(), item);
//        }
//
//        return items;
//    }
//
//    public ServiceCommandProcessor getLeafInstanceFor(String leafName) {
//        return leafInstanceMap.get( leafName );
//    }
//
//    protected void leafInstanceMap( List<MenuItemDTO> menuListing,
//                                    ConfigDTO vasConfigDTO) {
//
//        leafInstanceMap = new HashMap<>();
//        for (MenuItemDTO item : menuListing) {
//
//            if ("active-leaf".equalsIgnoreCase(item.getMenuItemType())) {
//                try {
//
//                    if ( leafInstanceMap.get( item.getItemProcessor() ) != null )
//                        continue;
//
//                    Constructor constructor
//                            = Class.forName(item.getItemProcessor())
//                                    .getConstructor(EBridgeVasKernel.class,
//                                                    ConfigDTO.class);
//                    leafInstanceMap.put( item.getItemProcessor(),
//                                         (ServiceCommandProcessor)constructor.newInstance(
//                                                 new EBridgeVasKernel(), vasConfigDTO ) );
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    protected void nodeInstanceMap( List<MenuItemDTO> menuListing, ConfigDTO vasConfigDTO) {
//
//        nodeInstanceMap = new HashMap<>();
//
//        for ( MenuItemDTO item : menuListing ) {
//
//            if ( "unfiltered-node".equalsIgnoreCase( item.getMenuItemType() ) ) {
//
//                try {
//
//                    if ( leafInstanceMap.get( item.getItemProcessor() ) != null )
//                        continue;
//
//                    Constructor constructor
//                            = Class.forName(item.getItemProcessor())
//                            .getConstructor( EBridgeVasKernel.class,
//                                             ConfigDTO.class,
//                                             Map.class );
//
//                    leafInstanceMap.put( item.getItemProcessor(),
//                            (ServiceCommandProcessor)constructor.newInstance(
//                                    new EBridgeVasKernel(), vasConfigDTO ) );
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public static void main(String[] args) throws NodeTypeNotFoundException {
//
////        MenuTree menuTree = new MenuTree("/Users/david/workspace/telecel/ebridgeSmppConnector/resources/conf/menu.conf",
////                   null);
//////        String menu = menuTree.menu("33073");
//////        System.out.println( menu );
//////
////        try {
////            System.out.println( menuTree.menuItem("33073", "bundle").getMenuNarration() );
////        } catch (MenuItemNotFoundException e) {
////            System.err.println( e.getMessage() );
////        }
//    }
//}
