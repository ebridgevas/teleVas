package com.ebridge.vas.model;

/**
 * Created with IntelliJ IDEA.
 * User: David
 * Date: 3/15/13
 * Time: 5:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class Message {
    public static final String SIM_REG_MAIN_MENU = "Please Select either 1 or 2\n 1. Register SIM \n 2. Exit";
    public static final String SIM_REG_EXIT = "Exiting SIM Registration, Good Bye";
    public static final String SIM_REG_PROMPT_FIRSTNAME = "Please enter your first name";
    public static final String SIM_REG_INVALID_FIRSTNAME = "Error, You entered an invalid first name, please re-enter your first name";
    public static final String SIM_REG_BLANK_FIRSTNAME = "Error, first name cannot be empty, please re-enter your first name";
    public static final String SIM_REG_PROMPT_LASTNAME = "Please enter your last name";
    public static final String SIM_REG_PROMPT_ID_NUMBER = "Please enter your ID Number without spaces or any special characters";
    public static final String SIM_REG_PROMPT_PHYSICAL_ADDRESS = "Please enter your full address including city";
    public static final String SIM_REG_BLANK_LASTNAME = "Error, last name cannot be empty, please re-enter your last name";
    public static final String SIM_REG_INVALID_LASTNAME = "Error, You entered an last name, please re-enter your last name";
    public static final String SIM_REG_BLANK_IDNUMBER = "Error, ID Number cannot be empty, please re-enter your ID Number";
    public static final String SIM_REG_INVALID_IDNUMBER = "Error, You entered an invalid ID Number, please re-enter your ID Number without spaces or any special characters";
    public static final String SIM_REG_BLANK_PHYSICAL_ADDRESS = "Error,address cannot be empty, please re-enter your full address including city";
    public static final String SIM_REG_INVALID_PHYSICAL_ADDRESS = "Error,You entered an invalid address, please re-enter your full address including city";
    public static final String SIM_REG_ALREADY_REGISTERED = "Error, You entered a Mobile Number that is already in use in the system.Please contact the Call Center";
    public static final String SIM_REG_INVALID_SELECTION = "INVALID SELECTION\\nPlease Select either 1 or 2\\n 1. Register SIM \\n 2. Exit";

    public static final String NIGHT_PROMO_MAIN_MENU = "Press\n 1 - Opt In \n2 - Opt Out \n3 - Collect bonus \n4. Exit \n";

    public static final String SHORT_HELP = " For assistance call 150 or 153 ";
    public static final String HELP = "To transfer airtime enter amount then # and the number you are transferring to. To recharge another number enter PIN then # and the number you are recharging.";
    public static final String HELP2 = " To recharge another Telecel number: enter recharge PIN followed by # and the beneficial prepaid mobile number\nTo buy bundle send Bundle";
    public static final String HELP_BALANCE_TRANSFER = "To transfer airtime enter amount then # and the number you are tranferring to. To recharge another number enter PIN then # and the number your are recharging.";
    public static final String HELP_DATA_BUNDLE_PURCHASE = "To recharge another number enter PIN then # and the number you are recharging. To buy bundle send Bundle";
    public static final String HELP_VOUCHER_RECHARGE = "To recharge another number enter PIN then # and the number you are recharging or just the PIN to recharge your number.";
    public static final String INVALID_MESSAGE = "Invalid command. Please send HELP to 350 for assistance.";
    public static final String INVALID_CBZ_PASSWORD = "Sorry, password parts must be two digits. e.g. BANK*83";
    public static final String INVALID_CATEGORY = " is not a valid category. Send H for assistance";
    public static final String CONTENT_TOO_SHORT = " Content should be a least 10 characters long. Send H for assistance";
    public static final String INVALID_BAL_MESSAGE = "Type: u#bal#passcode for balance enquiry." + SHORT_HELP;
    public static final String INVALID_TOPUP_MESSAGE = "Type: u#topup#passcode#amount or u#topup#passcode#amount#target" + SHORT_HELP;
    public static final String INVALID_BILL_MESSAGE = "To pay a bill: type: u#pay#passcode#amount#target " + SHORT_HELP;
    public static final String INVALID_ZESA_MESSAGE = "To pay a Zesa bill: type: u#zesa#passcode#amount " + SHORT_HELP;
    public static final String INVALID_TRANSFER_MESSAGE = "Type: u#transfer#passcode#amount#bankCode#targetPhone " + SHORT_HELP;
    public static final String INVALID_UTILITY = " is not a valid utility. Type: u#bill#utility#amount#passcode" + SHORT_HELP;
    public static final String INVALID_PASSWORD = "Invalid password. Must start with bank code. Type: u#codes. " + SHORT_HELP;
    public static final String WRONG_PASSWORD = "Wrong passcode supplied. Please try again. " + SHORT_HELP;
    public static final String SHORT_PASSWORD = "747 Service passcode must be 5 digits long. " + SHORT_HELP;
    public static final String NOT_SUPPORTED = " not yet supported.";
    public static final String COMMAND = "Service command";
    public static final String INVALID_COMMAND = " is an invalid service command " + SHORT_HELP;
    public static final String INVALID_COMMAND_SPECIFIED = " Invalid service command specified " + SHORT_HELP;
    public static final String SYSTEM_ERROR = "Sorry an error occured. Please try again. " + SHORT_HELP;
    public static final String INVALID_AMOUNT = " is not a valid amount." + " " + SHORT_HELP;
    public static final String ACCOUNT_INACTIVE = "Your phone is not activated. " + SHORT_HELP;
    public static final String NOT_REGISTERED = "Sorry your phone is not registered. " + SHORT_HELP;
    public static final String AMOUNT_TOO_LOW = "Please specify amount greater than";
    public static final String AMOUNT_TOO_HIGH = "Please specify amount less than";
    public static final String BENEFICIARY_NOT_REGISTERED = "You can only transfer money to registered beneficiaries. " + SHORT_HELP;
    public static final String BANK_SYSTEM_OFFLINE = " Bank system is tempolarily offline " + SHORT_HELP;
    public static final String BANK_OFFLINE = " is tempolarily offline " + SHORT_HELP;
    public static final String CODES = "11=Agribank, 12=Beverly, 13=Buzz, 14=CABS, 15=CBZ, 16=FBC, 17=Zimbank";
    public static final String NOT_MEMBER_BANK = "is not yet on 747 Service. " + SHORT_HELP;
    public static final String BANK_NOT_ACTIVE = " is not yet active. ";
    public static final String WRONG_BANK_CODE = " is not a valid bank code. Type U#bank " + SHORT_HELP;
    public static final String BENEFICIARY_LISTING = " ZES - Zesa\n HRE - City of Harare\n TEL - Telone\n EDG - Edgars.";
    public static final String BILL_NOT_AVAILABLE = "Sorry no beneficiary online yet. " + SHORT_HELP;
    public static final String COMMAND_LISTING = " BANK\n BAL\n BEN\n BILL\n COMM\n HELP\n PASS\n STOP\n TOPUP\n TRANSFER\n PAY " + SHORT_HELP;
    public static final String INVALID_CHANGE_PASSCODE_MESSAGE = "To change passcode: U#pass#currentPasscode#newPasscode " + SHORT_HELP;
    public static final String PASSCODES_MISMATCH = "The first two digits of the passcodes can not be changed. " + SHORT_HELP;
    public static final String ERROR_ON_PASSCODE_CHANGE = "An error occured during passcode change. " + SHORT_HELP;
    public static final String INVALID_STOP_MESSAGE = "To stop for transacting: U#stop#passcode. e.g u#stop#27303 where 27303 is your passcode";
    public static final String INVALID_STOCK_MESSAGE = "For stocklisting : U#stock#passcode#network. e.g u#stock#27303#11 where 11=NetOne, 23=Telecel, 91=Econet";
    public static final String ERROR_ON_STOP_PHONE = "An error occured while trying to stop your phone. " + SHORT_HELP;
    public static final String INVALID_BANK_LISTING_COMMAND = "For bank codes type: U#banks " + SHORT_HELP;
    public static final String REQUEST_REJECTED = " Your request was rejected. ";
    public static final String DENOMINATION_NOT_AVAILABLE = " dollar recharge numbers are not available at the moment. " + SHORT_HELP;
    public static final String PIN_MESSAGE = " recharge number : ";
    public static final String ZESA_MESSAGE = " Zesa bill payment accepted : ";
    public static final String BILL_PAYMENT_MESSAGE = " bill payment ";
    public static final String COMPLIMENTS = " :: powered by Uniswitch ::";
    public static final String BILL_DEPRECATED = "Please use command PAY instead of BILL. " + SHORT_HELP;
    public static final String ZESA_DEPRECATED = "Please use command PAY e.g. u#pay#passcode#amount#zesa. " + SHORT_HELP;
    public static final String BLANK_MESSAGE = "Blank message is not allowed";
    public static final String INVALID_MOBILE_NUMBER = " is not a valid mobile number";
    public static final String ORIGINAL_REQUEST_NOTFOUND = " Please send your request first before submitting password.";
    public static final String TRANSFER_INTO_OWN_PHONE = "You can not transfer airtime to your own mobile number.";
    public static final String INVALID_BANK_CODE = " is an invalid bank code.";
    public static final String CBZ_HELP = "To Topup:\nBANK*topup*amount\nTopup someone\nBANK*topup*amount*phone\nPay Telecel bill:\nBANK*pay*telecel*amount\nTo Send Password:BANK*passwordDigits\ne.g.BANK*87";
    public static final String TARIFF_HELP = "To see the tariff you are on: enter tarriff\n To change to per second billing: enter tarriff#second\n To change to per minute billing: enter tarriff#minute";
    public static final String DATA_BUNDLE_PRICE_LIST = "To see the tariff you are on: enter tarriff\n To change to per second billing: enter tarriff#second\n To change to per minute billing: enter tarriff#minute";

}
