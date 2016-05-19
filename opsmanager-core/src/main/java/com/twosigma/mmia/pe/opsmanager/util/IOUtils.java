package com.twosigma.mmia.pe.opsmanager.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {

    private static final int EOF = -1;
    private static final Logger logger = LoggerFactory.getLogger(IOUtils.class);
    private static final int BUFFER_SIZE = 4096;

    public static void closeQuietly(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                // ignore, score
            }
        }
    }

    public static String toString(InputStream input) throws IOException {
        if(input == null) {
            return null;
        }

        final InputStreamReader inputStreamReader = new InputStreamReader(input);
        final StringBuilder stringBuilder = new StringBuilder();
        final char[] buffer = new char[BUFFER_SIZE];

        int n = 0;

        while(EOF != (n = inputStreamReader.read(buffer))) {
            stringBuilder.append(buffer, 0, n);
        }

        return stringBuilder.toString();
    }

    public static void consumeAndClose(InputStream is) {
        if (is == null) {
            return;
        }

        try {
            while (is.read() != EOF) {}
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        } finally {
            closeQuietly(is);
        }
    }

    public static InputStream getResourceAsStream(String name) {
        return IOUtils.class.getClassLoader().getResourceAsStream(name);
    }

    public static String getResourceAsString(String name) {
        try {
            return toString(getResourceAsStream(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
