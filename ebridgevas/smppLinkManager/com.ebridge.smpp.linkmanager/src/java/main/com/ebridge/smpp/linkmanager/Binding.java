package com.ebridge.smpp.linkmanager;

/**
 * @author david@tekeshe.com
 */
public interface Binding {

    public void start();

    public void stop() throws InterruptedException;
}
