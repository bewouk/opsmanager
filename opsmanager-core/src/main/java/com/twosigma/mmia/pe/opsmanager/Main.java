package com.twosigma.mmia.pe.opsmanager;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.twosigma.mmia.pe.opsmanager.kafka.KafkaConfiguration;
import com.twosigma.mmia.pe.opsmanager.messages.Message;
import com.twosigma.mmia.pe.opsmanager.status.StatusMessage;
import com.twosigma.mmia.pe.opsmanager.status.StatusModule;
import com.twosigma.mmia.pe.opsmanager.status.StatusPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final int EOF = -1;
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws Exception {

        // StatusPublisher needs to be wired up with a Kafka service (messaging) and ??
        Injector injector = Guice.createInjector(new StatusModule());

        KafkaConfiguration kafkaConfiguration =
                new KafkaConfiguration("broker1:9092,broker2:9092 ",
                        "kafka.serializer.StringEncoder",
                        "example.producer.SimplePartitioner",
                        "1");


        StatusPublisher statusPublisher = injector.getInstance(StatusPublisher.class);

        while(true) {
            logger.info("Sleeping for a bit");
            Message m = new StatusMessage();
            m.setSubject("some.random.subject");
            m.setPartition(1);
            m.setKey("somedaemon");
            m.setPayload("UP");
            statusPublisher.sendStatus(m);
            Thread.sleep(5000);

        }

    }
}
