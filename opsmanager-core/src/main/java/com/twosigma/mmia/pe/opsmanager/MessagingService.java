package com.twosigma.mmia.pe.opsmanager;


import com.twosigma.mmia.pe.opsmanager.messages.Message;

import java.util.concurrent.Future;

public interface MessagingService<T> {
    Future<T> sendMessage(Message m);
}
