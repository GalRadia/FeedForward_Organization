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

import com.bumptech.glide.Glide;
import com.example.feedforward_association.MainActivity;
import com.example.feedforward_association.R;
import com.example.feedforward_association.databinding.FragmentLogInBinding;
import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Association;
import com.example.feedforward_association.models.server.user.UserBoundary;
import com.example.feedforward_association.models.server.user.UserSession;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;


public class LogInFragment extends Fragment {
    private FragmentLogInBinding binding;
    private SignInViewModel signInViewModel;
    private TextInputEditText emailEditText;
    private ExtendedFloatingActionButton logInButton;
    private ExtendedFloatingActionButton registerButton;
    private ShapeableImageView image;

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
        image = binding.imageView;
        initViews();
    }

    private void initViews() {
        registerButton.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id.action_logInFragment_to_registerFragment));
        Glide.with(this).load(R.drawable.non_profit_organisation).placeholder(R.drawable.ic_launcher_foreground).fitCenter().into(image);

        logInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            signInViewModel.logIn(email, new ApiCallback<UserBoundary>() {
                @Override
                public void onSuccess(UserBoundary result) {
                    UserSession.getInstance().setUser(result);// save user in session
                    Toast.makeText(getContext(), getString(R.string.logged_in_successfully), Toast.LENGTH_SHORT).show();

                    signInViewModel.fetchAssociation(result.getUserName(), result.getUserId().getEmail(), UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getSUPERAPP(), new ApiCallback<Association>() {
                        @Override
                        public void onSuccess(Association result) {
                            UserSession.getInstance().setAssociation(result); // save association in session
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(getContext(), getString(R.string.association_doesnt_exists), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getContext(), getString(R.string.email_doesnt_exists), Toast.LENGTH_SHORT).show();

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




