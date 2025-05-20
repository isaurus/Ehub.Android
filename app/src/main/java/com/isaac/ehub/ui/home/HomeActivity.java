package com.isaac.ehub.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.isaac.ehub.R;
import com.isaac.ehub.core.utils.InsetsUtils;
import com.isaac.ehub.core.utils.UIVisibilityUtils;
import com.isaac.ehub.databinding.ActivityHomeBinding;
import com.isaac.ehub.ui.auth.AuthActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {

    // ViewBinding para acceder a los elementos de la UI
    private ActivityHomeBinding binding;

    // NagivationController para navegar por la UI
    private NavController navController;

    private HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        InsetsUtils.applySystemWindowInsetsPadding(binding.getRoot());

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null){
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNavigation, navController);


            UIVisibilityUtils.setupVisibilityListener(  // ¡NO ME ESCONDE EL LOGOUT DEL TOOLBAR!
                    navController,
                    new Integer[] { R.id.userProfileFragment, R.id.editUserProfileFragment },
                    binding.linearLayout,
                    binding.bottomNavigation
            );
        }

        setUpToolbar();
    }

    private void setUpToolbar() {
        // Configurar clic en el avatar
        binding.actionUserProfile.setOnClickListener(v -> {
            // Navegar al perfil de usuario
            navController.navigate(R.id.userProfileFragment);
        });

        binding.actionLogout.setOnClickListener(v -> signOut());
    }


    private void signOut() {
        homeViewModel.signOut();

        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Limpiar la referencia de View Binding
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}