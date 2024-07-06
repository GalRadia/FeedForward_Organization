package com.example.feedforward_association.ui.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.Restaurant;
import com.example.feedforward_association.models.server.object.ObjectBoundary;
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
       // repository.getAllOrders("2024b.gal.said", "ziv@gmail.com", 50, 0, callback);
        repository.getAllObjectsByType("Order", UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getUserEmail(), 50, 0, new ApiCallback<List<ObjectBoundary>>() {
            @Override
            public void onSuccess(List<ObjectBoundary> result) {
                callback.onSuccess(Order.convertObjectBoundaryList(result));
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getRestaurants(ApiCallback<List<Restaurant>> callback) {
        //  repository.getAllRestaurantsByCommand(callback);
       // repository.getAllRestaurants(UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getUserEmail(), 50, 0, callback);
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

    public void postOrder(Order order, ApiCallback<Order> callback) {
        ObjectBoundary object = order.convert(order);
       // repository.createObject(object, callback); TODO: to change
       // repository.createOrder(order, callback);
        repository.createObject(object, new ApiCallback<ObjectBoundary>() {
            @Override
            public void onSuccess(ObjectBoundary result) {
                Order order = new Order(result);
                callback.onSuccess(order);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void updateRestaurant(Restaurant restaurant, ApiCallback<Void> callback){
       // repository.updateRestaurant(restaurant, callback);
        ObjectBoundary object = restaurant.toObjectBoundary();
        repository.updateObject(object,callback);
    }


}