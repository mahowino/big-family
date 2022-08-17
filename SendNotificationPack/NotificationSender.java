package com.example.bigfamilyv20.SendNotificationPack;

import com.google.gson.annotations.SerializedName;

public class NotificationSender {

    @SerializedName("to") //  "to" changed to token
    private String token;

    @SerializedName("notification")
    private Data notification;

    @SerializedName("data")
    private DataModel data;

    public NotificationSender(String token, Data notification, DataModel data) {
        this.token = token;
        this.notification = notification;
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Data getNotification() {
        return notification;
    }

    public void setNotification(Data notification) {
        this.notification = notification;
    }

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }
}

