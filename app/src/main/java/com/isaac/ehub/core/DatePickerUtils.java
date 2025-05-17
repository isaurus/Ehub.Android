package com.isaac.ehub.core;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerUtils {

    public interface DatePickerListener {
        void onDateSelected(String isoDate, String displayDate);
    }

    /**
     * Muestra un diálogo de selección de fecha con restricciones de fecha mínima y máxima.
     *
     * @param context Contexto de la aplicación
     * @param minDate Fecha mínima seleccionable (null para no establecer límite)
     * @param maxDate Fecha máxima seleccionable (null para no establecer límite)
     * @param initialDate Fecha inicial que se mostrará en el picker
     * @param listener Callback para recibir la fecha seleccionada
     */
    public static void showDatePickerDialog(Context context,
                                            Calendar minDate,
                                            Calendar maxDate,
                                            Calendar initialDate,
                                            DatePickerListener listener) {

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);

            // Formatear fecha para almacenamiento (ISO)
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String isoDate = isoFormat.format(selectedDate.getTime());

            // Formatear fecha para mostrar
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String displayDate = displayFormat.format(selectedDate.getTime());

            if (listener != null) {
                listener.onDateSelected(isoDate, displayDate);
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(
                context,
                dateSetListener,
                initialDate.get(Calendar.YEAR),
                initialDate.get(Calendar.MONTH),
                initialDate.get(Calendar.DAY_OF_MONTH)
        );

        // Establecer límites de fecha si se especificaron
        if (minDate != null) {
            dialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        }

        if (maxDate != null) {
            dialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        }

        dialog.show();
    }

    /**
     * Configura un TextView para mostrar el selector de fecha al hacer clic.
     *
     * @param textView TextView que activará el selector
     * @param initialDate Fecha inicial que se mostrará
     * @param minDate Fecha mínima permitida (null para no límite)
     * @param maxDate Fecha máxima permitida (null para no límite)
     * @param listener Callback para la fecha seleccionada
     */
    public static void setUpDatePicker(TextView textView,
                                       Calendar initialDate,
                                       Calendar minDate,
                                       Calendar maxDate,
                                       DatePickerListener listener) {

        textView.setOnClickListener(v -> {
            showDatePickerDialog(
                    textView.getContext(),
                    minDate,
                    maxDate,
                    initialDate,
                    listener
            );
        });
    }

    /**
     * Versión simplificada sin límites de fecha.
     */
    public static void setUpDatePicker(TextView textView,
                                       Calendar initialDate,
                                       DatePickerListener listener) {
        setUpDatePicker(textView, initialDate, null, null, listener);
    }
}