package com.ebridge.vas.dto;

/**
 * @author david@tekeshe.com
 */
public final class SmppConfigDto {

    private final String smppIPAdress;
    private final Integer smppPort;
    private final String systemId;
    private final String systemPassword;

    public SmppConfigDto( String smppIPAdress,
                          Integer smppPort,
                          String systemId,
                          String systemPassword) {

        this.smppIPAdress = smppIPAdress;
        this.smppPort = smppPort;
        this.systemId = systemId;
        this.systemPassword = systemPassword;
    }

    public String getSmppIPAdress() {
        return smppIPAdress;
    }

    public Integer getSmppPort() {
        return smppPort;
    }

    public String getSystemId() {
        return systemId;
    }

    public String getSystemPassword() {
        return systemPassword;
    }

    public String toString() {
        return "{smpp-config : { " +
                " smpp-ip-address : " + smppIPAdress +
                ", smpp-port : " + smppPort +
                ", system-id : " + systemId +
                ", system-password : " + systemPassword +
                "}}";
    }
}
