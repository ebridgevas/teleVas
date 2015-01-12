package com.ebridge.vas.service;

import com.ebridge.commons.dto.MenuItemDTO;
import com.ebridge.commons.dto.Request;
import com.ebridge.commons.dto.Response;

import java.util.Set;

/**
 * @author david@tekeshe.com
 */
public interface ValueAddedService {

    public static final String NAME_PROPERTY = "value.added.service.name";

    public Response process( Request request, Set<MenuItemDTO> items );
}
