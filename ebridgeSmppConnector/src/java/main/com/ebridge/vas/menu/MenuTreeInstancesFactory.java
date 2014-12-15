package com.ebridge.vas.menu;

import com.ebridge.vas.dto.ConfigDTO;
import com.ebridge.vas.dto.MenuItemDTO;
import com.ebridge.vas.kernel.EBridgeVasKernel;
import com.ebridge.vas.model.TreeNode;
import com.ebridge.vas.model.UserSession;
import com.ebridge.vas.processors.ServiceCommandProcessor;
import com.ebridge.vas.util.JsonConfigurationService;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author david@tekeshe.com
 */
public class MenuTreeInstancesFactory {

    private final static EBridgeVasKernel kernel;
    private final static ConfigDTO configDTO;

    static {
        kernel = new EBridgeVasKernel();
        configDTO = new JsonConfigurationService().config(
                "/Users/david/workspace/telecel/ebridgeSmppConnector/resources/conf/vas.conf");
    }

    public void instantiate(TreeNode<MenuItemDTO> root, Map<String, UserSession> userSessions) {

        instantiate( root.data, userSessions );
        instantiate(root.children, userSessions );
    }

    protected void instantiate( List<TreeNode<MenuItemDTO>> nodes, Map<String, UserSession> userSessions ) {

        List<TreeNode<MenuItemDTO>> children = new ArrayList<>();

        for ( TreeNode<MenuItemDTO> node : nodes) {
            instantiate( node.data, userSessions );
            if ( ! node.isLeaf() ) {
                children.addAll(node.children);
            }
        }

        if ( children.size() > 0 ) {
            instantiate(children, userSessions);
        }
    }

    protected void instantiate(MenuItemDTO item, Map<String, UserSession> userSessions) {

        try {

            Constructor constructor
                    = Class.forName(item.getItemProcessorName())
                                .getConstructor(
                                        EBridgeVasKernel.class,
                                        ConfigDTO.class,
                                        Map.class );

            item.setServiceCommandProcessor((ServiceCommandProcessor)
                    constructor.newInstance(kernel, configDTO, userSessions ) );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
