/*
 * Copyright (c) 2003 - 2006
 * TopClock Systems (Private) Limited
 * All rights reserved.
 *
 * This software is distributed under TopClock Systems (Private) Limited ("Licence Agreement"). 
 * You shall use it and distribute only in accordance with the terms of the License Agreement.
 *
 */

package com.ebridge.smpp;

import com.ebridge.smpp.pdu.*;
import com.ebridge.smpp.util.ByteBuffer;
import com.ebridge.smpp.util.NotEnoughDataInByteBufferException;
import com.ebridge.smpp.util.ProcessingThread;
import com.ebridge.smpp.util.Unprocessed;

import java.io.IOException;


/**
 * Abstract base class for classes which can receive PDUs from connection.
 * The receiving of PDUs can be be performed on background within a separate
 * thread using the <code>ProcessingThread</code> class.
 * 
 * @author David Tekeshe
 * @author david.tekeshe\@gmail.com
 * @version 1.0, 21 August 2006
 *
 * @see Receiver
 * @see Connection
 * @see Transmitter
 * @see Session
 *  
 */
public abstract class ReceiverBase extends ProcessingThread
{
    /**
     * Timeout for receiving the rest of PDU from the connection.
     * If the rest of PDU isn't receive in time, <code>TimeoutException</code>
     * is thrown.
     *
     * @see TimeoutException
     */
    private long receiveTimeout = Data.RECEIVER_TIMEOUT;
    
    /**
     * Method repeatedly called from <code>process</code> method.
     * It's expected that derived classes implement atomic receive of
     * one PDU in this method using ReceiverBase's
     * <code>tryReceivePDUWithTimeout</code> and 
     * <code>receivePDUFromConnection</code> methods.
     *
     * @see #tryReceivePDUWithTimeout(Connection,PDU,long)
     * @see ProcessingThread#process()
     * @see ProcessingThread#run()
     */
    protected abstract void receiveAsync();
    
    /**
     * This method should try to receive one PDU from the connection.
     * It is called in cycle from <code>tryReceivePDUWithTimeout</code> until
     * timeout expires. The method should check if the actualy received
     * PDU is equal to the <code>expectedPDU</code>.
     *
     * @param connection  the connection from which the PDU should be received
     * @param expectedPDU the command id and sequence id of the received PDU
     *                    should be equal to those of expectedPDU
     * @return the received PDU if any or null if none received
     *
     */
    protected abstract PDU tryReceivePDU(Connection connection, PDU expectedPDU)
    throws UnknownCommandIdException,
           TimeoutException,
            PDUException,
           IOException;


    /**
     * This is an implementation of <code>ProcessingThread</code>'s
     * <code>process</code> method, which is method called in loop from
     * the <code>run</code> method.<br>
     * This simply calls <code>receiveAsync</code>.
     */
    public void process()
    {
        receiveAsync();
    }

    /**
     * Calls <code>tryReceivePDUWithTimeout(Connection,PDU,long)</code> with
     * timeout set by <code>setReceiveTimeout</code>.
     *
     * @param connection  the connection from which the PDU should be received
     * @param expectedPDU the command id and sequence id of the received PDU
     *                    should be equal to those of expectedPDU
     * @return the received PDU if any or null if none received
     *
     * @exception java.io.IOException exception during communication
     * @exception PDUException incorrect format of PDU
     * @exception TimeoutException rest of data not received for too long time
     * @exception UnknownCommandIdException PDU with unknown id was received
     * @see #tryReceivePDUWithTimeout(Connection,PDU,long)
     */
    final protected PDU tryReceivePDUWithTimeout(Connection connection, PDU expectedPDU)
    throws UnknownCommandIdException,
           TimeoutException,
           PDUException,
           IOException
    {
        return tryReceivePDUWithTimeout(connection,expectedPDU,getReceiveTimeout());
    }

