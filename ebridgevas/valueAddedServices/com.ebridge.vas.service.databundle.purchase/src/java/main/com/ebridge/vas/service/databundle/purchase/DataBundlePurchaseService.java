package com.ebridge.vas.service.databundle.purchase;

import com.ebridge.commons.dto.MenuItemDTO;
import com.ebridge.commons.dto.Request;
import com.ebridge.commons.dto.Response;
import com.ebridge.commons.dto.SessionState;
import com.ebridge.vas.service.ValueAddedService;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;

import java.util.Set;

/**
 * @author david@tekeshe.com
 */
@Component(immediate = true)
@Provides
public class DataBundlePurchaseService implements ValueAddedService {

    @ServiceProperty(name = ValueAddedService.NAME_PROPERTY)
    String name = "DataBundlePurchaseService";

    @Override
    public Response process( Request request, Set<MenuItemDTO> items) {

        return Response.clone(request, "You bought at 76MB databundle for $5.00", "", SessionState.TERMINATE);
    }
}
