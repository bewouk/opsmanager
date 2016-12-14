package com.twosigma.mmia.pe.opsmanager.status;

import com.twosigma.mmia.pe.opsmanager.messages.Message;

/**
 * Created by sam on 12/14/16.
 */
public class StatusMessage implements Message {
    private String subject;
    private Integer partition;
    private String key;
    private String payload;

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public Integer getPartition() {
        return partition;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void setPayload(String payload) {
        this.payload = payload;
    }
}
