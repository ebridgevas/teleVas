package com.ebridge.vas.esme;

/**
 * @author david@tekeshe.com
 */

import static com.ebridge.vas.esme.factory.PduFactory.processSmppResponse;
import static com.ebridge.vas.esme.factory.PduFactory.sendEnquireLinkResp;

import com.ebridge.smpp.ServerPDUEvent;
import com.ebridge.smpp.Session;
import com.ebridge.smpp.WrongSessionStateException;
import com.ebridge.smpp.pdu.PDU;
import com.ebridge.smpp.pdu.Request;
import com.ebridge.smpp.pdu.Response;
import com.ebridge.smpp.pdu.ValueNotSetException;

import java.io.IOException;

/**
 * Parse smpp pdu received from smpp server,
 * makes an async call to billing platform interface (served by tomcat or any app server)
 * receives response from billing platform and pass it back to smpp server.
 */
public class SmppEventListener {

    private Session session;

    /* Entry point. This method is called by the Session object upon receiving a PDU from
     *  SMPP interface via the TCP layer
     *
     *  @see TCPIPConnection
     */
    public void handleEvent( ServerPDUEvent event ) {

        try {

            PDU pdu = event.getPDU();

            /*
             * process smpp server responses.
             */
            if (pdu.isResponse()) {

                processSmppResponse((Response) pdu);
                return;
            }

            Request request = (Request) pdu;

            /*
             * process enquiry link requests from smpp server.
             */
            if ( request.getCommandId() == 21 ) {

                sendEnquireLinkResp( session );
                return;
            }

        } catch (ValueNotSetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WrongSessionStateException e) {
            e.printStackTrace();
        }
    }
}
