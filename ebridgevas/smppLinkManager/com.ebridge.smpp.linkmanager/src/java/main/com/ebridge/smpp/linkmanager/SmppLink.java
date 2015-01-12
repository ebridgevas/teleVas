package com.ebridge.smpp.linkmanager;

import com.ebridge.command.processor.DefaultServiceCommandProcessor;
import com.ebridge.protocol.smpp.*;
import com.ebridge.protocol.smpp.pdu.*;
import com.ebridge.smpp.linkmanager.util.SmppUtils;

import java.io.IOException;
import java.util.Map;

import static com.ebridge.smpp.linkmanager.util.SmppUtils.*;

/**
 * @author david@tekeshe.com
 */
public class SmppLink extends SmppObject implements ServerPDUEventListener, Runnable, Binding {

    Thread thread = new Thread(this);

    private final Map<String, String> config;

    final DefaultServiceCommandProcessor processor;

    private Session session;

    public SmppLink ( Map<String, String> config,
                      DefaultServiceCommandProcessor processor,
                      String logPath
                      ) {

        this.config = config;
        this.processor = processor;
    }

    @Override
    public void run() {

        // TCP Connection
        while ( ! thread.interrupted() ) {

            try {

                System.out.println("[" + config.get("system-id") + "] --> Connecting to " + config.get("server-ip-address") +
                        " / " + config.get("server-tcp-port"));
                session = connection(config.get("server-ip-address"),
                        Integer.parseInt(config.get("server-tcp-port")));

                if (session == null) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        break;
                    }

                    continue;
                } else {
                    break;
                }
            } catch (Exception e ) {
                e.printStackTrace();
            }
        }

        // Binding

        while ( ! thread.interrupted() ) {

            try {

                BindResponse ussdBindResponse = bind(   session,
                                                        this,
                                                        config.get("system-id"),
                                                        config.get("system-password"));

                System.out.println("[" + config.get("system-id") + "] <-- " + ussdBindResponse != null ? ussdBindResponse.debugString() : "NOT BOUND");
                if ( ussdBindResponse!= null && ussdBindResponse.getCommandStatus() == Data.ESME_ROK) {
                    break;
                } else {

                    // TODO Send notification to system admin.
                }
            } catch (PDUException e) {
                System.out.println("[" + config.get("system-id") + "] <-- " + e.getMessage());
            } catch (IOException e) {
                System.out.println("[" + config.get("system-id") + "] <-- " + e.getMessage());
            } catch (WrongSessionStateException e) {
                System.out.println("[" + config.get("system-id") + "] <-- " + e.getMessage());
            } catch (TimeoutException e) {
                System.out.println("[" + config.get("system-id") + "] <-- " + e.getMessage());
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * Entry point.
     *
     * Handles SMPP PDU received from SMPP Server.
     *
     * This method is called by the Session object upon receiving a PDU from SMPP interface via the TCP layer
     *
     *  @see com.ebridge.protocol.smpp.TCPIPConnection
     *
     * @param serverPDUEvent
     **/
    @Override
    public void handleEvent( ServerPDUEvent serverPDUEvent ) {

        try {

            PDU pdu = serverPDUEvent.getPDU();

            /*
             * process smpp server responses.
             */
            if (pdu.isResponse()) {

                SmppUtils.processSmppResponse((Response) pdu);
                return;
            }

            Request request = (Request) pdu;

            /*
             * process enquiry link requests from smpp server.
             */
            if ( request.getCommandId() == 21 ) {

                sendEnquireLinkResp(session);
                return;
            }

            System.out.println("####################### " + request.debugString());

            // Process PDU
            processor.process( null);
        } catch (ValueNotSetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WrongSessionStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {

        thread.start();
    }

    @Override
    public void stop() throws InterruptedException {

        unbind(session, config);
        thread.interrupt();
        // smpp unbind
        thread.join();
    }
}
