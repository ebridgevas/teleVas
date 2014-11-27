package com.ebridge.vas.util.billing;

import com.ebridge.vas.dto.DataBundleDTO;
import com.google.inject.Inject;

/**
 * @author david@tekeshe.com
 */
public class DataBundleConfigWrapper {

    @Inject
    private ConfigurationService configurationService;

    public DataBundleDTO dataBundle( String productCode ) {

        configurationService.config().getDataBundles();
        return null;
    }
}
