package com.ebridge.vas.factory.halys;

import com.ebridge.smpp.pdu.Address;
import com.ebridge.smpp.pdu.SubmitSM;
import com.ebridge.smpp.pdu.WrongLengthOfStringException;
import com.ebridge.vas.Response;
import com.ebridge.vas.factory.UssdPDUFactory;

/**
 * @author david@tekeshe.com
 */
public class HalysUssdPDUFactory implements UssdPDUFactory {

    @Override
    public SubmitSM submitSM(Response response) throws WrongLengthOfStringException {

        SubmitSM pdu = new SubmitSM();
        pdu.setSourceAddr(new Address(response.getSourceAddress()));
        pdu.setDestAddr(new Address( response.getDestinationAddress()));
        pdu.setShortMessage( prefix(response) + " " + response.getShortMessage() );

        return pdu;
    }

    protected String prefix( Response response ) {
        return ( response.isSessionTerminating() ? "81" : "72" ) + " " +
                response.getSessionId() +
                ( response.isSessionTerminating() ? "" : " 30000" ) + " " + "0";
    }
}
