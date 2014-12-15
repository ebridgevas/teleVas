//package com.ebridge.vas.menu.impl;
//
///**
//* @author david@tekeshe.com
//*/
//
//import com.ebridge.vas.Request;
//import com.ebridge.vas.Response;
//import com.ebridge.vas.dto.ConfigDTO;
//import com.ebridge.vas.dto.MenuItemDTO;
//import com.ebridge.vas.dto.PduDto;
//import com.ebridge.vas.menu.MenuDriver;
//import com.ebridge.vas.model.MTSM;
//import com.ebridge.vas.model.MenuTree;
//import com.ebridge.vas.model.UserSession;
//import com.ebridge.vas.util.MenuItemNotFoundException;
//
//import java.util.*;
//
//public class JsonMenuDriver implements MenuDriver {
//
//    private ConfigDTO vasConfigDTO;
//
//    private MenuTree menuTree;
//
//    /* Maintains mobile user state */
//    private  Map<String, UserSession> userSessions;
//
//    public JsonMenuDriver( ConfigDTO vasConfigDTO,
//                           String menuConfigFilename,
//                           String shortCode ) {
//
//        menuTree = new MenuTree( menuConfigFilename, vasConfigDTO );
//        userSessions = new HashMap<>();
//    }
//
//    @Override
//    /**
//     * Create a response with menu as payload based on request information.
//     *
//     * @param request
//     * @return response
//     */
//    public Response menu( Request request )  {
//
//        try {
//
//            // If initial dial
//            if ( request.isSessionStart() ) {
//
//                MenuItemDTO menuItem = menuTree.menuItem( request.getDestinationId(), request.getPayload() );
//
//                // save user state
////                UserSession userSession = new UserSession( request.getSourceId() );
////                userSession.setPreviousMenuUuid( request.getDestinationId() );
////                userSessions.put(request.getSourceId(), userSession);
//
//                // response
//                return Response.clone(request, menuTree.menu( request.getDestinationId() ) , null, Boolean.FALSE );
//            }
//
//            /* Get the user session. */
//            UserSession userSession = userSessions.get(request.getSourceId());
//
//            /* Get previous parent Id */
//            String previousParentId = userSession.getPreviousMenuUuid();
//
//
//            MenuItemDTO selectedMenuItem = menuTree.menuItem(previousParentId, request.getPayload() );
//
//            /* Is this menu item a leaf. */
//            if ( isLeaf ( selectedMenuItem ) ) {
//                return menuTree.getLeafInstanceFor(selectedMenuItem.getItemProcessor()).process( request );
//            } else {  /* otherwise this is a node hence get its children. */
//
//                userSession.setPreviousMenuUuid( selectedMenuItem.getMenuId() );
//                userSessions.put(request.getSourceId(), userSession);
//
//                // response
//                return Response.clone(request, menuTree.menu( selectedMenuItem.getMenuId() ), null, Boolean.FALSE );
//            }
//
//        } catch (MenuItemNotFoundException e) {
//            e.printStackTrace();
//            return Response.clone(request, "An error occurred. Please re-try", null, Boolean.TRUE );
//        } /* catch (NodeTypeNotFoundException e) {
//            return Response.clone(request, "An error occurred. Please re-try", null, Boolean.TRUE );
//        }  */
//
////        return null;
//    }
//
////    private boolean hasChild(MenuItemDTO selectedMenuItem) {
////        return USSD_MENUS.childrenFor( selectedMenuItem.getMenuId() ).isEmpty();
////    }
//
//    protected Boolean isLeaf( MenuItemDTO selectedMenuItem ) {
//        return "active-leaf".equalsIgnoreCase(selectedMenuItem.getMenuItemType());
//    }
//
//    private List<MTSM> toMTSM(PduDto pdu, Collection<MenuItemDTO> children, Boolean isTerminating) {
//        List<MTSM> result = new ArrayList<MTSM>();
//
////        String flatChildren = "";
////        for (MenuItemDTO child : children) {
////            flatChildren += (child + "\n");
////        }
////        flatChildren = getUssdMessagePrefix(Boolean.TRUE, getSessionId(pdu.getShortMessage())) + flatChildren;
//
//        MTSM mtsm = new MTSM(pdu.getSourceId(), pdu.getDestinationId(), flat(children, pdu, isTerminating));
//        result.add( mtsm );
//        return result;
//    }
//
//    private String flat( Collection<MenuItemDTO> menuItems, PduDto pdu, Boolean isTerminating ) {
//
////        Set<String> sortedMenuItems = new TreeSet<String>();
////        for (MenuItemDTO menuItem : menuItems ) {
////            sortedMenuItems.add(menuItem.toString());
////        }
////
////        String str = "";
////        for (String menuItem : sortedMenuItems ) {
////            str += ( menuItem + "\n" );
////        }
////
////        //TODO Use proper data structures in order to avoid this!
////        str = str.replace("0. ","");
////        return getUssdMessagePrefix(isTerminating, getSessionId(pdu.getShortMessage())) + str;
//
//        return null;
//    }
//
////    Deprecated
////    protected Boolean isInitialDial(String userId ) {
////
////        return USER_SESSIONS.get( userId ) == null;
////    }
//
//   public static void main(String[] args) {
//
//   }
//}