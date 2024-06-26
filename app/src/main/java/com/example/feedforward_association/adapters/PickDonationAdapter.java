package com.example.feedforward_association.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_association.interfaces.PickDonationCallback;
import com.example.feedforward_association.databinding.PickDonationItemBinding;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.Restaurant;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class PickDonationAdapter extends RecyclerView.Adapter<PickDonationAdapter.PickDonationViewHolder> {
    private Context context;
    private PickDonationItemBinding binding;
    private List<Restaurant> restaurants;
    private PickDonationCallback pickDonationCallback;

    public PickDonationAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = new ArrayList<>();
        this.restaurants.addAll(restaurants);
    }

    public void setPickDonationCallback(PickDonationCallback pickDonationCallback) {
        this.pickDonationCallback = pickDonationCallback;
    }

    @NonNull
    @Override
    public PickDonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = PickDonationItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PickDonationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PickDonationViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.donatorName.setText(restaurant.getRestaurantName());
        holder.donatorLocation.setText(restaurant.getRestaurantLocation().toString());
        holder.foodItems.setText(restaurant.getStorage().toString()); //TODO: Implement a way to show the food items
        if (pickDonationCallback != null) {
            holder.donatorButton.setOnClickListener(v -> pickDonationCallback.onDonationPicked(restaurant));
        }
    }

    @Override
    public int getItemCount() {
        if (restaurants != null) {
            return restaurants.size();
        }
        return 0;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }
    public void filterOrders(String query){
        List<Restaurant> filteredorders = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getRestaurantName().toLowerCase().contains(query.toLowerCase())) {
                filteredorders.add(restaurant);
            }
        }
        setRestaurants(filteredorders);
    }

    public class PickDonationViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView donatorName;
        private MaterialTextView donatorLocation;
        private MaterialTextView foodItems;
        private ExtendedFloatingActionButton donatorButton;

        public PickDonationViewHolder(PickDonationItemBinding binding) {
            super(binding.getRoot());
            donatorName = binding.TXTPCKDonatorName;
            donatorLocation = binding.TXTPCKDonatorLocation;
            foodItems = binding.TXTPCKFoodItems;
            donatorButton = binding.BTNPCKStart;

        }
    }
}
