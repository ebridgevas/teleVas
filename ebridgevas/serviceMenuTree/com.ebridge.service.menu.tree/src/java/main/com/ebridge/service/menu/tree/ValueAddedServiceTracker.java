package com.ebridge.service.menu.tree;

import java.util.Dictionary;

import com.ebridge.vas.service.ValueAddedService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import javax.swing.*;

/**
 * A tracker for <tt>ServiceCommandProcessor</tt> extensions.
 * Responsible for listening for <tt>ServiceCommandProcessor</tt> extensions
 * and informing the MenuTree about the availability of service command processors.

 * Extends the <tt>ServiceTracker</tt> to create a tracker for
 * <tt>SimpleShape</tt> services. The tracker is responsible for listener for
 * the arrival/departure of <tt>SimpleShape</tt> services and informing the
 * application about the availability of shapes. This tracker forces all
 * notifications to be processed on the Swing event thread to avoid
 * synchronization and redraw issues.
 **/
public class ValueAddedServiceTracker extends ServiceTracker {

    private static final int ADDED = 1;
    private static final int MODIFIED = 2;
    private static final int REMOVED = 3;
    private BundleContext context;

    // The service menu tree to notify.
    private ServiceMenuTree serviceMenuTree;

    /**
     * Constructs a tracker that uses the specified bundle context to track
     * services and notifies the specified application object about changes.
     *
     * @param context The bundle context to be used by the tracker.
     * @param serviceMenuTree The application object to notify about service changes.
     **/
    public ValueAddedServiceTracker(BundleContext context, ServiceMenuTree serviceMenuTree) {

        super(context, ValueAddedService.class.getName(), null);
        this.context = context;
        this.serviceMenuTree = serviceMenuTree;
    }

    /**
     * Overrides the <tt>ServiceTracker</tt> functionality to inform the
     * application object about the added service.
     *
     * @param serviceReference The service reference of the added service.
     * @return The service object to be used by the tracker.
     **/
    public Object addingService( ServiceReference serviceReference ) {

        ValueAddedService valueAddedService = new DefaultValueAddedService(context, serviceReference);
        processValueAddedServiceRequestOnEventThread(ADDED, serviceReference, valueAddedService);
        return valueAddedService;
    }

    /**
     * Overrides the <tt>ServiceTracker</tt> functionality to inform the
     * application object about the modified service.
     *
     * @param serviceReference The service reference of the modified service.
     * @param service The service object of the modified service.
     **/
    public void modifiedService( ServiceReference serviceReference,
                                 Object service ) {

        processValueAddedServiceRequestOnEventThread(
                        MODIFIED,
                        serviceReference,
                        (ValueAddedService) service);
    }

    /**
     * Overrides the <tt>ServiceTracker</tt> functionality to inform the
     * application object about the removed service.
     *
     * @param serviceReference The service reference of the removed service.
     * @param service The service object of the removed service.
     **/
    public void removedService(ServiceReference serviceReference, Object service ) {

        processValueAddedServiceRequestOnEventThread(REMOVED, serviceReference, (ValueAddedService) service);
        ((DefaultValueAddedService) service).dispose();
    }

    /**
     * Processes a received service notification from the <tt>ServiceTracker</tt>,
     * forcing the processing of the notification onto the Swing event thread if
     * it is not already on it.
     *
     * @param action The type of action associated with the notification.
     * @param serviceReference The service reference of the corresponding service.
     * @param valueAddedService The service object of the corresponding service.
     **/
    private void processValueAddedServiceRequestOnEventThread(
                        int action,
                        ServiceReference serviceReference,
                        ValueAddedService valueAddedService ) {

        if ((context.getBundle(0).getState() & (Bundle.STARTING | Bundle.ACTIVE)) == 0) {
            return;
        }

        try {
//            if (SwingUtilities.isEventDispatchThread()) {
                processValueAddedServiceRequest(action, serviceReference, valueAddedService);
//            } else {
//                SwingUtilities.invokeAndWait(new ShapeRunnable(action, ref, shape));
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Actually performs the processing of the service notification. Invokes the
     * appropriate callback method on the application object depending on the
     * action type of the notification.
     *
     * @param action The type of action associated with the notification.
     * @param serviceReference The service reference of the corresponding service.
     * @param valueAddedService The service object of the corresponding service.
     **/
    private void processValueAddedServiceRequest(   int action,
                                                    ServiceReference serviceReference,
                                                    ValueAddedService valueAddedService ) {

        String name = (String) serviceReference.getProperty( ValueAddedService.NAME_PROPERTY );

        switch (action) {
            case MODIFIED:
//                m_frame.removeShape(name);
                // Purposely let this fall through to the 'add' case to
                // reload the service.

            case ADDED:
//                Icon icon = (Icon) ref.getProperty(SimpleShape.ICON_PROPERTY);
//                m_frame.addShape(name, icon, shape);
                break;

            case REMOVED:
//                m_frame.removeShape(name);
                break;
        }
    }

//    /**
//     * Simple class used to process service notification handling on the Swing
//     * event thread.
//     **/
//    private class ShapeRunnable implements Runnable {
//        private int m_action;
//        private ServiceReference m_ref;
//        private SimpleShape m_shape;
//
//        /**
//         * Constructs an object with the specified action, service reference, and
//         * service object for processing on the Swing event thread.
//         *
//         * @param action The type of action associated with the notification.
//         * @param ref The service reference of the corresponding service.
//         * @param shape The service object of the corresponding service.
//         **/
//        public ShapeRunnable(int action, ServiceReference ref, SimpleShape shape) {
//            m_action = action;
//            m_ref = ref;
//            m_shape = shape;
//        }
//
//        /**
//         * Calls the <tt>processShape()</tt> method.
//         **/
//        public void run() {
//            processShape(m_action, m_ref, m_shape);
//        }
//    }
}
