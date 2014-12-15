package com.ebridge.vas.processors.impl;

import com.ebridge.vas.Request;
import com.ebridge.vas.Response;
import com.ebridge.vas.dto.ConfigDTO;
import com.ebridge.vas.dto.MenuItemDTO;
import com.ebridge.vas.kernel.EBridgeVasKernel;
import com.ebridge.vas.model.TreeNode;
import com.ebridge.vas.model.UserSession;

import java.util.Map;

/**
 * @author david@tekeshe.com
 */
public class NullProcessor extends NodeProcessor {

    public NullProcessor(
            EBridgeVasKernel ebridgeVasKernel,
            ConfigDTO configDTO,
            Map<String, UserSession> userSessions) {

        super( ebridgeVasKernel, configDTO, userSessions );
    }

    @Override
    public Response process( Request request, TreeNode<MenuItemDTO> node ) {

        return Response.clone(request, "Invalid selection", "", Boolean.FALSE );
    }
}
