package com.example.bigfamilyv20.SendNotificationPack;

public class Data {
    private String Title;
    private String Message;

    public Data(String title, String message) {
        Title = title;
        this.Message = message;
    }
    public Data() {

    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }
}
