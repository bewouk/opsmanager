package com.twosigma.mmia.pe.opsmanager.status;

import com.google.inject.Inject;
import com.twosigma.mmia.pe.opsmanager.MessagingService;
import com.twosigma.mmia.pe.opsmanager.messages.Message;

/**
 * Created by sam on 12/14/16.
 */
public class StatusPublisher {
    private final MessagingService messagingService;

    @Inject
    StatusPublisher(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    public void sendStatus(Message message) {
        messagingService.sendMessage(message);
    }

}
