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
import com.isaac.ehub.HomeActivity;
import com.isaac.ehub.R;
import com.isaac.ehub.core.TextWatcherUtils;
import com.isaac.ehub.databinding.FragmentLoginBinding;


import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragmento de login. Utiliza ViewBinding para gestionar las vistas de la UI y utiliza el
 * 'AuthViewModel' para delegar la lógica.
 */
@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private AuthViewModel authViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
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

        TextWatcherUtils.enableViewOnTextChange(binding.etEmail, binding.btnLogin, null);
        TextWatcherUtils.enableViewOnTextChange(binding.etPassword, binding.btnLogin, binding.tilPassword);
    }

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
     * Lanza el 'Credential Manager' en el fragment cuando se pulsa sobre el botón de Google para
     * iniciar sesión con las cuentas asociadas al dispositivo.
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
     * Gestiona la respuesta de éxito en el login.
     *
     * @param response El resultado de la solicitud de la credencial.
     */
    private void handleSignIn(GetCredentialResponse response) {
        Credential credential = response.getCredential();
        // Check if credential is of type Google ID
        if (credential instanceof CustomCredential) {
            CustomCredential customCredential = (CustomCredential) credential;
            if (TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
                // Create Google ID Token
                Bundle credentialData = customCredential.getData();
                GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);

                // Sign in to Firebase using the token
                authViewModel.loginWithGoogle(googleIdTokenCredential.getIdToken());
            } else {
                Log.w(TAG, "Credential is not of type Google ID!");
            }
        } else {
            Log.w(TAG, "Credential is not a CustomCredential!");
        }
    }

    /**
     * Gestiona la respuesta de error en el login desde el 'Credential Manager'
     *
     * @param e El error lanzado por el 'Credential Manager'.
     */
    private void handleFailure(GetCredentialException e) {
        Log.e(TAG, "Sign in failed", e);

        // Mostrar mensaje de error al usuario
        if (getContext() != null) {
            Toast.makeText(getContext(), "Error during Google sign in: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        // Opcional: Manejar tipos específicos de errores
        if (e instanceof NoCredentialException) {
            // El usuario canceló el flujo de autenticación
            Log.d(TAG, "User canceled the sign in flow");
        } else if (e instanceof GetCredentialInterruptedException) {
            // El flujo fue interrumpido
            Log.w(TAG, "Sign in flow was interrupted");
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}