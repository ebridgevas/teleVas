package com.ebridge.vas.parsers;

import com.ebridge.smpp.pdu.DeliverSM;
import com.ebridge.vas.Request;
import com.ebridge.vas.dto.ShortCodeConfigDTO;
import com.ebridge.vas.util.PDUParseException;

/**
 * @author david@tekeshe.com
 */
public interface PayloadParser {

    public Request parse( DeliverSM deliverSM ) throws PDUParseException;
}
