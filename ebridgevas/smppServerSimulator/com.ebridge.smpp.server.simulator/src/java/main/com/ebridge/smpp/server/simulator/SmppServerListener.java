package com.ebridge.smpp.server.simulator;

import java.io.IOException;

public interface SmppServerListener {

    /**
	 * Starts the listening.
     * If the listener is asynchronous, then new thread is created which listens on the port
     * and the <code>start</code> method returns to the caller.
     * Otherwise the caller is blocked in the start method.
	 **/
	public abstract void start() throws IOException;

    /**
	 * Signals the listener that it should stop listening and wait
	 * until the listener stops.
     * Based on the timeout settings it can take some time before this method is finished
     * - the listener can be blocked on i/o operation and only after exiting i/o it can detect that it should stop.
	 */
	public abstract void stop() throws IOException;

	/**
	 * The actual listening code which is run either from the thread
	 * (for async listener) or called from <code>start</code> method
	 * (for sync listener).
     * The method can be exited by calling of method <code>stop</code>.
	 */
	public abstract void run();

	/**
	 * Sets a PDU processor factory to use for generating PDU processors.
	 * @param processorFactory the new PDU processor factory
	 */
	public abstract void setPDUProcessorFactory( PDUProcessorFactory processorFactory );
	/**
	 * Sets new timeout for accepting new connection.
	 * The listening blocks the for maximum this time, then it
	 * exits regardless the connection was acctepted or not.
	 * @param value the new value for accept timeout
	 */
	public abstract void setAcceptTimeout(int value);
	/**
	 * Returns the current setting of accept timeout.
	 * @return the current accept timeout
	 * @see #setAcceptTimeout(int)
	 */
	public abstract long getAcceptTimeout();
}
/*
* $Log: not supported by cvs2svn $
*/