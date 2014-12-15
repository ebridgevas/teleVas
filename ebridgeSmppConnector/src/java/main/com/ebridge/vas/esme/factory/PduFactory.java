package com.ebridge.vas.esme.factory;

import com.ebridge.smpp.Session;
import com.ebridge.smpp.WrongSessionStateException;
import com.ebridge.smpp.pdu.EnquireLinkResp;
import com.ebridge.smpp.pdu.Response;
import com.ebridge.smpp.pdu.ValueNotSetException;

import java.io.IOException;

/**
 * @author david@tekeshe.com
 */
public class PduFactory {

    public static void processSmppResponse(Response response) {

        // TODO - logging
        System.out.println( response.debugString() );
        /*
         * TODO - if smpp server does respond N times then
         * TODO - then re-bind
         * TODO - Deprecated because of erratic smpp server behaviour
        if (request.getCommandStatus() == 0) {
            SMPPTransciever.linkState = "LINK_IS_UP";
        }
        */
    }

    public static void sendEnquireLinkResp(Session session)
                            throws ValueNotSetException,
                                   IOException,
                                   WrongSessionStateException {

        EnquireLinkResp response = new EnquireLinkResp();
        response.setCommandStatus(0);

        // TODO - logging
        System.out.println(response.debugString());
        session.respond(response);
    }
}
