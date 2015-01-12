package com.ebridge.smpp.server.simulator.shell;

import com.ebridge.smpp.server.simulator.SmppServerSimulator;

import java.io.PrintStream;

public class SendMessageCommand extends BasicCommand {

    private SmppServerSimulator smppServerSimulator;

    public SendMessageCommand(SmppServerSimulator smppServerSimulator) {
        this.smppServerSimulator = smppServerSimulator;
    }

    public void exec(String args, PrintStream out, PrintStream err) throws Exception {

        String[] tokens = args.split(" ");

        if ( tokens.length < 3) {
            out.println("usage: send <sourceId> <destinationId> <message>");
            return;
        }

        String sourceId = tokens[1];
        String destinationId = tokens[2];
        String message = join(tokens, 3);

        out.println("msg to: " + destinationId + ", from : " + sourceId + " : " + message );
        smppServerSimulator.sendMessage( out, sourceId, destinationId, message );
    }

    protected String join(String[] tokens, Integer fromIdx) {
        StringBuilder sb = new StringBuilder();
        for ( int idx = fromIdx; idx < tokens.length; ++idx ) {
            sb.append(tokens[idx] + " ");
        }

        return sb.toString().trim();
    }
}
