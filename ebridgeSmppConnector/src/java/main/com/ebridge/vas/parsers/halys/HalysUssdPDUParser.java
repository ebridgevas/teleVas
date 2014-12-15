package com.ebridge.vas.parsers.halys;

import static com.ebridge.vas.util.StringUtils.string;

import com.ebridge.smpp.pdu.DeliverSM;
import com.ebridge.vas.Request;
import com.ebridge.vas.dto.ShortCodeConfigDTO;
import com.ebridge.vas.parsers.PayloadParser;
import com.ebridge.vas.parsers.halys.util.PayloadType;
import com.ebridge.vas.util.PDUParseException;

import java.util.Arrays;

/**
 * @author david@tekeshe.com
 */
public class HalysUssdPDUParser implements PayloadParser {

    private ShortCodeConfigDTO shortCodeParameters;

    public HalysUssdPDUParser() {
    }

    public HalysUssdPDUParser(ShortCodeConfigDTO shortCodeParameters) {
        this.shortCodeParameters = shortCodeParameters;
    }

    @Override
    public Request parse(DeliverSM deliverSM) throws PDUParseException {

        try {
            String shortMessage = deliverSM.getShortMessage();

            Integer sessionId = token(shortMessage, 1);
            Integer payloadId = token(shortMessage, 0);
            PayloadType payloadType = PayloadType.payloadType(payloadId);

            return new Request(
                    sessionId,
                    deliverSM.getSourceAddr().getAddress(),
                    deliverSM.getDestAddr().getAddress(),
                    deliverSM.getSourceAddr().getAddress(),
                    content(shortMessage, payloadType),
                    payloadType);
        } catch ( Exception e ) {

            throw new PDUParseException( e );
        }
    }

    protected Integer token( String shortMessage, Integer index ) throws Exception {

        return Integer.parseInt( shortMessage.split(" ")[index]);
    }

    protected String content( String payload, PayloadType payloadType ) throws Exception {

        // 80 33495 971#
        // 74 33495 0 0 0 0 15 1

        String[] tokens = payload.split(" ");

        Integer index = null;
        switch(payloadType) {

            case SESSION_START:
                index = 2;
                break;
            case USSD_ANSWER:
                index = 7;
                break;
            default:
                return null;
        }

        return string( Arrays.copyOfRange( tokens, index, tokens.length ) );
    }
}
