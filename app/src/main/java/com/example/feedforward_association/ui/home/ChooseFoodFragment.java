package com.example.feedforward_association.ui.home;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.feedforward_association.adapters.FoodAdapter;
import com.example.feedforward_association.databinding.FragmentChooseFoodBinding;
import com.example.feedforward_association.models.Restaurant;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ChooseFoodFragment extends Fragment {

    private HomeViewModel mViewModel;
    private RecyclerView recyclerView;
    private MaterialButton finishButton;
    private FragmentChooseFoodBinding binding;
    private FoodAdapter adapter;
    private Restaurant restaurant;

    public static ChooseFoodFragment newInstance() {
        return new ChooseFoodFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        binding = FragmentChooseFoodBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        findViews();
        return root;
    }


    private void findViews(){
        recyclerView = binding.RCVChooseFood;
        finishButton = binding.BTNFinish;
        initViews();
    }
    private void initViews(){
        Bundle bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            restaurant = gson.fromJson(bundle.getString("restaurant"), Restaurant.class);
        }
        adapter = new FoodAdapter(getContext(), restaurant.getStorage());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        finishButton.setOnClickListener(v -> {

        });
    }

}