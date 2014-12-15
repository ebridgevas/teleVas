package com.ebridge.vas.menu;

import com.ebridge.vas.Request;
import com.ebridge.vas.Response;
import com.ebridge.vas.dto.ConfigDTO;
import com.ebridge.vas.dto.MenuItemDTO;
import com.ebridge.vas.kernel.EBridgeVasKernel;
import com.ebridge.vas.model.TreeNode;
import com.ebridge.vas.model.UserSession;
import com.ebridge.vas.parsers.halys.util.PayloadType;
import com.ebridge.vas.processors.impl.NodeChildSelector;
import com.ebridge.vas.util.JsonConfigurationService;
import com.ebridge.vas.util.NodeTypeNotFoundException;
import com.ebridge.vas.vo.*;
import com.google.gson.Gson;

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
* @author david@tekeshe.com
*/
public class JsonMenuDataWrapper implements MenuDataWrapper<MenuItemDTO> {

    private TreeNode<MenuItemDTO> menuTree;

    private String menuConfigFilename;
    private String shortCode;

    private Map<String, Map<String, MenuItemDTO>> map;

    private Map<String, UserSession> userSessions;

    private NodeChildSelector nodeChildSelector;

    public JsonMenuDataWrapper( String menuConfigFilename, String shortCode ) throws NodeTypeNotFoundException {

        this.menuConfigFilename = menuConfigFilename;
        this.shortCode = shortCode;

        try {
            map = new Gson().fromJson(
                    new BufferedReader( new FileReader( menuConfigFilename ) ),
                    new TypeToken<Map<String, Map<String, MenuItemDTO>>>() {}.getType());

            normalise( map );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        userSessions = new HashMap<>();

        menuTree = load();
        new MenuTreeInstancesFactory().instantiate( menuTree, userSessions );
        nodeChildSelector = new NodeChildSelector(null, null, userSessions);
    }

    public Response process( Request request ) {

        // get node id
        String nodeId = nodeId( request );

        // find node from the menu tree
        TreeNode<MenuItemDTO> node = menuTree.findTreeNode(new MenuItemDTO( nodeId ) );

        try {
            if ( request.getPayloadType() == PayloadType.SESSION_START ) {

                // execute this node.
                return node.data.getServiceCommandProcessor().process(request, node);
            } else {

                // if ussd answer. node is previous one.
                // use payload to pick the selected node amount the children
                return nodeChildSelector.process( request, node );
            }
        } catch (NodeTypeNotFoundException e) {
            e.printStackTrace();
            return Response.clone( request, e.getMessage(), "", true );
        }
    }

    @Override
    public TreeNode<MenuItemDTO> load( ) {

        TreeNode<MenuItemDTO> tree = new TreeNode<>(
                new MenuItemDTO( shortCode, null, "0", MenuItemType.FILTERED_NODE, "Telecel", null,
                        "com.ebridge.vas.processors.impl.NodeChildSelector"));

        Map<String, Map<String, MenuItemDTO>> childMap = new HashMap<>();
        childMap.put(tree.data.getMenuId(), map.get( tree.data.getMenuId() ) );

        while ( childMap.size() > 0 ) {
            childMap = addChildren ( tree, childMap );
        }

        return tree;
    }

    protected Map<String, Map<String, MenuItemDTO>>
                                addChildren( TreeNode<MenuItemDTO> tree,
                                             Map<String, Map<String, MenuItemDTO>> childMap ) {

        Map<String, Map<String, MenuItemDTO>> grandChildMap = new HashMap<>();

        for ( String parentId : childMap.keySet() ) {

            // get parent
            TreeNode<MenuItemDTO> node = tree.findTreeNode(new MenuItemDTO(parentId));

            // System.out.print(parentId + " :: ");

            Map<String, MenuItemDTO> grandChildren = new HashMap<>();

            for (String childId : childMap.get( parentId ).keySet() ) {

                // Add child to node
//                System.out.print(childMap.get(parentId).get( childId).getMenuId() + ", ");
                node.addChild( childMap.get(parentId).get( childId) );

                if ( map.get(childId) != null ) {
                    grandChildMap.put(childId, map.get(childId));
                }
            }
        }
        return grandChildMap;
    }

    protected void normalise( Map<String, Map<String, MenuItemDTO>> map ) {

        for ( String parentId : map.keySet() ) {

            if ( parentId.split(",").length > 1 ) {

                for ( String newParentId : parentId.split(",") ) {
                    map.put(newParentId, map.get( parentId ) );
                }
            }
        }
    }

    public String debugString(TreeNode<MenuItemDTO> node) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(node.data.getMenuId() + " - " + node.data.getMenuNarration() + " :: ");

        debugString(node.children, stringBuilder);

        return stringBuilder.toString();
    }

    protected void debugString( List<TreeNode<MenuItemDTO>> children, StringBuilder stringBuilder ) {

        List<TreeNode<MenuItemDTO>> grandChildren = new ArrayList<>();
        for ( TreeNode<MenuItemDTO> child : children ) {
            stringBuilder.append( child.data.getMenuId() + " - " + child.data.getMenuNarration() + ", " );

            List<TreeNode<MenuItemDTO>> list = child.children;
            if ( list.size() > 0 ) {
                grandChildren.addAll(child.children);
            }
        }
        stringBuilder.append("\n");
        if ( grandChildren.size() > 0 ) {
            debugString(grandChildren, stringBuilder);
        }
    }

    protected String nodeId ( Request request) {

        if (request.getPayloadType() == PayloadType.SESSION_START ) {

            return request.getDestinationId();
        } else {
            String nodeId = null;
            try {
                nodeId = userSessions.get(request.getSourceId()).getPreviousMenuUuid();
            } catch (Exception e) {
            }
            // if no user session then default to root node.
            return nodeId != null ? nodeId : request.getDestinationId();
        }
    }
}
