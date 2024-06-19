package com.example.feedforward_association.ui.current_orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.feedforward_association.databinding.FragmentCurrentOrdersBinding;
import com.example.feedforward_association.databinding.FragmentDashboardBinding;

public class CurrentOrdersFragment extends Fragment {

    private FragmentCurrentOrdersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CurrentOrdersViewModel currentOrdersViewModel =
                new ViewModelProvider(this).get(CurrentOrdersViewModel.class);

        binding = FragmentCurrentOrdersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       // final TextView textView = binding.textDashboard;
      //  currentOrdersViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}