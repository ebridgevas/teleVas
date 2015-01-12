package com.ebridge.smpp.server.simulator;

import com.ebridge.protocol.smpp.Connection;
import com.ebridge.protocol.smpp.Data;
import com.ebridge.protocol.smpp.SmppObject;
import com.ebridge.protocol.smpp.TCPIPConnection;

import java.io.InterruptedIOException;
import java.io.IOException;

public class SmppServerListenerImpl extends SmppObject implements Runnable, SmppServerListener {

	private Connection serverConn = null;
	private int port;
	private long acceptTimeout = Data.ACCEPT_TIMEOUT;
	private PDUProcessorFactory processorFactory = null;
	private boolean keepReceiving = true;
	private boolean isReceiving = false;
	private boolean asynchronous = false;

	public SmppServerListenerImpl(int port) {
		this.port = port;
	}

	public SmppServerListenerImpl(int port, boolean asynchronous) {
		this.port = port;
		this.asynchronous = asynchronous;
	}

	public synchronized void start() throws IOException {

		System.out.println("Starting SmppServerListener on port " + port);

		if (!isReceiving) {

			serverConn = new TCPIPConnection(port);
			serverConn.setReceiveTimeout(getAcceptTimeout());
			serverConn.open();
			keepReceiving = true;
			if (asynchronous) {
				Thread serverThread = new Thread(this);
				serverThread.start();
			} else {
				run();
			}
		} else {
			System.err.println("already receiving, not starting the listener.");
		}
	}

	public synchronized void stop() throws IOException {

		System.out.println("going to stop SMSCListener on port " + port);
		keepReceiving = false;
		while (isReceiving) {
			Thread.yield();
		}
		serverConn.close();
	}

	public void run() {

		isReceiving = true;
		try {
			while (keepReceiving) {
				listen();
				Thread.yield();
			}
		} finally {
			isReceiving = false;
		}
	}

	private void listen() {

		try {
			Connection connection = null;
			serverConn.setReceiveTimeout(getAcceptTimeout());
			connection = serverConn.accept();

			if (connection != null) {

				SmppServerSession session = new SmppServerSessionImpl(connection);
				PDUProcessor pduProcessor = null;
				if (processorFactory != null) {
					pduProcessor = processorFactory.createPDUProcessor(session);
				}
				session.setPDUProcessor(pduProcessor);
				Thread thread = new Thread(session);
				thread.start();
				System.out.println("SMSCListener launched a session on the accepted connection.");
			} else {
			}
		} catch (InterruptedIOException e) {
			System.err.println("InterruptedIOException accepting, timeout? -> " + e);
		} catch (IOException e) {
			keepReceiving = false;
		}
	}

	public void setPDUProcessorFactory(PDUProcessorFactory processorFactory) {

		this.processorFactory = processorFactory;
	}

	public void setAcceptTimeout(int value) {

		acceptTimeout = value;
	}

	public long getAcceptTimeout() {
		return acceptTimeout;
	}
}