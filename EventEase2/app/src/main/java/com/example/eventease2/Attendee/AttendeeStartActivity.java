package com.example.eventease2.Attendee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.eventease2.R;
import com.example.eventease2.databinding.ActivityAttendeeStartBinding;
import com.example.eventease2.databinding.ActivityMainBinding;

import java.util.Iterator;
import java.util.Objects;

public class AttendeeStartActivity extends AppCompatActivity{

    ActivityAttendeeStartBinding binding;
    private ItemViewModel viewModel;
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    String eventID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        binding = ActivityAttendeeStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new QRFragment());


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            eventID = viewModel.getString();
            int itemId = item.getItemId();
            if (itemId == R.id.QR_Scanner) {
//                Intent i = new Intent(this, QRFragment.class);
//                startActivity(i);
                replaceFragment(new QRFragment());
            } else if (itemId == R.id.Event) {
                if(!Objects.equals(eventID, "")){
                    replaceFragment(new EventFragment(eventID));
                }else{
                    replaceFragment(new EventFragment());
                }
            } else if (itemId == R.id.Profile) {
                replaceFragment(new ProfileFragment());
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