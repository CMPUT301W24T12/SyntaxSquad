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

public class OrganizerWarningDialog extends DialogFragment {

    interface QRCodeTypeChangeListener {
        void onQRCodeTypeChange(String type);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof QRCodeTypeChangeListener) {
            mListener = (QRCodeTypeChangeListener) context;
        } else {
            throw new RuntimeException(context + "must implement AddBookListener");
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
//                Intent intent = new Intent(getActivity(),ReuseQRCodeFragment.class);
//                intent.putExtra("OrganizerID",organizerID);
//                startActivity(intent);

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
