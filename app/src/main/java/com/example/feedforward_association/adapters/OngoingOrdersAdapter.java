package com.example.feedforward_association.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_association.R;
import com.example.feedforward_association.interfaces.OngoingDonationCallback;
import com.example.feedforward_association.databinding.OngoingItemBinding;
import com.example.feedforward_association.models.Food;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.OrderStatus;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class OngoingOrdersAdapter extends RecyclerView.Adapter<OngoingOrdersAdapter.OngoingDonationViewHolder> {
    private Context context;
    private OngoingItemBinding binding;
    private List<Order> originalOrders;
    private List<Order> filteredOrders;
    private List<OrderStatus> currentFilterStatuses;
    private OngoingDonationCallback ongoingDonationCallback;

    public OngoingOrdersAdapter(Context context, List<Order> donations) {
        this.context = context;
        this.originalOrders = donations != null ? donations : new ArrayList<>();
        this.filteredOrders = new ArrayList<>(this.originalOrders);
        this.currentFilterStatuses = new ArrayList<>();

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
        Order order = filteredOrders.get(position);
        holder.donatorName.setText(order.getDonatorName());
        holder.donatorLocation.setText(order.getDonatorLocation().toString());
        switch (order.getOrderStatus()) {
            case PENDING:
                holder.statusButton.setText("Pending");
                holder.statusButton.setIcon(context.getDrawable(R.drawable.ic_pending));
                break;
            case DELIVERED:
                holder.statusButton.setText("Delivered");
                holder.statusButton.setIcon(context.getDrawable(R.drawable.ic_done));
                break;
            case ACTIVE:
                holder.statusButton.setText("Active");
                holder.statusButton.setIcon(context.getDrawable(R.drawable.ic_ongoing_shipment));
                break;

        }
        StringBuilder items = new StringBuilder();
        for (Food food : order.getFoods()) {
            items.append(food.getName()).append(", ");
        }
        if (items.charAt(items.length() - 1) == ' ')
            items.deleteCharAt(items.length() - 2);
        holder.foodItems.setText(items);//TODO: Implement a way to show the food items
        holder.statusButton.setOnClickListener(v -> {
            if (ongoingDonationCallback != null) {
                //TODO: Implement a way to change the status of the order
            }
        });
        holder.donationDate.setText(order.getOrderDate());
        holder.donationTime.setText(order.getOrderTime());
    }

    public void setDonations(List<Order> orders) {
        this.originalOrders = orders != null ? orders : new ArrayList<>();
        filterDonationsByStatus(this.currentFilterStatuses);
    }





    public void filterDonationsByStatus(List<OrderStatus> statuses) {
        this.currentFilterStatuses = statuses;
        if (statuses.isEmpty()) {
            filteredOrders = new ArrayList<>(originalOrders);
        } else {
            filteredOrders = new ArrayList<>();
            for (Order order : originalOrders) {
                if (statuses.contains(order.getOrderStatus())) {
                    filteredOrders.add(order);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (filteredOrders != null) {
            return filteredOrders.size();
        }
        return 0;
    }

    public void filterDonationsByName(String newText) {
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
