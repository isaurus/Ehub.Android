package com.isaac.ehub.ui.home.userprofile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isaac.ehub.R;
import com.isaac.ehub.databinding.FragmentUserProfileBinding;
import com.isaac.ehub.domain.model.UserModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserProfileFragment extends Fragment {

    private FragmentUserProfileBinding binding;
    private UserProfileViewModel userProfileViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);

        userProfileViewModel = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpListeners();
        observeViewModel();
    }

    private void setUpListeners() {
        binding.actionBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_userProfileFragment_to_teamContainerFragment);
        });

        binding.btnEditProfile.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_userProfileFragment_to_editUserProfileFragment);
        });
    }

    private void observeViewModel(){
        userProfileViewModel.userProfileViewState.observe(getViewLifecycleOwner(), userProfileViewState -> {
            switch (userProfileViewState.getStatus()){
                case SUCCESS:
                    UserModel user = userProfileViewState.getData();
                    binding.tvUserName.setText(user.getName());
                    binding.tvEmail.setText(user.getEmail());
                    binding.tvUserBirthdate.setText(user.getBirthDate().toString());
                    binding.tvCountry.setText(user.getCountry());
            }
        });
    }
}