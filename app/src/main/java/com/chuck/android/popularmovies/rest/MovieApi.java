package com.chuck.android.popularmovies.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieApi {
    //Defines the API for MovieDB using Retrofit
    public static Retrofit getClient(){
        final String Base_URL = "https://api.themoviedb.org/3/movie/";

        return new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
