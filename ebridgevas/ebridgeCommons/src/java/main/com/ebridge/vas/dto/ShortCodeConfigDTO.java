package com.ebridge.vas.dto;

/**
 * @author david@tekeshe.com
 */
public class ShortCodeConfigDTO {

    private String shortCode;
    private String serverIpAddress;
    private Integer servicePort;
    private String systemId;
    private String systemPassword;
    private String payloadParser;
    private String sessionStartIndicator;

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getServerIpAddress() {
        return serverIpAddress;
    }

    public void setServerIpAddress(String serverIpAddress) {
        this.serverIpAddress = serverIpAddress;
    }

    public Integer getServicePort() {
        return servicePort;
    }

    public void setServicePort(Integer servicePort) {
        this.servicePort = servicePort;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemPassword() {
        return systemPassword;
    }

    public void setSystemPassword(String systemPassword) {
        this.systemPassword = systemPassword;
    }

    public String getPayloadParser() {
        return payloadParser;
    }

    public void setPayloadParser(String payloadParser) {
        this.payloadParser = payloadParser;
    }

    public String getSessionStartIndicator() {
        return sessionStartIndicator;
    }

    public void setSessionStartIndicator(String sessionStartIndicator) {
        this.sessionStartIndicator = sessionStartIndicator;
    }
}
