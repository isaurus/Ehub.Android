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

import com.isaac.ehub.HomeActivity;
import com.isaac.ehub.R;
import com.isaac.ehub.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private AuthViewModel authViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        // ESTUDIAR TO' EL TEMA DE TEXTWATCHER
        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Validar email en tiempo real
                String email = charSequence.toString().trim();
                authViewModel.validateRegisterForm(email, binding.etPassword.getText().toString().trim(), binding.etConfirmPassword.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Validar password en tiempo real
                String password = charSequence.toString().trim();
                authViewModel.validateRegisterForm(binding.etEmail.getText().toString().trim(), password, binding.etConfirmPassword.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Validar confirmPassword en tiempo real
                String confirmPassword = charSequence.toString().trim();
                authViewModel.validateRegisterForm(binding.etEmail.getText().toString().trim(), binding.etPassword.getText().toString().trim(), confirmPassword);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void observeViewModel(){
        authViewModel.getRegisterViewState().observe(getViewLifecycleOwner(), viewState -> {
            binding.progressBar.setVisibility(viewState.isLoading() ? View.VISIBLE : View.GONE);

            if (!viewState.isEmailValid()){
                binding.tilEmail.setError("Email no válido");
            } else{
                binding.tilEmail.setError(null);
            }

            if (!viewState.isPasswordValid()){
                binding.tilPassword.setError("Contraseña inválida");
            } else{
                binding.tilPassword.setError(null);
            }

            if(!viewState.isConfirmPasswordValid()){
                binding.tilConfirmPassword.setError("Las contraseñas no coinciden");
            } else{
                binding.tilConfirmPassword.setError(null);
            }

            // ¡MEJORAR!
            boolean allValid = viewState.isEmailValid() &&
                    viewState.isPasswordValid() &&
                    viewState.isConfirmPasswordValid();
            binding.btnRegister.setEnabled(allValid);

            if (viewState.isSuccess()){
                requireActivity().startActivity(new Intent(requireContext(), HomeActivity.class));
                requireActivity().finish();
            }
        });
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}