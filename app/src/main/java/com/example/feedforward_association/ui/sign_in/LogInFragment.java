package com.example.feedforward_association.ui.sign_in;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.feedforward_association.MainActivity;
import com.example.feedforward_association.databinding.FragmentLogInBinding;
import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.server.user.UserBoundary;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;


public class LogInFragment extends Fragment {
    FragmentLogInBinding binding;
    SignInViewModel signInViewModel;
    TextInputEditText emailEditText;
    ExtendedFloatingActionButton logInButton;
    ExtendedFloatingActionButton registerButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        binding = FragmentLogInBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        findViews();
        return root;
    }

    private void findViews() {
        emailEditText = binding.emailEditText;
        logInButton = binding.buttonLogIn;
        registerButton = binding.buttonSignUp;
        initViews();
    }

    private void initViews() {
        logInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            signInViewModel.logIn(email, new ApiCallback<UserBoundary>() {

                @Override
                public void onSuccess(UserBoundary result) {
                    if (result == null) {
                        Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    Gson gson = new Gson();
                    intent.putExtra("user", gson.toJson(result));
                    startActivity(intent);
                    getActivity().finish();
                }

                @Override
                public void onError(String error) {

                }
            });
        });
        registerButton.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(binding.getRoot().getId(), new RegisterFragment()).commit();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}




