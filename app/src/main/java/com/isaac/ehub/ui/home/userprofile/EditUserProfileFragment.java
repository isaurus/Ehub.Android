package com.isaac.ehub.ui.home.userprofile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isaac.ehub.R;
import com.isaac.ehub.core.utils.InsetsUtils;
import com.isaac.ehub.databinding.FragmentEditUserProfileBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditUserProfileFragment extends Fragment {

    private FragmentEditUserProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditUserProfileBinding.inflate(inflater, container, false);

        InsetsUtils.applySystemWindowInsetsPadding(binding.getRoot());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpListeners();
    }

    private void setUpListeners() {
        binding.actionBack.setOnClickListener(v -> {
            // ¿LANZAR UN DIALOG DE "SEGURO QUE QUIERES VOLVER ATRÁS? LOS CAMBIOS NO SE VAN A GUARDAR
            NavHostFragment.findNavController(this).navigate(R.id.action_editUserProfileFragment_to_userProfileFragment);
        });

        binding.btnSaveProfile.setOnClickListener(v -> {
            // PERSISTIR CAMBIOS EN FIRESTORE
            NavHostFragment.findNavController(this).navigate(R.id.action_editUserProfileFragment_to_userProfileFragment);
        });
    }
}