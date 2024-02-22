package com.example.eventease.ui.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
public class EventDescriptionViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    public EventDescriptionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is descr fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
