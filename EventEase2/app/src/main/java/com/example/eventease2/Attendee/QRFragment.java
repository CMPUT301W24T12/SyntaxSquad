package com.example.eventease2.Attendee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.eventease2.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRFragment extends Fragment{
    private ItemViewModel viewModel;
    public QRFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_q_r, container, false);
        super.onViewCreated(view,savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        Button btnScanQR = view.findViewById(R.id.btnScanQR);
        btnScanQR.setOnClickListener(v -> startQRScanner());
        return view;
    }

    private void startQRScanner() {
        // Initialize QR code scanner

        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setPrompt("Scan QR Code");
        integrator.setOrientationLocked(false);
//        String scannedData = "LOL";
//        viewModel.setString(scannedData);
        integrator.initiateScan();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedData = result.getContents();
                // Handle the scanned data, for example, display it or perform further actions
                displayScanResult(scannedData);
                viewModel.setString(scannedData);
            } else {
                // Handle if no QR code is found
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void displayScanResult(String scannedData) {
        // Display a message using a Toast
        Toast.makeText(getContext(), "Scan Successful: " + scannedData, Toast.LENGTH_SHORT).show();
    }

}
