package com.twosigma.mmia.pe.opsmanager;

import com.twosigma.mmia.pe.opsmanager.elasticsearch.ElasticsearchClient;
import com.twosigma.mmia.pe.opsmanager.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by sam on 11/23/15.
 */
public class Main {

    private static final int EOF = -1;
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws Exception {
        ElasticsearchClient esc = new ElasticsearchClient("http://127.0.0.1:9200");

        esc.sendMappingTemplateAsync("mmia-events-mapping.json", "mmia-events");
        esc.createIndex("mmia-events-2015.11.24", IOUtils.getResourceAsStream("mmia-events-mapping.json"));
        esc.createIndex("metrics", IOUtils.getResourceAsStream("mmia-metrics-mapping.json"));

        while(true) {
            logger.info("Sleeping for a bit");
            Thread.sleep(5000);
            UptimeEvent ue = new UptimeEvent();
            ue.setAlive(false);
            ue.setTimestamp(System.currentTimeMillis());
            ue.setReason("Fail");
            ue.setDaemon("SAM1");
            ue.setSource("SnapUI");
            esc.index("mmia-events-2015.11.24", "event", ue);
        }

    }
}
