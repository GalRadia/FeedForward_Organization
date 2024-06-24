package com.example.feedforward_association.utils;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.interfaces.ApiService;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.server.object.ObjectBoundary;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatabaseRepository {
    private ApiService apiService;

    public DatabaseRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    public LiveData<List<Order>> getAllOrders(String userSuperApp, String userEmail, int size, int page) {
        final MutableLiveData<List<Order>> data = new MutableLiveData<>();
        apiService.getAllObjcts("Order",userSuperApp, userEmail, size, page).enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
            public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful()) {
                    List<Order> orders = Order.convertObjectBoundaryList(response.body());
                    data.setValue(orders);
                }
            }
            @Override
            public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
                // Handle failure
                data.setValue(null);
            }
        });
        return data;
    }
    public void createOrder(Order order, final ApiCallback<Order> callback) throws IOException {
        ObjectBoundary object = order.convert(order);
        Call<ObjectBoundary> call = apiService.createObject(object);
        call.enqueue(new Callback<ObjectBoundary>() {
            @Override
            public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {
                if (response.isSuccessful()) {
                    Order order = new Order(response.body());
                    callback.onSuccess(order);
                    Log.d(" DatabaseRepository", "onResponse: " + order);
                } else {
                    callback.onError("Error: " + response.code());
                    Log.d(" DatabaseRepository", "onError: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ObjectBoundary> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
                Log.d(" DatabaseRepository", "onFailure: " + t.getMessage());
            }
        });
    }

}
