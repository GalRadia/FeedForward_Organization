package com.example.feedforward_association.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_association.R;
import com.example.feedforward_association.adapters.PickDonationAdapter;
import com.example.feedforward_association.databinding.FragmentHomeBinding;
import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Restaurant;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.gson.Gson;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private SearchView searchBar;
    private PickDonationAdapter adapter;
    private MaterialButtonToggleGroup toggleGroup;
    private MaterialButton KM5, KM15, KM30;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<String[]> locationPermissionLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                    if (fineLocationGranted != null && fineLocationGranted) {
                        getCurrentLocation();
                    } else if (coarseLocationGranted != null && coarseLocationGranted) {
                        getCurrentLocation();
                    } else {
                        Log.e("HomeFragment", "Location permission not granted");
                    }
                }
        );
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        findViews();
        initViews();
        requestLocationPermissions();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void findViews() {
        recyclerView = binding.recyclerViewDonations;
        searchBar = binding.VSearch;
        toggleGroup = binding.buttonGroup;
        KM5 = binding.KM5BTN;
        KM15 = binding.KM15BTN;
        KM30 = binding.KM30BTN;
    }

    private void initViews() {
        adapter = new PickDonationAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        homeViewModel.getRestaurants(new ApiCallback<List<Restaurant>>() {
            @Override
            public void onSuccess(List<Restaurant> restaurants) {
                Toast.makeText(getContext(), "Success + " + restaurants.size(), Toast.LENGTH_SHORT).show();
                adapter.setRestaurants(restaurants);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                Log.d("HomeFragment", "onError: " + error);
            }
        });

        adapter.setPickDonationCallback(restaurant -> {
            // Handle donation picking logic here
            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            String json = gson.toJson(restaurant);
            bundle.putString("restaurant", json);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_home_to_chooseFoodFragment, bundle);
        });

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filterOrders(newText);
                return true;
            }
        });

        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                double distance = 0;
                if (checkedId == KM5.getId()) {
                    distance = 5;
                } else if (checkedId == KM15.getId()) {
                    distance = 15;
                } else if (checkedId == KM30.getId()) {
                    distance = 30;
                }

                if (currentLocation != null) {
                    homeViewModel.getOrdersByLocation(distance, new ApiCallback<List<Restaurant>>() {
                        @Override
                        public void onSuccess(List<Restaurant> restaurants) {
                            adapter.setRestaurants(restaurants);
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            Log.d("HomeFragment", "onError: " + error);
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Current location is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        } else {
            getCurrentLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                homeViewModel.setCurrentLocation(location);
            } else {
                Log.e("HomeFragment", "Location is null");
            }
        });
    }
}
