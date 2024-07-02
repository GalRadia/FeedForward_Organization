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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Objects;


public class RegisterFragment extends Fragment {
   private SignInViewModel signInViewModel;
   private FragmentRegisterBinding binding;
   private TextInputEditText emailEditText;
   private TextInputEditText usernameEditText;
   private TextInputEditText phoneEditText;
   private TextInputEditText AddressEditText;
   private ExtendedFloatingActionButton registerButton;
   private PlacesClient placesClient;
    private LatLng latLng;


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
       // initializePlaces();
        autoComplete();
        AddressEditText.setEnabled(false);
        registerButton.setOnClickListener(v -> {
            if (!validate()) {
                return;
            }
            String email = emailEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String avatar = "DEFAULT_AVATAR";
            signInViewModel.signUp(email, username, avatar, new ApiCallback<UserBoundary>() {
                @Override
                public void onSuccess(UserBoundary userBoundary) {
                    Toast.makeText(getActivity(), "User created successfully", Toast.LENGTH_SHORT).show();
                    Association association = new Association(new ObjectId(UserSession.getInstance().getSUPERAPP(),email), username, AddressEditText.getText().toString(), phoneEditText.getText().toString(), email, new Location(latLng.latitude, latLng.longitude));
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
    private boolean validate() {
        if (TextUtils.isEmpty(emailEditText.getText().toString())) {
            emailEditText.setError("Email is required");
            return false;
        }
        if (TextUtils.isEmpty(usernameEditText.getText().toString())) {
            usernameEditText.setError("Username is required");
            return false;
        }
        if (TextUtils.isEmpty(phoneEditText.getText().toString())) {
            phoneEditText.setError("Phone is required");
            return false;
        }
        if (TextUtils.isEmpty(AddressEditText.getText().toString())) {
            AddressEditText.setError("Address is required");
            return false;
        }
        return true;
    }

    public void autoComplete() {
        Places.initialize(getActivity().getApplicationContext(), BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(getActivity());

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(31.937750, 34.834125),
                new LatLng(32.225814, 34.896383)
        ));
        autocompleteFragment.setCountries("IL");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                AddressEditText.setText(place.getName());
                latLng = place.getLatLng();


            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e("Places test", "An error occurred: " + status);
            }
        });


    }

}