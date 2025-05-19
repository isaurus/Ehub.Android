package com.isaac.ehub;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.isaac.ehub.databinding.ActivityLaunchBinding;
import com.isaac.ehub.ui.auth.AuthActivity;
import com.isaac.ehub.ui.auth.AuthViewModel;
import com.isaac.ehub.ui.home.HomeActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LaunchActivity extends AppCompatActivity {

    private ActivityLaunchBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityLaunchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        checkAuthState();
    }

    private void checkAuthState() {
        if (authViewModel.isUserAuthenticated()){
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            startActivity(new Intent(this, AuthActivity.class));
        }
        finish();
    }
}