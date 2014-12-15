package com.ebridge.vas.parsers.impl;

import com.ebridge.vas.Request;
import com.ebridge.vas.parsers.SessionStartIndicator;

/**
 * PayloadTypeBasedSessionStartIndicator
 *
 * It looks for a particular value of PDU type.
 *
 * @author david@tekeshe.com
 */
public class PayloadTypeBasedSessionStartIndicator implements SessionStartIndicator  {

    @Override
    public Boolean isSessionStart(Request request) {
        return null;
    }
}
