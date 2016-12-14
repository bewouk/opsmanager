package com.twosigma.mmia.pe.opsmanager.kafka;

import com.google.inject.Inject;
import com.twosigma.mmia.pe.opsmanager.MessagingService;
import com.twosigma.mmia.pe.opsmanager.messages.Message;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.record.Record;

import java.util.concurrent.Future;

public class KafkaService implements MessagingService<RecordMetadata> {

    private KafkaConfiguration configuration;

    @Inject
    public KafkaService(KafkaConfiguration config) {
        this.configuration = config;
    }

    public Future<RecordMetadata> sendMessage(Message m) {
        Record r = new Record(System.currentTimeMillis(), m.getPayload().getBytes());

        ProducerRecord<String, Record> record = new ProducerRecord<String, Record>(
                m.getSubject(),
                m.getPartition(),
                m.getKey(),
                r
        );

        return configuration.getProducer().send(record);
    }
}
