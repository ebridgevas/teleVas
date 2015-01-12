package com.ebridge.vas.util;

import com.ebridge.vas.dto.ConfigDTO;

/**
 * @author david@tekeshe.com
 */
public interface ConfigurationService {

    public ConfigDTO config( String configFilename );
}
