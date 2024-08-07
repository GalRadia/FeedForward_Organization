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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.feedforward_association.R;
import com.example.feedforward_association.adapters.FoodAdapter;
import com.example.feedforward_association.databinding.FragmentChooseFoodBinding;
import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Association;
import com.example.feedforward_association.models.Food;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.OrderStatus;
import com.example.feedforward_association.models.Restaurant;
import com.example.feedforward_association.models.WhoCarries;
import com.example.feedforward_association.models.server.object.ObjectId;
import com.example.feedforward_association.models.server.user.UserSession;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            if(selectedFoods.isEmpty())
            {
                Toast.makeText(getContext(), getString(R.string.no_selected_foods), Toast.LENGTH_SHORT).show();
                return;
            }
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            // Format the current date and time
            String date = now.format(formatter);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String time = now.format(timeFormatter);


            OrderStatus status = spinner.getSelectedItem().toString().equals(getString(R.string.take_away)) ? OrderStatus.ACTIVE : OrderStatus.PENDING;
            Order order = getOrder(date, time, status);
            mViewModel.postOrder(order, new ApiCallback<Order>() {
                @Override
                public void onSuccess(Order result) {
                    restaurant = updateStorage(selectedFoods);
                    Toast.makeText(getContext(), getString(R.string.order_posted), Toast.LENGTH_SHORT).show();
                    mViewModel.updateRestaurant(restaurant, new ApiCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            Toast.makeText(getContext(), getString(R.string.restaurant_updated), Toast.LENGTH_SHORT).show();
                            navigateBackToHome();
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(getContext(), getString(R.string.restaurant_update_failed), Toast.LENGTH_SHORT).show();
                        }
                    });


                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getContext(), getString(R.string.order_failed), Toast.LENGTH_SHORT).show();

                }
            });
            //TODO post order
            //TODO update restaurant

        });
    }

    private @NonNull Order getOrder(String date, String time, OrderStatus status) {
        Association association = UserSession.getInstance().getAssociation();
        Order order = new Order(new ObjectId(UserSession.getInstance().getSUPERAPP(), "123"), restaurant.getRestaurantEmail(), restaurant.getRestaurantName(),
                restaurant.getRestaurantLocation(), date, time, selectedFoods, status, WhoCarries.values()[spinner.getSelectedItemPosition()],
                association.getAssociationName(), association.getAssociationLocation(),
                association.getAssociationAddress(), restaurant.getRestaurantAddress());//TODO CHANGE
        return order;
    }

    private void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{getString(R.string.take_away), getString(R.string.delivery)});
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
        tvSliderValue.setText(getString(R.string.current_value)+" 0");
        tvSliderValue.setTextSize(18);
        layout.addView(tvSliderValue);

        // Create a Slider
        final Slider slider = new Slider(getContext());
        slider.setValueFrom(0);
        slider.setValueTo(food.getAmount());
        slider.setStepSize(1);
        layout.addView(slider);

        // Set a listener to update the TextView as the Slider is moved
        slider.addOnChangeListener((slider1, value, fromUser) -> {
            tvSliderValue.setText(getString(R.string.current_value) + (int) value);
        });

        // Build and show the AlertDialog
        new AlertDialog.Builder(slider.getContext())
                .setTitle(getString(R.string.choose_a_value))
                .setView(layout)
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                    // Handle the OK button press
                    int selectedValue = (int) slider.getValue();
                    updateFoodSelection(food, selectedValue);
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                    // Handle the Cancel button press
                })
                .show();
    }

    private @NonNull MaterialTextView showSelectedFood() {

        final MaterialTextView selectedFoodsTextView = new MaterialTextView(getContext());
        selectedFoodsTextView.setTextSize(18);
        StringBuilder selectedFoodsText = new StringBuilder(getString(R.string.selected_foods_so_far));
        for (Food selectedFood : selectedFoods) {
            selectedFoodsText.append(selectedFood.getName())
                    .append(": ")
                    .append(selectedFood.getAmount())
                    .append("\n");
        }
        selectedFoodsTextView.setText(selectedFoodsText.toString());
        return selectedFoodsTextView;
    }

    private void updateFoodSelection(Food food, int selectedValue) {

        boolean found = selectedFoods.contains(food);
        if (found)
            selectedFoods.get(selectedFoods.indexOf(food)).setAmount(selectedValue);
        else
            selectedFoods.add(new Food(food.getName(), food.getType(), selectedValue, food.getExpiryDate()));

        for (int i = 0; i < selectedFoods.size(); i++) {
            if (selectedFoods.get(i).getAmount() == 0)
                selectedFoods.remove(i);
        }
        adapter.notifyDataSetChanged();
    }

    private void navigateBackToHome() {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_chooseFoodFragment_to_navigation_home);
    }

    private Restaurant updateStorage(List<Food> selectedFoods) {
        for (Food selectedFood : selectedFoods) {
            Food food = restaurant.getStorage().get(restaurant.getStorage().indexOf(selectedFood));
            food.setAmount(food.getAmount() - selectedFood.getAmount());
            if (food.getAmount() == 0)
                restaurant.getStorage().remove(food);
        }
        return restaurant;
    }


}