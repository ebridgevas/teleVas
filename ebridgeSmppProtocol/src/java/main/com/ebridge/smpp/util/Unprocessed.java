/*
 * Copyright (c) 2003 - 2006
 * TopClock Systems (Private) Limited
 * All rights reserved.
 *
 * This software is distributed under TopClock Systems (Private) Limited ("Licence Agreement"). 
 * You shall use it and distribute only in accordance with the terms of the License Agreement.
 *
 */

package com.ebridge.smpp.util;

import com.ebridge.smpp.Data;
import com.ebridge.smpp.TimeoutException;

/**
 * Utility class <code>Unprocessed</code> is used for data received from
 * connection which aren't complete PDU yet.
 *
 * 
 * @author David Tekeshe
 * @author david.tekeshe\@gmail.com
 * @version 1.0, 21 August 2006
 *
 */

public class Unprocessed
{
    /**
    * Buffer for data which aren't complete PDU yet. Each time new data
    * are received, they are appended to this buffer and PDU is created
    * from this buffer.
    *
    * @see #hasUnprocessed
    * @see com.logica.smpp.ReceiverBase#receivePDUFromConnection(Connection, com.ebridge.smpp.util.Unprocessed)
    */
    private ByteBuffer unprocessed = new ByteBuffer();

    /**
     * Contains haw many bytes is expected after it's been decided that
     * the current bytes aren't enough to build up complete PDU.
     */
    private int expected = 0;

    /**
    * Indicates that after creating PDU from <code>unprocessed</code> buffer
    * there were still some data left in the <code>unprocessed</code> buffer.
    * In the next receive even if no new data will be received an attempt
    * to create PDU from <code>unprocessed</code> buffer will be performed.
    *
    * @see #unprocessed
    * @see com.logica.smpp.ReceiverBase#receivePDUFromConnection(Connection, com.ebridge.smpp.util.Unprocessed)
    */
    private boolean hasUnprocessed = false;

    /**
    * Contains the time when some data were received from connection.
    * If it is currently longer from <code>lastTimeReceived</code>
    * than specified by <code>receiveTimeout</code>,
    * <code>TimeoutException</code> is thrown.
    *
    * @see com.logica.smpp.ReceiverBase#receiveTimeout
    * @see TimeoutException
    * @see com.logica.smpp.ReceiverBase#receivePDUFromConnection(Connection, com.ebridge.smpp.util.Unprocessed)
    */
    private long lastTimeReceived = 0;
        
    /**
    * Resets flag <code>hasUnprocessed</code>, removes all bytes
    * from <code>unprocessed</code> buffer and sets <code>expected</code>
    * to zero.
    *
    * @see #hasUnprocessed
    * @see #unprocessed
    * @see #expected
    */
    public void reset()
    {
        hasUnprocessed = false;
        unprocessed.setBuffer(null);
        expected = 0;
    }

    /**
    * Sets flag <code>hasUnprocessed</code> if there are any
    * unprocessed bytes in <code>unprocessed</code> buffer.
    *
    * @see #hasUnprocessed
    * @see #unprocessed
    */
    public void check()
    {
        hasUnprocessed = unprocessed.length() > 0;
    }

    public void setHasUnprocessed(boolean value) { hasUnprocessed = value; }
    public void setExpected(int value) { expected = value; }
    public void setLastTimeReceived(long value) { lastTimeReceived = value; }
    public void setLastTimeReceived() { lastTimeReceived = Data.getCurrentTime(); }

    public ByteBuffer getUnprocessed() { return unprocessed; }
    public boolean getHasUnprocessed() { return hasUnprocessed; }
    public int getExpected()           { return expected; }
    public long getLastTimeReceived()  { return lastTimeReceived; }
}

