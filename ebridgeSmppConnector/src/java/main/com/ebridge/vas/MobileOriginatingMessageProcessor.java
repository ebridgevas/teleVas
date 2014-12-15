//package com.ebridge.vas;
//
//import com.ebridge.vas.menu.impl.JsonMenuDriver;
//import com.ebridge.vas.menu.MenuDriver;
//import com.ebridge.vas.util.ConfigurationService;
//import com.ebridge.vas.util.JsonConfigurationService;
//
///**
// * @author david@tekeshe.com
// */
//public class MobileOriginatingMessageProcessor {
//
//    private ConfigurationService configurationService;
//
//    private MenuDriver menuDriver;
//
//    private String menuConfigFilename;
//    private String shortCode;
//
//    public MobileOriginatingMessageProcessor(
//                      String vasConfigFilename,
//                      String menuConfigFilename,
//                      String shortCode ) {
//
//        this.menuConfigFilename = menuConfigFilename;
//        this.shortCode = shortCode;
//
//        menuDriver = new JsonMenuDriver(
//                            new JsonConfigurationService()
//                                    .config(vasConfigFilename), /*
//                                        .getShortCodeParameters()
//                                            .get(shortCode), */
//                            menuConfigFilename,
//                            shortCode);
//    }
//
//    /**
//     * Attach a processor to a short code
//     */
//    public void start() {
//
////        Request request = new Request("263733803480", "971", "80 36233 144#");
////        System.out.println( menuDriver.menu( request ).getPayload() );
//    }
//
//    public static void main(String args[]) {
//
//        if (args.length < 3) {
//            System.err.println("Usage: java com.ebridge.com.ebridge.vas.boot.BootStrap <vasConfigFile> <menuConfigFile> <shortCode>");
//            System.exit(1);
//        }
//
//        new MobileOriginatingMessageProcessor(args[ 0 ], args [ 1 ], args[ 2 ]).start();
//    }
//}
