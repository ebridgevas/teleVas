package com.ebridge.smpp.server.simulator.shell;

import com.ebridge.smpp.server.simulator.SmppServerSimulator;

import java.io.PrintStream;

public class StartCommand extends BasicCommand {

    private SmppServerSimulator smppServerSimulator;

    public StartCommand(SmppServerSimulator smppServerSimulator) {
        this.smppServerSimulator = smppServerSimulator;
    }

    public void exec(String args, PrintStream out, PrintStream err) throws Exception {

        out.print("Starting Smpp Server on port : " + args + " ... ");

        Integer port = null;

        try {
            port = Integer.parseInt( args.trim() );
        } catch(Exception e ) {
            out.println("[ Invalid port ]" );
            return;
        }

        smppServerSimulator.start( port, out, err );
    }
}
