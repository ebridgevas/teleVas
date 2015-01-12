package com.ebridge.command.processor;

import com.ebridge.commons.model.ServiceCommand;
import com.ebridge.commons.model.ServiceResponse;

/**
 * @author david@tekeshe.com
 */
public interface ServiceCommandProcessor {

    ServiceResponse process( ServiceCommand serviceCommand );
}
