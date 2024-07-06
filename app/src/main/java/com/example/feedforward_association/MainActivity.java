package com.example.feedforward_association;

import android.os.Bundle;
import android.widget.Toast;

import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Association;
import com.example.feedforward_association.models.Food;
import com.example.feedforward_association.models.Restaurant;
import com.example.feedforward_association.models.server.object.Location;
import com.example.feedforward_association.models.server.object.ObjectBoundary;
import com.example.feedforward_association.models.server.object.ObjectId;
import com.example.feedforward_association.models.server.user.UserBoundary;
import com.example.feedforward_association.models.server.user.UserSession;
import com.example.feedforward_association.utils.Repository;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.feedforward_association.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  initData();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nestedGraph, R.id.navigation_Current_Orders, R.id.navigation_history_orders)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }


    private void CheckPremmitions() {

        ActivityResultContracts.RequestMultiplePermissions requestMultiplePermissions = new ActivityResultContracts.RequestMultiplePermissions();
        requestMultiplePermissions.createIntent(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION});

    }

    private void initData() {
//        Association association = new Association();
//        association.setAssociationName("LATET");
//        association.setAssociationEmail("Gal@mail.com");
//        association.setAssociationPhone("054-1234567");
//        association.setAssociationAddress("Hertzel 1, Tel Aviv");
//        association.setAssociationLocation(new Location(32.1234, 34.1234));
//        association.setAssociationId(new ObjectId("2024b.gal.said", "123"));
        Repository repository = Repository.getInstance();
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName("mcDonalds");
        restaurant.setRestaurantEmail(UserSession.getInstance().getUserEmail());
        restaurant.setRestaurantPhone("054-1234567");
        restaurant.setRestaurantAddress("Hertzel 1, Tel Aviv");
        restaurant.setRestaurantLocation(new Location(32.1234, 34.1234));
        ArrayList<Food> storage = new ArrayList<>();
        storage.add(new Food( "Big Mac", "KOSHER", 20,"11/10/2010"));
        storage.add(new Food( "Fries", "VEGAN", 10,"11/10/2010"));
        storage.add(new Food( "Coca Cola", "VEGI", 5,"11/10/2010"));

        restaurant.setStorage(storage);
        restaurant.setRestaurantId(new ObjectId(UserSession.getInstance().getSUPERAPP(), "123"));
        repository.createObject(restaurant.toObjectBoundary(), new ApiCallback<ObjectBoundary>() {
            @Override
            public void onSuccess(ObjectBoundary result) {
                Toast.makeText(MainActivity.this, "Restaurant created", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MainActivity.this, "Restaurant not created", Toast.LENGTH_SHORT).show();
            }
        });
//        repository.createAssociation(association, new ApiCallback<ObjectBoundary>() {
//            @Override
//            public void onSuccess(ObjectBoundary result) {
//
//            }
//
//            @Override
//            public void onError(String error) {
//
//            }
//        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}