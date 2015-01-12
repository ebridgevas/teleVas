package com.ebridge.smpp.linkmanager.util;

import com.ebridge.protocol.smpp.*;
import com.ebridge.protocol.smpp.pdu.*;
import com.ebridge.protocol.smpp.util.SmppParamaters;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author david@tekeshe.com
 */
public class SmppUtils {

    public static final String SYSTEM_TYPE;
    private static final Byte SMPP_VERSION;
    static {
        SYSTEM_TYPE = "EBridge";
        SMPP_VERSION = (byte) 0x34;
    }

    public static Session connection( String smppIPAddress, Integer smppPort ) {

        Long timeout = new Long(20 * 1000);

        TCPIPConnection ussdConnection = new TCPIPConnection( smppIPAddress, smppPort );
        ussdConnection.setReceiveTimeout( timeout );

        return new Session(ussdConnection);
    }

    public static BindResponse bind( Session session,
                                     ServerPDUEventListener serverPDUEventListener,
                                     String systemId,
                                     String systemPassword)
            throws PDUException, IOException, WrongSessionStateException, TimeoutException {

        BindRequest request = new BindTransciever();
        request.setSystemId(systemId);
        request.setPassword(systemPassword);
        request.setSystemType(SYSTEM_TYPE);
        request.setInterfaceVersion(SMPP_VERSION);
        request.setAddressRange(SmppParamaters.getAddressRange());
        System.out.println("[" + systemId + "] --> " + request.debugString());
        return session.bind(request, serverPDUEventListener);
    }

    public static void unbind(Session session, Map<String, String> config) {

        try {
            System.out.println("[" + config.get("system-id") + "] --> Unbinding " + config.get("system-id") + "...");

            if (session == null) {
                System.out.println("[" + config.get("system-id") + "] <-- " + "Not bound.");
                return;
            }

            UnbindResp response = session.unbind();
            System.out.println("[" + config.get("system-id") + "] <-- " + ( response != null ? response.debugString() : " Smpp Server unreachable") );
        } catch (Exception e) {
            System.out.println("[" + config.get("system-id") + "] <-- " + "Unbind failed. " + e.getMessage());
            e.printStackTrace();
        }
    }

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
