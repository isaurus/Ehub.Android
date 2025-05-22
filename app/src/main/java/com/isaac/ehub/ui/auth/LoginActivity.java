package com.isaac.ehub.ui.auth;

import static android.content.ContentValues.TAG;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import androidx.lifecycle.ViewModelProvider;

import android.os.CancellationSignal;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.isaac.ehub.core.utils.InsetsUtils;
import com.isaac.ehub.databinding.ActivityLoginBinding;
import com.isaac.ehub.ui.home.HomeActivity;
import com.isaac.ehub.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        InsetsUtils.applySystemWindowInsetsPadding(binding.getRoot());
        
        setUpListeners();
        observeViewModel();
    }

    private void setUpListeners() {
        binding.btnGoogleSignIn.setOnClickListener(v -> {
            launchGoogleSignInFlow();
        });
    }

    private void observeViewModel() {
        loginViewModel.getLoginViewState().observe(this, loginViewState -> {
            switch(loginViewState.getStatus()){
                case VALIDATING:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnGoogleSignIn.setEnabled(false);

                    break;
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.btnGoogleSignIn.setEnabled(false);
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    this.startActivity(new Intent(this, HomeActivity.class));
                    this.finish();
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, loginViewState.getMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    private void launchGoogleSignInFlow() {
        GetSignInWithGoogleOption getSignInWithGoogleOption = new GetSignInWithGoogleOption
                .Builder(getString(R.string.default_web_client_id))
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(getSignInWithGoogleOption)
                .build();

        CredentialManager credentialManager = CredentialManager.create(this);

        credentialManager.getCredentialAsync(
                this,
                request,
                new CancellationSignal(),
                ContextCompat.getMainExecutor(this),
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

    private void handleSignIn(GetCredentialResponse response) {
        Credential credential = response.getCredential();
        if (credential instanceof CustomCredential) {
            CustomCredential customCredential = (CustomCredential) credential;
            if (TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
                Bundle credentialData = customCredential.getData();
                GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);

                loginViewModel.loginWithGoogle(googleIdTokenCredential.getIdToken());
            } else {
                Log.w(TAG, "Credential is not of type Google ID!");
            }
        } else {
            Log.w(TAG, "Credential is not a CustomCredential!");
        }
    }

    private void handleFailure(GetCredentialException e) {
        Log.e(TAG, "Sign in failed", e);

        if (getContext() != null) {
            Toast.makeText(this, "Error during Google sign in: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        if (e instanceof NoCredentialException) {
            Log.d(TAG, "User canceled the sign in flow");
        } else if (e instanceof GetCredentialInterruptedException) {
            Log.w(TAG, "Sign in flow was interrupted");
        }
    }

}