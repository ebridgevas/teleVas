package com.ebridge.vas.billing.prepaid;

import com.comverse_in.prepaid.ccws.ServiceSoap;
import com.ebridge.vas.billing.VoucherRecharge;
import com.ebridge.vas.dto.BalanceDTO;
import com.ebridge.vas.util.TransactionException;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.log4j.Category;

import javax.inject.Named;
import java.rmi.RemoteException;

import static com.ebridge.vas.util.billing.Util.balance;

/**
 * @author david@tekeshe.com
 */
public class PrepaidVoucherRecharge implements VoucherRecharge {

    @Inject
    @Named("prepaidServiceSoapProvider")
    private Provider<ServiceSoap> prepaidServiceSoapProvider;

    @Inject
    @Named("prepaidAccountBalanceFactory")
    private AccountBalanceFactory prepaidAccountBalanceFactory;

    private static final String CORE_BALANCE = "Core";

    private static Category CAT = Category.getInstance(PrepaidBalanceTransfer.class);

    @Override
    public BalanceDTO recharge( String uuid, String beneficiaryId, String rechargeVoucher )
            throws RemoteException, TransactionException {
        BalanceDTO balanceBeforeTxn = balance(
                CORE_BALANCE,
                prepaidAccountBalanceFactory.balances(beneficiaryId));
        prepaidServiceSoapProvider.get()
                .rechargeAccountBySubscriber(
                        beneficiaryId.substring(3),
                        null,
                        rechargeVoucher, "Voucher recharge # " + uuid);

        BalanceDTO balanceAfterTxn = balance(
                CORE_BALANCE,
                prepaidAccountBalanceFactory.balances(beneficiaryId));
        balanceAfterTxn.setMobileNumber( beneficiaryId );
        balanceAfterTxn.setOtherAmount( balanceAfterTxn.getBalance().subtract( balanceBeforeTxn.getBalance()) );

        CAT.info( "{ result : " + balanceAfterTxn + "}" );

        return balanceAfterTxn;
    }
}
