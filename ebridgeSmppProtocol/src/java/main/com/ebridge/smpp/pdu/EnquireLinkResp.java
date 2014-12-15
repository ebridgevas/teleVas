
package com.ebridge.smpp.pdu;

import com.ebridge.smpp.Data;
import com.ebridge.smpp.util.ByteBuffer;
import com.ebridge.smpp.util.NotEnoughDataInByteBufferException;
import com.ebridge.smpp.util.TerminatingZeroNotFoundException;

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