package com.isaac.ehub.core;

import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

public class InsetsUtils {

    /**
     * Aplica padding para que el contenido no quede bajo la barra de estado ni barra de navegación.
     * Usa WindowInsets para detectar tamaños dinámicos.
     *
     * @param rootView Vista raíz donde aplicar el padding
     */
    public static void applySystemWindowInsetsPadding(View rootView) {
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(
                    view.getPaddingLeft(),
                    insets.top,
                    view.getPaddingRight(),
                    insets.bottom
            );
            return WindowInsetsCompat.CONSUMED;
        });
    }
}
