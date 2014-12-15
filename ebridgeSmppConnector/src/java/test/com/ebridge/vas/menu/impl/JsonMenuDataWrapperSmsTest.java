package com.ebridge.vas.menu.impl;

import com.ebridge.smpp.pdu.DeliverSM;
import com.ebridge.smpp.pdu.SubmitSM;
import com.ebridge.smpp.pdu.WrongLengthOfStringException;
import com.ebridge.vas.Request;
import com.ebridge.vas.Response;
import com.ebridge.vas.dto.ConfigDTO;
import com.ebridge.vas.dto.MenuItemDTO;
import com.ebridge.vas.dto.ShortCodeConfigDTO;
import com.ebridge.vas.factory.UssdPDUFactory;
import com.ebridge.vas.factory.halys.HalysUssdPDUFactory;
import com.ebridge.vas.menu.JsonMenuDataWrapper;
import com.ebridge.vas.menu.MenuDriver;
import com.ebridge.vas.model.TreeNode;
import com.ebridge.vas.parsers.PayloadParser;
import com.ebridge.vas.parsers.halys.util.PayloadType;
import com.ebridge.vas.util.JsonConfigurationService;
import com.ebridge.vas.util.NodeTypeNotFoundException;
import com.ebridge.vas.util.PDUParseException;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ebridge.vas.util.SmppUtils.deliverSM;
import static com.ebridge.vas.util.Utils.submitSM;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.junit.Assert.assertThat;

/**
* @author david@tekeshe.com
*/
public class JsonMenuDataWrapperSmsTest {

    private JsonMenuDataWrapper menuTreeWrapper;

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

        menuTreeWrapper = new JsonMenuDataWrapper(menuConfigFilename, "26373");

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

        factory = new HalysUssdPDUFactory();
    }

//    @Test
    public void testSmsBalanceEnquiry() throws WrongLengthOfStringException, PDUParseException {

        // main menu

        DeliverSM input = deliverSM("263733803480", "33073", "bal");

        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        SubmitSM expected = submitSM("33073",
                "263733803480",
                "Airtime bal = 11.49usd. Exp on " + date + ". Data Bundle = 11.49mb. Exp on " + date + ".");

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
                "33803480",
                "You have bought the 76.00mb bundle. Your main balance is now 8.49usd. Your data balance is 76.00mb exp on 02/01/2015");

        request = parser.parse(input);
        assertEquals(request.getSourceId(), input.getSourceAddr().getAddress());
        assertEquals(PayloadType.USSD_ANSWER, request.getPayloadType() );
        response = menuTreeWrapper.process( request);

        assertThat( slice( response.getShortMessage(), 10 ),
                    equalToIgnoringWhiteSpace(slice(expected.getShortMessage(), 10)));
    }

    @Test
    public void testSmsAirtimeTransfer() {

        String payload = "123#263733803480";

        String pattern = "^(\\d{1,3})[#](\\d{1,3})\\d{9}";

        payload = payload.replaceAll("[^0-9#.]", "");

        assertTrue(payload.matches( pattern ) );
    }

    @Test
    public void testSmsVoucherRechargeForOwnPhone() {

        String payload = "1234567890121111";

        String pattern = "^(\\d{12,16})|((\\d{12,16})[#](\\d{1,3})\\d{9})";

        payload = payload.replaceAll("[^0-9#.]", "");

        assertTrue(payload.matches( pattern ) );
    }

    @Test
    public void testSmsVoucherRechargeForSomeonePhone() {

        String payload = "1234567890121111#263733803480";

        String pattern = "^(\\d{12,16})|((\\d{12,16})[#](\\d{1,3})\\d{9})";

        payload = payload.replaceAll("[^0-9#.]", "");

        assertTrue(payload.matches( pattern ) );
    }

    protected String slice(String input, Integer offset) {
        return input.substring(0, (input.length() - offset));
    }
}