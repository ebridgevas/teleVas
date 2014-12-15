package com.ebridge.vas.parsers.sms;

import com.ebridge.smpp.pdu.DeliverSM;
import com.ebridge.vas.Request;
import com.ebridge.vas.dto.ShortCodeConfigDTO;
import com.ebridge.vas.parsers.PayloadParser;
import com.ebridge.vas.parsers.halys.util.PayloadType;
import com.ebridge.vas.util.PDUParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author david@tekeshe.com
 */
public class SmsPayloadParser implements PayloadParser {

    private ShortCodeConfigDTO shortCodeParameters;
    private List<String> serviceCommands;

    public SmsPayloadParser() {
    }

    public SmsPayloadParser(ShortCodeConfigDTO shortCodeParameters) {
        this.shortCodeParameters = shortCodeParameters;

        serviceCommands = Arrays.asList( shortCodeParameters.getSessionStartIndicator().split(","));
    }

    @Override
    public Request parse(DeliverSM deliverSM) throws PDUParseException {

        return new Request(
                null,
                deliverSM.getSourceAddr().getAddress(),
                deliverSM.getDestAddr().getAddress(),
                deliverSM.getSourceAddr().getAddress(),
                deliverSM.getShortMessage(),
                payloadType( deliverSM.getShortMessage() ) );
    }

    protected PayloadType payloadType(String shortMessage ) {

        for ( String serviceCommand : serviceCommands ) {
            if (shortMessage.trim().startsWith( serviceCommand.trim() ) ) {
                return PayloadType.SESSION_START;
            }
        }

        return PayloadType.USSD_ANSWER;
    }
}
