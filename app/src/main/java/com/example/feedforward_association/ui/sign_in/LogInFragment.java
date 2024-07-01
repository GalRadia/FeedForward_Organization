package com.example.feedforward_association.ui.sign_in;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.feedforward_association.MainActivity;
import com.example.feedforward_association.R;
import com.example.feedforward_association.databinding.FragmentLogInBinding;
import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Association;
import com.example.feedforward_association.models.server.user.UserBoundary;
import com.example.feedforward_association.models.server.user.UserSession;
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
        registerButton.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id.action_logInFragment_to_registerFragment));

        logInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            signInViewModel.logIn(email, new ApiCallback<UserBoundary>() {
                @Override
                public void onSuccess(UserBoundary result) {
                    UserSession.getInstance().setUserEmail(result.getUserId().getEmail());
                    UserSession.getInstance().setBoundaryId(result.getUserName());
                    Toast.makeText(getContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                    signInViewModel.fetchAssociation(new ApiCallback<Association>() {
                        @Override
                        public void onSuccess(Association result) {
                            UserSession.getInstance().setAssociation(result);
                            Toast.makeText(getContext(), "Welcome " + result.getAssociationName(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(getContext(), "Cant find association", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getContext(), "Email doesnt exists", Toast.LENGTH_SHORT).show();

                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}




