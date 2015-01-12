package com.ebridge.smpp.server.simulator.shell;

import com.ebridge.smpp.server.simulator.SmppServerSimulator;

import java.io.PrintStream;

public class ListClientsCommand extends BasicCommand {

    public ListClientsCommand( SmppServerSimulator smppServerSimulator ) {
        this.smppServerSimulator = smppServerSimulator;
    }

    private SmppServerSimulator smppServerSimulator;

    public void exec(String args, PrintStream out, PrintStream err) throws Exception {

        out.println("Connected Clients ... ");
        smppServerSimulator.listClients( out );
    }
}
