package com.ebridge.vas.menu.impl;

import static com.ebridge.vas.util.SmppUtils.deliverSM;
import static com.ebridge.vas.util.Utils.submitSM;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.junit.Assert.assertThat;

import com.ebridge.smpp.pdu.DeliverSM;
import com.ebridge.smpp.pdu.SubmitSM;
import com.ebridge.smpp.pdu.WrongLengthOfStringException;
import com.ebridge.vas.Request;
import com.ebridge.vas.Response;
import com.ebridge.vas.dto.ConfigDTO;
import com.ebridge.vas.dto.MenuItemDTO;
import com.ebridge.vas.dto.ShortCodeConfigDTO;
import com.ebridge.vas.factory.UssdPDUFactory;
import com.ebridge.vas.menu.JsonMenuDataWrapper;
import com.ebridge.vas.menu.MenuDriver;
import com.ebridge.vas.model.TreeNode;
import com.ebridge.vas.parsers.PayloadParser;
import com.ebridge.vas.parsers.halys.HalysUssdPDUParser;
import com.ebridge.vas.factory.halys.HalysUssdPDUFactory;
import com.ebridge.vas.parsers.halys.util.PayloadType;
import com.ebridge.vas.util.JsonConfigurationService;
import com.ebridge.vas.util.NodeTypeNotFoundException;
import com.ebridge.vas.util.PDUParseException;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* @author david@tekeshe.com
*/
public class JsonMenuDriverTest {

    private TreeNode<MenuItemDTO> menuTree;
    private JsonMenuDataWrapper menuTreeWrapper;

    private MenuDriver menuDriver;

    private PayloadParser parser;
    private UssdPDUFactory factory;

    private ConfigDTO vasConfigDTO;

