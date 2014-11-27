package com.ebridge.vas.services.impl;

import com.ebridge.vas.dao.OutboundMsgDao;
import com.ebridge.vas.model.OutboundMsg;
import com.ebridge.vas.services.SecurityTokenSender;
import com.ebridge.vas.util.DatabaseException;

import java.io.IOException;

/**
 * @author david@tekeshe.com
 *
 */
public class DatabaseBackedSecurityTokenSender implements SecurityTokenSender {

    @Override
    public void send ( OutboundMsg outboundMsg) throws IOException {

        try {
            OutboundMsgDao.persist(outboundMsg);
        } catch (DatabaseException e) {
            throw new IOException( e );
        }
    }
}
