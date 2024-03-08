package com.example.eventease2.Attendee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.example.eventease2.R;
import com.example.eventease2.databinding.ActivityAttendeeStartBinding;

import java.security.SecureRandom;
import java.util.Objects;
/**
 * This activity holds the bottom navigation code, the functionality for replacing the fragments
 * based off what the user chooses, as well as provide the user with a unique ID
 * @author Sean
 */
public class AttendeeStartActivity extends AppCompatActivity{

    ActivityAttendeeStartBinding binding;
    private AttendeeItemViewModel viewModel;
    private String eventID;
    private String organizerID;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_LENGTH = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AttendeeItemViewModel.class);
        binding = ActivityAttendeeStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new AttendeeQRFragment());

        String randomID = generateRandomID();
        viewModel.setAttendeeID(randomID);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            eventID = viewModel.getEvent();
            organizerID = viewModel.getOrganizer();

            int itemId = item.getItemId();
            if (itemId == R.id.QR_Scanner) {
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
    /**
     * When a user clicks on the a different fragment, it replaces the fragment with the
     * desired fragment
     * @param fragment
     * Desired fragment that the user wants to navigate to.
     */
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
    /**
     * When a user starts the app, a attendee ID is created
     */

    public  String generateRandomID() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

}