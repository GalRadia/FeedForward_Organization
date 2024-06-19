package com.example.feedforward_association.ui.current_orders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CurrentOrdersViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CurrentOrdersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}