package com.ebridge.smpp.server.simulator;

import com.ebridge.protocol.smpp.Connection;
import com.ebridge.protocol.smpp.pdu.PDU;
import com.ebridge.protocol.smpp.pdu.PDUException;

import java.io.IOException;

public interface SmppServerSession extends Runnable {

	/**
	 * Signals the session's thread that it should stop.
	 * Doesn't wait for the thread to be completely finished.
	 */
	public abstract void stop();

	/**
	 * Receives PDUs from client and pass them to PDU processor.
     **/
	public abstract void run();

    /**
	 * Sends a PDU to the client.
	 * @param pdu the PDU to send
	 */
	public abstract void send(PDU pdu) throws IOException, PDUException;

	/**
	 * Sets new PDU processor.
	 * @param pduProcessor the new PDU processor
	 */
	public abstract void setPDUProcessor(PDUProcessor pduProcessor);

	public abstract void setPDUProcessorFactory(PDUProcessorFactory pduProcessorFactory);

	public abstract void setReceiveTimeout(long timeout);

	public abstract long getReceiveTimeout();

	public abstract Object getAccount();

	public abstract void setAccount(Object account);

	public abstract Connection getConnection();
}