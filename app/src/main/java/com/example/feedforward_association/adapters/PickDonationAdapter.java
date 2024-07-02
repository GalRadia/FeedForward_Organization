package com.example.feedforward_association.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_association.interfaces.PickDonationCallback;
import com.example.feedforward_association.databinding.PickDonationItemBinding;
import com.example.feedforward_association.models.Food;
import com.example.feedforward_association.models.Restaurant;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class PickDonationAdapter extends RecyclerView.Adapter<PickDonationAdapter.PickDonationViewHolder> {
    private Context context;
    private List<Restaurant> originalRestaurants;
    private List<Restaurant> filteredRestaurants;
    private PickDonationCallback pickDonationCallback;

    public PickDonationAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.originalRestaurants = restaurants != null ? restaurants : new ArrayList<>();
        this.filteredRestaurants = new ArrayList<>(this.originalRestaurants);
    }

    public void setPickDonationCallback(PickDonationCallback pickDonationCallback) {
        this.pickDonationCallback = pickDonationCallback;
    }

    @NonNull
    @Override
    public PickDonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PickDonationItemBinding binding = PickDonationItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PickDonationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PickDonationViewHolder holder, int position) {
        Restaurant restaurant = filteredRestaurants.get(position);
        holder.donatorName.setText(restaurant.getRestaurantName());
        holder.donatorLocation.setText(restaurant.getRestaurantAddress());

        StringBuilder items = new StringBuilder();
        for (Food food : restaurant.getStorage()) {
            items.append(food.getName()).append(", ");
        }
        if (items.length() > 0 && items.charAt(items.length() - 2) == ',') {
            items.delete(items.length() - 2, items.length());
        }
        holder.foodItems.setText(items);
        holder.foodItems.setOnClickListener(v -> {
            if (holder.foodItems.getMaxLines() == 2)
                holder.foodItems.setMaxLines(100);
            else
                holder.foodItems.setMaxLines(2);
        });
        if (pickDonationCallback != null) {
            holder.donatorButton.setOnClickListener(v -> pickDonationCallback.onDonationPicked(restaurant));
        }
    }

    @Override
    public int getItemCount() {
        return filteredRestaurants != null ? filteredRestaurants.size() : 0;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.originalRestaurants = restaurants != null ? restaurants : new ArrayList<>();
        this.filteredRestaurants = new ArrayList<>(this.originalRestaurants);
        notifyDataSetChanged();
    }

    public void filterOrders(String query) {
        filteredRestaurants.clear();
        if (query.isEmpty()) {
            filteredRestaurants.addAll(originalRestaurants);
        } else {
            for (Restaurant restaurant : originalRestaurants) {
                if (restaurant.getRestaurantName().toLowerCase().contains(query.toLowerCase())) {
                    filteredRestaurants.add(restaurant);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class PickDonationViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView donatorName;
        private MaterialTextView donatorLocation;
        private MaterialTextView foodItems;
        private MaterialTextView date;
        private MaterialTextView time;
        private ExtendedFloatingActionButton donatorButton;

        public PickDonationViewHolder(PickDonationItemBinding binding) {
            super(binding.getRoot());
            donatorName = binding.TXTPCKDonatorName;
            donatorLocation = binding.TXTPCKDonatorLocation;
            foodItems = binding.TXTPCKFoodItems;
            donatorButton = binding.BTNPCKStart;
            date = binding.TXTPCKDate;
            time = binding.TXTPCKTime;
        }
    }
}
