package com.ebridge.vas.billing.postpaid;

import static com.ebridge.vas.util.billing.Util.balance;

import com.ebridge.vas.billing.DataBundlePurchase;
import com.ebridge.vas.billing.prepaid.AccountBalanceFactory;
import com.ebridge.vas.dto.DataBundleDTO;
import com.ebridge.vas.dto.BalanceDTO;
import com.ebridge.vas.util.TransactionException;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ztesoft.zsmart.bss.ws.customization.zimbabwe.AddUserAcctByIndiPricePlanSubsReqDto;
import com.ztesoft.zsmart.bss.ws.customization.zimbabwe.QueryUserProfileReqDto;
import com.ztesoft.zsmart.bss.ws.customization.zimbabwe.QueryUserProfileRetDto;
import com.ztesoft.zsmart.bss.ws.customization.zimbabwe.WebServices;
import org.apache.log4j.Category;
import org.joda.time.DateTime;

import javax.inject.Named;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author david@tekeshe.com
 */
public class PostpaidDataBundlePurchase implements DataBundlePurchase {

    @Inject
    @Named("postpaidServiceSoapProvider")
    private Provider<WebServices> postpaidServiceSoapProvider;

    @Inject
    @Named("postpaidAccountBalanceFactory")
    private AccountBalanceFactory postpaidAccountBalanceFactory;

    private static final Map<String, String> USER_STATE;
    static {
        /* A : Active, G : Inactive, D : One-Way Block, E : Two-Way Block, B : Termination */
        USER_STATE = new HashMap<String, String>();
        USER_STATE.put("A","active");
        USER_STATE.put("G","inactive");
        USER_STATE.put("D","one-way block");
        USER_STATE.put("E","two-way block");
        USER_STATE.put("B","termination");
    }
    private static final String PRICE_PLAN = "166";
    private static final String CORE_BALANCE = "PostpaidCore";

    private static Category CAT = Category.getInstance(PostpaidDataBundlePurchase.class);

    @Override
    public BalanceDTO[] dataBundlePurchase(
                                String uuid, String mobileNumber, DataBundleDTO dataBundle, String beneficiaryId )
            throws RemoteException, TransactionException {

        Calendar windowPeriod = Calendar.getInstance();
        windowPeriod.setTime( new DateTime(windowPeriod.getTime()).plusDays( dataBundle.getWindowSize()).toDate() );

        QueryUserProfileReqDto profileRequest = new QueryUserProfileReqDto();
        profileRequest.setMSISDN( mobileNumber );
        QueryUserProfileRetDto userProfile = postpaidServiceSoapProvider.get().queryUserProfile(profileRequest);
        String userState = userProfile.getUserProfileDto().getState();

        if (userState == null) throw new TransactionException("account state unknown.");
        userState = userState.toUpperCase();

        String userStateDescription = USER_STATE.get(userState);
        if (userStateDescription == null) throw new TransactionException("account state unknown.");
        if (! "A".equalsIgnoreCase(userState))
                throw new TransactionException(" account " + userStateDescription);

        BalanceDTO balance = balance(CORE_BALANCE, postpaidAccountBalanceFactory.balances( mobileNumber ));

        Calendar expirationDate = balance.getExpiryDate().after( windowPeriod ) ?
                                        balance.getExpiryDate() : windowPeriod;

        AddUserAcctByIndiPricePlanSubsReqDto payload = new AddUserAcctByIndiPricePlanSubsReqDto();
        payload.setBundleType( dataBundle.getBundleType() );
        payload.setEffDate( new Date());
        payload.setExpDate( expirationDate.getTime() );
        payload.setMSISDN( mobileNumber );
        payload.setPricePlanID( PRICE_PLAN );

        postpaidServiceSoapProvider.get().addUserAcctByIndiPricePlanSubs( payload );

        BalanceDTO balanceAftTxn = balance(CORE_BALANCE, postpaidAccountBalanceFactory.balances( mobileNumber ));
        balanceAftTxn.setMobileNumber( mobileNumber );
        balanceAftTxn.setSubscriberPackage("POSTPAID");
        CAT.info("{result: " + balanceAftTxn + "}");

        return new BalanceDTO[]{ balanceAftTxn };
    }
}
