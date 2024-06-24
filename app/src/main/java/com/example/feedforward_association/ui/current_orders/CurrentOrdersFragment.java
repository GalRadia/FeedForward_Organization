package com.example.feedforward_association.ui.current_orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_association.databinding.FragmentCurrentOrdersBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.search.SearchBar;

public class CurrentOrdersFragment extends Fragment {

    private FragmentCurrentOrdersBinding binding;
    private SearchBar searchBar;
    private ChipGroup  chipGroup;
    private Chip chipPendning,chipOngoing,chipfinished;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CurrentOrdersViewModel currentOrdersViewModel =
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
    }
    private void refreshUI(){
        // Update UI
    }
}