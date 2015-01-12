package com.ebridge.vas.model;

/**
 * david@ebridgevas.com
 *
 */
public enum ServiceCommand {

    REGISTER_USER("user registration"),
    GENERATE_ACTIVATION_CODE("activation code generation"),
    ACTIVATE_USER("user activation"),
    CHANGE_USER_PASSWORD("password change"),
    RESET_USER_PASSWORD("password reset"),
    DATA_BUNDLE_PURCHASE("data bundle purchase"),
    DEACTIVATE_USER("user deletion"),
    DELETE_USER("user deletion"),

    USSD_MENU_LISTING_REQUEST(""),
    USSD_MENU_ITEM_PROCESSING_REQUEST("");

    private String description;

    private ServiceCommand(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static  ServiceCommand toServiceCommand(String description){
        ServiceCommand result = null;

        ServiceCommand nodes[] =  values();

        for( int i = 0; i < nodes.length;i++){
            if( nodes[i].description.equals( description ) ) {
                result =  nodes[i];
                break;
            }
        }
        return result;
    }
}
