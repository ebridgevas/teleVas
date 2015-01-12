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
public class AlertNotification extends Request
{
    public AlertNotification()
    {
        super(Data.ALERT_NOTIFICATION);
    }

    protected Response createResponse()
    {
        return null;
    }

	public boolean canResponse()
	{
		return false;
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
        String dbgs = "(alertnotification: ";
        dbgs += super.debugString();
        dbgs += ") ";
        return dbgs;
    }
}