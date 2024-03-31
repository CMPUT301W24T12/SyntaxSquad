package com.example.eventease2.Organizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.eventease2.R;

public class OrganizerWarningDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.warning, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Yes",(dialog, which) ->{

                })
                .create();
    }
}
