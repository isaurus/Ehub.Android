package com.isaac.ehub;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.isaac.ehub.databinding.ActivityLaunchBinding;
import com.isaac.ehub.ui.auth.LoginActivity;
import com.isaac.ehub.ui.auth.LoginViewModel;
import com.isaac.ehub.ui.home.HomeActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Esta clase lanza al usuario al corazón de la aplicación si está logeado y, en caso contrario,
 * lo direcciona a la fase de registro/login.
 */
@AndroidEntryPoint
public class LaunchActivity extends AppCompatActivity {

    private ActivityLaunchBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Inflamos el layout con el binding y lo seteamos
        binding = ActivityLaunchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Iniciamos el AuthViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Comprobamos si el usuario está ya autenticado
        checkAuthState();
    }

    /**
     * Comprueba si el usuario está previamente logeado. En caso afirmativo, lanza al usuario a las
     * funcionalidades principales de la aplicación. En caso contrario, lo direcciona a la fase de
     * registro/login.
     */
    private void checkAuthState() {
        if (loginViewModel.isUserAuthenticated()){   // Si está autenticado
            startActivity(new Intent(this, HomeActivity.class));
        } else {    // Si NO está autenticado
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}