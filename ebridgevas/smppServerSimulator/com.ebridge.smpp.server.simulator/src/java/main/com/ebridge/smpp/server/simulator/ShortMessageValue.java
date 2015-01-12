package com.ebridge.smpp.server.simulator;

import com.ebridge.protocol.smpp.pdu.SubmitSM;

class ShortMessageValue {

	String systemId;
	String serviceType;
	String sourceAddr;
	String destinationAddr;
	String shortMessage;

	ShortMessageValue(String systemId, SubmitSM submit) {

		this.systemId = systemId;
		serviceType = submit.getServiceType();
		sourceAddr = submit.getSourceAddr().getAddress();
		destinationAddr = submit.getDestAddr().getAddress();
		shortMessage = submit.getShortMessage();
	}
}