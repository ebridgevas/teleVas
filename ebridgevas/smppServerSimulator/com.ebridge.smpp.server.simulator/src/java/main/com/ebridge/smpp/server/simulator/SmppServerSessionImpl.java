package com.ebridge.smpp.server.simulator;

import com.ebridge.protocol.smpp.*;
import com.ebridge.protocol.smpp.pdu.PDU;
import com.ebridge.protocol.smpp.pdu.PDUException;
import com.ebridge.protocol.smpp.pdu.Request;
import com.ebridge.protocol.smpp.pdu.Response;

import java.io.IOException;

public class SmppServerSessionImpl extends SmppObject implements SmppServerSession {

	private Receiver receiver;
	private Transmitter transmitter;
	private PDUProcessor pduProcessor;
	private Connection connection;
	private long receiveTimeout = Data.RECEIVER_TIMEOUT;
	private boolean keepReceiving = true;
	private boolean isReceiving = false;
	private int timeoutCntr = 0;

	public SmppServerSessionImpl( Connection connection ) {

		this.connection = connection;
		transmitter = new Transmitter(connection);
		receiver = new Receiver(transmitter, connection);
	}

	public void stop() {
		System.out.println("SMSCSession stopping");
		keepReceiving = false;
	}

	public void run() {

		PDU pdu = null;

		System.out.println("SMSCSession starting receiver");
		receiver.start();
		isReceiving = true;
		try {
			while (keepReceiving) {
				try {
					pdu = receiver.receive(getReceiveTimeout());
				} catch (Exception e) {
					System.err.println("SMSCSession caught exception receiving PDU " + e.getMessage());
				}

				if (pdu != null) {

					timeoutCntr = 0;
					if (pdu.isRequest()) {
						pduProcessor.clientRequest((Request) pdu);
					} else if (pdu.isResponse()) {
						pduProcessor.clientResponse((Response) pdu);
					} else {
						System.out.println("SMSCSession not reqest nor response => not doing anything.");
					}
				} else {
					timeoutCntr++;
					if (timeoutCntr > 5) {
						stop();
					}
				}
			}
		} finally {
			isReceiving = false;
		}

		System.out.println("SMSCSession stopping receiver");
		receiver.stop();
		pduProcessor.exit();

		try {
			System.out.println("SMSCSession closing connection");
			connection.close();
		} catch (IOException e) {
		}

        System.out.println("SMSCSession exiting run()");
	}

	public void send(PDU pdu) throws IOException, PDUException {

		timeoutCntr = 0;
		transmitter.send(pdu);
	}

	public void setPDUProcessor(PDUProcessor pduProcessor) {

		this.pduProcessor = pduProcessor;
	}

	public void setPDUProcessorFactory(PDUProcessorFactory pduProcessorFactory) {
		// Ignore, the pdu processor is created by listene
	}

	public void setReceiveTimeout(long timeout) {
		receiveTimeout = timeout;
	}

	public long getReceiveTimeout() {
		return receiveTimeout;
	}

	public Object getAccount() {
		return null;
	}

	public void setAccount(Object account) {
	}

	public boolean isReceiving() {
		return isReceiving;
	}

	public void setReceiving(boolean isReceiving) {
		this.isReceiving = isReceiving;
	}

	public Connection getConnection() {
		return connection;
	}
}