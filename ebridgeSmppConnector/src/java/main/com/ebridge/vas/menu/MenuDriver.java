package com.ebridge.vas.menu;

import com.ebridge.vas.Request;
import com.ebridge.vas.Response;
import com.ebridge.vas.util.NodeTypeNotFoundException;

/**
 * @author david@tekeshe.com
 */
public interface MenuDriver {

    /**
     * Create a response with menu as payload based on request information.
     *
     * @param request
     * @return response
     */
    public Response menu(Request request);
}
