package com.example.feedforward_association.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_association.interfaces.FoodCallback;
import com.example.feedforward_association.databinding.FoodItemBinding;
import com.example.feedforward_association.models.Food;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private Context context;
    private FoodItemBinding binding;
    private FoodCallback foodCallback;
    private List<Food> foods;

    public FoodAdapter(Context context, List<Food> foods) {
        this.context = context;
        this.foods = new ArrayList<>();
        this.foods.addAll(foods);
    }

    public void setFoodCallback(FoodCallback foodCallback) {
        this.foodCallback = foodCallback;
    }



    @NonNull
    @Override
    public FoodAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = FoodItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.FoodViewHolder holder, int position) {
        Food food = foods.get(position);
        holder.foodName.setText(food.getName());
        holder.foodQuantity.setText(String.valueOf(food.getQuantity()));
        holder.foodDescription.setText(food.getDescription());
        holder.selectFoodb.setOnClickListener(v -> {
            if (foodCallback != null) {
                foodCallback.onFoodSelected(food);
            }
        });
    }



    @Override
    public int getItemCount() {
        if (foods != null) {
            return foods.size();
        }
        return 0;
    }
    public void setFoods(List<Food> foods) {
        ArrayList<Food> foodArrayList = new ArrayList<>(); //change to sorted list in the future
        foodArrayList.addAll(foods);
        notifyDataSetChanged();
    }
    public List<Food> getFoods() {
        return foods;
    }



    public class FoodViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView foodName, foodQuantity, foodDescription;
        FloatingActionButton selectFoodb;

        public FoodViewHolder(FoodItemBinding binding) {
            super(binding.getRoot());
            foodName = binding.FoodItemName;
            foodQuantity = binding.TXTQuantity;
            foodDescription = binding.FoodItemDesc;
            selectFoodb = binding.BTNAddFood;
        }
    }
}

