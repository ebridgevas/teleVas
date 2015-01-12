package com.ebridge.smpp.server.simulator;

import java.util.Vector;

public class PDUProcessorGroup {

	private Vector<PDUProcessor> processors = null;

	public PDUProcessorGroup() {
		processors = new Vector<>();
	}

	public PDUProcessorGroup(int initSize) {
		processors = new Vector<>(initSize);
	}

	public void add(PDUProcessor p) {

		synchronized (processors) {
			if (!processors.contains(p)) {
				processors.add(p);
			}
		}
	}

	public void remove(PDUProcessor p) {

		synchronized (processors) {
			processors.remove(p);
		}
	}

	public int count() {

		synchronized (processors) {
			return processors.size();
		}
	}

	public PDUProcessor get(int i) {

		synchronized (processors) {
			return (PDUProcessor) processors.get(i);
		}
	}
}
