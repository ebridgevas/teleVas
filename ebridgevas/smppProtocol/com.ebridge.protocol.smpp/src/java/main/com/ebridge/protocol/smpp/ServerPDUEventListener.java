/*
 * @author david@tekeshe.com
 *
 */
package com.ebridge.protocol.smpp;

import java.util.EventListener;

public interface ServerPDUEventListener extends EventListener {

    public abstract void handleEvent(ServerPDUEvent event);
}
