package com.example.asif047.mr_informer.ImageUpload;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by admin on 9/17/2017.
 */

public interface ApiInterface {


    @FormUrlEncoded
    @POST("informer/upload.php")
    Call<ImageClass> UploadImage(@Field("title")String title, @Field("image")String image);

}
