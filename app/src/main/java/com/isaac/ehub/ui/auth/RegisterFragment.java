package com.isaac.ehub.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.isaac.ehub.HomeActivity;
import com.isaac.ehub.R;
import com.isaac.ehub.core.TextWatcherUtils;
import com.isaac.ehub.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private AuthViewModel authViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpListeners();
        observeViewModel();
    }

    // Los listeners gestionan la interacción del usuario con la UI y llaman al ViewModel para delegar la lógica
    private void setUpListeners(){
        binding.btnRegister.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

            authViewModel.validateRegisterForm(email, password, confirmPassword);
        });

        binding.tvLogin.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_registrationFragment_to_loginFragment);
        });

        TextWatcherUtils.enableViewOnTextChange(binding.etEmail, binding.btnRegister, null);
        TextWatcherUtils.enableViewOnTextChange(binding.etPassword, binding.btnRegister, binding.tilPassword);
        TextWatcherUtils.enableViewOnTextChange(binding.etConfirmPassword, binding.btnRegister, binding.tilConfirmPassword);
    }

    private void observeViewModel(){
        // Observa los cambios del estado actual (RegisterViewState) y, cada vez que cambie ese estado, se ejecuta el lambda
        authViewModel.getRegisterViewState().observe(getViewLifecycleOwner(), registerViewState -> {
            switch (registerViewState.getStatus()){
                case VALIDATING:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnRegister.setEnabled(false);

                    binding.tilEmail.setError(registerViewState.isEmailValid() ? null : "Email no válido");
                    binding.tilPassword.setError(registerViewState.isPasswordValid() ? null : "Contraseña no válida");
                    binding.tilConfirmPassword.setError(registerViewState.isConfirmPasswordValid() ? null : "Las contraseñas no coinciden");
                    break;
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.btnRegister.setEnabled(false);
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    requireActivity().startActivity(new Intent(requireContext(), HomeActivity.class));
                    requireActivity().finish();
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), registerViewState.getMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}