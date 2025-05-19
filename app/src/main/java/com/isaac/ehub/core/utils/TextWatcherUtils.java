package com.isaac.ehub.core.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

public class TextWatcherUtils {
    public static void enableViewOnTextChange(EditText et, final View viewToEnable, @Nullable final TextInputLayout inputLayout){
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                viewToEnable.setEnabled(true);
                if (inputLayout != null) {
                    inputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}
