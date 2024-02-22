package com.example.eventease.ui.event;

import android.graphics.drawable.Drawable;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventease.R;

public class EventDetailsViewModel extends ViewModel{
    private final MutableLiveData<String> mText;
    public EventDetailsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\"");
    }
    public LiveData<String> getText() {
        return mText;
    }
}
