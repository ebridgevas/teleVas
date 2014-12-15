//package com.ebridge.vas.parsers.impl;
//
//import static com.ebridge.vas.util.SmppUtils.getContent;
//
//import com.ebridge.vas.Request;
//import com.ebridge.vas.parsers.SessionStartIndicator;
//import com.ebridge.vas.dto.ShortCodeConfigDTO;
//
///**
// * PayloadBasedSessionStartIndicator
// *
// * Content based method. It looks for a particular value of a field in the content.
// *
// * @author david@tekeshe.com
// */
//public class PayloadBasedSessionStartIndicator implements SessionStartIndicator {
//
//    private ShortCodeConfigDTO shortCodeConfigDTO;
//
//    public PayloadBasedSessionStartIndicator( ShortCodeConfigDTO shortCodeConfigDTO ) {
//        this.shortCodeConfigDTO = shortCodeConfigDTO;
//    }
//
//    @Override
//    public Boolean isSessionStart(Request request) {
//
//        return shortCodeConfigDTO.getSessionStartIndicatorValue()
//                    .equals( getContent( request.getPayload(),
//                                    shortCodeConfigDTO.getSessionStartIndicatorPosition(),
//                                    " ") );
//    }
//}
