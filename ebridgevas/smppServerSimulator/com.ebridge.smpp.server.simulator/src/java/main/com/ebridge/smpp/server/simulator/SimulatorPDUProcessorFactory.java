package com.ebridge.smpp.server.simulator;

import java.util.Map;

public class SimulatorPDUProcessorFactory implements PDUProcessorFactory {

	private PDUProcessorGroup procGroup;
	private ShortMessageStore messageStore;
	private DeliveryInfoSender deliveryInfoSender;
	private Map<String, Map<String, String>> clients;

	private boolean displayInfo = false;

	public SimulatorPDUProcessorFactory(

		PDUProcessorGroup procGroup,
		ShortMessageStore messageStore,
		DeliveryInfoSender deliveryInfoSender,
		Map<String, Map<String, String>> clients ) {

		this.procGroup = procGroup;
		this.messageStore = messageStore;
		this.deliveryInfoSender = deliveryInfoSender;
		this.clients = clients;
	}

	public PDUProcessor createPDUProcessor(SmppServerSession session) {

		SimulatorPDUProcessor simPDUProcessor = new SimulatorPDUProcessor(session, messageStore, clients);
		simPDUProcessor.setDisplayInfo(getDisplayInfo());
		simPDUProcessor.setGroup(procGroup);
		simPDUProcessor.setDeliveryInfoSender(deliveryInfoSender);
		display("new connection accepted");
		return simPDUProcessor;
	}

	/**
	 * Sets if the info about processing has to be printed on
	 * the standard output.
	 */
	public void setDisplayInfo(boolean on) {
		displayInfo = on;
	}

	/**
	 * Returns status of printing of processing info on the standard output.
	 */
	public boolean getDisplayInfo() {
		return displayInfo;
	}

	private void display(String info) {
		if (getDisplayInfo()) {
			System.out.println( " [sys] " + info);
		}
	}
}
