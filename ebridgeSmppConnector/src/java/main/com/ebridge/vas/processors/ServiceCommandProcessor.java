package com.ebridge.vas.processors;

/**
 * @author david@tekeshe.com
 */

import com.ebridge.vas.Request;
import com.ebridge.vas.Response;
import com.ebridge.vas.dto.MenuItemDTO;
import com.ebridge.vas.model.TreeNode;
import com.ebridge.vas.util.NodeTypeNotFoundException;

public interface ServiceCommandProcessor {

    Response process( Request request, TreeNode<MenuItemDTO> node ) throws NodeTypeNotFoundException;
}
