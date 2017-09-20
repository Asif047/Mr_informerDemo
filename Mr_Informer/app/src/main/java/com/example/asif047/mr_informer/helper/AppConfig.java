package com.example.asif047.mr_informer.helper;

import com.google.gson.JsonElement;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by admin on 9/17/2017.
 */

public class AppConfig {


    public interface insert {
        @FormUrlEncoded
        @POST("/informer/insertData.php")
        void insertData(
                @Field("phone") String phone,
                @Field("email") String email,
                @Field("message") String message,
                @Field("latitude") String latitude,
                @Field("longitude") String longitude,
                @Field("address") String address,
                @Field("city") String city,
                @Field("country") String country,
                @Field("image") String image,
                @Field("date_time") String date_image,
                Callback<Response> callback);
    }





}
