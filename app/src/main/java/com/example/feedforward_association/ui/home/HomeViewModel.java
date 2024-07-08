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
        getRestaurantsByLocation(5,callback);
    }

    public void getRestaurantsByLocation(double distance, ApiCallback<List<Restaurant>> callback) {
            repository.getAllRestaurantsByCommandAndLocation(DistanceUnit.KILOMETERS, UserSession.getInstance().getAssociation().getAssociationLocation(), distance, callback);
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
