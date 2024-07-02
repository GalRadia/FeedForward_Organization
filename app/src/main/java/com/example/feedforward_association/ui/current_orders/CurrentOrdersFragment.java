package com.example.feedforward_association.ui.current_orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.search.SearchBar;

import java.util.ArrayList;
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
        searchBar = binding.VSearch;
        chipGroup = binding.GRPFilter;
        chipPendning = binding.chipPending;
        chipOngoing = binding.chipOngoing;
        chipfinished = binding.chipFinished;
        recyclerView = binding.RCVOngoingOrder;
        initViews();
    }

    private void initViews() {
        initRecyclerView();
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filterDonationsByName(newText);
                return false;
            }
        });
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


}