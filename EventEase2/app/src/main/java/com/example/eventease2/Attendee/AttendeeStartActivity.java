package com.example.eventease2.Attendee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.eventease2.R;
import com.example.eventease2.databinding.ActivityAttendeeStartBinding;

import java.util.Objects;

public class AttendeeStartActivity extends AppCompatActivity{

    ActivityAttendeeStartBinding binding;
    private AttendeeItemViewModel viewModel;
    String eventID;
    String organizerID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AttendeeItemViewModel.class);
        binding = ActivityAttendeeStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new AttendeeQRFragment());


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            eventID = viewModel.getEvent();
            organizerID = viewModel.getOrganizer();
            int itemId = item.getItemId();
            if (itemId == R.id.QR_Scanner) {
//                Intent i = new Intent(this, QRFragment.class);
//                startActivity(i);
                replaceFragment(new AttendeeQRFragment());
            } else if (itemId == R.id.Event) {
                if(!Objects.equals(eventID, "")){
                    replaceFragment(new AttendeeEventFragment(eventID,organizerID));
                }else{
                    replaceFragment(new AttendeeEventFragment());
                }
            } else if (itemId == R.id.Profile) {
                if(!Objects.equals(eventID, "")){
                    replaceFragment(new AttendeeProfileFragment(eventID,organizerID));
                }else {
                    replaceFragment(new AttendeeProfileFragment());
                }
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();

    }
}