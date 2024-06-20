package com.example.feedforward_association.interfaces;


import com.example.feedforward_association.models.server.object.ObjectBoundary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("superapp/objects")
    Call<ObjectBoundary> createObject(@Body ObjectBoundary boundary);
    @GET("superapp/objects")
    Call<List<ObjectBoundary>> getAllObjects(@Query("userSuperApp") String userSuperApp, @Query("userEmail") String userEmail);
}