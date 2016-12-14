package com.twosigma.mmia.pe.opsmanager.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClient {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public int send(final String method, final String url) {
        return send(method, url, null, null);
    }

    public JsonNode getJson(String url, Map<String, String> headers) {
        headers = new HashMap<String, String>(headers);

        headers.put("Accept", "application/json");

        return send("GET", url, headers, null, new ResponseHandler<JsonNode>() {
            @Override
            public JsonNode handleResponse(InputStream is, Integer statusCode) throws IOException {
                return JsonUtils.getMapper().readTree(is);
            }
        });
    }

    public int sendAsJson(final String method, final String url, final Object requestBody) {
        return sendAsJson(method, url, requestBody, new HashMap<String, String>());
    }

    public int sendAsJson(final String method, final String url, final Object requestBody,
                          Map<String, String> headerFields)  {

        headerFields = new HashMap<String, String>(headerFields);
        headerFields.put("Content-Type", "application/json");

        logger.info("Sending [{}] to {}", requestBody.getClass(), url);
        return send(method, url, headerFields, new OutputStreamHandler() {
            @Override
            public void withHttpURLConnection(OutputStream os) throws IOException {
                writeRequestBody(requestBody, os);
            }
        });

    }

    public int send(String method, String url, final List<String> requestBodyLines) {
        return send(method, url, null, new OutputStreamHandler() {
            @Override
            public void withHttpURLConnection(OutputStream os) throws IOException {
                for(String line : requestBodyLines) {
                    os.write(line.getBytes("UTF-8"));
                    os.write("\n".getBytes("UTF-8"));
                }

                os.flush();
            }
        });
    }

    public int send(final String method, final String url, final Map<String, String> headerFields, OutputStreamHandler outputStreamHandler) {
        logger.warn("method [" + method + "] URL: [" + url + "] Headers: [" + headerFields.toString() + "] OutputStream:" + outputStreamHandler.toString() + "" );
        return send(method, url, headerFields, outputStreamHandler, new ErrorLoggingResponseHandler(url));
    }

    public <T> T send(final String method, final String uri, final Map<String, String> headerFields,
                      OutputStreamHandler outputStreamHandler, ResponseHandler<T> responseHandler) {

        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) new URL(uri).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod(method);

            if(headerFields != null) {
                for (Map.Entry<String, String> header : headerFields.entrySet()) {
                    connection.setRequestProperty(header.getKey(), header.getValue());
                }
            }

            if (outputStreamHandler != null) {
                outputStreamHandler.withHttpURLConnection(connection.getOutputStream());
            }

            inputStream = connection.getInputStream();

            return responseHandler.handleResponse(inputStream, connection.getResponseCode());
        } catch (IOException ioe) {
            if(connection != null) {
                inputStream = connection.getErrorStream();

                try {
                    return responseHandler.handleResponse(inputStream, getResponseCode(connection));
                } catch (IOException e) {
                    logger.warn(e.getMessage(), e);
                }
            }

            return null;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

    }

    public Integer getResponseCode(HttpURLConnection connection) {
        try {
            return connection.getResponseCode();
        } catch (IOException e) {
            logger.warn(e.getMessage());
            return null;
        }
    }

    private void writeRequestBody(Object requestBody, OutputStream os) throws IOException {
        if(requestBody != null) {
            if (requestBody instanceof InputStream) {
                byte[] buf = new byte[8192];
                int n;
                while ((n = ((InputStream) requestBody).read(buf)) > 0) {
                    os.write(buf, 0, n);
                }
            } else if (requestBody instanceof String) {
                os.write(((String) requestBody).getBytes("UTF-8"));
            } else {
                JsonUtils.writeJsonToOutputStream(requestBody, os);
            }
        }
    }
    public interface OutputStreamHandler {
        void withHttpURLConnection(OutputStream os) throws IOException;
    }

    public interface ResponseHandler<T> {
        T handleResponse(InputStream is, Integer statusCode) throws IOException;
    }

    private static class ErrorLoggingResponseHandler implements ResponseHandler<Integer> {
        private final Logger logger = LoggerFactory.getLogger(getClass());
        private final String url;

        public ErrorLoggingResponseHandler(String url) {
            this.url = url;
        }

        @Override
        public Integer handleResponse(InputStream is, Integer statusCode) throws IOException {
            if(statusCode == null) {
                return -1;
            }

            if(statusCode >= 400) {
                logger.warn(url + ": " + statusCode + " " + IOUtils.toString(is));
            } else {
                IOUtils.consumeAndClose(is);
            }

            return statusCode;
        }

    }

}



