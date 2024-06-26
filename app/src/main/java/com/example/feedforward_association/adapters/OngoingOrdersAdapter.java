package com.example.feedforward_association.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_association.interfaces.OngoingDonationCallback;
import com.example.feedforward_association.databinding.OngoingItemBinding;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.OrderStatus;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class OngoingOrdersAdapter extends RecyclerView.Adapter<OngoingOrdersAdapter.OngoingDonationViewHolder> {
    private Context context;
    private OngoingItemBinding binding;
    private List<Order> donations;
    private OngoingDonationCallback ongoingDonationCallback;

    public OngoingOrdersAdapter(Context context, List<Order> donations) {
        this.context = context;
        this.donations = new ArrayList<>();
        this.donations.addAll(donations);
    }
    public void setOngoingDonationCallback(OngoingDonationCallback ongoingDonationCallback) {
        this.ongoingDonationCallback = ongoingDonationCallback;
    }

    @NonNull
    @Override
    public OngoingDonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = OngoingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OngoingDonationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OngoingDonationViewHolder holder, int position) {
        Order order = donations.get(position);
        holder.donatorName.setText(order.getDonatorName());
        holder.donatorLocation.setText(order.getDonatorLocation().toString());
        holder.foodItems.setText(order.getFoods().toString()); //TODO: Implement a way to show the food items
        holder.statusButton.setOnClickListener(v -> {
            if (ongoingDonationCallback != null) {
                //TODO: Implement a way to change the status of the order
            }
        });
        holder.donationDate.setText(order.getOrderDate());
        holder.donationTime.setText(order.getOrderTime());
    }
    public void setDonations(List<Order> donations) {
        this.donations = donations;
        notifyDataSetChanged();
    }
    public List<Order> getDonations() {
        return donations;
    }
    public void filterDonationsByDonatorName(String query){
        List<Order> filteredDonations = new ArrayList<>();
        for (Order order : donations) {
            if (order.getDonatorName().toLowerCase().contains(query.toLowerCase())) {
                filteredDonations.add(order);
            }
        }
        setDonations(filteredDonations);
    }
    public void filererDonationByStatus(OrderStatus status){
        List<Order> filteredDonations = new ArrayList<>();
        for (Order order : donations) {
            if (order.getOrderStatus() == status) {
                filteredDonations.add(order);
            }
        }
        setDonations(filteredDonations);
    }
    @Override
    public int getItemCount() {
        if (donations != null) {
            return donations.size();
        }
        return 0;
    }

    public class OngoingDonationViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView donatorName;
        MaterialTextView donatorLocation;
        MaterialTextView foodItems;
        MaterialButton statusButton;
        MaterialTextView donationDate;
        MaterialTextView donationTime;

        public OngoingDonationViewHolder(OngoingItemBinding binding) {
            super(binding.getRoot());
            donatorName = binding.TXTONGDonatorName;
            donatorLocation = binding.TXTONGDonatorLocation;
            foodItems = binding.TXTONGFoodItems;
            statusButton = binding.BTNONGStatus;
            donationDate = binding.TXTONGDate;
            donationTime = binding.TXTONGTime;
        }

    }


}
