package com.ebridge.vas.factory;

import com.ebridge.smpp.pdu.SubmitSM;
import com.ebridge.smpp.pdu.WrongLengthOfStringException;
import com.ebridge.vas.Response;

/**
 * @author david@tekeshe.com
 */
public interface UssdPDUFactory {

    public SubmitSM submitSM( Response response ) throws WrongLengthOfStringException;
}
