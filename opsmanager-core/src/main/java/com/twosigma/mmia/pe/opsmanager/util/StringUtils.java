package com.twosigma.mmia.pe.opsmanager.util;

/**
 * Created by sam on 11/23/15.
 */
public class StringUtils {
    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
