package com.ebridge.vas.billing;

import com.ebridge.vas.dto.BalanceDTO;
import com.ebridge.vas.util.BalanceTransferReversalFailedException;
import com.ebridge.vas.util.TransactionException;

import java.math.BigDecimal;
import java.rmi.RemoteException;

/**
 * @author david@tekeshe.com
 */
public interface BalanceTransfer {

    public BalanceDTO[] transfer(String uuid, String mobileNumber, String beneficiaryId, BigDecimal amount)
            throws RemoteException, TransactionException, BalanceTransferReversalFailedException;
}
