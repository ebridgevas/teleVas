package com.ebridge.smpp.server.simulator;

/**
* Passed to <code>SMSCListener</code> for generating instances of classes derived from <code>PDUProcessor</code>.
*/
public interface PDUProcessorFactory {

	/**
	 * Should generate proper PDU processor for processing of PDUs.
	 * @param session the session the PDU processor should work on
	 * @return the new PDU processor for processing reqests and responses
	 */
	public abstract PDUProcessor createPDUProcessor(SmppServerSession session);
}
