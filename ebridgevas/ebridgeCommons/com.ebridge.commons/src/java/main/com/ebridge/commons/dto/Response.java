package com.ebridge.commons.dto;

/**
 * @author david@tekeshe.com
 */
public class Response {

    private final Integer sessionId;
    private final String sourceAddress;
    private final String beneficiaryId;
    private final String destinationAddress;
    private final String shortMessage;
    private final String additionalShortMessage;
    private final SessionState sessionState;

    public Response( Integer sessionId,
                     String sourceAddress,
                     String beneficiaryId,
                     String destinationAddress,
                     String shortMessage,
                     String additionalShortMessage,
                     SessionState sessionState) {

        this.sessionId = sessionId;
        this.sourceAddress = sourceAddress;
        this.beneficiaryId = beneficiaryId;
        this.destinationAddress = destinationAddress;
        this.shortMessage = shortMessage;
        this.additionalShortMessage = additionalShortMessage;
        this.sessionState = sessionState;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public String getAdditionalShortMessage() {
        return additionalShortMessage;
    }

    public SessionState getSessionState() {
        return sessionState;
    }

    public static Response clone (  Request request,
                                    String shortMessage,
                                    String additionalShortMessage,
                                    SessionState sessionState ) {

        return new Response( request.getSessionId(),
                             request.getDestinationId(),
                             request.getBeneficiaryId(),
                             request.getSourceId(),
                             shortMessage,
                             additionalShortMessage,
                             sessionState );
    }
}
