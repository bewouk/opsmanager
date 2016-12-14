package com.twosigma.mmia.pe.opsmanager.status;

import com.google.inject.AbstractModule;
import com.twosigma.mmia.pe.opsmanager.MessagingService;
import com.twosigma.mmia.pe.opsmanager.kafka.KafkaService;

public class StatusModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MessagingService.class).to(KafkaService.class);
    }
}
