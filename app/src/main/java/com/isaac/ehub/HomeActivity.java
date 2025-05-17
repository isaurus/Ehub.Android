package com.isaac.ehub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.isaac.ehub.databinding.ActivityHomeBinding;
import com.isaac.ehub.ui.auth.AuthActivity;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding; // Objeto de View Binding
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                performLogout();
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void performLogout() {
        // Lógica de logout
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