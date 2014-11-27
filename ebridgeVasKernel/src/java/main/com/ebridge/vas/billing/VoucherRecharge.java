package com.ebridge.vas.billing;

import com.ebridge.vas.dto.BalanceDTO;
import com.ebridge.vas.util.TransactionException;

import java.rmi.RemoteException;

/**
 * @author david@tekeshe.com
 */
public interface VoucherRecharge {

    public BalanceDTO recharge(String uuid, String mobileNumber, String rechargeVoucher)
            throws RemoteException, TransactionException;

}
