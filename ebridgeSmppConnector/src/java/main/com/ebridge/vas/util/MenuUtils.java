//package com.ebridge.vas.util;
//
//import com.ebridge.vas.dto.FilteredNode;
//import com.ebridge.vas.dto.MenuItemDTO;
//import com.ebridge.vas.dto.Node;
//import com.ebridge.vas.model.TreeNode;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import java.util.List;
//
//import static com.ebridge.vas.util.StringUtils.fromFile;
//
///**
// * @author david@tekeshe.com
// */
//public class MenuUtils {
//
//    public static List<MenuItemDTO> menuList( String menuConfigFilename) {
//
//        return new Gson()
//                    .fromJson(  fromFile(menuConfigFilename),
//                                new TypeToken<List<MenuItemDTO>>(){}.getType() );
//    }
//
//    public static TreeNode<MenuItemDTO> root( List<MenuItemDTO> list ) throws NodeTypeNotFoundException {
//
//        for (MenuItemDTO item : list ) {
//
//            if ( item.getParent().isEmpty() ) {
//
//                return new TreeNode<>(item);
//            }
//        }
//
//        throw new NodeTypeNotFoundException("Root node not founder.");
//    }
//
//    public static void addChildren(TreeNode<MenuItemDTO> parent, List<MenuItemDTO> list) {
//
//    }
//
//    public static Node nodeType (String unqualifiedNode, String menuItemType) throws NodeTypeNotFoundException {
//
//        if ( "filtered-node".equals( menuItemType ) ) {
//            return new FilteredNode( unqualifiedNode );
//        }
//
//        throw new NodeTypeNotFoundException( "Node id: " + unqualifiedNode + " is of undefined type.");
//    }
//}
