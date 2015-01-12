package com.ebridge.commons.dto;

/**
 * @author david@tekeshe.com
 */
public enum PayloadType {

    SESSION_START(80), USSD_ANSWER(74);

    private Integer payloadId;

    PayloadType(Integer payloadId) {
        this.payloadId = payloadId;
    }

    public static PayloadType payloadType( Integer payloadId ) {
        for ( PayloadType value : PayloadType.values()) {
            if ( value.payloadId == payloadId) {
                return value;
            }
        }

        return null;
    }

    public void setPayloadId(Integer payloadId) {
        this.payloadId = payloadId;
    }
}
