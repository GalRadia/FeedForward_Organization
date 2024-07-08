package com.example.feedforward_association.ui.sign_in;

import androidx.lifecycle.ViewModel;

import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Association;
import com.example.feedforward_association.models.server.object.ObjectBoundary;
import com.example.feedforward_association.models.server.user.UserBoundary;
import com.example.feedforward_association.utils.Repository;

import java.util.Objects;

public class SignInViewModel extends ViewModel {
    private Repository repository;

    public SignInViewModel() {
        repository = Repository.getInstance();

    }

    public void logIn(String email, ApiCallback<UserBoundary> userBoundaryApiCallback) {
        repository.getUser(email, userBoundaryApiCallback);
    }

    public void fetchAssociation(String id, String email, String superApp, String userSuperApp, ApiCallback<Association> callback) {
        // repository.getAssociation(callback);
        repository.getSpecificObject(id, superApp, email, userSuperApp, new ApiCallback<ObjectBoundary>() {
            @Override
            public void onSuccess(ObjectBoundary result) {
                if ("Association".equals(result.getType())) {
                    Association association = new Association(result);
                    callback.onSuccess(association);
                } else {
                    callback.onError("Not an association");
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void signUp(String email, String username, String avatar, ApiCallback<UserBoundary> callback) {
        repository.createUser(email, username, avatar, callback);

    }

    public void updateProfile(UserBoundary user, String id) {
        user.setUserName(id);
        repository.updateUser(user, new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void createAssociation(Association association, ApiCallback<ObjectBoundary> callback) {
        ObjectBoundary object = association.toObjectBoundary();
        repository.createObject(object, callback);
        // repository.createAssociation(association, callback);
    }


}
