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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
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
        loadContent();
        //.makeText(this, viewModel.getAttendeeID(), Toast.LENGTH_SHORT).show();

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
    //App would crash if user swticehd too fast. this slows down switching to avoid crashes
    public void fragmentWait(){
        synchronized (this) {
            try {
                this.wait(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onDestroy() {
        File path = getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer = new FileOutputStream(new File(path,"AttendeeInfo.txt"));
            ArrayList<String> attendeeInfo = new ArrayList<String>();
            attendeeInfo.add(viewModel.getAttendeeID());
            attendeeInfo.add(viewModel.getProfileName());
            attendeeInfo.add(viewModel.getProfilePhone());
            attendeeInfo.add(viewModel.getProfileEmail());
            attendeeInfo.add(viewModel.getProfileBio());
            writer.write(attendeeInfo.toString().getBytes());
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.onDestroy();
    }
    public void loadContent() {
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path,"AttendeeInfo.txt");
        byte[] content = new byte[(int) readFrom.length()];

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(readFrom);
            stream.read(content);

            String s = new String(content);
            s = s.substring(1,s.length()-1);
            String split[] = s.split(", ");
            if(split.length == 5){
                viewModel.setAttendeeID(split[0]);
                viewModel.setProfileName(split[1]);
                viewModel.setProfilePhone(split[2]);
                viewModel.setProfileEmail(split[3]);
                viewModel.setProfileBio(split[4]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}