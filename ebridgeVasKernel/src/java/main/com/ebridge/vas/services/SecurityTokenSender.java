package com.ebridge.vas.services;

import com.ebridge.vas.model.OutboundMsg;

import java.io.IOException;

/**
 * david@ebridgevas.com
 *
 */
public interface SecurityTokenSender {

    public void send(OutboundMsg outboundMsg) throws IOException;
}
