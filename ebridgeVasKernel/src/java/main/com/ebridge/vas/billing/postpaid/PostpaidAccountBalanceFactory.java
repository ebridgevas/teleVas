package com.ebridge.vas.billing.postpaid;

import com.ebridge.vas.billing.prepaid.AccountBalanceFactory;
import com.ebridge.vas.dto.BalanceDTO;
import com.ebridge.vas.util.billing.ConfigurationService;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ztesoft.zsmart.bss.ws.customization.zimbabwe.QueryAcmBalReqDto;
import com.ztesoft.zsmart.bss.ws.customization.zimbabwe.QueryAcmBalRetDto;
import com.ztesoft.zsmart.bss.ws.customization.zimbabwe.WebServices;
import org.apache.log4j.Category;

import javax.inject.Named;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;

/**
* @author david@tekeshe.com
*/
public class PostpaidAccountBalanceFactory implements AccountBalanceFactory {

    @Inject
    @Named("postpaidServiceSoapProvider")
    private Provider<WebServices> postpaidServiceSoapProvider;

    @Inject
    private ConfigurationService configurationService;

    private static Category CAT = Category.getInstance(PostpaidAccountBalanceFactory.class);

    private static final String PRICE_PLAN = "166";
    private static final Double OCTET = 1048576.00;

    public Set<BalanceDTO> balances ( String mobileNumber ) throws RemoteException {

        CAT.debug("balances()");

        QueryAcmBalReqDto payload = new QueryAcmBalReqDto();
        payload.setMSISDN( mobileNumber );
        payload.setPricePlanID(PRICE_PLAN);

        QueryAcmBalRetDto response = postpaidServiceSoapProvider.get().queryAcmBal(payload);

        Calendar expirationDate = Calendar.getInstance();
        try {
            expirationDate.setTime(
                    new SimpleDateFormat("dd/MM/yyyy").parse(response.getExpDate()));
        } catch (ParseException e) {
            CAT.error("Failed to parse date string : " + response.getExpDate() );
            e.printStackTrace();
        }

        TreeSet<BalanceDTO> result
                = new TreeSet<>(
                     Arrays.asList(
                        new BalanceDTO[]{
                                new BalanceDTO(
                                        mobileNumber,
                                        configurationService.config().getBalances().get("PostpaidCore"),
                                        new BigDecimal(
                                                Double.parseDouble(response.getBalance()) / OCTET)
                                                .setScale(2, BigDecimal.ROUND_HALF_UP), expirationDate, "POSTPAID")
                        }));

        CAT.info( "INFO : {mobileNumber : " + mobileNumber + ", balances : " + result + "}" );

        return result;
    }
}
