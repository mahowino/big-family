package com.example.bigfamilyv20.SendNotificationPack;

import com.google.gson.JsonObject;

import java.security.Key;


public class Jsoner {

    public Jsoner() {
    }

    public static JsonObject buildNotificationPayload(String Token, String title,String message) {
        // compose notification json payload
        JsonObject payload = new JsonObject();
        payload.addProperty("to", Token);
        // compose data payload here
        JsonObject data = new JsonObject();
        data.addProperty("Title", title);
        data.addProperty("Message", message);

        // add data payload
        payload.add("data", data);
        return payload;
    }
}
