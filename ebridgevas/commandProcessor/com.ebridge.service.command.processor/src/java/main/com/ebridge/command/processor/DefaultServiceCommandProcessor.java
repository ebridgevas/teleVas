package com.ebridge.command.processor;

import com.ebridge.commons.model.ServiceCommand;
import com.ebridge.commons.model.ServiceResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.concurrent.atomic.AtomicReference;

/**
 * This class is used as a proxy to defer object creation from shape provider
 * bundles and also as a placeholder shape when previously used shapes are no
 * longer available. These two purposes are actually orthogonal, but were
 * combined into a single class to reduce the number of classes in the
 * application. The proxy-related functionality is introduced as a way to lazily
 * create shape objects in an effort to improve performance; this level of
 * indirection could be removed if eager creation of objects is not a concern.
 * Since this application uses the service-based extension approach, lazy shape
 * creation will only come into effect if service providers register service
 * factories instead of directly registering <tt>SimpleShape</tt> or if they use
 * a technology like Declarative Services or iPOJO to register services. Since
 * the example providers register services instances directly there is no
 * laziness in the example, but the proxy approach is still used to demonstrate
 * how to make laziness possible and to keep it similar to the extender-based
 * approach.
 *
 * @author david@tekeshe.com
 *
 **/
public class DefaultServiceCommandProcessor implements ServiceCommandProcessor {

    private AtomicReference<ServiceCommandProcessor> processorReference = new AtomicReference<>();


    public DefaultServiceCommandProcessor() {
    }

    /**
     * This constructs a proxy shape that lazily gets the value added service.
     *
     * @param processor The service reference of the service.
     **/
    public DefaultServiceCommandProcessor( ServiceCommandProcessor processor ) {
        processorReference.set( processor );
    }

    /**
     * This method tells the proxy to dispose of its service object; this is
     * called when the underlying service goes away.
     **/
    public void dispose() {

        processorReference.set( null );
    }

    public void setServiceCommandProcessor(ServiceCommandProcessor processor ) {

        System.out.println(" #################### Set Service Command Processor");
        processorReference.set( processor );
    }

    /**
     *
     * Implements the <tt>ValueAddedService</tt> interface method. When acting as a
     * proxy, this method gets the value added service and then uses it to process
     * the service command.
     *
     * When acting as a placeholder shape, this method returns a 'service not available' message.
     *
     * @param serviceCommand
     * @return
     */
    @Override
    public ServiceResponse process(ServiceCommand serviceCommand) {

        ServiceCommandProcessor processor = processorReference.get();

        if ( processor != null ) {
            return processor.process( serviceCommand );
        }

//        return Response.clone( request, "Service not available", null, SessionState.TERMINATE );
        return new ServiceResponse("Service not available. Please try later");
    }
}
