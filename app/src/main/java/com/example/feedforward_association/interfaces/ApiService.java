package com.example.feedforward_association.interfaces;


import com.example.feedforward_association.models.server.object.ObjectBoundary;
import com.example.feedforward_association.models.server.user.UserBoundary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {


    @POST("superapp/objects")
    Call<ObjectBoundary> createObject(@Body ObjectBoundary boundary);

    @POST("superapp/users")
    Call<UserBoundary> createUser(@Body UserBoundary boundary);

    @GET("superapp/objects/search/byType/{type}")
    Call<List<ObjectBoundary>> getAllObjcts(
            @Path("type") String type,
            @Query("userSuperApp") String userSuperApp,
            @Query("userEmail") String userEmail,
            @Query("size") int size,
            @Query("page") int page
    );
    @GET("superapp/users/login/{superapp}/{email}")
    Call<UserBoundary> getUser(
            @Path("superapp") String superapp,
            @Path("email") String email
    );


    @PUT("superapp/objects/{superapp}/{id}")
    Call<Void> updateObject(
            @Path("id") String id,
            @Path("superapp") String superapp,
            @Query("userSuperApp") String  userSuperApp,
            @Query("userEmail") String userEmail,
            @Body ObjectBoundary boundary
    );

    @PUT("superapp/users/{superapp}/{userEmail}")
    Call<Void> updateUser(
            @Path("superapp") String superapp,
            @Path("userEmail") String userEmail,
            @Body UserBoundary boundary
    );


}
