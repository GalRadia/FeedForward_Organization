package com.example.feedforward_association.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.feedforward_association.databinding.FragmentHistoryBinding;
import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Association;
import com.example.feedforward_association.models.server.user.UserSession;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private HistoryViewModel historyViewModel;
    private TextInputLayout updatedName;
    private TextInputLayout updatedAddress;
    private TextInputLayout updatedPhone;
    private MaterialButton updateButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         historyViewModel =
                new ViewModelProvider(this).get(HistoryViewModel.class);

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        findViews();
        initViews();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void findViews(){
        updateButton = binding.button;
        updatedName = binding.updatedName;
        updatedAddress = binding.updatedAddress;
        updatedPhone = binding.updatedPhoneNumber;
    }
    private void initViews(){
        updatedName.setHint("Association Name");
        updatedAddress.setHint("Association Address");
        updatedPhone.setHint("Association Phone");
        updatedName.getEditText().setText(UserSession.getInstance().getAssociation().getAssociationName());
        updatedAddress.getEditText().setText(UserSession.getInstance().getAssociation().getAssociationAddress());
        updatedPhone.getEditText().setText(UserSession.getInstance().getAssociation().getAssociationPhone());
        updateButton.setOnClickListener(v -> {
            Association association = UserSession.getInstance().getAssociation();
            association.setAssociationName(updatedName.getEditText().getText().toString());
            association.setAssociationAddress(updatedAddress.getEditText().getText().toString());
            association.setAssociationPhone(updatedPhone.getEditText().getText().toString());
            if(!validate())
                return;
            historyViewModel.updateAssociation(association, new ApiCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Toast.makeText(getContext(), "Association updated successfully", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(String message) {
                    Toast.makeText(getContext(), "Failed to update association", Toast.LENGTH_SHORT).show();
                    updatedName.setHint(UserSession.getInstance().getAssociation().getAssociationName());
                    updatedAddress.setHint(UserSession.getInstance().getAssociation().getAssociationAddress());
                    updatedPhone.setHint(UserSession.getInstance().getAssociation().getAssociationPhone());
                }
            });
        });
    }
    private boolean validate(){
        if(updatedName.getEditText().getText().toString().isEmpty()){
            updatedName.setError("Name cannot be empty");
            return false;
        }
        if(updatedAddress.getEditText().getText().toString().isEmpty()){
            updatedAddress.setError("Address cannot be empty");
            return false;
        }
        if(updatedPhone.getEditText().getText().toString().isEmpty()){
            updatedPhone.setError("Phone cannot be empty");
            return false;
        }
        return true;
    }
}