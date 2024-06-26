package com.example.feedforward_association.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_association.R;
import com.example.feedforward_association.adapters.PickDonationAdapter;
import com.example.feedforward_association.databinding.FragmentHomeBinding;
import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Order;
import com.example.feedforward_association.models.Restaurant;
import com.google.android.material.search.SearchBar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private SearchBar searchBar;
    private PickDonationAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        findViews();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void findViews() {
        recyclerView = binding.recyclerViewDonations;
        searchBar = binding.VSearch;
        initViews();
    }

    private void initViews() {
        adapter = new PickDonationAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d("HomeFragment", "Observing LiveData...");
        adapter.setRestaurants(homeViewModel.getRestaurants(new ApiCallback<List<Restaurant>>() {
            @Override
            public void onSuccess(List<Restaurant> result) {
                adapter.setRestaurants(result);
                RefreshUI();
            }

            @Override
            public void onError(String error) {

            }
        }));
        adapter.setPickDonationCallback(restaurant -> {
            // Handle donation picking logic here
            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            String json = gson.toJson(restaurant);
            bundle.putString("restaurant", json);
            Navigation.findNavController(getView()).navigate(R.id.action_navigation_home_to_chooseFoodFragment, bundle);

        });
        Log.d("HomeFragment", "initViews: CHECK");
    }
    private void RefreshUI() {
        adapter.notifyDataSetChanged();
    }


}