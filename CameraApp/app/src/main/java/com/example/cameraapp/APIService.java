package com.example.cameraapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    String ENDPOINT = "http://10.0.2.2:3000/";

    @Headers("Content-Type: application/json")
    @POST("postData")
    Call<Post> sendPost(@Body String body);

    @GET("getTask")
    Call<Task> recieveGet();

}
