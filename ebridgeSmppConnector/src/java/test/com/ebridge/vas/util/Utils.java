package com.ebridge.vas.util;

import com.ebridge.smpp.pdu.Address;
import com.ebridge.smpp.pdu.DeliverSM;
import com.ebridge.smpp.pdu.SubmitSM;
import com.ebridge.smpp.pdu.WrongLengthOfStringException;

/**
 * @author david@tekeshe.com
 */
public class Utils {

    public static DeliverSM deliverSM( String sourceId, String destinationId, String shortMessage)
            throws WrongLengthOfStringException {

        DeliverSM pdu = new DeliverSM();
        pdu.setSourceAddr(new Address(sourceId));
        pdu.setDestAddr(new Address(destinationId));
        pdu.setShortMessage( shortMessage );

        return pdu;
    }

    public static SubmitSM submitSM( String sourceId, String destinationId, String shortMessage)
            throws WrongLengthOfStringException {

        SubmitSM pdu = new SubmitSM();
        pdu.setSourceAddr(new Address(sourceId));
        pdu.setDestAddr(new Address(destinationId));
        pdu.setShortMessage( shortMessage.trim() );

        return pdu;
    }
}
