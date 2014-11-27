package com.ebridge.vas.kernel;

import static com.ebridge.vas.util.billing.Util.*;

import com.ebridge.vas.billing.BillingPlatformInterface;
import com.ebridge.vas.billing.inject.BillingPlatformInterfaceModule;
import com.ebridge.vas.billing.inject.stubs.BillingPlatformInterfaceTestModule;
import com.ebridge.vas.dto.BalanceDTO;
import com.ebridge.vas.dto.DataBundleDTO;
import com.ebridge.vas.dto.WebAccessCommand;
import com.ebridge.vas.util.TransactionException;
import com.ebridge.vas.util.WebAccessCommandParser;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author david@tekeshe.com
 */
public class EBridgeVasKernel {

    /* Inject billing interface using Google Guice Dependency Injection framework.
       See https://github.com/google/guice */
    private BillingPlatformInterface billingPlatformInterface;

    public EBridgeVasKernel()  {

        /* For testing use : com.ebridge.vas.billing.inject.stubs.BillingPlatformInterfaceTestModule
         * For production use: com.ebridge.vas.billing.inject.BillingPlatformInterfaceModule */
        Injector injector = Guice.createInjector(new BillingPlatformInterfaceTestModule());

        billingPlatformInterface = injector.getInstance(BillingPlatformInterface.class);
    }

    public <T> T process(Map<String, String> request) {

        try {

            WebAccessCommand webAccessCommand = WebAccessCommandParser.parse( request.get("service-command") );

            switch (webAccessCommand) {

                case GET_MOBILE_ACCOUNT_LIST:
                    return (T)billingPlatformInterface.balances( request.get("mobile-number") );

                case DATA_BUNDLE_PRICE_LIST:
                    return (T)billingPlatformInterface.dataBundleList();

                case DATA_BUNDLE_PURCHASE:
                    String uuid = "" + System.currentTimeMillis();

                    BalanceDTO[] result = billingPlatformInterface
                            .dataBundlePurchase(
                                    uuid,
                                    request.get("mobile-number"),
                                    request.get("data-bundle-id"),
                                    request.get("beneficiary-id"));

                    DataBundleDTO dataBundle = billingPlatformInterface
                            .dataBundleList().get(request.get("data-bundle-id"));

                    persistDataBundleResponse(uuid, result, dataBundle);

                    return (T)result;

                case DATA_BUNDLE_DETAIL:
                    return (T)billingPlatformInterface.dataBundleList().get(request.get("data-bundle-id"));

                case AIRTIME_TRANSFER:

                    uuid = "" + System.currentTimeMillis();
                    BigDecimal amount = new BigDecimal(request.get("amount"));
                    BalanceDTO[] balances =
                            billingPlatformInterface
                                    .transfer(  uuid,
                                            request.get("mobile-number"),
                                            request.get("beneficiary-id"),
                                            amount );

                    persistBalanceTransferResponse( balances, amount, uuid );

                    return (T)balances;

                case VOUCHER_RECHARGE:

                    uuid = "" + System.currentTimeMillis();

                    BalanceDTO rechargeResult
                            = billingPlatformInterface
                            .recharge(  uuid,
                                    request.get("beneficiary-id"),
                                    request.get("recharge-voucher") );

                    persistVoucherRechargeResponse(uuid, request.get("mobile-number"), rechargeResult);
                    Set<BalanceDTO> set = new TreeSet<>();
                    set.add(rechargeResult);
                    return (T)set;

                case TRANSACTION_HISTORY:

                    return (T)transactionHistory( request.get("mobile-number") );
            }

            return null;
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }


    }

    public static void main(String[] args) {

        // TODO Replace with proper JUNIT tests.

        EBridgeVasKernel kernel = new EBridgeVasKernel();
        Map<String, String> map = new HashMap<>();

        // Test: Account listing
        map.put("service-command", "get-mobile-account-list");
        map.put("mobile-number", "263733803480");
        Set<BalanceDTO> balances = kernel.process(map);
        for (BalanceDTO balance : balances) {
            System.out.println(balance.getBalanceName() + " - " + balance.getBalance());
        }

        // Test : Data bundle price listing
        map.put("service-command", "data-bundle-price-list");
        Map<String, DataBundleDTO> bundles = kernel.process(map);
        for (String bundleId : bundles.keySet()) {
            DataBundleDTO bundle = bundles.get( bundleId );
            System.out.println("bundleId : " + bundleId + "" + bundle.getBundleDescription());
        }

        // Test: Data bundle purchase
        map.put("mobile-number", "263733803480");
        map.put("beneficiary-id", "263733665588");
        map.put("service-command", "data-bundle-purchase");
        map.put("data-bundle-id", "3");
        BalanceDTO[] dataBundlePurchaseResult = kernel.process(map);

        map.put("service-command", "data-bundle-detail");
        map.put("data-bundle-id", "3");
        DataBundleDTO dataBundle = kernel.process(map);

        try {
            /* result[0] is sender response. result[1] is beneficiary response if not topping up own phone. */
            String[] result = dataBundleResponse( dataBundlePurchaseResult, dataBundle );
            System.out.println("sender msg : " + result[0]);
            System.out.println("beneficiary msg : " + ( result.length == 2 ? result[1] : "none"));

        } catch (TransactionException e) {
            e.printStackTrace();
        }

        // Test: Send sms to device

        // Test: Airtime transfer
        // Call prepaid platform
        map.put("mobile-number", "263733803480");
        map.put("beneficiary-id", "263733803480");
        map.put("service-command", "airtime-transfer");
        map.put("amount", "1.00");
        BalanceDTO[] transferResult = kernel.process(map);
        String[] result = balanceTransferResponse(transferResult, new BigDecimal(map.get("amount")), "");
        System.out.println("sender msg : " + result[0]);
        System.out.println("beneficiary msg : " + result[1] );

        // Test: Voucher Recharge
        map.put("mobile-number", "263733803480");
        map.put("beneficiary-id", "263733803480");
        map.put("service-command", "voucher-recharge");
        map.put("recharge-voucher", "123456789012");
        Set<BalanceDTO> rechargeResult = kernel.process(map);
        for ( BalanceDTO balance : rechargeResult ) {
            String[] rechargeResponse = voucherRechargeResponse("", map.get("mobile-number"), balance);
            System.out.println("sender msg : " + rechargeResponse[0]);
            System.out.println("beneficiary msg : " + (rechargeResponse.length == 2 ? rechargeResponse[1] : "none"));
        }

    }
}
