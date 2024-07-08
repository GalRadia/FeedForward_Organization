package com.example.feedforward_association.ui.update;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.feedforward_association.R;
import com.example.feedforward_association.databinding.FragmentUpdateBinding;
import com.example.feedforward_association.interfaces.ApiCallback;
import com.example.feedforward_association.models.Association;
import com.example.feedforward_association.models.server.user.UserSession;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class UpdateFragment extends Fragment {

    private FragmentUpdateBinding binding;
    private UpdateViewModel updateViewModel;
    private TextInputLayout updatedName;
    private TextInputLayout updatedAddress;
    private TextInputLayout updatedPhone;
    private MaterialButton updateButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         updateViewModel =
                new ViewModelProvider(this).get(UpdateViewModel.class);

        binding = FragmentUpdateBinding.inflate(inflater, container, false);
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
        updatedName.setHint(getString(R.string.association_name));
        updatedAddress.setHint(getString(R.string.association_address));
        updatedPhone.setHint(getString(R.string.association_phone));
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
            updateViewModel.updateAssociation(association, new ApiCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Toast.makeText(getContext(), getString(R.string.association_updated_successfully), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(String message) {
                    Toast.makeText(getContext(), getString(R.string.failed_to_update_association), Toast.LENGTH_SHORT).show();
                    updatedName.setHint(UserSession.getInstance().getAssociation().getAssociationName());
                    updatedAddress.setHint(UserSession.getInstance().getAssociation().getAssociationAddress());
                    updatedPhone.setHint(UserSession.getInstance().getAssociation().getAssociationPhone());
                }
            });
        });
    }
    private boolean validate(){
        if(updatedName.getEditText().getText().toString().isEmpty()){
            updatedName.setError(getString(R.string.name_cannot_be_empty));
            return false;
        }
        if(updatedAddress.getEditText().getText().toString().isEmpty()){
            updatedAddress.setError(getString(R.string.address_cannot_be_empty));
            return false;
        }
        if(updatedPhone.getEditText().getText().toString().isEmpty()){
            updatedPhone.setError(getString(R.string.phone_cannot_be_empty));
            return false;
        }
        return true;
    }
}