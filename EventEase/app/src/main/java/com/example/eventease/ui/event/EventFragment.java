package com.example.eventease.ui.event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.eventease.R;
import com.example.eventease.databinding.FragmentEventBinding;

public class EventFragment extends Fragment {

    private FragmentEventBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EventTitleViewModel eventTitleViewModel =
                new ViewModelProvider(this).get(EventTitleViewModel.class);

        EventDescriptionViewModel eventDescriptionViewModel =
                new ViewModelProvider(this).get(EventDescriptionViewModel.class);

        EventDetailsViewModel eventDetailsViewModel =
                new ViewModelProvider(this).get(EventDetailsViewModel.class);


        binding = FragmentEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textEventTitle;
        final TextView textView1 = binding.textEventDescription;
        final TextView textView2 = binding.textEventDetails;
        final ImageView imageView = binding.imageView;

        eventTitleViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        eventDescriptionViewModel.getText().observe(getViewLifecycleOwner(), textView1::setText);
        eventDetailsViewModel.getText().observe(getViewLifecycleOwner(), textView2::setText);
        imageView.setImageResource(R.mipmap.ic_event_foreground);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}