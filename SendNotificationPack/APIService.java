package com.example.bigfamilyv20.SendNotificationPack;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Authorization: key=AAAAqU5ggpk:APA91bEC7eHr0has7UH-20MIgO9XU5H0XF8y0AmNQP4CDKV-neHqnMOI3r-SIKQ-Sj1J6mEUPVYrq3VZ5oldvI6Zcf_2Jgp_wMW3HYRN5gr-Xv4q7Yph0AZMFDPpYn7A9hJeUUIzVmOl",
                    "Content-Type: application/json"
                //your server key...refer to firebase for it

            }
            )


    @POST("fcm/send")
    Call<JSONObject> sendNotification(@Body JsonObject body);

}
