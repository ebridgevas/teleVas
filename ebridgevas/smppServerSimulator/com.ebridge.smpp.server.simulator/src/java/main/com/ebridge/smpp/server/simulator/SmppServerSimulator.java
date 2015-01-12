package com.ebridge.smpp.server.simulator;

import static com.ebridge.commons.util.JsonUtils.config;

import com.ebridge.protocol.smpp.pdu.DeliverSM;
import com.ebridge.protocol.smpp.pdu.PDUException;
import com.ebridge.protocol.smpp.pdu.WrongLengthOfStringException;

import java.io.*;
import java.util.Map;

public class SmppServerSimulator {

	private static final String CONFIG_FILENAME = System.getProperty("ebridge.conf.path") + "/" + "service.config";
	private static final String SMPP_SIMULATOR_CLIENT_MAP_KEY = "smpp-simulator-service-clients";
	private static Map<String, Map<String, String>> clients;
	private static final String SMPP_SIMULATOR_SHORT_CODES_MAP_KEY = "smpp-simulator-short-codes";
	private static Map<String, Map<String, String>> shortCodes;
	private static final String SYSTEM_ID = "system-id";

	public static final int DSIM = 16;
	public static final int DSIMD = 17;
	public static final int DSIMD2 = 18;

	boolean keepRunning = true;
	private SmppServerListener smppServerListener = null;
	private SimulatorPDUProcessorFactory factory = null;
	private PDUProcessorGroup processors = null;
	private ShortMessageStore messageStore = null;
	private DeliveryInfoSender deliveryInfoSender = null;
	private boolean displayInfo = true;

	public SmppServerSimulator( ) {
	}

	public void start( int port, PrintStream out, PrintStream err ) throws IOException {

		if ( smppServerListener == null ) {

			smppServerListener = new SmppServerListenerImpl(port, true);
			processors = new PDUProcessorGroup();
			messageStore = new ShortMessageStore();
			deliveryInfoSender = new DeliveryInfoSender();
			deliveryInfoSender.start();
			clients = config(CONFIG_FILENAME).get( SMPP_SIMULATOR_CLIENT_MAP_KEY );
			shortCodes = config(CONFIG_FILENAME).get(SMPP_SIMULATOR_SHORT_CODES_MAP_KEY);
			factory = new SimulatorPDUProcessorFactory(processors, messageStore, deliveryInfoSender, clients);
			factory.setDisplayInfo(displayInfo);
			smppServerListener.setPDUProcessorFactory(factory);
			smppServerListener.start();
			out.println("[ Started ] ");
		} else {
			out.println("[ Already running ]");
		}
	}

	public void stop() throws IOException {

		if ( smppServerListener != null ) {

			System.out.println("Stopping listener...");
			synchronized (processors) {
				int procCount = processors.count();
				SimulatorPDUProcessor proc;
				SmppServerSession session;
				for (int i = 0; i < procCount; i++) {
					proc = (SimulatorPDUProcessor) processors.get(i);
					session = proc.getSession();
					System.out.print("Stopping session " + i + ": " + proc.getSystemId() + " ...");
					session.stop();
					System.out.println(" stopped.");
				}
			}
			smppServerListener.stop();
			smppServerListener = null;
			if (deliveryInfoSender != null) {
				deliveryInfoSender.stop();
			}
			System.out.println("Stopped.");
		}
	}

	protected void exit() throws IOException {
		stop();
		keepRunning = false;
	}


	public void messageList() {

		if (smppServerListener != null) {
			messageStore.print();
		} else {
			System.out.println("You must start listener first.");
		}
	}

	public void reloadUsers() {

		if (smppServerListener != null) {
			try {
//				if (users != null) {
//					users.reload();
//				} else {
//					users = new Table( usersFileName );
					clients = config(CONFIG_FILENAME).get( SMPP_SIMULATOR_CLIENT_MAP_KEY );
//				}
				System.out.println("Users file reloaded.");
			} catch (FileNotFoundException e) {
                e.printStackTrace();
			}
		} else {
			System.out.println("You must start listener first.");
		}
	}

	protected void logToScreen() {
		if (smppServerListener != null) {
			synchronized (processors) {
				displayInfo = !displayInfo;
				int procCount = processors.count();
				SimulatorPDUProcessor proc;
				for (int i = 0; i < procCount; i++) {
					proc = (SimulatorPDUProcessor) processors.get(i);
					proc.setDisplayInfo(displayInfo);
				}
			}
			factory.setDisplayInfo(displayInfo);
		}
	}

	public void listClients( PrintStream out) {

		if (smppServerListener != null) {

			synchronized (processors) {

				int procCount = processors.count();

				if (procCount > 0) {
					SimulatorPDUProcessor proc;
					for (int i = 0; i < procCount; i++) {
						proc = (SimulatorPDUProcessor) processors.get(i);
						out.print(proc.getSystemId());
						if (!proc.isActive()) {
							out.println(" (inactive)");
						} else {
							out.println();
						}
					}
				} else {
					out.println("No client connected.");
				}
			}
		} else {
			System.out.println("You must start listener first.");
		}
	}

	public void sendMessage(PrintStream out, String sourceId, String destinationId, String message )
			throws IOException {


		if ( smppServerListener == null ) {
			out.println("You must start listener first.");
			return;
		}

		Map<String, String> shortCodeConfig = shortCodes.get(destinationId);
		if (shortCodeConfig == null) {
			out.println("Short code " + destinationId + " not defined in service.config");
			return;
		}

		SimulatorPDUProcessor processor = processor(shortCodeConfig.get(SYSTEM_ID), out);
		DeliverSM request = new DeliverSM();
		try {
			request.setShortMessage(message);
			processor.serverRequest(request);
			System.out.println("Message sent.");
		} catch (WrongLengthOfStringException e) {
			System.out.println("Message sending failed");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PDUException e) {
			e.printStackTrace();
		}
	}

	protected SimulatorPDUProcessor processor( String systemId, PrintStream out ) {

		for (int idx = 0; idx < processors.count(); ++idx) {
			SimulatorPDUProcessor processor = (SimulatorPDUProcessor)processors.get( idx );
			if (systemId.equalsIgnoreCase(processor.getSystemId())) {
				if (processor.isActive()) {
					return processor;
				} else {
					out.println("Session for " + systemId + " is not active.");
					return null;
				}
			}
		}
		out.println("No session for " + systemId + ".");
		return null;
	}
}
