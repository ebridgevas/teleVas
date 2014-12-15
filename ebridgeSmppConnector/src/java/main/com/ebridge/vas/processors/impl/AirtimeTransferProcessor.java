package com.ebridge.vas.processors.impl;

import com.ebridge.vas.Request;
import com.ebridge.vas.Response;
import com.ebridge.vas.dto.MenuItemDTO;
import com.ebridge.vas.dto.PduDto;
import com.ebridge.vas.model.MTSM;
import com.ebridge.vas.model.TreeNode;
import com.ebridge.vas.processors.ServiceCommandProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author david@tekeshe.com
 */
public class AirtimeTransferProcessor implements ServiceCommandProcessor {

    @Override
    public Response process(Request request, TreeNode<MenuItemDTO> node) {

        System.out.println("service-command=airtime-transfer&mobile-number=" + request.getSourceId());
        return null;
    }
}
