package com.example.feedforward_association.ui.current_orders;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_association.R;
import com.example.feedforward_association.adapters.OngoingOrdersAdapter;
import com.example.feedforward_association.databinding.FragmentCurrentOrdersBinding;
import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.OrderStatus;
import com.example.feedforward_association.models.Review;
import com.example.feedforward_association.models.server.object.ObjectBoundary;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.search.SearchBar;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CurrentOrdersFragment extends Fragment {

    private FragmentCurrentOrdersBinding binding;
    private SearchView searchBar;
    private ChipGroup chipGroup;
    private Chip chipPendning, chipOngoing, chipfinished;
    private RecyclerView recyclerView;
    private OngoingOrdersAdapter adapter;
    private CurrentOrdersViewModel currentOrdersViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        currentOrdersViewModel =
                new ViewModelProvider(this).get(CurrentOrdersViewModel.class);

        binding = FragmentCurrentOrdersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        findViews();

        // final TextView textView = binding.textDashboard;
        //  currentOrdersViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void findViews() {
        chipGroup = binding.GRPFilter;
        chipPendning = binding.chipPending;
        chipOngoing = binding.chipOngoing;
        chipfinished = binding.chipFinished;
        recyclerView = binding.RCVOngoingOrder;
        initViews();
    }

    private void initViews() {
        initRecyclerView();
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            List<OrderStatus> orderStatuses = new ArrayList<>();

            if (checkedIds.contains(chipPendning.getId())) {
                orderStatuses.add(OrderStatus.PENDING);
            }
            if (checkedIds.contains(chipOngoing.getId())) {
                orderStatuses.add(OrderStatus.ACTIVE);
            }
            if (checkedIds.contains(chipfinished.getId())) {
                orderStatuses.add(OrderStatus.DELIVERED);
            }
            adapter.filterDonationsByStatus(orderStatuses);

        });

    }


    private void initRecyclerView() {
        adapter = new OngoingOrdersAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);
        adapter.setOngoingDonationCallback(order -> {
            switch (order.getOrderStatus()) {
                case PENDING:
                case ACTIVE:
                    Toast.makeText(getContext(), getString(R.string.cant_write_a_review), Toast.LENGTH_SHORT).show();
                    break;
                case DELIVERED:
                    showRateReviewDialog(order);
                    break;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        currentOrdersViewModel.getOrders(new ApiCallback<List<Order>>() {
            @Override
            public void onSuccess(List<Order> result) {
                adapter.setDonations(result);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void showRateReviewDialog(Order order) {
        // Inflate the custom layout/view
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_review, null);

        // Find the RatingBar and TextInputEditText in the custom layout
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        TextInputEditText reviewText = dialogView.findViewById(R.id.reviewText);

        // Create the AlertDialog
        AlertDialog.Builder builder = getBuilder(dialogView, ratingBar, reviewText, order);
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private AlertDialog.Builder getBuilder(View dialogView, RatingBar ratingBar, TextInputEditText reviewText, Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Rate and Review");
        builder.setView(dialogView);
        builder.setPositiveButton("Submit", (dialogInterface, i) -> {
                    Review review = new Review();
                    review.setRating(ratingBar.getRating());
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    // Format the current date and time
                    String date = now.format(formatter);
                    review.setDate(date);
                    review.setName(order.getAssociationName());
                    review.setOverview(reviewText.getText().toString());
                    currentOrdersViewModel.creatReview(review, order.getDonatorEmail(),new ApiCallback<ObjectBoundary>() {
                        @Override
                        public void onSuccess(ObjectBoundary result) {
                            Toast.makeText(getContext(), getString(R.string.review_submitted), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String error) {
                        }
                    });
                }

        );
        return builder;
    }


}