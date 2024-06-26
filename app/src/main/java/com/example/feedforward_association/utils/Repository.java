package com.example.feedforward_association.utils;

import android.util.Log;

import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.interfaces.ApiService;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.Restaurant;
import com.example.feedforward_association.models.server.object.ObjectBoundary;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static Repository instance;
    private ApiService apiService;

    private Repository() {
        this.apiService = RetrofitClient.getApiService();
    }
    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }


    public List<Order> getAllOrders(String userSuperApp, String userEmail, int size, int page, ApiCallback<List<Order>> callback) {
        List<Order> orders = new ArrayList<>();
        apiService.getAllObjcts("Order",userSuperApp, userEmail, size, page).enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
            public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful()) {
                    List<Order> orders = Order.convertObjectBoundaryList(response.body());
                    orders.addAll(orders);
                    callback.onSuccess(orders);
                    Log.d("DatabaseRepository", "onResponse: GET " + orders);
                }
            }
            @Override
            public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
                // Handle failure
                Log.d("DatabaseRepository", "onFailure: GET " + t.getMessage());
            }
        });
        return orders;
    }
    public List<Restaurant> getAllRestaurants(String userSuperApp, String userEmail, int size, int page, ApiCallback<List<Restaurant>> callback) {
        List<Restaurant> restaurants = new ArrayList<>();
        apiService.getAllObjcts("Restaurant",userSuperApp, userEmail, size, page).enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
            public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful()) {
                    List<Restaurant> restaurants = Restaurant.convertObjectBoundaryList(response.body());
                    restaurants.addAll(restaurants);
                    callback.onSuccess(restaurants);
                    Log.d("DatabaseRepository", "onResponse: GET " + restaurants);
                }
            }
            @Override
            public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
                // Handle failure
                Log.d("DatabaseRepository", "onFailure: GET " + t.getMessage());
            }
        });
        return restaurants;
    }
//    private void fetchOrdersFromServer(String userSuperApp, String userEmail, int size, int page) {
//        Log.d("DatabaseRepository", "Fetching orders from server...");
//
//        apiService.getAllObjcts("Order", userSuperApp, userEmail, size, page).enqueue(new Callback<List<ObjectBoundary>>() {
//            @Override
//            public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Order> orders = Order.convertObjectBoundaryList(response.body());
//                    ordersLiveData.setValue(orders);
//                    Log.d("DatabaseRepository", "Orders fetched successfully: " + orders.size());
//                } else {
//                    ordersLiveData.setValue(new ArrayList<>());
//                    Log.d("DatabaseRepository", "Failed to fetch orders: response unsuccessful or body is null");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
//                ordersLiveData.setValue(new ArrayList<>());
//                Log.d("DatabaseRepository", "Failed to fetch orders: " + t.getMessage());
//            }
//        });
//    }
    public void createOrder(Order order, final ApiCallback<Order> callback) {
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
