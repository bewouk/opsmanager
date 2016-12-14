package com.twosigma.mmia.pe.opsmanager.messages;

/**
 * Created by sam on 12/12/16.
 */
public interface Message {
    public String getSubject();
    public Integer getPartition();
    public String getKey();
    public String getPayload();

    public void setSubject(String subject);
    public void setPartition(Integer partition);
    public void setKey(String key);
    public void setPayload(String payload);
}
