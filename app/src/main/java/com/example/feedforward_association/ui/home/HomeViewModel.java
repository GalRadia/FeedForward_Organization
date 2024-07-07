package com.example.feedforward_association.ui.home;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.Restaurant;
import com.example.feedforward_association.models.server.DistanceUnit;
import com.example.feedforward_association.models.server.object.ObjectBoundary;
import com.example.feedforward_association.models.server.user.UserSession;
import com.example.feedforward_association.utils.Repository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private Repository repository;
    private MutableLiveData<Location> currentLocation = new MutableLiveData<>();

    public HomeViewModel(Application application) {
        super(application);
        Log.d("HomeViewModel", "Initializing HomeViewModel...");

        repository = Repository.getInstance();
    }

    public void setCurrentLocation(Location location) {
        currentLocation.setValue(location);
    }

    public MutableLiveData<Location> getCurrentLocation() {
        return currentLocation;
    }

    public void getRestaurants(ApiCallback<List<Restaurant>> callback) {
        repository.getAllObjectsByType("Restaurant", UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getUserEmail(), 50, 0, new ApiCallback<List<ObjectBoundary>>() {
            @Override
            public void onSuccess(List<ObjectBoundary> result) {
                callback.onSuccess(Restaurant.convertObjectBoundaryList(result));
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getOrdersByLocation(double distance, ApiCallback<List<Restaurant>> callback) {
        if (currentLocation.getValue() != null) {
            Location location = currentLocation.getValue();
            com.example.feedforward_association.models.server.object.Location serverLocation =
                    new com.example.feedforward_association.models.server.object.Location(location.getLatitude(), location.getLongitude());
            repository.getAllOrdersByCommandAndLocation(DistanceUnit.KILOMETERS, serverLocation, distance, callback);
        } else {
            Log.e("HomeViewModel", "Current location is null");
            callback.onError("Current location is not available");
        }
    }

    public void updateRestaurant(Restaurant restaurant, ApiCallback<Void> apiCallback) {
        repository.updateObject(restaurant.toObjectBoundary(), apiCallback);
    }

    public void postOrder(Order order, ApiCallback<Order> apiCallback) {
        repository.createObject(order.convert(order), new ApiCallback<ObjectBoundary>() {
            @Override
            public void onSuccess(ObjectBoundary result) {
                apiCallback.onSuccess(new Order(result));
            }

            @Override
            public void onError(String error) {
                apiCallback.onError(error);
            }
        });
    }
}
