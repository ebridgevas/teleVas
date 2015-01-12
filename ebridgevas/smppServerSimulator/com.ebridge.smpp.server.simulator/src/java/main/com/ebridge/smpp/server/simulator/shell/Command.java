package com.ebridge.smpp.server.simulator.shell;

import java.io.PrintStream;

public interface Command {
  public void exec(String args, PrintStream out, PrintStream err) throws Exception;
}
