package com.example.feedforward_association.ui.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Association;
import com.example.feedforward_association.models.server.object.ObjectBoundary;
import com.example.feedforward_association.utils.Repository;

public class HistoryViewModel extends ViewModel {

    private Repository repository;

    public HistoryViewModel() {
        repository = Repository.getInstance();

    }

    public void updateAssociation(Association association, ApiCallback<Void> callback) {

        // repository.updateAssociation(association,callback);
        ObjectBoundary associationBoundary = association.toObjectBoundary();
        repository.updateObject(associationBoundary, callback);
    }


}