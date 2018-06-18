package com.example.minh.lapitchat;

import android.util.Log;

/**
 * Created by Minh on 6/15/2018.
 */

public class Messages {
    private String message,seen,type,from;
    private Long time;


    public Messages() {
    }

    public Messages(String message, String seen, Long time, String type,String from) {
        this.message = message;
        this.seen = seen;
        this.time = time;
        this.type = type;
        this.from=from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
