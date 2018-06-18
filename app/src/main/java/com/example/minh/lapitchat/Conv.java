package com.example.minh.lapitchat;

/**
 * Created by Minh on 6/16/2018.
 */

public class Conv {
    public String seen;
    public long timestamp;

    public Conv(String seen, long timestamp) {
        this.seen = seen;
        this.timestamp = timestamp;
    }

    public Conv() {
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
