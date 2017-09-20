package com.example.asif047.mr_informer.ImageUpload;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 9/17/2017.
 */

public class ImageClass {


    @SerializedName("title")
    private String Title;

    @SerializedName("image")
    private String Image;

    @SerializedName("response")
    private String Response;

    public String getResponse() {
        return Response;
    }

}
