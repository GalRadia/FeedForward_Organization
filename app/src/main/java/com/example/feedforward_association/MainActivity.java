package com.example.feedforward_association;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.feedforward_association.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_Current_Orders, R.id.navigation_history_orders)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
//        List<Food> foods = new ArrayList<>();
//        foods.add(new Food("apple", "yammy", 10));
//        Order o = new Order("DOCNATOR", "132,123", "12313 ", "999", foods, OrderStatus.PENDING);
//        DatabaseRepository databaseRepository = new DatabaseRepository();
//        databaseRepository.createOrder(o, new ApiCallback<Order>() {
//            @Override
//            public void onSuccess(Order order) {
//                System.out.println("order created");
//            }
//
//            @Override
//            public void onError(String error) {
//                Log.d(TAG, "onError: " + error);
//            }
//        });


    }

}