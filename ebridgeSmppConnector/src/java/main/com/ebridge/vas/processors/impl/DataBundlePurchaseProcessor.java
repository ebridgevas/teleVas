package com.ebridge.vas.processors.impl;

import com.ebridge.vas.Request;
import com.ebridge.vas.Response;
import com.ebridge.vas.dto.*;
import com.ebridge.vas.kernel.EBridgeVasKernel;
import com.ebridge.vas.model.TreeNode;
import com.ebridge.vas.model.UserSession;
import com.ebridge.vas.processors.ServiceCommandProcessor;
import com.ebridge.vas.model.MTSM;
import com.ebridge.vas.util.TransactionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ebridge.vas.util.billing.Util.dataBundleResponse;

/**
 * @author david@tekeshe.com
 */
public class DataBundlePurchaseProcessor implements ServiceCommandProcessor {

    private EBridgeVasKernel ebridgeVasKernel;
    private ConfigDTO configDTO;
    private Map<String, UserSession> userSessions;

    public DataBundlePurchaseProcessor( EBridgeVasKernel ebridgeVasKernel,
                                        ConfigDTO configDTO,
                                        Map<String, UserSession> userSessions ) {
        this.ebridgeVasKernel = ebridgeVasKernel;
        this.configDTO = configDTO;
        this.userSessions = userSessions;
    }

    @Override
    public Response process( Request request, TreeNode<MenuItemDTO> node ) {

        Map<String, String> map = new HashMap<>();
        map.put("mobile-number", request.getSourceId());
        map.put("beneficiary-id", request.getBeneficiaryId());
        map.put("service-command", "data-bundle-purchase");
        map.put("data-bundle-id", request.getPayload());

        BalanceDTO[] dataBundlePurchaseResult = ebridgeVasKernel.process(map);

        map.put("service-command", "data-bundle-detail");
        map.put("data-bundle-id", "3");
        DataBundleDTO dataBundle = ebridgeVasKernel.process(map);

        try {

            /* result[0] is sender response. result[1] is beneficiary response if not topping up own phone. */
            String[] result = dataBundleResponse( dataBundlePurchaseResult, dataBundle );
            return Response.clone( request, result[0], result.length == 2 ? result[1] : null, Boolean.TRUE );
        } catch (TransactionException e) {
            e.printStackTrace();
            return Response.clone( request, "An error occurred, Please re-try", null, Boolean.TRUE );
        }
    }
}
