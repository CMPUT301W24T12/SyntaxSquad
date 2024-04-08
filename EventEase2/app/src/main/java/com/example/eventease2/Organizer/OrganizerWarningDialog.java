package com.example.eventease2.Organizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.eventease2.R;
/**
 * This dialog fragment is used by organizers to choose whether to use an existing QR code or generate a new one
 * when adding an event. It provides options to select an existing QR code or generate a new QR code.
 * The {@link #onAttach(Context)} method attaches the QRCodeTypeChangeListener to the context to communicate
 * with the activity hosting the fragment.
 * The {@link #onCreateDialog(Bundle)} method creates the dialog with two buttons: one for selecting an existing QR code
 * and another for generating a new QR code. It handles the button clicks and communicates the selected QR code type
 * to the hosting activity via the QRCodeTypeChangeListener.
 * The {@link #updateORCodeType(String)} method updates the QR code type based on the user's selection.
 * The {@link #intentToReuseQR()} method initiates the intent to reuse an existing QR code.
 */
public class OrganizerWarningDialog extends DialogFragment {

    interface QRCodeTypeChangeListener {
        void onQRCodeTypeChange(String type);
        void intentTOReuseQRCode();
    }

    private QRCodeTypeChangeListener mListener;

    // Method to set the listener
    public void setOrganizerIDChangeListener(QRCodeTypeChangeListener listener) {
        mListener = listener;
    }

    // Inside your dialog fragment, when you want to change the organizerID:
    private void updateORCodeType(String newType) {
        if (mListener != null) {
            mListener.onQRCodeTypeChange(newType);
        }
    }

    private void intentToReuseQR(){
        mListener.intentTOReuseQRCode();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof QRCodeTypeChangeListener) {
            mListener = (QRCodeTypeChangeListener) context;
        } else {
            throw new RuntimeException(context + "must implement QRCodeTypeListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.warning, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        Button exist = view.findViewById(R.id.existing);
        Button newQR = view.findViewById(R.id.new_qr);

        exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateORCodeType("exist");
                dismiss();
                intentToReuseQR();
            }
        });

        newQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateORCodeType("new");
                dismiss();
            }
        });

        builder.setView(view);

        // Create the dialog
        AlertDialog dialog = builder.create();
        // Make the dialog not cancelable by touching outside
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();

        return dialog;
    }
}
