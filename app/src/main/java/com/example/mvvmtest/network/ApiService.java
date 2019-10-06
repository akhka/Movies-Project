package com.example.mvvmtest.network;

import com.example.mvvmtest.model.MovieResponse;
import com.example.mvvmtest.model.ReviewResponse;
import com.example.mvvmtest.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // For getteing movies
    @GET("movie/{sort_type}")
    Call<MovieResponse> getMovies(
            @Path("sort_type") String sort_type,
            @Query("api_key") String api_key,
            @Query("language") String language
    );


    // For getting reviews
    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getReviews(
            @Path("id") int id,
            @Query("api_key") String api_key
    );


    // For getting trailers
    @GET("movie/{id}/videos")
    Call<TrailerResponse> getTrailers(
            @Path("id") int id,
            @Query("api_key") String api_key
    );

}
