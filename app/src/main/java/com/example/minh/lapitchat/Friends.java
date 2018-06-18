package com.example.minh.lapitchat;

/**
 * Created by Minh on 6/14/2018.
 */

public class Friends {
    public String date;
    public String name;
    public String image;
    String online_status;

    public Friends() {
    }

    public Friends(String date, String name, String image, String online_status) {
        this.date = date;
        this.name = name;
        this.image = image;
        this.online_status = online_status;
    }

    public String isOnline_status() {
        return online_status;
    }

    public void setOnline_status(String online_status) {
        this.online_status = online_status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
