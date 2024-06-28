package com.example.feedforward_association.ui.home;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.feedforward_association.adapters.FoodAdapter;
import com.example.feedforward_association.databinding.FragmentChooseFoodBinding;
import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Food;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.OrderStatus;
import com.example.feedforward_association.models.Restaurant;
import com.example.feedforward_association.models.WhoCarries;
import com.example.feedforward_association.models.server.object.ObjectId;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ChooseFoodFragment extends Fragment {

    private HomeViewModel mViewModel;
    private RecyclerView recyclerView;
    private MaterialButton finishButton;
    private FragmentChooseFoodBinding binding;
    private FoodAdapter adapter;
    private Restaurant restaurant;
    private ArrayList<Food> selectedFoods = new ArrayList<>();

    public static ChooseFoodFragment newInstance() {
        return new ChooseFoodFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        binding = FragmentChooseFoodBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        findViews();
        return root;
    }


    private void findViews() {
        recyclerView = binding.RCVChooseFood;
        finishButton = binding.BTNFinish;
        initViews();
    }

    private void initViews() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            restaurant = gson.fromJson(bundle.getString("restaurant"), Restaurant.class);
        }
        adapter = new FoodAdapter(getContext(), restaurant.getStorage());
        adapter.setFoodCallback(food -> showFoodDialog(food));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        finishButton.setOnClickListener(v -> {
            //TODO dialog who carries
            Order order = new Order(new ObjectId("2024b.gal.said",""),restaurant.getRestaurantEmail(),restaurant.getRestaurantName(),
                    restaurant.getRestaurantLocation(),"","",selectedFoods, OrderStatus.PENDING, WhoCarries.TAKE_AWAY,"",restaurant.getRestaurantLocation());//TODO CHANGE
            mViewModel.postOrder(order, new ApiCallback<Order>() {
                @Override
                public void onSuccess(Order result) {

                }

                @Override
                public void onError(String error) {

                }
            });
            mViewModel.updateRestaurant(restaurant, new ApiCallback<Restaurant>() {
                @Override
                public void onSuccess(Restaurant result) {

                }

                @Override
                public void onError(String error) {

                }
            });
            //TODO post order
            //TODO update restaurant

        });
    }

    private void showFoodDialog(Food food) {
        // Create a LinearLayout to hold the TextView and Slider
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 30, 30, 30);

        // Create a TextView to display the current value
        final MaterialTextView tvSliderValue = new MaterialTextView(getContext());
        tvSliderValue.setText("Current value: 0");
        tvSliderValue.setTextSize(18);
        layout.addView(tvSliderValue);

        // Create a Slider
        final Slider slider = new Slider(getContext());
        slider.setValueFrom(0);
        slider.setValueTo(food.getQuantity());
        slider.setStepSize(1);
        layout.addView(slider);

        // Set a listener to update the TextView as the Slider is moved
        slider.addOnChangeListener((slider1, value, fromUser) -> {
            tvSliderValue.setText("Current value: " + (int) value);
        });

        // Build and show the AlertDialog
        new AlertDialog.Builder(slider.getContext())
                .setTitle("Choose a value")
                .setView(layout)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Handle the OK button press
                    int selectedValue = (int) slider.getValue();
                    selectedFoods.add(new Food(food.getName(), food.getDescription(), selectedValue));
                    restaurant.getStorage().get(restaurant.getStorage().indexOf(food)).setQuantity(food.getQuantity() - selectedValue);
                    // Do something with the selected value

                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle the Cancel button press
                })
                .show();
    }

}