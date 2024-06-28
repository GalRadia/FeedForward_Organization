package com.example.feedforward_association.ui.sign_in;

import androidx.lifecycle.ViewModel;

import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.server.user.RoleEnum;
import com.example.feedforward_association.models.server.user.UserBoundary;
import com.example.feedforward_association.utils.Repository;

public class SignInViewModel extends ViewModel {
    private Repository repository;

    public SignInViewModel() {
        repository = Repository.getInstance();

    }
    public void logIn(String email, ApiCallback<UserBoundary> callback) {
         repository.getUser(email, callback);
    }
    public void signUp(String email, String username, RoleEnum role, String avatar, ApiCallback<UserBoundary> callback) {
        repository.createUser(email,username,role,avatar, callback);
    }
}
