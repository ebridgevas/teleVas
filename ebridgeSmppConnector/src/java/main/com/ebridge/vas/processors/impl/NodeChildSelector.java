package com.ebridge.vas.processors.impl;

import com.ebridge.vas.Request;
import com.ebridge.vas.Response;
import com.ebridge.vas.dto.ConfigDTO;
import com.ebridge.vas.dto.MenuItemDTO;
import com.ebridge.vas.kernel.EBridgeVasKernel;
import com.ebridge.vas.model.ServiceCommand;
import com.ebridge.vas.model.TreeNode;
import com.ebridge.vas.model.UserSession;
import com.ebridge.vas.processors.ServiceCommandProcessor;
import com.ebridge.vas.util.NodeTypeNotFoundException;

import java.util.List;
import java.util.Map;

/**
 * @author david@tekeshe.com
 */
public class NodeChildSelector extends NodeProcessor {

    public NodeChildSelector(
            EBridgeVasKernel ebridgeVasKernel,
            ConfigDTO configDTO,
            Map<String, UserSession> userSessions) {

        super(ebridgeVasKernel, configDTO, userSessions );
    }

    public Response process( Request request, TreeNode<MenuItemDTO> node )
                throws NodeTypeNotFoundException {

        for ( TreeNode<MenuItemDTO> childNode : node.children ) {

            if ( request.getPayload().trim().toLowerCase().startsWith(
                        childNode.data.getItemFilter().trim().toLowerCase())) {
                return childNode.data.getServiceCommandProcessor().process(request, childNode );
            }
        }

        throw new NodeTypeNotFoundException("Node for filter : " + request.getPayload() +
                       " for short code : " + request.getDestinationId() + " not found.");
    }
}
