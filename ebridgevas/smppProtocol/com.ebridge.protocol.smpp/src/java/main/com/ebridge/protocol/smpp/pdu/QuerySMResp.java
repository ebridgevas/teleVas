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
public class QuerySMResp extends Response
{
    private String messageId   = Data.DFLT_MSGID;
    private String finalDate   = Data.DFLT_DATE;//1 or 17
    private byte messageState  = Data.DFLT_MSG_STATE;
    private byte errorCode     = Data.DFLT_ERR;

	public QuerySMResp()
	{
	    super(Data.QUERY_SM_RESP);
	}

    public void setBody(ByteBuffer buffer)
    throws NotEnoughDataInByteBufferException,
           TerminatingZeroNotFoundException,
           PDUException
    {
	    setMessageId(buffer.removeCString());
	    setFinalDate(buffer.removeCString());
	    setMessageState(buffer.removeByte());
	    setErrorCode(buffer.removeByte());
    }

    public ByteBuffer getBody()
    {
	    ByteBuffer buffer = new ByteBuffer();
        buffer.appendCString(getMessageId());
        buffer.appendCString(getFinalDate());
        buffer.appendByte(getMessageState());
        buffer.appendByte(getErrorCode());
	    return buffer;
    }
    
    public void setMessageId(String value)
    throws WrongLengthOfStringException {
        checkString(value, Data.SM_MSGID_LEN);
        messageId = value;
    }
    
    public void setFinalDate(String value)
    throws WrongDateFormatException {
        checkDate(value);
        finalDate = value;
    }
    
    public void setMessageState(byte value) { messageState = value; }
    public void setErrorCode(byte value)    { errorCode = value;     }
    
    
    public String getMessageId()  { return messageId; }
    public String getFinalDate()  { return finalDate; }
    public byte getMessageState() { return messageState; }
    public byte getErrorCode()    { return errorCode; }
    
    public String debugString()
    {
        String dbgs = "(query_resp: ";
        dbgs += super.debugString();
        dbgs += getMessageId(); dbgs += " ";
        dbgs += getFinalDate(); dbgs += " ";
        dbgs += getMessageState(); dbgs += " ";
        dbgs += getErrorCode(); dbgs += " ";
        dbgs += debugStringOptional();
        dbgs += ") ";
        return dbgs;
    }
}