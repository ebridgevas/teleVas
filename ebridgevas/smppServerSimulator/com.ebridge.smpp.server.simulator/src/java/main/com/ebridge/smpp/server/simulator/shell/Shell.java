package com.ebridge.smpp.server.simulator.shell;

import com.ebridge.smpp.server.simulator.shell.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class Shell implements Runnable {
    private final Command m_command;
    private final BufferedReader in;
    private final PrintStream out;
    private final PrintStream err;

    public Shell(Command command, BufferedReader in, PrintStream out, PrintStream err) {
        m_command = command;
        this.in = in;
        this.out = out;
        this.err = err;
    }

    public void run() {

        out.println("\n\n");

        while ( ! Thread.currentThread().isInterrupted() ) {
            out.print("ebridge-smpp-server > ");

            String cmdLine;
            try {
                cmdLine = in.readLine();
            } catch (IOException ex) {
                if (!Thread.currentThread().isInterrupted()) {
                    ex.printStackTrace(err);
                    err.println("Unable to read from stdin - exiting now");
                }
                return;
            }

            if (cmdLine == null) {
                out.println("ebridge smpp server closing ...");
                return;
            }

            try {
                m_command.exec(cmdLine, out, err);
            } catch (Throwable t) {
                t.printStackTrace(err);
            }
        }
    }
}
