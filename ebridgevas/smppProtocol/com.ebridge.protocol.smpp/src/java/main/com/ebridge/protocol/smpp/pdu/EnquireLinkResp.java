
package com.ebridge.protocol.smpp.pdu;

import com.ebridge.protocol.smpp.Data;
import com.ebridge.protocol.smpp.util.ByteBuffer;
import com.ebridge.protocol.smpp.util.NotEnoughDataInByteBufferException;
import com.ebridge.protocol.smpp.util.TerminatingZeroNotFoundException;

/**
 * @author david@ebridgevas.com
 *
 */
public class EnquireLinkResp extends Response {

	public EnquireLinkResp() {
	    super(Data.ENQUIRE_LINK_RESP);
	}

    public void setBody(ByteBuffer buffer)
                    throws NotEnoughDataInByteBufferException,
                           TerminatingZeroNotFoundException,
                           PDUException {
    }

    public ByteBuffer getBody() {
        return null;
    }

    public String debugString() {
        String dbgs = "(enquirelink_resp: ";
        dbgs += super.debugString();
        dbgs += ") ";
        return dbgs;
    }
}