package com.ebridge.vas.util;

import com.ebridge.vas.dto.ConfigDTO;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author david@tekeshe.com
 */
public class JsonConfigurationService implements ConfigurationService {

    @Override
    public ConfigDTO config(String configFilename) {

        try {
            return new Gson().fromJson(
                    new BufferedReader( new FileReader(configFilename)), ConfigDTO.class );
        } catch (FileNotFoundException e) {
            // TODO handle exception
            e.printStackTrace();
        }
        return null;
    }
}
