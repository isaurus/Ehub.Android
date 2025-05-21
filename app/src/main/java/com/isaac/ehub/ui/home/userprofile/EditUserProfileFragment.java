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
import android.widget.Toast;

import com.isaac.ehub.R;
import com.isaac.ehub.core.utils.DatePickerUtils;
import com.isaac.ehub.core.utils.InsetsUtils;
import com.isaac.ehub.core.utils.TextWatcherUtils;
import com.isaac.ehub.databinding.FragmentEditUserProfileBinding;

import java.util.Calendar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditUserProfileFragment extends Fragment {

    private FragmentEditUserProfileBinding binding;
    private UserProfileViewModel userProfileViewModel;

    private Calendar calendar;
    private String selectedBirthDateIso; // Almacena la fecha en formato ISO

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditUserProfileBinding.inflate(inflater, container, false);

        InsetsUtils.applySystemWindowInsetsPadding(binding.getRoot());

        userProfileViewModel = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendar = Calendar.getInstance();
        setUpDatePicker();

        setUpListeners();
        observeViewModel();
    }

    private void setUpListeners() {
        binding.actionBack.setOnClickListener(v -> {
            // ¿LANZAR UN DIALOG DE "SEGURO QUE QUIERES VOLVER ATRÁS? LOS CAMBIOS NO SE VAN A GUARDAR
            NavHostFragment.findNavController(this).navigate(R.id.action_editUserProfileFragment_to_userProfileFragment);
        });

        binding.btnSaveProfile.setOnClickListener(v -> {
            // ¿CÓMO PILLO EL AVATAR?
            String avatar = "";
            String name = binding.etName.getText().toString().trim();
            String birthDate = binding.etBirthdate.getText().toString().trim();
            String country = binding.etCountry.getText().toString().trim();

            userProfileViewModel.validateEditUserForm(avatar, name, birthDate,country);
        });

        enableButtonOnTextChange();
    }

    private void observeViewModel() {
        userProfileViewModel.getEditProfileViewState().observe(getViewLifecycleOwner(), editUserProfileViewState -> {
            switch (editUserProfileViewState.getStatus()){
                case VALIDATING:
                    binding.btnSaveProfile.setEnabled(false);

                    binding.tilName.setError(editUserProfileViewState.isNameValid() ? null : "Nombre no permitido");
                    binding.tilBirthdate.setError(editUserProfileViewState.isBirthDateValid() ? null : "Fecha errónea");
                    binding.tilCountry.setError(editUserProfileViewState.isCountryValid() ? null : "País erróneo");
                    break;
                case LOADING:
                    binding.btnSaveProfile.setEnabled(false);
                    break;
                case SUCCESS:
                    NavHostFragment.findNavController(this).navigate(R.id.action_editUserProfileFragment_to_userProfileFragment);
                    break;
                case ERROR:
                    Toast.makeText(getContext(), editUserProfileViewState.getMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    private void setUpDatePicker() {
        // Configurar límites de fecha (ejemplo: mayores de 18 años)
        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.YEAR, -120); // 120 años atrás como mínimo

        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.YEAR, -16); // Máximo 18 años atrás

        DatePickerUtils.setUpDatePicker(
                binding.etBirthdate,
                maxDate, // Fecha inicial mostrada (18 años atrás)
                minDate,
                maxDate,
                new DatePickerUtils.DatePickerListener() {
                    @Override
                    public void onDateSelected(String isoDate, String displayDate) {
                        binding.etBirthdate.setText(displayDate);
                        selectedBirthDateIso = isoDate;
                    }
                }
        );
    }

    private void enableButtonOnTextChange(){
        TextWatcherUtils.enableViewOnTextChange(binding.etName, binding.btnSaveProfile,binding.tilName);
        TextWatcherUtils.enableViewOnTextChange(binding.etBirthdate, binding.btnSaveProfile,binding.tilBirthdate);
        TextWatcherUtils.enableViewOnTextChange(binding.etCountry, binding.btnSaveProfile,binding.tilCountry);
    }
}