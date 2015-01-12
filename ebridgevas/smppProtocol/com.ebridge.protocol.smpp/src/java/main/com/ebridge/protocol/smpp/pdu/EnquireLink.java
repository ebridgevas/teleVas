/*
 * Copyright (c) 2006
 * TopClock Systems (Private) Limited
 * All rights reserved.
 *
 * This software is distributed under TopClock Systems (Private) Limited ("Licence Agreement"). 
 * You shall use it and distribute only in accordance with the terms of the License Agreement.
 *
 */

package com.ebridge.protocol.smpp.pdu;


import com.ebridge.protocol.smpp.Data;
import com.ebridge.protocol.smpp.util.ByteBuffer;
import com.ebridge.protocol.smpp.util.NotEnoughDataInByteBufferException;
import com.ebridge.protocol.smpp.util.TerminatingZeroNotFoundException;

/**
 * 
 * @author David Tekeshe
 * @author david.tekeshe\@gmail.com
 * @version 1.0, 21 August 2006
 * 
 */
public class EnquireLink extends Request
{
    public EnquireLink()
    {
        super(Data.ENQUIRE_LINK);
    }

    protected Response createResponse()
    {
        return new EnquireLinkResp();
    }

    public void setBody(ByteBuffer buffer)
    throws NotEnoughDataInByteBufferException,
            TerminatingZeroNotFoundException,
           PDUException
    {
    }

    public ByteBuffer getBody()
    {
        return null;
    }

    public String debugString()
    {
        String dbgs = "(enquirelink: ";
        dbgs += super.debugString();
        dbgs += ") ";
        return dbgs;
    }
}