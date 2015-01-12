package com.ebridge.smpp.server.simulator.shell;

public abstract class BasicCommand implements Command {

    private volatile String help;

    BasicCommand setHelp(String help) {
        this.help = help;
        return this;
    }

    public String toString() {
        String result = help;
        if (result == null) {
            result = super.toString();
        }
        return result;
    }
}
