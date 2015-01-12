package com.ebridge.smpp.server.simulator.shell;

/**
 * @author david@tekeshe.com
 */
public interface Binding {

    public void start();

    public void stop() throws InterruptedException;
}
