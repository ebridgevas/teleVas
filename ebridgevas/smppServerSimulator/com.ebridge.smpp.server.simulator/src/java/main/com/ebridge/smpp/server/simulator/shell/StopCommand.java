package com.ebridge.smpp.server.simulator.shell;

import java.io.PrintStream;

public class StopCommand extends BasicCommand {

    public void exec(String args, PrintStream out, PrintStream err) throws Exception {

        out.println("Stopping eBridge SMPP Server Simulator." + args);
    }
}
