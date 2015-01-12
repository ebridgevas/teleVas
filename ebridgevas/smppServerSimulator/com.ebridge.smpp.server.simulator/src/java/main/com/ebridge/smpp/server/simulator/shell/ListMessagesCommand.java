package com.ebridge.smpp.server.simulator.shell;

import java.io.PrintStream;

public class ListMessagesCommand extends BasicCommand {

    public void exec(String args, PrintStream out, PrintStream err) throws Exception {

        out.println("Listing Messages ... " + args);
    }
}
