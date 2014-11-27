package com.ebridge.vas.billing.prepaid;

import static com.ebridge.vas.util.billing.Util.balance;
import static com.ebridge.vas.util.billing.Util.dataBalance;

import com.comverse_in.prepaid.ccws.BalanceCreditAccount;
import com.comverse_in.prepaid.ccws.ServiceSoap;
import com.ebridge.vas.billing.DataBundlePurchase;
import com.ebridge.vas.dto.DataBundleDTO;
import com.ebridge.vas.dto.BalanceDTO;
import com.ebridge.vas.util.TransactionException;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.log4j.Category;
import org.joda.time.DateTime;

import javax.inject.Named;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Set;

/**
 * @author david@tekeshe.com
 */
public class PrepaidDataBundlePurchase implements DataBundlePurchase {

    private static Category CAT = Category.getInstance(PrepaidDataBundlePurchase.class);

    @Inject
    @Named("prepaidServiceSoapProvider")
    private Provider<ServiceSoap> prepaidServiceSoapProvider;

    @Inject
    @Named("prepaidAccountBalanceFactory")
    private AccountBalanceFactory prepaidAccountBalanceFactory;

    @Override
    public BalanceDTO[] dataBundlePurchase(
                String uuid, String sourceId, DataBundleDTO dataBundle, String beneficiaryId )
            throws RemoteException, TransactionException {

        Calendar expirationDate = Calendar.getInstance();
        expirationDate.setTime( new DateTime(expirationDate.getTime()).plusDays( dataBundle.getWindowSize()).toDate() );

        CAT.info("{dataBundlePurchase : { mobileNumber : " + sourceId +
                  ", debitValue : " + dataBundle.getDebit() +
                    ", creditValue : " + dataBundle.getCredit() + ", expirationDate : " +
                    String.format("%1$td/%1$tm/%1$tY", expirationDate ) + "}}");

        Boolean ownPhone = sourceId.equals( beneficiaryId );

        Set<BalanceDTO> destinationBalances = null;

        BalanceCreditAccount[] payload = new BalanceCreditAccount[ownPhone ? 2 : 1];
        BalanceCreditAccount[] creditPayload = null;


        BalanceDTO beneficiaryDataBalance = null;

        Set<BalanceDTO> sourceBalances = prepaidAccountBalanceFactory.balances( sourceId );
        BalanceDTO sourceCoreBalance = balance("Core", sourceBalances);
        beneficiaryDataBalance = dataBalance( sourceBalances );
        payload[0] = new BalanceCreditAccount();
        payload[0].setBalanceName(sourceCoreBalance.getBalanceName().getSystemValue());
        payload[0].setCreditValue(0 - dataBundle.getDebit().doubleValue());
        payload[0].setExpirationDate( sourceCoreBalance.getExpiryDate() );

        CAT.debug("Debit : " + payload[0].getCreditValue() );
        System.out.println("Debit : " + payload[0].getCreditValue() );
        if ( ownPhone ) {
            beneficiaryDataBalance = dataBalance( sourceBalances );
            payload[1] = new BalanceCreditAccount();
            payload[1].setBalanceName(beneficiaryDataBalance.getBalanceName().getSystemValue());
            payload[1].setCreditValue(dataBundle.getCredit().doubleValue());
            payload[1].setExpirationDate(
                            expirationDate.after(beneficiaryDataBalance.getExpiryDate())
                                    ? expirationDate : beneficiaryDataBalance.getExpiryDate());

            CAT.debug("Credit : " + payload[1].getCreditValue() );
            System.out.println("Credit : " + payload[1].getCreditValue() );

        } else {
            destinationBalances = prepaidAccountBalanceFactory.balances( beneficiaryId );
            beneficiaryDataBalance = dataBalance( destinationBalances );
            creditPayload = new BalanceCreditAccount[1];
            creditPayload[0] = new BalanceCreditAccount();
            creditPayload[0].setBalanceName( beneficiaryDataBalance.getBalanceName().getSystemValue() );
            creditPayload[0].setCreditValue(dataBundle.getCredit().doubleValue());
            creditPayload[0].setExpirationDate(
                                expirationDate.after( beneficiaryDataBalance.getExpiryDate() )
                                        ? expirationDate : beneficiaryDataBalance.getExpiryDate());
        }

        CAT.debug("Parsing ...");
        prepaidServiceSoapProvider.get().creditAccount(
                sourceId.substring(3), null, payload, "", "VAS Gw: Data Bundle Purchase Ref: " + uuid);

        if ( ! ownPhone ) {
            CAT.debug("Crediting ...");
            try {
                prepaidServiceSoapProvider.get().creditAccount(
                        beneficiaryId.substring(3), null, creditPayload, "", "VAS Gw: Data Bundle Purchase Ref: " + uuid);
            } catch (Exception e) {
                e.printStackTrace();
                payload[0].setCreditValue( dataBundle.getDebit().doubleValue() );
                CAT.debug("Reversing ...");
                try {
                    prepaidServiceSoapProvider.get().creditAccount(
                            sourceId.substring(3), null, payload, "", "VAS Gw: Data Bundle Purchase Ref: " + uuid);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    throw new TransactionException("A system error occurred");
                    // TODO Fatal log reversal failure
                }
            }
        }
        CAT.debug("Billing interface responded.");

        BalanceDTO[] result = new BalanceDTO[2];
        result[0] = new BalanceDTO();
        result[0].setMobileNumber( sourceId );
        result[0].setBalance(sourceCoreBalance.getBalance()
                        .subtract(dataBundle.getDebit()).setScale(2, RoundingMode.HALF_UP));
        result[0].setExpiryDate( payload[0].getExpirationDate() );
        result[0].setSubscriberPackage("PREPAID");

        result[1] = new BalanceDTO();
        result[1].setMobileNumber( beneficiaryId );
        result[1].setBalance(( beneficiaryDataBalance.getBalance())
                                    .add((dataBundle.getCredit().divide(
                                            dataBundle.getConsumptionTariff(), 2, RoundingMode.HALF_UP))
                                            .setScale(2, RoundingMode.HALF_UP)));
        result[1].setExpiryDate( ownPhone ? payload[1].getExpirationDate()
                                            : creditPayload[0].getExpirationDate());
        result[1].setSubscriberPackage("PREPAID");

        CAT.info("{result: " + result + "}");

        return result;
    }
}
