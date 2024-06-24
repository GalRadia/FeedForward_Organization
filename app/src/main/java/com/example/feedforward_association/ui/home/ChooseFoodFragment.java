package com.example.feedforward_association.ui.home;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.feedforward_association.R;
import com.example.feedforward_association.databinding.FragmentChooseFoodBinding;
import com.google.android.material.button.MaterialButton;

public class ChooseFoodFragment extends Fragment {

    private ChooseFoodViewModel mViewModel;
    private RecyclerView recyclerView;
    private MaterialButton finishButton;
    private FragmentChooseFoodBinding binding;

    public static ChooseFoodFragment newInstance() {
        return new ChooseFoodFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_food, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChooseFoodViewModel.class);
        // TODO: Use the ViewModel
    }
    private void findViews(){
        recyclerView = binding.RCVChooseFood;
        finishButton = binding.BTNFinish;
        initViews();
    }
    private void initViews(){

    }

}