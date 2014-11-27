package com.ebridge.vas.billing.inject.stubs;

import com.comverse_in.prepaid.ccws.*;
import com.google.inject.Provider;

/**
 * @author david@tekeshe.com
 */
public class PrepaidPlatformServiceSoapStubProvider implements Provider<ServiceSoap> {

    @Override
    public ServiceSoap get() {
        return new PrepaidPlatformServiceSoapStub();
    }
}
