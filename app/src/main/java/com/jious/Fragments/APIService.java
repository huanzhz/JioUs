package com.jious.Fragments;

import com.jious.Notification.MyResponse;
import com.jious.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-type:application/json",
                    "Authorization:key=AAAAV1xE0Qc:APA91bGG5INIY5NqvZa6e1cfWnmIODR3EZMKkMRCrtMt9L2z6Kr5X14sIlAP4yD9OaHAGKhHZWUKfhMidoB1SyFBMv44Y_ZbfHR7v45aNQhPRcGR0dPhinwXkt9mzDtmbMPdOiMPFR7Q"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
