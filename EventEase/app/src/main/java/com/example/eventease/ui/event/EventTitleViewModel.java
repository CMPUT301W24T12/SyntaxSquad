package com.example.eventease.ui.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventTitleViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EventTitleViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is title fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }
}