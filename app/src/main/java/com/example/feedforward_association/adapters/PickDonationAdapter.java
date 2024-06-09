package com.example.feedforward_association.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_association.callbacks.PickDonationCallback;
import com.example.feedforward_association.databinding.PickDonationItemBinding;
import com.example.feedforward_association.models.Order;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class PickDonationAdapter extends RecyclerView.Adapter<PickDonationAdapter.PickDonationViewHolder> {
    private Context context;
    private PickDonationItemBinding binding;
    private List<Order> donations;
    private PickDonationCallback pickDonationCallback;

    public PickDonationAdapter(Context context, List<Order> donations) {
        this.context = context;
        this.donations = new ArrayList<>();
        this.donations.addAll(donations);
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
        Order order = donations.get(position);
        holder.donatorName.setText(order.getDonatorName());
        holder.donatorLocation.setText(order.getDonatorLocation());
        holder.foodItems.setText(order.getFoods().toString()); //TODO: Implement a way to show the food items
        holder.donatorButton.setOnClickListener(v -> {
            if (pickDonationCallback != null) {
                //TODO: Implement the callback
            }

        });
    }

    @Override
    public int getItemCount() {
        if (donations != null) {
            return donations.size();
        }
        return 0;
    }

    public void setDonations(List<Order> donations) {
        this.donations = new ArrayList<>();
        this.donations.addAll(donations);
        notifyDataSetChanged();
    }

    public class PickDonationViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView donatorName;
        MaterialTextView donatorLocation;
        MaterialTextView foodItems;
        ExtendedFloatingActionButton donatorButton;

        public PickDonationViewHolder(PickDonationItemBinding binding) {
            super(binding.getRoot());
            donatorName = binding.TXTPCKDonatorName;
            donatorLocation = binding.TXTPCKDonatorLocation;
            foodItems = binding.TXTPCKFoodItems;
            donatorButton = binding.BTNPCKStart;

        }
    }
}
