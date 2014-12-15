package com.ebridge.vas.model;

/**
 *  UserSession
 *
 *  Keeps state for a transacting user.
 *
 * david@ebridgevas.com
 */
public class UserSession {

    private final String mobileNumber;

    private String previousMenuUuid;

    public UserSession( String mobileNumber ) {

        this.mobileNumber = mobileNumber;
    }

    public UserSession( String mobileNumber, String previousMenuUuid ) {

        this.mobileNumber = mobileNumber;
        this.previousMenuUuid = previousMenuUuid;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getPreviousMenuUuid() {
        return previousMenuUuid;
    }

    public void setPreviousMenuUuid(String previousMenuUuid) {
        this.previousMenuUuid = previousMenuUuid;
    }
}
