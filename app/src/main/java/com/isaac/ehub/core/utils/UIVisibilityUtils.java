package com.isaac.ehub.core.utils;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Utilidad para gestionar la visibilidad de elementos comunes de la UI
 * según el fragmento de destino en el NavController.
 */
public class UIVisibilityUtils {

    /**
     * Aplica lógica de visibilidad condicional para las vistas indicadas,
     * según el fragmento actual.
     *
     * @param navController NavController asociado
     * @param hiddenInFragments IDs de fragments donde se deben ocultar las vistas
     * @param views Vistas que se deben ocultar/mostrar
     */
    public static void setupVisibilityListener(NavController navController,
                                               @IdRes Integer[] hiddenInFragments,
                                               View... views) {
        final Set<Integer> fragmentIdSet = new HashSet<>(Arrays.asList(hiddenInFragments));

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int destinationId = destination.getId();
            boolean shouldHide = fragmentIdSet.contains(destinationId);

            for (View view : views) {
                view.setVisibility(shouldHide ? View.GONE : View.VISIBLE);
            }
        });
    }
}
