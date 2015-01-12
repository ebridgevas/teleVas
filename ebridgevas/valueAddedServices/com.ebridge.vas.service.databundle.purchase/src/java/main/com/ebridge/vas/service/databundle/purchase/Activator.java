package com.ebridge.vas.service.databundle.purchase;

import java.util.Hashtable;

import com.ebridge.vas.service.ValueAddedService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Creates an instance of Data Bundle Purchase Service object
 * and registers it with the service registry along with the service properties
 * indicating the service's name.
 */

public class Activator implements BundleActivator {

    private BundleContext context = null;

    /**
     * Implements the <tt>BundleActivator.start()</tt> method,
     * which registers the data bundle purchase service <tt>ValueAddedService</tt>.
     *
     * @param context The context for the bundle.
     */
    public void start(BundleContext context) {

        this.context = context;

        Hashtable dictionary = new Hashtable();
        dictionary.put(ValueAddedService.NAME_PROPERTY, "DataBundlePurchaseService");

        context.registerService( ValueAddedService.class.getName(),
                                 new DataBundlePurchaseService(),
                                 dictionary );
    }

    /**
     * Implements the <tt>BundleActivator.stop()</tt>
     *
     * @param context The context for the bundle.
     */
    public void stop(BundleContext context) {
    }
}
