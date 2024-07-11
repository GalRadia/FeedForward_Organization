package com.example.feedforward_association.utils;

import android.util.Log;

import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.interfaces.ApiService;
import com.example.feedforward_association.models.Association;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.Restaurant;
import com.example.feedforward_association.models.server.DistanceUnit;
import com.example.feedforward_association.models.server.command.CommandBoundary;
import com.example.feedforward_association.models.server.command.CommandId;
import com.example.feedforward_association.models.server.command.InvokedBy;
import com.example.feedforward_association.models.server.command.TargetObject;
import com.example.feedforward_association.models.server.object.Location;
import com.example.feedforward_association.models.server.object.ObjectBoundary;
import com.example.feedforward_association.models.server.user.NewUserBoundary;
import com.example.feedforward_association.models.server.user.RoleEnum;
import com.example.feedforward_association.models.server.user.UserBoundary;
import com.example.feedforward_association.models.server.user.UserSession;

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


    public void getAllOrdersByCommand(ApiCallback<List<Order>> callback) {
        CommandBoundary commandBoundary = new CommandBoundary("SBECT");
        Map<String, Object> commandMap = Map.of("type", "Order", "email", UserSession.getInstance().getUser().getUserId().getEmail());
        commandBoundary.setCommandAttributes(commandMap);
        Call<List<ObjectBoundary>> call = apiService.command(UserSession.getInstance().getSUPERAPP(), commandBoundary);
        call.enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
            public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful()) {
                    List<Order> orders = Order.convertObjectBoundaryList(response.body());
                    callback.onSuccess(orders);
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<ObjectBoundary>> call, Throwable throwable) {

            }
        });

    }
    public void updateObject(ObjectBoundary object, final ApiCallback<Void> callback) {
        UserBoundary user = UserSession.getInstance().getUser();
        user.setRole(RoleEnum.SUPERAPP_USER);
        updateUser(user, new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Call<Void> call = apiService.updateObject(object.getObjectId().getId(), object.getObjectId().getSuperapp(), object.getObjectId().getSuperapp(), object.getCreatedBy().getUserId().getEmail(), object);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            user.setRole(RoleEnum.MINIAPP_USER);
                            updateUser(user, new ApiCallback<Void>() {
                                @Override
                                public void onSuccess(Void result) {
                                    callback.onSuccess(null);
                                    Log.d(" DatabaseRepository", "onResponse: " + object);
                                }

                                @Override
                                public void onError(String error) {

                                }
                            });

                        } else {
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

            @Override
            public void onError(String error) {

            }
        });

    }

    public void updateUser(UserBoundary user, final ApiCallback<Void> callback) {
        Call<Void> call = apiService.updateUser(user.getUserId().getSuperapp(), user.getUserId().getEmail(), user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                    Log.d(" DatabaseRepository", "onResponse: " + response.body());
                } else {
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

    public void createUser(String email, String userName, String avatar, RoleEnum role, final ApiCallback<UserBoundary> callback) {
        NewUserBoundary user = new NewUserBoundary(email, role, userName, avatar);
        Call<UserBoundary> call = apiService.createUser(user);
        call.enqueue(new Callback<UserBoundary>() {
            @Override
            public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
                if (response.isSuccessful()) {
                    UserBoundary user = response.body();
                    UserSession.getInstance().setUser(user);
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
        Call<UserBoundary> call = apiService.getUser(UserSession.getInstance().getSUPERAPP(), email);
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

    public void createObject(ObjectBoundary object, final ApiCallback<ObjectBoundary> callback) {
        UserBoundary user = UserSession.getInstance().getUser();
        user.setRole(RoleEnum.SUPERAPP_USER);
        updateUser(user, new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Call<ObjectBoundary> call = apiService.createObject(object);
                call.enqueue(new Callback<ObjectBoundary>() {
                    @Override
                    public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {
                        if (response.isSuccessful()) {
                            user.setRole(RoleEnum.MINIAPP_USER);
                            updateUser(user, new ApiCallback<Void>() {
                                @Override
                                public void onSuccess(Void result) {
                                    ObjectBoundary object = response.body();
                                    callback.onSuccess(object);
                                    Log.d(" DatabaseRepository", "onResponse: " + object);
                                }

                                @Override
                                public void onError(String error) {
//
                                }
                            });

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

            @Override
            public void onError(String error) {
//
            }
        });

    }

    public void getSpecificObject(String id, String userSuperApp, String userEmail, String superApp, final ApiCallback<ObjectBoundary> callback) {
        Call<ObjectBoundary> call = apiService.getSpecificObject(userSuperApp, id, superApp, userEmail);
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

    public void getAllRestaurantsByCommandAndLocation(DistanceUnit unit, Location location, double distance, ApiCallback<List<Restaurant>> callback) {
        CommandBoundary commandBoundary = new CommandBoundary("SBRT");
        Map<String, Object> commandMap = Map.of(
                "type", "Restaurant",
                "lat", location.getLat(),
                "lng", location.getLng(),
                "distance", distance,
                "distanceUnit", unit
        );
        commandBoundary.setCommandAttributes(commandMap);
        Call<List<ObjectBoundary>> call = apiService.command(UserSession.getInstance().getSUPERAPP(), commandBoundary);

        call.enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
            public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful()){
                    List<Restaurant> restaurants = Restaurant.convertObjectBoundaryList(response.body());
                    callback.onSuccess(restaurants);
                }
                else {
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ObjectBoundary>> call, Throwable throwable) {
                callback.onError("Error: " + throwable.getMessage());
            }
        });


    }

}



