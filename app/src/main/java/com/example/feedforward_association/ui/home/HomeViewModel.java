package com.example.feedforward_association.ui.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.Restaurant;
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

    public List<Order> getOrders(ApiCallback<List<Order>> callback) {
        return repository.getAllOrders("2024b.gal.said", "ziv@gmail.com", 50, 0, callback);
    }

    public List<Restaurant> getRestaurants(ApiCallback<List<Restaurant>> callback) {
        return repository.getAllRestaurants("2024b.gal.said", "ziv@gmail.com", 50, 0, callback);
    }

}