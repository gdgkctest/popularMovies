package com.chuck.android.popularmovies.rest;


import com.chuck.android.popularmovies.models.Movie;
import com.chuck.android.popularmovies.models.MovieList;
import com.chuck.android.popularmovies.models.MovieTrailerList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieInterface {
    //Defines the Parameters for MovieDB api

    //Get Popular Movies
    @GET("popular")
    Call<MovieList> getPopularMovies(@Query("api_key") String apiKey);
    //Get Top Rated Movies
    @GET("top_rated")
    Call<MovieList> getTopRatedMovies(@Query("api_key") String apiKey);
    //Get Movie Details
    @GET("{id}")
    Call<Movie> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
    @GET("{id}/videos")
    Call<MovieTrailerList> getMovieTrailers(@Path("id") int id, @Query("api_key") String apiKey);
}
