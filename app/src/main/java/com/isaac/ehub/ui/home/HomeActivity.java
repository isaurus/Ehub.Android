package com.isaac.ehub.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.isaac.ehub.R;
import com.isaac.ehub.core.InsetsUtils;
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
        }

        setUpToolbar();

    }

    private void setUpToolbar() {
        // Configurar clic en el avatar
        binding.userAvatar.setOnClickListener(v -> {
            // Navegar al perfil de usuario
            navController.navigate(R.id.userProfileFragment);
        });

        // Configurar menú de 3 puntos
        binding.menuButton.setOnClickListener(v -> {
            showPopupMenu();
        });
    }

    private void showPopupMenu() {
        PopupMenu popup = new PopupMenu(this, binding.menuButton);
        popup.inflate(R.menu.toolbar_menu);

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_about) {
                navController.navigate(R.id.aboutFragment);
                return true;
            } else if (itemId == R.id.action_logout) {
                signOut();
                return true;
            }
            return false;
        });

        popup.show();
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