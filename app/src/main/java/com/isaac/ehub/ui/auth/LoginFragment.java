package com.isaac.ehub.ui.auth;

import static android.content.ContentValues.TAG;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.credentials.exceptions.GetCredentialInterruptedException;
import androidx.credentials.exceptions.NoCredentialException;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.os.CancellationSignal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.isaac.ehub.ui.home.HomeActivity;
import com.isaac.ehub.R;
import com.isaac.ehub.core.TextWatcherUtils;
import com.isaac.ehub.databinding.FragmentLoginBinding;


import dagger.hilt.android.AndroidEntryPoint;
/**
 * {@code LoginFragment} es un fragmento de la interfaz de usuario responsable de manejar
 * el inicio de sesión en la aplicación, ya sea mediante correo electrónico y contraseña
 * o a través del acceso con Google.
 *
 * <p>Este fragmento utiliza ViewBinding para interactuar con las vistas del layout
 * {@code fragment_login.xml} y se apoya en un {@link AuthViewModel} para gestionar
 * la lógica de validación y autenticación.</p>
 *
 * <p>Además, implementa la integración con el Credential Manager para permitir
 * el inicio de sesión con cuentas de Google previamente asociadas al dispositivo.</p>
 *
 * <p>Anotado con {@code @AndroidEntryPoint} para habilitar la inyección de dependencias con Hilt.</p>
 *
 * @author Isaac
 * @version 1.0
 */
@AndroidEntryPoint
public class LoginFragment extends Fragment {

    // ViewBinding para acceder a los elementos de la UI.
    private FragmentLoginBinding binding;

    // ViewModel que gestiona la lógica de autenticación.
    private AuthViewModel authViewModel;

    /**
     * Inflamos el layout del fragmento usando ViewBinding.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        return binding.getRoot();
    }

    /**
     * Se llama una vez que la vista ha sido creada. Aquí se configuran los
     * listeners y la observación del ViewModel.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpListeners();
        observeViewModel();
    }

    /**
     * Configura los listeners de los botones y los campos de texto.
     * - {@code btnLogin}: valida y procesa el inicio de sesión por correo.
     * - {@code btnGoogleSignIn}: lanza el flujo de autenticación con Google.
     * - {@code tvRegister}: navega al fragmento de registro.
     * También se habilita la lógica para activar el botón cuando se introduzca texto.
     */
    private void setUpListeners(){
        binding.btnLogin.setOnClickListener(v ->{
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            authViewModel.validateLoginForm(email, password);
        });

        binding.btnGoogleSignIn.setOnClickListener(v -> {
            launchGoogleSignInFlow();
        });

        binding.tvRegister.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_registrationFragment);
        });

        // Habilita el botón de login al introducir texto.
        TextWatcherUtils.enableViewOnTextChange(binding.etEmail, binding.btnLogin, null);
        TextWatcherUtils.enableViewOnTextChange(binding.etPassword, binding.btnLogin, binding.tilPassword);
    }

    /**
     * Observa el estado de la vista del login a través del ViewModel,
     * y actualiza la interfaz de usuario en función del estado.
     */
    private void observeViewModel(){
        authViewModel.getLoginViewState().observe(getViewLifecycleOwner(), loginViewState -> {
            switch(loginViewState.getStatus()){
                case VALIDATING:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnLogin.setEnabled(false);

                    binding.tilEmail.setError(loginViewState.isEmailValid() ? null : "Email no registrado");
                    binding.tilPassword.setError(loginViewState.isPasswordValid() ? null : "Contraseña no válida");
                    break;
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.btnLogin.setEnabled(false);
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    requireActivity().startActivity(new Intent(requireContext(), HomeActivity.class));
                    requireActivity().finish();
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), loginViewState.getMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    /**
     * Inicia el flujo de autenticación con Google mediante el Credential Manager.
     * Este método crea una solicitud de credenciales específicas para Google y
     * lanza el flujo de selección de cuenta.
     */
    private void launchGoogleSignInFlow() {
        GetSignInWithGoogleOption getSignInWithGoogleOption = new GetSignInWithGoogleOption
                .Builder(getString(R.string.default_web_client_id))
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(getSignInWithGoogleOption)
                .build();

        CredentialManager credentialManager = CredentialManager.create(requireContext());

        credentialManager.getCredentialAsync(
                requireActivity(),
                request,
                new CancellationSignal(),
                ContextCompat.getMainExecutor(requireContext()),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleSignIn(result);
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        handleFailure(e);
                    }
                }
        );
    }

    /**
     * Procesa la respuesta exitosa del flujo de inicio con Google.
     *
     * @param response La respuesta del CredentialManager con la credencial obtenida.
     */
    private void handleSignIn(GetCredentialResponse response) {
        Credential credential = response.getCredential();
        if (credential instanceof CustomCredential) {
            CustomCredential customCredential = (CustomCredential) credential;
            if (TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
                Bundle credentialData = customCredential.getData();
                GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);

                authViewModel.loginWithGoogle(googleIdTokenCredential.getIdToken());
            } else {
                Log.w(TAG, "Credential is not of type Google ID!");
            }
        } else {
            Log.w(TAG, "Credential is not a CustomCredential!");
        }
    }

    /**
     * Maneja los errores que puedan ocurrir durante el flujo de autenticación con Google.
     *
     * @param e La excepción lanzada por el CredentialManager.
     */
    private void handleFailure(GetCredentialException e) {
        Log.e(TAG, "Sign in failed", e);

        if (getContext() != null) {
            Toast.makeText(getContext(), "Error during Google sign in: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        if (e instanceof NoCredentialException) {
            Log.d(TAG, "User canceled the sign in flow");
        } else if (e instanceof GetCredentialInterruptedException) {
            Log.w(TAG, "Sign in flow was interrupted");
        }
    }

    /**
     * Libera el binding al destruirse la vista para evitar memory leaks.
     */
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}
