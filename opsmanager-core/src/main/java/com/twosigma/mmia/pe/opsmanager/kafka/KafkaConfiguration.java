package com.twosigma.mmia.pe.opsmanager.kafka;

import com.google.inject.Inject;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

public class KafkaConfiguration {

    private String brokerList;
    private String serializerClass;
    private String partitionerClass;
    private String requestRequireAcks;

    @Inject
    public KafkaConfiguration(String brokerList, String serializerClass,
                              String partitionerClass, String requestRequireAcks) {

        this.brokerList = brokerList;
        this.serializerClass = serializerClass;
        this.partitionerClass = partitionerClass;
        this.requestRequireAcks = requestRequireAcks;
    }

    public KafkaProducer getProducer() {
        Properties props = new Properties();

        props.put("serializer.class", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        props.put("request.required.acks", "1");
        props.put("bootstrap.servers", "localhost:9092");

        return new KafkaProducer(props);
    }
}
