/*
 * Copyright (c) 2006
 * TopClock Systems (Private) Limited
 * All rights reserved.
 *
 * This software is distributed under TopClock Systems (Private) Limited ("Licence Agreement"). 
 * You shall use it and distribute only in accordance with the terms of the License Agreement.
 *
 */

package com.ebridge.smpp.pdu;

import com.ebridge.smpp.Data;
import com.ebridge.smpp.util.ByteBuffer;
import com.ebridge.smpp.util.NotEnoughDataInByteBufferException;
import com.ebridge.smpp.util.TerminatingZeroNotFoundException;

/**
 * 
 * @author David Tekeshe
 * @author david.tekeshe\@gmail.com
 * @version 1.0, 21 August 2006
 * 
 */
public class UnbindResp extends Response
{
	public UnbindResp()
	{
	    super(Data.UNBIND_RESP);
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
        String dbgs = "(unbind_resp: ";
        dbgs += super.debugString();
        dbgs += ") ";
        return dbgs;
    }
}