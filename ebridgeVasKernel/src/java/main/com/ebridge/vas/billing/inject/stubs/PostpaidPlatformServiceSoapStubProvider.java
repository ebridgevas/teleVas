package com.ebridge.vas.billing.inject.stubs;

import com.google.inject.Provider;
import com.ztesoft.zsmart.bss.ws.customization.zimbabwe.WebServices;

/**
 * @author david@tekeshe.com
 */
public class PostpaidPlatformServiceSoapStubProvider implements Provider<WebServices> {

    @Override
    public WebServices get() {
        return new WebServicesStub();
    }
}
