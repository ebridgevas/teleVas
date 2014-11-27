package com.ebridge.vas.billing;

import com.ebridge.vas.dto.DataBundleDTO;
import com.ebridge.vas.dto.BalanceDTO;
import com.ebridge.vas.util.TransactionException;

import java.rmi.RemoteException;

/**
 * @author david@tekeshe.com
 */
public interface DataBundlePurchase {

    public BalanceDTO[] dataBundlePurchase(
            String uuid, String mobileNumber, DataBundleDTO dataBundle, String beneficiaryId)
            throws RemoteException, TransactionException;

}
