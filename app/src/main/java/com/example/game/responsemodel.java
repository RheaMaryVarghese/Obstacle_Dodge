/*
package com.example.game;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class responsemodel {

    private static responsemodel instance = null;
    private Api myApi;

    private responsemodel() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(Api.class);
    }

    public static synchronized responsemodel getInstance() {
        if (instance == null) {
            instance = new responsemodel();
        }
        return instance;
    }

    public Api getMyApi() {
        return myApi;
    }

}
*/

