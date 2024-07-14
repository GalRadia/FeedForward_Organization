package com.example.feedforward_association.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.interfaces.ApiService;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.Restaurant;
import com.example.feedforward_association.models.server.CommandOptions;
import com.example.feedforward_association.models.server.DistanceUnit;
import com.example.feedforward_association.models.server.command.CommandBoundary;
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
    private final ApiService apiService;

    private Repository() {

        this.apiService = RetrofitClient.getApiService();
    }

    /**
     * Get instance repository.
     * @return the instance
     */

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    /**
     * Get all orders by command.
     * @param callback the callback
     */

    public void getAllOrdersByCommand(ApiCallback<List<Order>> callback) {
        CommandBoundary commandBoundary = new CommandBoundary(CommandOptions.SBECT.toString());
        Map<String, Object> commandMap = Map.of("type", "Order", "email", UserSession.getInstance().getUser().getUserId().getEmail());
        commandBoundary.setCommandAttributes(commandMap);
        Call<List<ObjectBoundary>> call = apiService.command(UserSession.getInstance().getSUPERAPP(), commandBoundary);
        call.enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
            public void onResponse(@NonNull Call<List<ObjectBoundary>> call, @NonNull Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<Order> orders = Order.convertObjectBoundaryList(response.body());
                    callback.onSuccess(orders);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ObjectBoundary>> call, @NonNull Throwable throwable) {
                callback.onError("Error: " + throwable.getMessage());
            }
        });

    }

    /**
     * update object.
     * @param object -ObjectBoundary
     * @param callback -ApiCallback
     */
    public void updateObject(ObjectBoundary object, final ApiCallback<Void> callback) {
        UserBoundary user = UserSession.getInstance().getUser();
        user.setRole(RoleEnum.SUPERAPP_USER); // change role to superapp user
        updateUser(user, new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Call<Void> call = apiService.updateObject(object.getObjectId().getId(), object.getObjectId().getSuperapp(), object.getObjectId().getSuperapp(), object.getCreatedBy().getUserId().getEmail(), object);
                call.enqueue(new Callback<Void>() { // update Object because only superapp user can update object
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            user.setRole(RoleEnum.MINIAPP_USER); // change role to miniapp user after updating object
                            updateUser(user, new ApiCallback<Void>() {
                                @Override
                                public void onSuccess(Void result) {
                                    callback.onSuccess(null);
                                }

                                @Override
                                public void onError(String error) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        callback.onError("Failure: " + t.getMessage());
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    /**
     * update user.
     * @param user - UserBoundary
     * @param callback - ApiCallback
     */
    public void updateUser(UserBoundary user, final ApiCallback<Void> callback) {
        Call<Void> call = apiService.updateUser(user.getUserId().getSuperapp(), user.getUserId().getEmail(), user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onError("Failure: " + t.getMessage());
            }
        });
    }

    /**
     * Create user.
     * @param email - String
     * @param userName - String
     * @param avatar - String
     * @param role - RoleEnum
     * @param callback - ApiCallback
     */
    public void createUser(String email, String userName, String avatar, RoleEnum role, final ApiCallback<UserBoundary> callback) {
        NewUserBoundary user = new NewUserBoundary(email, role, userName, avatar);
        Call<UserBoundary> call = apiService.createUser(user);
        call.enqueue(new Callback<UserBoundary>() {
            @Override
            public void onResponse(@NonNull Call<UserBoundary> call, @NonNull Response<UserBoundary> response) {
                if (response.isSuccessful()) {
                    UserBoundary user = response.body();
                    UserSession.getInstance().setUser(user); // set user in session
                    callback.onSuccess(user);
                } else {
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserBoundary> call, @NonNull Throwable t) {
                callback.onError("Failure: " + t.getMessage());
            }
        });
    }

    /**
     * Get user.
     * @param email - String
     * @param callback - ApiCallback
     */
    public void getUser(String email, final ApiCallback<UserBoundary> callback) {
        Call<UserBoundary> call = apiService.getUser(UserSession.getInstance().getSUPERAPP(), email);
        call.enqueue(new Callback<UserBoundary>() {
            @Override
            public void onResponse(@NonNull Call<UserBoundary> call, @NonNull Response<UserBoundary> response) {
                if (response.isSuccessful()) {
                    UserBoundary user = response.body();
                    callback.onSuccess(user);
                } else {
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserBoundary> call, @NonNull Throwable t) {
                callback.onError("Failure: " + t.getMessage());
            }
        });
    }

    /**
     * Create object.
     * @param object - ObjectBoundary
     * @param callback - ApiCallback
     */
    public void createObject(ObjectBoundary object, final ApiCallback<ObjectBoundary> callback) {
        UserBoundary user = UserSession.getInstance().getUser();
        user.setRole(RoleEnum.SUPERAPP_USER); // change role to superapp user
        updateUser(user, new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Call<ObjectBoundary> call = apiService.createObject(object);
                call.enqueue(new Callback<ObjectBoundary>() {
                    @Override
                    public void onResponse(@NonNull Call<ObjectBoundary> call, @NonNull Response<ObjectBoundary> response) {
                        if (response.isSuccessful()) {
                            user.setRole(RoleEnum.MINIAPP_USER); // change role to miniapp user after creating object
                            updateUser(user, new ApiCallback<Void>() {
                                @Override
                                public void onSuccess(Void result) {
                                    ObjectBoundary object = response.body();
                                    callback.onSuccess(object);
                                }

                                @Override
                                public void onError(String error) {
//
                                }
                            });

                        } else {
                            callback.onError("Error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ObjectBoundary> call, @NonNull Throwable t) {
                        callback.onError("Failure: " + t.getMessage());
                    }
                });
            }

            @Override
            public void onError(String error) {
//
            }
        });

    }

    /**
     * Get specific object by id.
     * @param id - String
     * @param userSuperApp - String
     * @param userEmail - String
     * @param superApp - String
     * @param callback - ApiCallback
     */
    public void getSpecificObject(String id, String userSuperApp, String userEmail, String superApp, final ApiCallback<ObjectBoundary> callback) {
        Call<ObjectBoundary> call = apiService.getSpecificObject(userSuperApp, id, superApp, userEmail);
        call.enqueue(new Callback<ObjectBoundary>() {
            @Override
            public void onResponse(@NonNull Call<ObjectBoundary> call, @NonNull Response<ObjectBoundary> response) {
                if (response.isSuccessful()) {
                    ObjectBoundary object = response.body();
                    callback.onSuccess(object);
                } else {
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ObjectBoundary> call, @NonNull Throwable t) {
                callback.onError("Failure: " + t.getMessage());
            }
        });
    }

    /**
     * Get all restaurants.
     * @param unit - DistanceUnit
     * @param location - Location(lat,lng)
     * @param distance - double
     * @param callback - ApiCallback
     */
    public void getAllRestaurantsByCommandAndLocation(DistanceUnit unit, Location location, double distance, ApiCallback<List<Restaurant>> callback) {
        CommandBoundary commandBoundary = new CommandBoundary(CommandOptions.SBRT.toString());
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
            public void onResponse(@NonNull Call<List<ObjectBoundary>> call, @NonNull Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    List<Restaurant> restaurants = Restaurant.convertObjectBoundaryList(response.body());
                    callback.onSuccess(restaurants);
                }
                else {
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ObjectBoundary>> call, @NonNull Throwable throwable) {
                callback.onError("Error: " + throwable.getMessage());
            }
        });


    }

}



