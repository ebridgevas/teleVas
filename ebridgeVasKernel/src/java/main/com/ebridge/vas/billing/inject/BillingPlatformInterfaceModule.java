package com.ebridge.vas.billing.inject;

import com.comverse_in.prepaid.ccws.ServiceSoap;
import com.ebridge.vas.billing.*;
import com.ebridge.vas.billing.impl.BillingPlatformInterfaceImpl;
import com.ebridge.vas.billing.impl.DatabaseBackedSecurityTokenSender;
import com.ebridge.vas.billing.inject.providers.PostpaidPlatformServiceSoapProvider;
import com.ebridge.vas.billing.inject.providers.PrepaidPlatformServiceSoapProvider;
import com.ebridge.vas.billing.postpaid.PostpaidAccountBalanceFactory;
import com.ebridge.vas.billing.postpaid.PostpaidDataBundlePurchase;
import com.ebridge.vas.billing.prepaid.*;
import com.ebridge.vas.services.SecurityTokenSender;
import com.ebridge.vas.util.billing.ClassOfServiceBasedSubscribedPackageIdentifier;
import com.ebridge.vas.util.billing.ConfigurationService;
import com.ebridge.vas.util.billing.JsonConfigurationService;
import com.ebridge.vas.util.billing.SubscribedPackageIdentifier;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import com.ztesoft.zsmart.bss.ws.customization.zimbabwe.WebServices;

/**
 * @author david@tekeshe.com
 */
public class BillingPlatformInterfaceModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(ConfigurationService.class).to(JsonConfigurationService.class);

        bind(SubscribedPackageIdentifier.class).to(ClassOfServiceBasedSubscribedPackageIdentifier.class);

        bind(AccountBalanceFactory.class).annotatedWith(Names.named("prepaidAccountBalanceFactory"))
                .to(PrepaidAccountBalanceFactory.class);

        bind(AccountBalanceFactory.class).annotatedWith(Names.named("postpaidAccountBalanceFactory"))
                .to(PostpaidAccountBalanceFactory.class);

        bind(DataBundlePurchase.class).annotatedWith(Names.named("prepaidDataBundlePurchase"))
                .to(PrepaidDataBundlePurchase.class);

        bind(DataBundlePurchase.class).annotatedWith(Names.named("postpaidDataBundlePurchase"))
                .to(PostpaidDataBundlePurchase.class);

        bind(BalanceTransfer.class).annotatedWith(Names.named("prepaidBalanceTransfer"))
                .to(PrepaidBalanceTransfer.class);

        bind(VoucherRecharge.class).annotatedWith(Names.named("prepaidVoucherRecharge"))
                .to(PrepaidVoucherRecharge.class);

        bind(ServiceSoap.class).annotatedWith(Names.named("prepaidServiceSoapProvider"))
                .toProvider(PrepaidPlatformServiceSoapProvider.class);

        bind(WebServices.class).annotatedWith(Names.named("postpaidServiceSoapProvider"))
                .toProvider(PostpaidPlatformServiceSoapProvider.class);

        bind(SecurityTokenSender.class).annotatedWith(Names.named("messageSender"))
                .to(DatabaseBackedSecurityTokenSender.class);

        bind(BillingPlatformInterface.class).to(BillingPlatformInterfaceImpl.class);
    }
}