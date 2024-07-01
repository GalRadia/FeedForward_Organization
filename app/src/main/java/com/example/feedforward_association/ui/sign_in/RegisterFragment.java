package com.example.feedforward_association.ui.sign_in;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.feedforward_association.BuildConfig;
import com.example.feedforward_association.MainActivity;
import com.example.feedforward_association.R;
import com.example.feedforward_association.databinding.FragmentRegisterBinding;
import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Association;
import com.example.feedforward_association.models.server.object.Location;
import com.example.feedforward_association.models.server.object.ObjectBoundary;
import com.example.feedforward_association.models.server.object.ObjectId;
import com.example.feedforward_association.models.server.user.UserBoundary;
import com.example.feedforward_association.models.server.user.UserSession;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Objects;


public class RegisterFragment extends Fragment {
    SignInViewModel signInViewModel;
    FragmentRegisterBinding binding;
    TextInputEditText emailEditText;
    TextInputEditText usernameEditText;
    TextInputEditText phoneEditText;
    TextInputEditText AddressEditText;
    ExtendedFloatingActionButton registerButton;
    PlacesClient placesClient;


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
        phoneEditText = binding.associationPhone;
        AddressEditText = binding.associationAddress;
        initViess();
    }

    private void initViess() {
        initializePlaces();
        autoComplete();
        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String avatar = "DEFAULT_AVATAR";
            signInViewModel.signUp(email, username, avatar, new ApiCallback<UserBoundary>() {
                @Override
                public void onSuccess(UserBoundary userBoundary) {
                    Toast.makeText(getActivity(), "User created successfully", Toast.LENGTH_SHORT).show();
                    Association association = new Association(new ObjectId(UserSession.getInstance().getSUPERAPP(),email), username, AddressEditText.getText().toString(), phoneEditText.getText().toString(), email, new Location(0.0, 0.0));
                    signInViewModel.createAssociation(association, new ApiCallback<ObjectBoundary>() {
                        @Override
                        public void onSuccess(ObjectBoundary objectBoundary) {
                            Toast.makeText(getActivity(), "Association created successfully", Toast.LENGTH_SHORT).show();
                            Gson gson = new Gson();
                            Association association = gson.fromJson((String) objectBoundary.getObjectDetails().get("Association"), Association.class);
                            UserSession.getInstance().setAssociation(association);
                            signInViewModel.updateProfile(userBoundary, objectBoundary.getObjectId().getId());
                            Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            UserSession.getInstance().setUserEmail(email);
                            UserSession.getInstance().setBoundaryId(objectBoundary.getObjectId().getId());
                            Toast.makeText(getActivity(), "Association created successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(getActivity(), "Cant create association", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getActivity(), "Email allready exists", Toast.LENGTH_SHORT).show();
                    Log.e("RegisterFragment", "onError: " + error);

                }
            });

        });
    }
    public void initializePlaces() {
        String apiKey = BuildConfig.MAPS_API_KEY;

        if (TextUtils.isEmpty(apiKey) || apiKey.equals("DEFAULT_API_KEY")) {
            Log.e("Places test", "No API key");
            return;
        }

        Places.initialize(getContext(), apiKey);
        placesClient = Places.createClient(getContext());
    }

    public void autoComplete() {
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    Log.i("Places", "Place: " + place.getName() + ", " + place.getId());
                }

                @Override
                public void onError(@NonNull Status status) {
                    Log.i("Places", "An error occurred: " + status);
                }
            });
        } else {
            Log.e("Places", "Autocomplete fragment is null");
        }
    }

}