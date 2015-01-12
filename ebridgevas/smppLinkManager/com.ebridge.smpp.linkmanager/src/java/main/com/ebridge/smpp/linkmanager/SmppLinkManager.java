package com.ebridge.smpp.linkmanager;

import static com.ebridge.commons.util.JsonUtils.config;

import com.ebridge.command.processor.DefaultServiceCommandProcessor;
import com.ebridge.command.processor.ServiceCommandProcessor;
import org.apache.felix.ipojo.annotations.*;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

import java.io.FileNotFoundException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * @author david@tekeshe.com
 */
@Component(immediate = true)
public class SmppLinkManager {

    private final DefaultServiceCommandProcessor delegate = new DefaultServiceCommandProcessor(null);

    private final static String configFilename = System.getProperty("ebridge.conf.path") + "/" + "service.config";
    private final static String logPath = System.getProperty("ebridge.log.path");

    private Map<String, Binding> smppLinks;

    @Requires(optional = true)
    private LogService logger;

    private BundleContext context;

    public SmppLinkManager() {
    }

    public SmppLinkManager(BundleContext context) {

        this.context = context;
        logger.log(LogService.LOG_INFO, "Smpp Listener Created " + this);
        System.out.println("Smpp Listener Created " + this);
    }

    @Validate
    protected void activate() {

        logger.log(LogService.LOG_INFO, "Smpp Listener Started.");
        // bind to smpp server - timed
        System.out.println("Smpp Listener Started.");

        smppLinks = new HashMap<>();

        try {
            Map<String, Map<String, String>> linkConfigs = config(configFilename).get("smpp-links");
            for (String shortCode : linkConfigs.keySet() ) {
                SmppLink smppLink = new SmppLink(linkConfigs.get(shortCode), delegate, logPath);
                smppLink.start();
                smppLinks.put(shortCode, smppLink );
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Invalidate
    protected void deactivate() throws Exception {

        if ( smppLinks != null ) {

            for (Binding smppLink : smppLinks.values()) {
                smppLink.stop();
            }
        }
    }

    @Bind(aggregate = false, id = "serviceCommandProcessor")
    public void bindServiceCommandProcessor(ServiceCommandProcessor processor, Dictionary attributes) {
        System.out.println("ServiceCommandProcessor bind ...");
        delegate.setServiceCommandProcessor(processor);
    }

    @Unbind(aggregate = false, id = "serviceCommandProcessor")
    public void unbindServiceCommandProcessor(ServiceCommandProcessor processor, Dictionary attributes) {

        System.out.println("ServiceCommandProcessor un bind ...");
        delegate.dispose();
    }
}
