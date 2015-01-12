package com.ebridge.commons.dto;

/**
 * @author david@tekeshe.com
 */
public class Request {

    private final Integer sessionId;
    private final String sourceId;
    private final String beneficiaryId;
    private final String destinationId;
    private final String payload;

    private final PayloadType payloadType;

    public Request( Integer sessionId,
                    String sourceId,
                    String destinationId,
                    String beneficiaryId,
                    String payload,
                    PayloadType payloadType ) {

        this.sessionId = sessionId;
        this.sourceId = sourceId;
        this.beneficiaryId = beneficiaryId;
        this.destinationId = destinationId;
        this.payload = payload;
        this.payloadType = payloadType;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public String getPayload() {
        return payload;
    }

    public PayloadType getPayloadType() {
        return payloadType;
    }

    public Boolean isSessionStart() {

        return payloadType == PayloadType.SESSION_START;
    }
}