    /**
     * For specified time tries to receive a PDU from given connection by
     * calling method <code>tryReceivePDU</code>.
     * The method <code>tryReceivePDU</code> must be implemented in the derived
     * class.
     * <p>
     * The timeout can be either value > 0, then it means for
     * how many milliseconds will be repeatedly tried to receive a PDU.
     * If the timeout is = 0 then there is only one attempt to receive a PDU.
     * If the timeout is equal to Data.RECEIVE_BLOCKING, then the this method
     * tries receive a PDU until it is received.
     *
     * @param connection  the connection from which the PDU should be received
     * @param expectedPDU the command id and sequence id of the received PDU
     *                    should be equal to those of expectedPDU
     * @param timeout     the timeout indication
     * @return the received PDU if any or null if none received
     *
     * @exception java.io.IOException exception during communication
     * @exception PDUException incorrect format of PDU
     * @exception TimeoutException rest of data not received for too long time
     * @exception UnknownCommandIdException PDU with unknown id was received
     * @see #tryReceivePDU(Connection,PDU)
     * @see PDU#equals(Object)
     */
    final protected PDU tryReceivePDUWithTimeout(Connection connection, PDU expectedPDU, long timeout)
    throws UnknownCommandIdException,
           TimeoutException,
           PDUException,
           IOException
    {
//        debug.write(DRXTX,"receivePDU: Going to receive response.");
        long startTime = Data.getCurrentTime();
        PDU pdu = null;
        if (timeout == 0) {
            // with no timeout try just once
            pdu = tryReceivePDU(connection, expectedPDU);
        } else {
            // with timeout keep trying until get some or timeout expires
            while ((pdu == null) && canContinueReceiving(startTime, timeout)) {
                pdu = tryReceivePDU(connection, expectedPDU);
            }
        }
        if (pdu != null) {
//            debug.write(DRXTX,"Got pdu "+pdu.debugString());
        }
        return pdu;
    }

    /**
     * Elementary method receiving data from connection, trying to create
     * PDU from them and buffering data in case the PDU
     * isn't still complete. It has timeout checking for incomplete
     * messages: if the message isn't received completly for certain time
     * and no new data are received for this time, then exception is thrown
     * as this could indicate communication problem.
     *
     * @param connection the connection to receive the data from
     * @return either PDU, if complete received or null
     *
     * @exception java.io.IOException exception during communication
     * @exception PDUException incorrect format of PDU
     * @throws TimeoutException rest of data not received for too long time
     * @throws UnknownCommandIdException PDU with unknown id was received
     * @see Connection
     */
    final protected PDU receivePDUFromConnection(Connection connection,
                                                 Unprocessed unprocessed)
    throws UnknownCommandIdException,
           TimeoutException,
           PDUException,
           IOException
    {
//        debug.write(DRXTXD2,"ReceiverBase.receivePDUFromConnection start");
        PDU pdu = null;
        ByteBuffer buffer;
        ByteBuffer unprocBuffer;
        // first check if there is something left from the last time
        if (unprocessed.getHasUnprocessed()) {
            unprocBuffer = unprocessed.getUnprocessed();
//            debug.write(DRXTX,"have unprocessed "+unprocBuffer.length()+" bytes from previous try");
            pdu = tryGetUnprocessedPDU(unprocessed);
        }
        if (pdu == null) { // only if we didn't manage to get pdu from unproc
            buffer = connection.receive();
            unprocBuffer = unprocessed.getUnprocessed();
            // if received something now or have something from the last receive
            if (buffer.length() != 0) {
                unprocBuffer.appendBuffer(buffer);
                unprocessed.setLastTimeReceived();
                pdu = tryGetUnprocessedPDU(unprocessed);
            } else {
//                debug.write(DRXTXD2,"no data received this time.");
                // check if it's not too long since we received any data
                long timeout = getReceiveTimeout();
                if ((unprocBuffer.length() > 0) &&
                    ((unprocessed.getLastTimeReceived() + timeout) <
                     Data.getCurrentTime())) {
//                    debug.write(DRXTX,"and it's been very long time.");
                    unprocessed.reset();
                    throw new TimeoutException(timeout,
                                               unprocessed.getExpected(),
                                               unprocBuffer.length());
                }
            }
        }
//        debug.write(DRXTXD2,"ReceiverBase.receivePDUFromConnection finished");
        return pdu;
    }

