package com.twosigma.mmia.pe.opsmanager.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String toJson(Object o, String exclude) {
        final ObjectNode jsonNode = MAPPER.valueToTree(o);
        jsonNode.remove(exclude);
        return jsonNode.toString();
    }
    public static void writeJsonToOutputStream(Object o, OutputStream os) throws IOException {
        MAPPER.writeValue(os, o);
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    public static ObjectNode toObjectNode(Object o) {
        return MAPPER.valueToTree(0);
    }
}
