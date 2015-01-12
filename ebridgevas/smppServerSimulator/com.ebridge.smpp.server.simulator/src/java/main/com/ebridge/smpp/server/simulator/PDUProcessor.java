package com.ebridge.smpp.server.simulator;

import com.ebridge.protocol.smpp.pdu.PDUException;
import com.ebridge.protocol.smpp.pdu.Request;
import com.ebridge.protocol.smpp.pdu.Response;

import java.io.IOException;

public abstract class PDUProcessor {

	private PDUProcessorGroup group = null;

	private boolean active = true;

	public PDUProcessor() {
	}

	public PDUProcessor(PDUProcessorGroup group) {
		setGroup(group);
	}

	public abstract void clientRequest(Request request);

	public abstract void clientResponse(Response response);

	public abstract void serverRequest(Request request) throws IOException, PDUException;

	public abstract void serverResponse(Response response) throws IOException, PDUException;

	public abstract void stop();

	public void setGroup(PDUProcessorGroup g) {

		if (group != null) {
			group.remove(this);
		}
		group = g;
		if (group != null) {
			group.add(this);
		}
	}

	public PDUProcessorGroup getGroup() {
		return group;
	}

	public boolean isActive() {
		return active;
	}

	public void exit() {
		if (group != null) {
			group.remove(this);
		}
		active = false;
		stop();
	}
}