package com.example.feedforward_association.utils;

import android.util.Log;

import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.interfaces.ApiService;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.Restaurant;
import com.example.feedforward_association.models.server.object.ObjectBoundary;
import com.example.feedforward_association.models.server.user.RoleEnum;
import com.example.feedforward_association.models.server.user.UserBoundary;

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


    public void getAllOrders(String userSuperApp, String userEmail, int size, int page, ApiCallback<List<Order>> callback) {
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
    }
    public void getAllRestaurants(String userSuperApp, String userEmail, int size, int page, ApiCallback<List<Restaurant>> callback) {
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
    public void updateRestaurant(Restaurant restaurant, final ApiCallback<Restaurant> callback) {
        ObjectBoundary object = restaurant.convert(restaurant);
        Call<Void> call = apiService.updateObject(restaurant.getRestaurantId().getId(), "2024b.gal.said", "2024b.gal.said", restaurant.getRestaurantEmail(),object);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(restaurant);
                    Log.d(" DatabaseRepository", "onResponse: " + restaurant);
                } else {
                    callback.onError("Error: " + response.code());
                    Log.d(" DatabaseRepository", "onError: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
                Log.d(" DatabaseRepository", "onFailure: " + t.getMessage());
            }
        });
    }
    public void updateUser(UserBoundary user, final ApiCallback<Void> callback) {
        Call<Void> call = apiService.updateUser(user.getUserId().getSuperapp(), user.getUserId().getEmail(),user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                    Log.d(" DatabaseRepository", "onResponse: " + response.body());
                } else {
                    callback.onError("Error: " + response.code());
                    Log.d(" DatabaseRepository", "onError: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
                Log.d(" DatabaseRepository", "onFailure: " + t.getMessage());
            }
        });
    }
    public void createUser(String email, String userName, RoleEnum role, String avatar, final ApiCallback<UserBoundary> callback) {
        UserBoundary user = new UserBoundary(email, userName, role, avatar);
        Call<UserBoundary> call = apiService.createUser(user);
        call.enqueue(new Callback<UserBoundary>() {
            @Override
            public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
                if (response.isSuccessful()) {
                    UserBoundary user = response.body();
                    callback.onSuccess(user);
                    Log.d(" DatabaseRepository", "onResponse: " + user);
                } else {
                    callback.onError("Error: " + response.code());
                    Log.d(" DatabaseRepository", "onError: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserBoundary> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
                Log.d(" DatabaseRepository", "onFailure: " + t.getMessage());
            }
        });
    }
   public void getUser(String email, final ApiCallback<UserBoundary> callback) {
        UserBoundary user = new UserBoundary();
        Call<UserBoundary> call = apiService.getUser("2024b.gal.said", email);
        call.enqueue(new Callback<UserBoundary>() {
            @Override
            public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
                if (response.isSuccessful()) {
                    UserBoundary user = response.body();
                    callback.onSuccess(user);
                    Log.d(" DatabaseRepository", "onResponse: " + user);
                } else {
                    callback.onError("Error: " + response.code());
                    Log.d(" DatabaseRepository", "onError: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserBoundary> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
                Log.d(" DatabaseRepository", "onFailure: " + t.getMessage());
            }
        });
    }

}
