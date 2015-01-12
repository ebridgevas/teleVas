package com.ebridge.service.menu.tree;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.SynchronousBundleListener;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract for classes that tracks active bundles.
 * Must be given a bundle context upon creation, which it uses to listen for bundle events.
 * Must be opened to track objects and closed when it is no longer needed.
 * <p/>
 * Subclasses must implement the <tt>addedBundle()</tt> and <tt>removedBundle()</tt> methods,
 * which can be used to perform some custom action upon the activation or
 * deactivation of bundles.
 */
public abstract class BundleTracker {

    final Set m_bundleSet = new HashSet();
    final BundleContext m_context;
    final SynchronousBundleListener m_listener;
    boolean m_open;

    public BundleTracker( BundleContext context ) {

        m_context = context;
        m_listener = new SynchronousBundleListener() {

            public void bundleChanged(BundleEvent evt) {

                synchronized (BundleTracker.this) {

                    if (!m_open) {
                        return;
                    }

                    if (evt.getType() == BundleEvent.STARTED) {

                        if ( !m_bundleSet.contains(evt.getBundle()) ) {

                            m_bundleSet.add(evt.getBundle());
                            addedBundle(evt.getBundle());
                        }
                    } else if (evt.getType() == BundleEvent.STOPPED) {

                        if (m_bundleSet.contains(evt.getBundle())) {
                            m_bundleSet.remove(evt.getBundle());
                            removedBundle(evt.getBundle());
                        }
                    }
                }
            }
        };
    }

    /**
     * Returns the current set of active bundles.
     *
     * @return The current set of active bundles.
     */
    public synchronized Bundle[] getBundles() {

        return (Bundle[]) m_bundleSet.toArray(new Bundle[m_bundleSet.size()]);
    }

    /**
     * Call this method to start the tracking of active bundles.
     */
    public synchronized void open() {

        if (!m_open) {

            m_open = true;

            m_context.addBundleListener(m_listener);

            Bundle[] bundles = m_context.getBundles();

            for (int i = 0; i < bundles.length; i++) {

                if (bundles[i].getState() == Bundle.ACTIVE) {
                    m_bundleSet.add(bundles[i]);
                    addedBundle(bundles[i]);
                }
            }
        }
    }

    /**
     * Call this method to stop the tracking of active bundles.
     */
    public synchronized void close() {

        if (m_open) {
            m_open = false;

            m_context.removeBundleListener(m_listener);

            Bundle[] bundles = (Bundle[]) m_bundleSet.toArray(new Bundle[m_bundleSet.size()]);
            for (int i = 0; i < bundles.length; i++) {
                if (m_bundleSet.remove(bundles[i])) {
                    removedBundle(bundles[i]);
                }
            }
        }
    }

    /**
     * Subclasses must implement this method; it can be used to perform actions
     * upon the activation of a bundle.
     *
     * @param bundle The bundle being added to the active set.
     */
    protected abstract void addedBundle(Bundle bundle);

    /**
     * Subclasses must implement this method; it can be used to perform actions
     * upon the deactivation of a bundle.
     *
     * @param bundle The bundle being removed from the active set.
     */
    protected abstract void removedBundle(Bundle bundle);
}
