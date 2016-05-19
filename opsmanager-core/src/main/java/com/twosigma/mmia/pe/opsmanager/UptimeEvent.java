package com.twosigma.mmia.pe.opsmanager;

/**
 * Created by sam on 11/23/15.
 */
public class UptimeEvent {

    private long timestamp;
    private String source;
    private boolean alive;
    private String reason;
    private String daemon;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDaemon() {
        return daemon;
    }

    public void setDaemon(String daemon) {
        this.daemon = daemon;
    }
}
