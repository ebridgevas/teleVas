
package com.ebridge.protocol.smpp;

/**
 * @author david@tekeshe.com
 */

import com.ebridge.protocol.smpp.pdu.PDU;

public class ServerPDUEvent extends ReceivedPDUEvent {

    public ServerPDUEvent( Receiver source,
                           Connection connection,
                           PDU pdu) {

        super(source,connection,pdu);
    }

    public Receiver getReceiver() {
        return (Receiver)getSource();
    }
}
