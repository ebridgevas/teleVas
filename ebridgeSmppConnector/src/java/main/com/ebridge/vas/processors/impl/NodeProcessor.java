package com.ebridge.vas.processors.impl;

import com.ebridge.vas.dto.ConfigDTO;
import com.ebridge.vas.kernel.EBridgeVasKernel;
import com.ebridge.vas.model.UserSession;
import com.ebridge.vas.processors.ServiceCommandProcessor;
import com.ebridge.vas.vo.MenuItemIndex;

import java.util.Map;

/**
 * @author david@tekeshe.com
 */
public abstract class NodeProcessor implements ServiceCommandProcessor {

    private EBridgeVasKernel ebridgeVasKernel;
    private ConfigDTO configDTO;
    private Map<MenuItemIndex, ServiceCommandProcessor> leafInstanceMap;
    protected Map<String, UserSession> userSessions;

    public NodeProcessor( EBridgeVasKernel ebridgeVasKernel,
                          ConfigDTO configDTO,
                          Map<String, UserSession> userSessions ) {

        if ( ebridgeVasKernel != null ) {
            this.ebridgeVasKernel = ebridgeVasKernel;
        }

        if ( configDTO != null ) {
            this.configDTO = configDTO;
        }

        if ( userSessions != null ) {
            this.userSessions = userSessions;
        }
    }
}
