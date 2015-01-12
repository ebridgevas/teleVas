package com.ebridge.smpp.server.simulator.util;

public class Attribute {

	private String name = null;
	private String value = null;
	public Attribute() {
	}

	public Attribute(String name) {
		this.name = name;
	}

	public Attribute(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public boolean nameEquals(String name) {

		if (this.name != null) {
			return this.name.equals(name);
		} else {
			return name == null; // nulls are equal
		}
	}

	public boolean equals(Attribute attr) {

		if (attr != null) {
			if (nameEquals(attr.getName())) {
				if (this.value != null) {
					return this.value.equals(value);
				} else {
					return value == null; // nulls are equal
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}