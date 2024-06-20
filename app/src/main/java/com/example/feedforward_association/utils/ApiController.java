package com.example.feedforward_association.utils;

import android.util.Log;

import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.interfaces.ApiService;
import com.example.feedforward_association.models.server.object.ObjectBoundary;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiController {

    private ApiService apiService;

    public ApiController() {
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    public void createObject(ObjectBoundary object, final ApiCallback<ObjectBoundary> callback) {
        Call<ObjectBoundary> call = apiService.createObject(object);
        call.enqueue(new Callback<ObjectBoundary>() {
            @Override
            public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    Log.e("ApiRepository", "Error response code: " + response.code());
                    Log.e("ApiRepository", "Error response message: " + response.message());
                    Log.e("ApiRepository", "Error response body: " + response.errorBody());
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ObjectBoundary> call, Throwable t) {
                Log.e("ApiRepository", "Failure message: " + t.getMessage(), t);
                callback.onError("Failure: " + t.getMessage());
            }
        });
    }
    public void getAllObjects(String userSuperApp, String userEmail, final ApiCallback<List<ObjectBoundary>> callback) {
        Call<List<ObjectBoundary>> call = apiService.getAllObjects(userSuperApp, userEmail);
          call.enqueue(new Callback<List<ObjectBoundary>>() {
                @Override
                public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                 if (response.isSuccessful()) {
                      callback.onSuccess(response.body());
                 } else {
                      Log.e("ApiRepository", "Error response code: " + response.code());
                      Log.e("ApiRepository", "Error response message: " + response.message());
                      Log.e("ApiRepository", "Error response body: " + response.errorBody());
                      callback.onError("Error: " + response.code());
                 }
                }

                @Override
                public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
                 Log.e("ApiRepository", "Failure message: " + t.getMessage(), t);
                 callback.onError("Failure: " + t.getMessage());
                }
          });
    }




}
