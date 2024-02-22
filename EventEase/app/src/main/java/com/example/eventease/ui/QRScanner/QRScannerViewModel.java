package com.example.eventease.ui.QRScanner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QRScannerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public QRScannerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is QR fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}