    @Before
    public void setup() throws NodeTypeNotFoundException {

        String shortCode = System.getProperty("shortCode");
        String vasConfigFilename = System.getProperty("vasConfigFilename");
        String menuConfigFilename = System.getProperty("menuConfigFilename");

        JsonConfigurationService configurationService
                = new JsonConfigurationService();

        vasConfigDTO = configurationService.config(vasConfigFilename);
//                        .getShortCodeParameters()
//                            .get(shortCode);

        menuTreeWrapper = new JsonMenuDataWrapper(menuConfigFilename, "26373");
        menuTree = menuTreeWrapper.load();

        ShortCodeConfigDTO shortCodeParameters
                = vasConfigDTO.getShortCodeParameters().get(shortCode);

        try {
            Constructor constructor =
                    Class.forName(shortCodeParameters.getPayloadParser())
                            .getConstructor(ShortCodeConfigDTO.class);
            parser = (PayloadParser) constructor.newInstance(shortCodeParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        menuTreeWrapper.debugString( menuTree );

//        System.out.println(menuTreeWrapper.debugString( menuTree ) );

        factory = new HalysUssdPDUFactory();
    }

//    @Test
    public void testUssdBalanceEnquiry() throws WrongLengthOfStringException, PDUParseException {

        // main menu

        DeliverSM input = deliverSM("263733803480", "971", "80 33495 971#");

        SubmitSM expected = submitSM("971",
                "263733803480",
                "72 33495 30000 0 " +
                        "Welcome to Telecel DATA bundles.\n" +
                        "Please select either 1 or 2\n" +
                        "1. Balance enquiry\n" +
                        "2. Buy my DATA bundle");


        Request request = parser.parse(input);
        assertEquals(PayloadType.SESSION_START, request.getPayloadType() );
        assertEquals(request.getSourceId(), input.getSourceAddr().getAddress());

        Response response = menuTreeWrapper.process(request);

        SubmitSM actual = factory.submitSM(response);
        assertThat(actual.getShortMessage(), equalToIgnoringWhiteSpace(expected.getShortMessage()));

        // balance enquiry

        input = deliverSM("263733803480", "971", "74 33495 0 0 0 0 15 1");

        String date = new SimpleDateFormat("12/12/2014").format(new Date());

        expected = submitSM("971",
                "263733803480",
                "81 33495 0 " +
                        "Airtime bal = 11.49usd. Exp on " + date + ". Data Bundle = 11.49mb. Exp on " + date + ".");

        request = parser.parse(input);
        response = menuTreeWrapper.process(request);
        actual = factory.submitSM(response);
        assertThat(actual.getShortMessage(), equalToIgnoringWhiteSpace(expected.getShortMessage()));
    }

    @Test
    public void testUssdDataBundlePurchase() throws WrongLengthOfStringException, PDUParseException {

        // main menu

        DeliverSM input = deliverSM("263733803480", "971", "80 33495 971#");

        SubmitSM expected = submitSM("971",
                "263733803480",
                "72 33495 30000 0 " +
                        "Welcome to Telecel DATA bundles.\n" +
                        "Please select either 1 or 2\n" +
                        "1. Balance enquiry\n" +
                        "2. Buy my DATA bundle");

        Request request = parser.parse(input);
        assertEquals( request.getSourceId(), input.getSourceAddr().getAddress() );
        Response response = menuTreeWrapper.process(request);
        SubmitSM actual = factory.submitSM(response);
        assertEquals(expected.getDestAddr().getAddress(), actual.getDestAddr().getAddress());
        assertThat(expected.getShortMessage(), equalToIgnoringWhiteSpace(actual.getShortMessage()));

        // data bundle price listing

        input = deliverSM("263733803480", "971", "74 32802 0 0 0 0 15 2");
        expected = submitSM("971",
                "263733803480",
                "72 32802 30000 0 Select bundle 1 to 8 " +
                        "1. 50c for 4.8MB\n" +
                        "2. $1  for 9.5MB\n" +
                        "3. $3  for 76MB\n" +
                        "4. $5  for 142.5MB\n" +
                        "5. $10 for 304MB\n" +
                        "6. $20 for 800MB\n" +
                        "7. $45 for 2000MB\n" +
                        "8. $75 for 4000MB\n");

        request = parser.parse(input);
        response = menuTreeWrapper.process( request );
        actual = factory.submitSM(response);
        assertThat(actual.getShortMessage(), equalToIgnoringWhiteSpace(expected.getShortMessage()));

        // bundle purchase
        input = deliverSM("263733803480", "971", "74 32802 0 0 0 0 15 4");

        expected = submitSM("971",
                "263733803480",
                "81 32802 0 You have bought the 76.00mb bundle. Your main balance is now 6.49usd. Your data balance is 142.50mb exp on 02/02/2015");

        request = parser.parse(input);
        response = menuTreeWrapper.process( request );
        actual = factory.submitSM(response);
        assertThat( slice(actual.getShortMessage(), 10), equalToIgnoringWhiteSpace(slice ( expected.getShortMessage(), 10 ) ));
    }

              /* User Sessions. */
//        Map<String, UserSession> userSessions = new HashMap<String, UserSession>();
    //Utils.readUserSessionFromFile(ussdConfig.getSmppIPAdress());
//
//
//         new ServerPDUEventListenerImpl(null, userSessions, BigDecimal.ONE);
//

//        System.out.println("Call 1");
//        Request request = new Request("263733803480", "971", "80 36233 144#");
//        System.out.println(driver.menu( request ).getPayload());


//        // Call 2
//        System.out.println("######## Call 2");
//        dto = new PduDto();
//        dto.setSourceId("263733803480");
//        dto.setDestinationId("971");
//        dto.setShortMessage("74 36233 0 0 0 0 15 2");
//        for ( MTSM mtsm : menuFactory.process(dto)) {
//            System.out.println(mtsm.getShortMessage());
//        }
//
////        // Call 3
//        System.out.println("######## Call 3");
//        dto = new PduDto();
//        dto.setSourceId("263733803480");
//        dto.setDestinationId("971");
//        dto.setShortMessage("74 36233 0 0 0 0 15 4");
//        dto.setUuid("" + System.currentTimeMillis());
//        dto.setChannel("USSD");
//
//        for ( MTSM mtsm : menuFactory.process(dto)) {
//            System.out.println(mtsm.getShortMessage());
//        }

//    @Test
    public void testSmsBalanceEnquiry() throws WrongLengthOfStringException, PDUParseException {

        // main menu

        DeliverSM input = deliverSM("263733803480", "33073", "bal");

        SubmitSM expected = submitSM("33073",
                "263733803480",
                "Airtime bal = 11.49usd. Exp on 10/12/2014. Data Bundle = 11.49mb. Exp on 10/12/2014.");

        Request request = parser.parse(input);
        assertEquals(request.getSourceId(), input.getSourceAddr().getAddress());
        assertEquals(PayloadType.SESSION_START, request.getPayloadType() );
        Response response = menuTreeWrapper.process( request);

        assertThat(response.getShortMessage(), equalToIgnoringWhiteSpace(expected.getShortMessage()));
    }


//    @Test
    public void testSmsDataBundleOfferListing() throws WrongLengthOfStringException, PDUParseException {

        DeliverSM input = deliverSM("263733803480", "33073", "bun");

        SubmitSM expected = submitSM("33073",
                "263733803480",
                "Select bundle 1 to 8\n" +
                "1. 50c for 4.8MB\n" +
                "2. $1 for 9.5MB\n" +
                "3. $3 for 76MB\n" +
                "4. $5 for 142.5MB\n" +
                "5. $10 for 304MB\n" +
                "6. $20 for 800MB\n" +
                "7. $45 for 2000MB\n" +
                "8. $75 for 4000MB");

        Request request = parser.parse(input);
        assertEquals(request.getSourceId(), input.getSourceAddr().getAddress());
        assertEquals(PayloadType.SESSION_START, request.getPayloadType() );
        Response response = menuTreeWrapper.process( request);

        assertThat(response.getShortMessage(), equalToIgnoringWhiteSpace(expected.getShortMessage()));
    }

//    @Test
    public void testSmsDataBundlePurchase() throws WrongLengthOfStringException, PDUParseException {

        DeliverSM input = deliverSM("263733803480", "33073", "bun");

        SubmitSM expected = submitSM("33073",
                "263733803480",
                "Select bundle 1 to 8\n" +
                        "1. 50c for 4.8MB\n" +
                        "2. $1 for 9.5MB\n" +
                        "3. $3 for 76MB\n" +
                        "4. $5 for 142.5MB\n" +
                        "5. $10 for 304MB\n" +
                        "6. $20 for 800MB\n" +
                        "7. $45 for 2000MB\n" +
                        "8. $75 for 4000MB");

        Request request = parser.parse(input);
        assertEquals(request.getSourceId(), input.getSourceAddr().getAddress());
        assertEquals(PayloadType.SESSION_START, request.getPayloadType() );
        Response response = menuTreeWrapper.process( request);

        assertThat(response.getShortMessage(), equalToIgnoringWhiteSpace(expected.getShortMessage()));

        input = deliverSM("263733803480", "33073", "3");

        expected = submitSM("33073",
                "263733803480",
                "You have bought the 76.00mb bundle. Your main balance is now 8.49usd. Your data balance is 76.00mb exp on 10/01/2015");

        request = parser.parse(input);
        assertEquals(request.getSourceId(), input.getSourceAddr().getAddress());
        assertEquals(PayloadType.USSD_ANSWER, request.getPayloadType() );
        response = menuTreeWrapper.process( request);

        assertThat(response.getShortMessage(), equalToIgnoringWhiteSpace(expected.getShortMessage()));
    }

    protected String slice(String input, Integer offset) {
        return input.substring(0, (input.length() - offset));
    }
}