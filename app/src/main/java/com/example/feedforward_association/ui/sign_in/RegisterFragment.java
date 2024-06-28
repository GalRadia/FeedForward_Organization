package com.example.feedforward_association.ui.sign_in;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.feedforward_association.MainActivity;
import com.example.feedforward_association.databinding.FragmentRegisterBinding;
import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.server.user.RoleEnum;
import com.example.feedforward_association.models.server.user.UserBoundary;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;


public class RegisterFragment extends Fragment {
    SignInViewModel signInViewModel;
    FragmentRegisterBinding binding;
    TextInputEditText emailEditText;
    TextInputEditText usernameEditText;
    ExtendedFloatingActionButton registerButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        findViews();
        return root;


    }

    private void findViews() {
        emailEditText = binding.registerEmail;
        usernameEditText = binding.registerName;
        registerButton = binding.registerFinishBTN;
        initViess();
    }

    private void initViess() {
        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String avatar = "";
            RoleEnum role = RoleEnum.SUPERAPP_USER;
            signInViewModel.signUp(email, username, role, avatar, new ApiCallback<UserBoundary>() {
                @Override
                public void onSuccess(UserBoundary result) {
                    if (result != null) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        Gson gson = new Gson();
                        intent.putExtra("user", gson.toJson(result));
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else {
                        // Handle failure
                        Toast.makeText(getActivity(), "Email allready exists", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String error) {

                }
            });

        });
    }
}