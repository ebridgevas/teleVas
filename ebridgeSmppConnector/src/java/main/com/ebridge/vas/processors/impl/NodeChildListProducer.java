package com.ebridge.vas.processors.impl;

import com.ebridge.vas.Request;
import com.ebridge.vas.Response;
import com.ebridge.vas.dto.ConfigDTO;
import com.ebridge.vas.dto.MenuItemDTO;
import com.ebridge.vas.kernel.EBridgeVasKernel;
import com.ebridge.vas.model.TreeNode;
import com.ebridge.vas.model.UserSession;
import com.ebridge.vas.util.NodeTypeNotFoundException;

import java.util.Map;
import java.util.Set;

/**
 * Creates a menu listing from a set of its child nodes
 *
 * @author david@tekeshe.com
 */
public class NodeChildListProducer extends NodeProcessor {

    private NodeChildSelector nodeChildSelector;

    public NodeChildListProducer(
            EBridgeVasKernel ebridgeVasKernel,
            ConfigDTO configDTO,
            Map<String, UserSession> userSessions) {

        super(ebridgeVasKernel, configDTO, userSessions );

        nodeChildSelector
                = new NodeChildSelector(
                        ebridgeVasKernel,
                        configDTO,
                        userSessions );
    }

    @Override
    public Response process( Request request, TreeNode<MenuItemDTO> child  )
            throws NodeTypeNotFoundException {

        Response result = Response.clone(request, menu(child.childSet()), "", false);

        userSessions.put(
                request.getSourceId(),
                new UserSession(request.getSourceId(), child.data.getMenuId()));

        return result;
    }

    /**
     * Creates a set of menu items for a node.
     * A menu is a defined as a parent and its children.
     *
     * @return set of menu items
     */
    public String menu( Set<MenuItemDTO> items ) {

        String menu = null;

        for ( MenuItemDTO item : items ) {

            if ( menu == null ) menu = item.getMenuNarration() + "\n";
            else menu += (item.getItemIndex() + ". " + item.getMenuNarration().trim() + "\n");
        }

        return menu;
    }
}
