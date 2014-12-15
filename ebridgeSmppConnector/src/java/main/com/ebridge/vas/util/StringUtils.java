package com.ebridge.vas.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author david@tekeshe.com
 */
public class StringUtils {

    /**
     * String array to string converter
     * @param a
     * @return
     */
    public static String string(String[] a) {
        StringBuilder sb = new StringBuilder();
        for (String s : a) {
            sb.append(s + " ");
        }
        return sb.toString().trim();
    }

    /**
     * reads a file into a string
     *
     * @param filename
     * @return string
     */
    public static String fromFile( String filename ) {

        String result = "";
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader( filename ));

            String line = "";
            while ( (line = in.readLine()) != null ) { result += line; }

            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
