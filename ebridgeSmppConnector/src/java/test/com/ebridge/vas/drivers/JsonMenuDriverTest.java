//package com.ebridge.vas.drivers;
//
//import com.ebridge.vas.menu.MenuDriver;
//import com.ebridge.vas.menu.impl.JsonMenuDriver;
//import com.ebridge.vas.util.JsonConfigurationService;
//import org.junit.Before;
//
///**
// * @author david@tekeshe.com
// */
//public class JsonMenuDriverTest {
//
//    private MenuDriver menuDriver;
//
//    @Before
//    public void setup() {
//
//        menuDriver = new JsonMenuDriver(
//
//                new JsonConfigurationService()
//                        .config(System.getProperty("vasConfigFilename")),
//                System.getProperty("menuConfigFilename"),
//                System.getProperty("shortCode") );
//    }
//
//    public void testBalanceEnquiryMenus() {
//
//        // balance enquiry message set
//
//        String mosm1 = "80 33495 971#";
//        String msmt1 = "72 33495 30000 0 Welcome to Telecel DATA bundles.\n Please select either 1 or 2\n1. Balance enquiry\n2. Buy my DATA  bundle";
//        String mosm2 = "74 33495 0 0 0 0 15 1";
//        String mtsm2 = "81 33495 0 Airtime bal = -0.55usd. Data Bundle= 0.00mb exp on 25/12/2014";
//    }
//}
