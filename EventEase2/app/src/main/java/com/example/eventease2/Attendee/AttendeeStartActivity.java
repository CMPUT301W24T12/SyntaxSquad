package com.example.eventease2.Attendee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.eventease2.R;
import com.example.eventease2.databinding.ActivityAttendeeStartBinding;
import com.example.eventease2.databinding.ActivityMainBinding;

public class AttendeeStartActivity extends AppCompatActivity {

    ActivityAttendeeStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendeeStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new QRFragment());


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.QR_Scanner) {
                replaceFragment(new QRFragment());
            } else if (itemId == R.id.Event) {
                replaceFragment(new EventFragment());
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