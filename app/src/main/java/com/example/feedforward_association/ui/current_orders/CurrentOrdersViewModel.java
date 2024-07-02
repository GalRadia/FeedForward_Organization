package com.example.feedforward_association.ui.current_orders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.server.user.UserBoundary;
import com.example.feedforward_association.models.server.user.UserSession;
import com.example.feedforward_association.utils.Repository;

import java.util.List;

public class CurrentOrdersViewModel extends ViewModel {
    private Repository repository;

    private final MutableLiveData<String> mText;

    public CurrentOrdersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
        repository = Repository.getInstance();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void getOrders(ApiCallback<List<Order>> callback) {
        repository.getAllOrdersByCommand(callback);
        //repository.getAllOrders(UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getUserEmail(), 50, 0,callback);
    }

}