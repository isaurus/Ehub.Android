package com.isaac.ehub.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.isaac.ehub.ui.home.HomeActivity;
import com.isaac.ehub.R;
import com.isaac.ehub.core.TextWatcherUtils;
import com.isaac.ehub.databinding.FragmentRegisterBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment encargado de gestionar la interfaz y lógica de registro de nuevos usuarios.
 * Utiliza {@link AuthViewModel} para delegar la lógica de validación y autenticación.
 *
 * Funcionalidades principales:
 * - Validación del formulario de registro.
 * - Navegación hacia el fragmento de login.
 * - Observación del estado del proceso de registro.
 *
 * Este fragmento está integrado con Hilt para la inyección de dependencias.
 */
@AndroidEntryPoint
public class RegisterFragment extends Fragment {

    // View binding para acceder de forma segura a los elementos de la vista
    private FragmentRegisterBinding binding;

    // ViewModel compartido con el resto del flujo de autenticación
    private AuthViewModel authViewModel;

    /**
     * Infla el layout del fragmento y obtiene una instancia del ViewModel compartido.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        return binding.getRoot();
    }

    /**
     * Configura los listeners y observa el estado del ViewModel una vez creada la vista.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpListeners();
        observeViewModel();
    }

    /**
     * Configura los listeners para los botones e inputs:
     * - Botón de registro: valida el formulario llamando al ViewModel.
     * - Texto "¿Ya tienes cuenta?": navega al fragmento de login.
     * - Cambios en los campos de texto: habilitan o deshabilitan el botón de registro dinámicamente.
     */
    private void setUpListeners(){
        binding.btnRegister.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

            authViewModel.validateRegisterForm(email, password, confirmPassword);
        });

        binding.tvLogin.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_registrationFragment_to_loginFragment)
        );

        // Activa el botón solo si los campos contienen texto y limpia errores en tiempo real
        TextWatcherUtils.enableViewOnTextChange(binding.etEmail, binding.btnRegister, null);
        TextWatcherUtils.enableViewOnTextChange(binding.etPassword, binding.btnRegister, binding.tilPassword);
        TextWatcherUtils.enableViewOnTextChange(binding.etConfirmPassword, binding.btnRegister, binding.tilConfirmPassword);
    }

    /**
     * Observa el estado del registro proporcionado por el ViewModel y actualiza la UI en consecuencia:
     * - VALIDATING: muestra errores de validación en los campos.
     * - LOADING: muestra un indicador de carga.
     * - SUCCESS: navega a la pantalla principal.
     * - ERROR: muestra un mensaje de error.
     */
    private void observeViewModel(){
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

    /**
     * Libera la referencia al binding para evitar fugas de memoria cuando se destruye la vista.
     */
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}
