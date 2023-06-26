package com.example.game;


import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface Api {
        @GET("/tip")
        Call<ApiResponse> getTip();

        @GET("/tip")
        Call<ApiResponse> getScores();


        @POST("/characters")
        Call<ApiResponse> getCharacters(@Body RequestBody requestBody);
    }


