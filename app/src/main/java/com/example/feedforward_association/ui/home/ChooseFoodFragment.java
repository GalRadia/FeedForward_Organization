package com.example.feedforward_association.ui.home;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.feedforward_association.R;
import com.example.feedforward_association.adapters.FoodAdapter;
import com.example.feedforward_association.databinding.FragmentChooseFoodBinding;
import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Food;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.OrderStatus;
import com.example.feedforward_association.models.Restaurant;
import com.example.feedforward_association.models.WhoCarries;
import com.example.feedforward_association.models.server.object.Location;
import com.example.feedforward_association.models.server.object.ObjectId;
import com.example.feedforward_association.models.server.user.UserSession;
import com.example.feedforward_association.utils.Repository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ChooseFoodFragment extends Fragment {

    private HomeViewModel mViewModel;
    private RecyclerView recyclerView;
    private MaterialButton finishButton;
    private FragmentChooseFoodBinding binding;
    private FoodAdapter adapter;
    private Restaurant restaurant;
    private Spinner spinner;
    private ArrayList<Food> selectedFoods = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        binding = FragmentChooseFoodBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Gson gson = new Gson();
        if (getArguments() != null)
            restaurant = gson.fromJson(getArguments().getString("restaurant"), Restaurant.class);
        else
            navigateBackToHome();
        findViews();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void findViews() {
        recyclerView = binding.RCVChooseFood;
        finishButton = binding.BTNFinish;
        spinner = binding.spinner;
        initViews();
    }

    private void initViews() {
        setSpinner();
        adapter = new FoodAdapter(getContext(), restaurant.getStorage());
        adapter.setFoodCallback(food -> showFoodDialog(food));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        finishButton.setOnClickListener(v -> {
            //TODO dialog who carries

            Order order = new Order(new ObjectId("2024b.gal.said", ""), restaurant.getRestaurantEmail(), restaurant.getRestaurantName(),
                    restaurant.getRestaurantLocation(), "", "", selectedFoods, OrderStatus.PENDING, WhoCarries.values()[spinner.getSelectedItemPosition()], UserSession.getInstance().getAssociation().getAssociationName(), new Location());//TODO CHANGE
            mViewModel.postOrder(order, new ApiCallback<Order>() {
                @Override
                public void onSuccess(Order result) {
                    restaurant = updateStorage(selectedFoods);
                    Toast.makeText(getContext(), "Order posted", Toast.LENGTH_SHORT).show();
                    mViewModel.updateRestaurant(restaurant, new ApiCallback<Restaurant>() {
                        @Override
                        public void onSuccess(Restaurant result) {
                            Toast.makeText(getContext(), "Restaurant updated", Toast.LENGTH_SHORT).show();
                            navigateBackToHome();
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(getContext(), "Restaurant update failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getContext(), "Order failed", Toast.LENGTH_SHORT).show();

                }
            });
            //TODO post order
            //TODO update restaurant

        });
    }

    private void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{"Take Away", "Delivery"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    private void showFoodDialog(Food food) {
        // Create a LinearLayout to hold the TextView and Slider
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 30, 30, 30);

        // Create a TextView to display the selected foods
        if (!selectedFoods.isEmpty()) {
            final MaterialTextView selectedFoodsTextView = showSelectedFood();
            layout.addView(selectedFoodsTextView);
        }

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
                    updateFoodSelection(food, selectedValue);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle the Cancel button press
                })
                .show();
    }

    private @NonNull MaterialTextView showSelectedFood() {
        final MaterialTextView selectedFoodsTextView = new MaterialTextView(getContext());
        selectedFoodsTextView.setTextSize(18);
        StringBuilder selectedFoodsText = new StringBuilder("Selected foods so far:\n");
        for (Food selectedFood : selectedFoods) {
            selectedFoodsText.append(selectedFood.getName())
                    .append(": ")
                    .append(selectedFood.getQuantity())
                    .append("\n");
        }
        selectedFoodsTextView.setText(selectedFoodsText.toString());
        return selectedFoodsTextView;
    }

    private void updateFoodSelection(Food food, int selectedValue) {

        boolean found = selectedFoods.contains(food);
        if (found)
            selectedFoods.get(selectedFoods.indexOf(food)).setQuantity(selectedValue);
        else
            selectedFoods.add(new Food(food.getName(), food.getDescription(), selectedValue));


        adapter.notifyDataSetChanged();
    }

    private void navigateBackToHome() {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_chooseFoodFragment_to_navigation_home);
    }

    private Restaurant updateStorage(List<Food> selectedFoods) {
        for (Food selectedFood : selectedFoods) {
            for (Food food : restaurant.getStorage()) {
                if (selectedFood.getName().equals(food.getName())) {
                    food.setQuantity(food.getQuantity() - selectedFood.getQuantity());
                }
            }
        }
        return restaurant;
    }


}