package com.example.feedforward_association.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.utils.DatabaseRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private DatabaseRepository databaseRepository;
    private final MutableLiveData<String> mText;
    private final LiveData<List<Order>> mOrders;

    public HomeViewModel() {
        databaseRepository = new DatabaseRepository();
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        mOrders=databaseRepository.getAllOrders("2024b.gal.said",
                "ziv@gmail.com",5,0);
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<List<Order>> getOrders() {
        return mOrders;
    }

}