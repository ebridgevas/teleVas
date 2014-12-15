//package com.ebridge.vas.drivers.util.impl;
//
//import com.ebridge.vas.Request;
//import com.ebridge.vas.parsers.SessionStartIndicator;
//import com.ebridge.vas.parsers.halys.PayloadBasedSessionStartIndicator;
//import com.ebridge.vas.util.JsonConfigurationService;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.assertFalse;
//
///**
// * @author david@tekeshe.com
// */
//public class PayloadBasedSessionStartIndicatorTest {
//
//    private SessionStartIndicator sessionStartIndicator;
//
//    @Before
//    public void setup() {
//
//        sessionStartIndicator
//                = new PayloadBasedSessionStartIndicator(
//                    new JsonConfigurationService()
//                        .config( System.getProperty("vasConfigFilename"))
//                            .getShortCodeParameters().get("971"));
//    }
//
//    @Test
//    public void balanceEnquirySessionStartTest() {
//
//        // balance enquiry message set
//
//        String mosm1 = "80 33495 971#";
//        String msmt1 = "72 33495 30000 0 Welcome to Telecel DATA bundles.\n Please select either 1 or 2\n1. Balance enquiry\n2. Buy my DATA  bundle";
//        String mosm2 = "74 33495 0 0 0 0 15 1";
//        String mtsm2 = "81 33495 0 Airtime bal = -0.55usd. Data Bundle= 0.00mb exp on 25/12/2014";
//
//        // 80 33495 971# - is a session start message
//        assertTrue( sessionStartIndicator.isSessionStart(new Request("263733803480", "971", mosm1)));
//
//        // 74 33495 0 0 0 0 15 1 - is not a session start message
//        assertFalse( sessionStartIndicator.isSessionStart(new Request("263733803480", "971", mosm2)));
//    }
//}
