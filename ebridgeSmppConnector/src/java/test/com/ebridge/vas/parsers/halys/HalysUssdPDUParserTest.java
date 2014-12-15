//package com.ebridge.vas.parsers.halys;
//
//import static com.ebridge.vas.util.Utils.deliverSM;
//import static com.ebridge.vas.util.Utils.submitSM;
//
//import com.ebridge.smpp.pdu.DeliverSM;
//import com.ebridge.smpp.pdu.SubmitSM;
//import com.ebridge.smpp.pdu.WrongLengthOfStringException;
//import com.ebridge.vas.dto.ConfigDTO;
//import com.ebridge.vas.menu.MenuDriver;
//import com.ebridge.vas.menu.impl.JsonMenuDriver;
//import com.ebridge.vas.factory.halys.HalysUssdPDUFactory;
//import com.ebridge.vas.util.ConfigurationService;
//import com.ebridge.vas.util.JsonConfigurationService;
//import com.ebridge.vas.util.PDUParseException;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import static junit.framework.Assert.assertEquals;
//import static junit.framework.TestCase.assertFalse;
//import static junit.framework.TestCase.assertTrue;
//
///**
// * @author david@tekeshe.com
// */
//public class HalysUssdPDUParserTest {
//
//    private HalysUssdPDUParser parser;
//    private ConfigurationService configurationService;
//    private MenuDriver menuDriver;
//
//    @Before
//    public void setup() {
//
//        String shortCode = System.getProperty("shortCode");
//        String vasConfigFilename = System.getProperty("vasConfigFilename");
//        String menuConfigFilename = System.getProperty("menuConfigFilename");
//
//        configurationService = new JsonConfigurationService();
//        ConfigDTO vasConfigDTO = configurationService.config(vasConfigFilename);
//
//        menuDriver = new JsonMenuDriver( vasConfigDTO, menuConfigFilename, shortCode );
//        parser = new HalysUssdPDUParser();
//
//    }
//
//    @Test
//    public void testSessionStartIndicator()
//                    throws WrongLengthOfStringException, PDUParseException {
//
//        // payloads
//        DeliverSM pdu1 = deliverSM("263733803480", "971", "80 33495 971#");
//        DeliverSM pdu2 = deliverSM("263733803480", "971", "74 33495 0 0 0 0 15 1");
//
//        assertTrue( parser.parse( pdu1 ).isSessionStart() );
//        assertFalse( parser.parse( pdu2 ).isSessionStart() );
//    }
//
//    @Test
//    public void testUssdMainMenu() throws WrongLengthOfStringException, PDUParseException {
//
//        DeliverSM input = deliverSM("263733803480", "971", "80 33495 971#");
//
//        SubmitSM expected = submitSM( "971",
//                                      "263733803480",
//                                      "72 33495 30000 0 " +
//                                              "Welcome to Telecel DATA bundles.\n " +
//                                              "Please select either 1 or 2\n" +
//                                              "1. Balance enquiry\n" +
//                                              "2. Buy my DATA  bundle");
//
//        SubmitSM actual = new HalysUssdPDUFactory()
//                                .submitSM(
//                                        menuDriver.menu( parser.parse(input) ) );
//
//        assertEquals( expected.getDestAddr().getAddress(), actual.getDestAddr().getAddress() );
//        assertEquals( expected.getSourceAddr().getAddress(), actual.getSourceAddr().getAddress() );
//        assertEquals( expected.getShortMessage(), actual.getShortMessage() );
//    }
//
//    public void testUssdBalanceEnquiry() {
//        //        String mosm2 = "74 33495 0 0 0 0 15 1";
//        //        String mtsm2 = "81 33495 0 Airtime bal = -0.55usd. Data Bundle= 0.00mb exp on 25/12/2014";
//    }
//}
