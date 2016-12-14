package com.twosigma.mmia.pe.opsmanager.elasticsearch;

import com.fasterxml.jackson.databind.JsonNode;
import com.twosigma.mmia.pe.opsmanager.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

public class ElasticsearchClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final HttpClient httpClient;
    private final String elasticSearchURL;

    private final ThreadPoolExecutor asyncRestPool;

    public ElasticsearchClient(String elasticSearchURL) {
        this.elasticSearchURL = elasticSearchURL;
        this.httpClient = new HttpClient();
        this.asyncRestPool = ExecutorUtils.createSingleThreadDeamonPool("async-elasticsearch", 4);
    }

    public JsonNode getJson(final String path) throws IOException {
        return JsonUtils.getMapper().readTree(
                new URL(this.elasticSearchURL + path).openStream());
    }


    public void index(final String index, final String type, final Object document) {
        sendAsJsonAsync("POST", "/" + index + "/" + type, document);
    }

    public void createIndex(final String index, final InputStream mapping) {
        sendAsJson("PUT", "/" + index, mapping);
    }

    public int sendAsJson(final String method, final String path, final Object requestBodyObject) {
        if(StringUtils.isEmpty(path)) {
            return -1;
        }
        logger.info("Sending Object of type [{}]", requestBodyObject.getClass());
        return httpClient.sendAsJson(method, this.elasticSearchURL + path, requestBodyObject);
    }

    public Future<?> sendMappingTemplateAsync(String templatePath, String templateName) {
        return sendAsJsonAsync("PUT", "/_template/" + templateName, IOUtils.getResourceAsStream(templatePath));
    }

    private Future<?> sendAsJsonAsync(final String method, final String path, final Object requestBody) {
        logger.info("Sending request to " + this.elasticSearchURL + "" + path);
        if (StringUtils.isNotEmpty(this.elasticSearchURL)) {
            try {
                return asyncRestPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        sendAsJson(method, path, requestBody);
                    }
                });
            } catch (RejectedExecutionException e) {
                ExecutorUtils.logRejectionWarning(e);
            }
        }
        return new CompletedFuture<>(null);
    }
}
