package com.example.feedforward_association.ui.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.Restaurant;
import com.example.feedforward_association.models.server.user.UserBoundary;
import com.example.feedforward_association.models.server.user.UserSession;
import com.example.feedforward_association.utils.Repository;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private Repository repository;
    private final MutableLiveData<String> mText;

    // private final MutableLiveData<List<Order>> mOrders;
    public HomeViewModel() {
        Log.d("HomeViewModel", "Initializing HomeViewModel...");

        repository = Repository.getInstance();
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public void getOrders(ApiCallback<List<Order>> callback) {
        repository.getAllOrders("2024b.gal.said", "ziv@gmail.com", 50, 0, callback);
    }

    public void getRestaurants(ApiCallback<List<Restaurant>> callback) {
        repository.getAllRestaurants(UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getUserEmail(), 50, 0, callback);
    }

    public void postOrder(Order order, ApiCallback<Order> callback) {
        repository.createOrder(order, callback);
    }

    public void updateRestaurant(Restaurant restaurant, ApiCallback<Restaurant> callback) {
        repository.updateRestaurant(restaurant, callback);
    }


}