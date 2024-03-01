package com.example.eventease.ui.QRScanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.eventease.databinding.FragmentQrScannerBinding;

public class QRScannerFragment extends Fragment {

    private FragmentQrScannerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        QRScannerViewModel QRScannerViewModel =
                new ViewModelProvider(this).get(QRScannerViewModel.class);

        binding = FragmentQrScannerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textQrScanner;
        QRScannerViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}