    /**
     * Tries to create a PDU from the buffer provided.
     * Returns the PDU if successfull or null if not or an exception
     * if the PDU is incorrect.
     */
    private final PDU tryGetUnprocessedPDU(Unprocessed unprocessed)
    throws UnknownCommandIdException,
           PDUException
    {
//        debug.write(DRXTX,"trying to create pdu from unprocessed buffer");
        PDU pdu = null;
        ByteBuffer unprocBuffer = unprocessed.getUnprocessed();
        try {
            pdu = PDU.createPDU(unprocBuffer);
            unprocessed.check();
        } catch (HeaderIncompleteException e) {
            // the header wasn't received completly, we will try to
            // receive the rest next time
//            debug.write(DRXTXD,"incomplete message header, will wait for the rest.");
            unprocessed.setHasUnprocessed(false); // as it's incomplete - wait for new data
            unprocessed.setExpected(Data.PDU_HEADER_SIZE);
        } catch (MessageIncompleteException e) {
            // the message wasn't received completly, less bytes than command
            // length has been received, will try to receive the rest next time
//            debug.write(DRXTXD,"incomplete message, will wait for the rest.");
            unprocessed.setHasUnprocessed(false); // as it's incomplete - wait for new data
            unprocessed.setExpected(Data.PDU_HEADER_SIZE);
        } catch (UnknownCommandIdException e) {
            // message with invalid id was received, should send generic_nack
//            debug.write(DRXTX,"unknown pdu, might remove from unprocessed buffer. CommandId=" + e.getCommandId());
            if (e.getCommandLength() <= unprocBuffer.length()) {
                // have already enough to remove
                try {
                    unprocBuffer.removeBytes(e.getCommandLength());
                } catch (NotEnoughDataInByteBufferException e1) {
                    // can't happen, we've checked it above
                    throw new Error("Not enough data in buffer even if previously checked that there was enough.");
                }
                unprocessed.check();
                throw e; // caller will decide what to do
            }
        }
        if (pdu != null) {
//            debug.write(DRXTX,"received complete pdu"+pdu.debugString());
//            debug.write(DRXTX,"there is "+unprocBuffer.length()+
//                        " bytes left in unprocessed buffer");
        }
        // unprocessed.check();
        return pdu;
    }

    /**
     * Sets the timeout for receiving the complete message.
     * If no data are received for time longer then this timeout and there
     * is still not completly received PDU in the internal buffer,
     * <code>TimeoutException</code> is thrown.
     *
     * @param timeout the new timeout value
     *
     * @see #receivePDUFromConnection(Connection,Unprocessed)
     * @see TimeoutException
     */
    public void setReceiveTimeout(long timeout) { receiveTimeout = timeout; }

    /**
     * Returns the current setting of the receiving timeout.
     *
     * @return the current timeout value
     */
    public long getReceiveTimeout() { return receiveTimeout; }

    /**
     * Depending on value of <code>timeout</code> and on <code>startTime</code>
     * returns if it's still possible to continue in receiving of message.
     * <code>timeout</code> can indicate either timeout in milliseconds
     * (if > 0), or that there has to be only one attempt to receive
     * a message (if = 0) or that the the receiving shuld continue until
     * a PDU is received (if = Data.RECEIVE_BLOCKING).
     *
     * @param startTime when the receiving started
     * @param timeout timeout indication
     * @return if it's possible to continue receiving
     */
    private boolean canContinueReceiving(long startTime, long timeout)
    {
        return timeout == Data.RECEIVE_BLOCKING ?
               true :
               Data.getCurrentTime() <= (startTime + timeout);
    }
    
}
