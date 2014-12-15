package com.ebridge.vas.util;

import com.ebridge.smpp.pdu.Address;
import com.ebridge.smpp.pdu.DeliverSM;
import com.ebridge.smpp.pdu.SubmitSM;
import com.ebridge.smpp.pdu.WrongLengthOfStringException;

/**
 * @author david@tekeshe.com
 */
public class SmppUtils {

    public static DeliverSM deliverSM( String sourceId, String destinationId, String shortMessage)
            throws WrongLengthOfStringException {

        DeliverSM pdu = new DeliverSM();
        pdu.setSourceAddr(new Address(sourceId));
        pdu.setDestAddr(new Address(destinationId));
        pdu.setShortMessage( shortMessage );

        return pdu;
    }

//    public static SubmitSM submitSM( Response response )
//            throws WrongLengthOfStringException {
//
//        SubmitSM pdu = new SubmitSM();
//        pdu.setSourceAddr(new Address(response.getDestinationId()));
//        pdu.setDestAddr(new Address(response.getDestinationId()));
//        pdu.setShortMessage( response.getPayload() );
//
//        return pdu;
//    }

    public static SubmitSM submitSM( String sourceId, String destinationId, String shortMessage)
            throws WrongLengthOfStringException {

        SubmitSM pdu = new SubmitSM();
        pdu.setSourceAddr(new Address(sourceId));
        pdu.setDestAddr(new Address(destinationId));
        pdu.setShortMessage( shortMessage );

        return pdu;
    }

//    // TODO parameterize timeout value
//    private static final Integer USSD_TIMEOUT = 30;
//
//    public static String getContent( String payload ) {
//
////        return payload;
//        String[] tokens = payload.split(" ");
//        return tokens.length > 6 ? str(Arrays.copyOfRange(tokens, 7, tokens.length)) : "";
//    }
//
//    public static String getContent( String payload, Integer index, String delimiter ){
//        return payload.split( delimiter )[ index ];
//    }
//
//    public static String getUssdMessagePrefix(Boolean terminateSession, Integer sessionId){
//        return ( terminateSession ? "81" : "72") + " " + sessionId + ( terminateSession ? "" : " " + USSD_TIMEOUT ) + " 0 "; // : "";
//    }
//
//    public static Integer getSessionId( String payload) {
//        return 0;
////        return (payload != null) ? new Integer( payload.split(" ")[1] ) : null;
//    }

    /**
     * String array to string converter
     * @param a
     * @return
     */
    public static String string(String[] a) {
        StringBuilder sb = new StringBuilder();
        for (String s : a) {
            sb.append(s + " ");
        }
        return sb.toString().trim();
    }
}
