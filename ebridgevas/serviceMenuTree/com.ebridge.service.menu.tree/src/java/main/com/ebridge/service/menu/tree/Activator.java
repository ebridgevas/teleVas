package com.ebridge.service.menu.tree;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The activator creates the main application <tt>ServiceMenuTree</tt>
 * and starts tracking <tt>ServiceCommandProcessors</tt> extensions.
 * All activity is performed on the Swing event thread to avoid synchronization and repainting issues.
 * Closing the application window will result in <tt>Bundle.stop()</tt> being called on the system bundle, which
 * will cause the framework to shutdown and the JVM to exit.
 *
 * The activator of the host application bundle. The activator creates the main
 * application <tt>JFrame</tt> and starts tracking <tt>SimpleShape</tt>
 * services. All activity is performed on the Swing event thread to avoid
 * synchronization and repainting issues. Closing the application window will
 * result in <tt>Bundle.stop()</tt> being called on the system bundle, which
 * will cause the framework to shutdown and the JVM to exit.
 *
 **/
public class Activator implements BundleActivator, Runnable {

  private BundleContext context = null;
  private ServiceMenuTree serviceMenuTree = null;
  private ValueAddedServiceTracker valueAddedServiceTracker = null;

  /**
   * Displays the applications window and starts extension tracking; everything
   * is done on the Swing event thread to avoid synchronization and repainting
   * issues.
   * 
   * @param context The context of the bundle.
   **/
  public void start(BundleContext context) {

    this.context = context;
//    if (SwingUtilities.isEventDispatchThread()) {
//      run();
//    } else {
//      try {
//        SwingUtilities.invokeAndWait(this);
//      } catch (Exception ex) {
//        ex.printStackTrace();
//      }
//    }
  }

  /**
   * Stops extension tracking and disposes of the application window.
   * 
   * @param context The context of the bundle.
   **/
  public void stop(BundleContext context) {

    valueAddedServiceTracker.close();
//    final PaintFrame frame = m_frame;
//    SwingUtilities.invokeLater(new Runnable() {
//      public void run() {
//        frame.setVisible(false);
//        frame.dispose();
//      }
//    });
  }

  /**
   * This method actually performs the creation of the application window.
   * It is intended to be called by the Swing event thread and should not be called directly.
   **/
  public void run() {

    serviceMenuTree = new ServiceMenuTree();

//    m_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//    m_frame.addWindowListener(new WindowAdapter() {
//      public void windowClosing(WindowEvent evt) {
//        try {
//          context.getBundle(0).stop();
//        } catch (BundleException ex) {
//          ex.printStackTrace();
//        }
//      }
//    });

//    m_frame.setVisible(true);

    valueAddedServiceTracker = new ValueAddedServiceTracker(context, serviceMenuTree );
    valueAddedServiceTracker.open();
  }
}
