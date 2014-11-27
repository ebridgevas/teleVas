package com.ebridge.vas.billing.inject.stubs;

import com.comverse_in.prepaid.ccws.*;
import com.ebridge.vas.billing.inject.AbstractServiceSoapStub;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Calendar;

/**
 * @author david@tekeshe.com
 */
public class PrepaidPlatformServiceSoapStub extends AbstractServiceSoapStub {

    @Override
    public SubscriberRetrieve retrieveSubscriberWithIdentityNoHistory(
            String subscriberID, String identity, Integer informationToRetrieve) throws RemoteException {

        System.out.println("############################# PREPAID ############################# ");

        SubscriberRetrieve result = new SubscriberRetrieve();
        SubscriberEntity subscriberData = new SubscriberEntity();

        if ( subscriberID.equals("733803480") || subscriberID.equals("733661588") ) {
            subscriberData.setCOSName("TEL_COS");
        } else {
            subscriberData.setCOSName("GPRS_COS");
        }
        BalanceEntity balance = new BalanceEntity();
        balance.setBalanceName("Core");
        balance.setAvailableBalance(11.49);
        balance.setBalance(11.49);
        Calendar calendar = Calendar.getInstance();
        balance.setAccountExpiration(calendar);

        /*
        {"balanceNames":{"Data_Bonus":{"systemValue":"Data_Bonus","normalizedValue":"Data Bonus","classOfService":"TEL_COS","index":3},"Gprs_bonus":{"systemValue":
        "Gprs_bonus","normalizedValue":"GPRS Bonus","classOfService":"GPRS_COS","index":5},"Gprs_sms":{"systemValue":"Gprs_sms","normalizedValue":"SMS Bonus",
        "classOfService":"GPRS_COS","index":6},"Gprs_modem":{"systemValue":"Gprs_modem","normalizedValue":"GPRS Account",
        "classOfService":"GPRS_COS","index":4},"Core":{"systemValue":"Core","normalizedValue":"Main Account","classOfService":"TEL_COS","index":1},
        "Gprs_bundle":{"systemValue":"Gprs_bundle","normalizedValue":"Data Account","classOfService":"TEL_COS","index":2}},"cosBalances":
        {"TEL_COS":["Core","Gprs_bundle","Data_Bonus"],"GPRS_COS":["Core","Gprs_modem","Gprs_bonus","Gprs_sms"]}}
~
~
         */

        BalanceEntity[] balances = new BalanceEntity[6];

        balance = new BalanceEntity();
        balance.setBalanceName("Core");
        balance.setAvailableBalance(11.49);
        balance.setBalance(11.49);
        calendar = Calendar.getInstance();
        balance.setAccountExpiration(calendar);
        balances[0] = balance;

        balance = new BalanceEntity();
        balance.setBalanceName("Data_Bonus");
        balance.setAvailableBalance(11.49);
        balance.setBalance(11.49);
        calendar = Calendar.getInstance();
        balance.setAccountExpiration(calendar);
        balances[1] = balance;

        balance = new BalanceEntity();
        balance.setBalanceName("Gprs_bonus");
        balance.setAvailableBalance(11.49);
        balance.setBalance(11.49);
        calendar = Calendar.getInstance();
        balance.setAccountExpiration(calendar);
        balances[2] = balance;

        balance = new BalanceEntity();
        balance.setBalanceName("Gprs_sms");
        balance.setAvailableBalance(11.49);
        balance.setBalance(11.49);
        calendar = Calendar.getInstance();
        balance.setAccountExpiration(calendar);
        balances[3] = balance;

        balance = new BalanceEntity();
        balance.setBalanceName("Gprs_modem");
        balance.setAvailableBalance(11.49);
        balance.setBalance(11.49);
        calendar = Calendar.getInstance();
        balance.setAccountExpiration(calendar);
        balances[4] = balance;

        balance = new BalanceEntity();
        balance.setBalanceName("Gprs_bundle");
        balance.setAvailableBalance(11.49);
        balance.setBalance(11.49);
        calendar = Calendar.getInstance();
        balance.setAccountExpiration(calendar);
        balances[5] = balance;

        subscriberData.setBalances(balances);
        result.setSubscriberData( subscriberData );



        return result;
    }

    @Override
    public Boolean creditAccount(
            String subscriberID,
            String identity,
            BalanceCreditAccount[] subCreditBalances,
            String balanceChangeCode,
            String balanceChangeComment) throws RemoteException {

        return Boolean.TRUE;
    }

    @Override
    public VoucherEntity retrieveVoucherBySecretCode(String secretCode) throws RemoteException {
        VoucherEntity voucher = new VoucherEntity();
        voucher.setFaceValue(new BigDecimal(5.00));
        return voucher;
    }
}
