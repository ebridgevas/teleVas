package com.ebridge.smpp.server.simulator;

import com.ebridge.protocol.smpp.pdu.SubmitSM;

import java.util.Enumeration;
import java.util.Hashtable;

public class ShortMessageStore {

	private Hashtable<String, ShortMessageValue> messages = new Hashtable<>();

	public ShortMessageStore() {
	}

	public synchronized void submit(SubmitSM message, String messageId, String systemId) {
		messages.put(messageId, new ShortMessageValue(systemId, message));
	}

	public synchronized void cancel(String messageId) {
		messages.remove(messageId);
	}

	public synchronized void replace(String messageId, String newMessage) {

		ShortMessageValue sMV = (ShortMessageValue) messages.get(messageId);
		if (sMV != null) {
			sMV.shortMessage = newMessage;
		}
	}

	public synchronized ShortMessageValue getMessage(String messageId) {

		return (ShortMessageValue) messages.get(messageId);
	}

	public synchronized void print() {

		if (messages.size() != 0) {
			ShortMessageValue sMV;
			Enumeration<String> keys = messages.keys();
			Object key;
			System.out.println("------------------------------------------------------------------------");
			System.out.println("| Msg Id   |Sender     |ServT|Source address |Dest address   |Message   ");
			System.out.println("------------------------------------------------------------------------");
			while (keys.hasMoreElements()) {
				key = keys.nextElement();
				sMV = (ShortMessageValue) messages.get(key);
				printMessage(key, sMV);
			}
		} else {
			System.out.println("There is no message in the message store.");
		}
	}

	private void printMessage(Object key, ShortMessageValue sMV) {

		String messageId, systemId, serviceType, sourceAddr, destAddr, shortMessage;

		messageId = key.toString();
		systemId = pad(sMV.systemId, 11);
		if (sMV.serviceType.equals("")) {
			serviceType = "null";
		} else {
			serviceType = sMV.serviceType;
		}
		serviceType = pad(serviceType, 5);
		sourceAddr = pad(sMV.sourceAddr, 15);
		destAddr = pad(sMV.destinationAddr, 15);
		shortMessage = sMV.shortMessage;
		System.out.println(
			"- "
				+ messageId
				+ " |"
				+ systemId
				+ "|"
				+ serviceType
				+ "|"
				+ sourceAddr
				+ "|"
				+ destAddr
				+ "|"
				+ shortMessage);
	}

	private String pad(String data, int length) {
		String result;
		if (data == null) {
			data = "";
		}
		if (data.length() > length) {
			result = data.substring(1, length + 1);
		} else {
			int padCount = length - data.length();
			result = data;
			for (int i = 1; i <= padCount; i++) {
				result += " ";
			}
		}
		return result;
	}
}
