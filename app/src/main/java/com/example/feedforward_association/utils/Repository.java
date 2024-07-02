package com.example.feedforward_association.utils;

import android.util.Log;

import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.interfaces.ApiService;
import com.example.feedforward_association.models.Association;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.Restaurant;
import com.example.feedforward_association.models.server.command.CommandBoundary;
import com.example.feedforward_association.models.server.command.CommandId;
import com.example.feedforward_association.models.server.command.InvokedBy;
import com.example.feedforward_association.models.server.command.TargetObject;
import com.example.feedforward_association.models.server.object.ObjectBoundary;
import com.example.feedforward_association.models.server.user.NewUserBoundary;
import com.example.feedforward_association.models.server.user.RoleEnum;
import com.example.feedforward_association.models.server.user.UserBoundary;
import com.example.feedforward_association.models.server.user.UserSession;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
        apiService.getAllObjctsByType("Order", userSuperApp, userEmail, size, page).enqueue(new Callback<List<ObjectBoundary>>() {
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
    public void getAllOrdersByCommand(ApiCallback<List<Order>> callback){
        CommandBoundary commandBoundary = new CommandBoundary("SBECT");
        Map<String, Object> commandMap = Map.of("type","Order","email",UserSession.getInstance().getUserEmail());
        commandBoundary.setCommandAttributes(commandMap);
        Call<List<ObjectBoundary>> call = apiService.command(UserSession.getInstance().getSUPERAPP(), commandBoundary);

        getUser(UserSession.getInstance().getUserEmail(), new ApiCallback<UserBoundary>() {
            @Override
            public void onSuccess(UserBoundary result) {
                result.setRole(RoleEnum.MINIAPP_USER);
                updateUser(result);
                call.enqueue(new Callback<List<ObjectBoundary>>() {
                    @Override
                    public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                        if (response.isSuccessful()) {
                            result.setRole(RoleEnum.SUPERAPP_USER);
                            updateUser(result);
                            List<Order> restaurants = Order.convertObjectBoundaryList(response.body());
                            callback.onSuccess(restaurants);
                        } else {
                            callback.onError("Error: " + response.code());
                            Log.d(" DatabaseRepository", "onError: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
                        callback.onError("Failure: " + t.getMessage());
                        Log.d(" DatabaseRepository", "onFailure: " + t.getMessage());
                    }
                });


            }

            @Override
            public void onError(String error) {

            }
        });
    }
    public void getAllRestaurantsByCommand(ApiCallback<List<Restaurant>> callback){
        CommandBoundary commandBoundary = new CommandBoundary("SBECT");
        Map<String, Object> commandMap = Map.of("type","Restaurant","email",UserSession.getInstance().getUserEmail());
        commandBoundary.setCommandAttributes(commandMap);
        Call<List<ObjectBoundary>> call = apiService.command(UserSession.getInstance().getSUPERAPP(), commandBoundary);

        getUser(UserSession.getInstance().getUserEmail(), new ApiCallback<UserBoundary>() {
            @Override
            public void onSuccess(UserBoundary result) {
                result.setRole(RoleEnum.MINIAPP_USER);
                updateUser(result);
                call.enqueue(new Callback<List<ObjectBoundary>>() {
                    @Override
                    public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                        if (response.isSuccessful()) {
                            result.setRole(RoleEnum.SUPERAPP_USER);
                            updateUser(result);
                            List<Restaurant> restaurants = Restaurant.convertObjectBoundaryList(response.body());
                            callback.onSuccess(restaurants);
                        } else {
                            callback.onError("Error: " + response.code());
                            Log.d(" DatabaseRepository", "onError: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
                        callback.onError("Failure: " + t.getMessage());
                        Log.d(" DatabaseRepository", "onFailure: " + t.getMessage());
                    }
                });


            }

            @Override
            public void onError(String error) {

            }
        });

    }

    public void getAllRestaurants(String userSuperApp, String userEmail, int size, int page, ApiCallback<List<Restaurant>> callback) {
        apiService.getAllObjctsByType("Restaurant", userSuperApp, userEmail, size, page).enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
            public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful()) {
                    List<Restaurant> restaurants = Restaurant.convertObjectBoundaryList(response.body());
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
        ObjectBoundary object = restaurant.toObjectBoundary();
        Call<Void> call = apiService.updateObject(restaurant.getRestaurantId().getId(), UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getSUPERAPP(), restaurant.getRestaurantEmail(), object);
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

    public void updateUser(UserBoundary user) {
        Call<Void> call = apiService.updateUser(user.getUserId().getSuperapp(), user.getUserId().getEmail(), user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(" DatabaseRepository", "onResponse: " + response.body());
                } else {
                    Log.d(" DatabaseRepository", "onError: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(" DatabaseRepository", "onFailure: " + t.getMessage());
            }
        });
    }

    public void createUser(String email, String userName, String avatar, final ApiCallback<UserBoundary> callback) {
        NewUserBoundary user = new NewUserBoundary(email, RoleEnum.SUPERAPP_USER,userName, avatar);
        Call<UserBoundary> call = apiService.createUser(user);
        call.enqueue(new Callback<UserBoundary>() {
            @Override
            public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
                if (response.isSuccessful()) {
                    UserBoundary user = response.body();
                    UserSession.getInstance().setUserEmail(email);
                    callback.onSuccess(user);
                    Log.d("DatabaseRepository", "onResponse: " + user);
                } else {
                    callback.onError("Error: " + response.code());
                    Log.e("DatabaseRepository", "onError: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserBoundary> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
                Log.e("DatabaseRepository", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getUser(String email, final ApiCallback<UserBoundary> callback) {
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

    public void getOrdersByEmail(String email, final ApiCallback<List<Order>> callback) {
        //TODO: implement this method
    }

    public void createObject(ObjectBoundary object, final ApiCallback<ObjectBoundary> callback) {
        Call<ObjectBoundary> call = apiService.createObject(object);
        call.enqueue(new Callback<ObjectBoundary>() {
            @Override
            public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {
                if (response.isSuccessful()) {
                    ObjectBoundary object = response.body();
                    callback.onSuccess(object);
                    Log.d(" DatabaseRepository", "onResponse: " + object);
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

    public void getAssociation(ApiCallback<Association> callback) {
        Call<ObjectBoundary> call = apiService.getSpecificObject(
                UserSession.getInstance().getSUPERAPP(),
                UserSession.getInstance().getBoundaryId(),
                UserSession.getInstance().getSUPERAPP(),
                UserSession.getInstance().getUserEmail()
        );
        call.enqueue(new Callback<ObjectBoundary>() {
            @Override
            public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {
                if (response.isSuccessful()) {
                    Association association = new Association(response.body());
                    Log.d(" DatabaseRepository INSIDE GET ASSOCIATION", "onResponse: " + association);
                    callback.onSuccess(association);
                } else {
                    Log.d(" DatabaseRepository INSIDE GET ASSOCIATION", "onError: " + response.code());
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ObjectBoundary> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
                Log.d(" DatabaseRepository", "onFailure: " + t.getMessage());
            }
        });
    }
    public void createAssociation(Association association, ApiCallback<ObjectBoundary> callback){
        ObjectBoundary object = association.toObjectBoundary();
        Call<ObjectBoundary> call = apiService.createObject(object);
        call.enqueue(new Callback<ObjectBoundary>() {
            @Override
            public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {
                if (response.isSuccessful()) {
                    ObjectBoundary object = response.body();
                    callback.onSuccess(object);
                    Log.d(" DatabaseRepository", "onResponse: " + object);
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
    public void createCommand(String miniApp, String command,ApiCallback<List<ObjectBoundary>> callback){
        CommandBoundary commandBoundary = new CommandBoundary();
        commandBoundary.setCommand(command);
        commandBoundary.setCommandId(new CommandId(UserSession.getInstance().getSUPERAPP(), miniApp,"d"));
        commandBoundary.setInvokedBy(new InvokedBy(UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getUserEmail()));
        commandBoundary.setTargetObject(new TargetObject(UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getBoundaryId()));
        Call<List<ObjectBoundary>> call = apiService.command(miniApp, commandBoundary);
        call.enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
            public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful()) {
                    List<ObjectBoundary> objects = response.body();
                    callback.onSuccess(objects);
                    Log.d(" DatabaseRepository", "onResponse: " + objects);
                } else {
                    callback.onError("Error: " + response.code());
                    Log.d(" DatabaseRepository", "onError: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
                Log.d(" DatabaseRepository", "onFailure: " + t.getMessage());
            }
        });
    }

}



