package com.ebridge.vas.model;

import java.util.Map;

/**
 * david@ebridgevas.com
 *
 */
public class UserSession {

    private final String userId;
    private final String sessionId;

    public UserSession( String userId,
                        String sessionId) {

        this.userId = userId;
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    private PduType pduType;
    private ServiceCommand serviceCommand;

    private Map<String, String> parameters;

    public UserSession(PduType pduType, ServiceCommand serviceCommand) {
        this(pduType, serviceCommand, null);
    }

    public UserSession(PduType pduType, ServiceCommand serviceCommand, Map<String, String> parameters) {
        userId = null;
        sessionId = null;
        this.pduType = pduType;
        this.serviceCommand = serviceCommand;
        this.parameters = parameters;
    }

    public PduType getPduType() {
        return pduType;
    }

    public void setPduType(PduType pduType) {
        this.pduType = pduType;
    }

    public ServiceCommand getServiceCommand() {
        return serviceCommand;
    }

    public void setServiceCommand(ServiceCommand serviceCommand) {
        this.serviceCommand = serviceCommand;
    }

    public Map<String, String> getParameters() {  return parameters;  }
}
