package com.example.eventease2.Attendee;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.eventease2.R;
import com.example.eventease2.databinding.ActivityAttendeeStartBinding;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This activity serves as the entry point for the Attendee app, managing bottom navigation,
 * fragment transactions, and the creation of unique attendee IDs.
 * <p>
 * The activity allows users to switch between fragments: QR Scanner, Event Details, and Profile.
 * It also generates a unique attendee ID when the app starts, manages fragment transactions,
 * and saves attendee information to a file when the activity is destroyed.
 * </p>
 * <p>
 * The AttendeeStartActivity uses {@link AttendeeItemViewModel} to manage attendee data and
 * {@link ActivityAttendeeStartBinding} for view binding.
 * </p>
 * <p>
 * This class utilizes {@link FirebaseStorage} to store attendee information.
 * </p>
 * <p>
 * Author: Sean
 * </p>
 */
public class AttendeeStartActivity extends AppCompatActivity {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_LENGTH = 15;
    private ActivityAttendeeStartBinding binding;
    private AttendeeItemViewModel viewModel;
    private String eventID;
    private String organizerID;

    /**
     * When the activity is created, initializes the view binding, sets up bottom navigation,
     * generates a unique attendee ID, and loads attendee information from a file.
     * <p>
     * The bottom navigation allows users to switch between fragments: QR Scanner, Event Details,
     * and Profile. It also listens for navigation item selections and replaces fragments accordingly.
     * </p>
     * <p>
     * This method uses {@link AttendeeItemViewModel} to manage attendee data.
     * </p>
     * @author Sean
     */
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
                if (!Objects.equals(eventID, "")) {
                    replaceFragment(new AttendeeEventFragment(eventID, organizerID));
                } else {
                    replaceFragment(new AttendeeEventFragment());
                }
            } else if (itemId == R.id.Profile) {
                if (!Objects.equals(eventID, "")) {
                    replaceFragment(new AttendeeProfileFragment(eventID, organizerID));
                } else {
                    replaceFragment(new AttendeeProfileFragment());
                }
            }
            return true;
        });
    }

    /**
     * /**
     * Replaces the current fragment with the specified fragment.
     * <p>
     * This method is used to navigate between fragments within the activity.
     * </p>
     *
     * @param fragment The fragment to replace the current fragment with.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Generates a random attendee ID.
     * <p>
     * This method generates a random string of alphanumeric characters to serve as
     * the unique attendee ID.
     * </p>
     *
     * @return A randomly generated attendee ID.
     */
    public String generateRandomID() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    //App would crash if user swticehd too fast. this slows down switching to avoid crashes
    public void fragmentWait() {
        synchronized (this) {
            try {
                this.wait(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Saves attendee information to a file when the activity is destroyed.
     * <p>
     * This method saves attendee ID, name, phone, email, and bio to a text file.
     * </p>
     */
    @Override
    protected void onDestroy() {
        File path = getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer = new FileOutputStream(new File(path, "AttendeeInfo.txt"));
            FileOutputStream writerImage = new FileOutputStream(new File(path, "AttendeeUri.png"));
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

    /**
     * Loads attendee information from a file.
     * <p>
     * This method reads attendee information from a text file and sets it in the ViewModel.
     * </p>
     */
    public void loadContent() {
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, "AttendeeInfo.txt");
        byte[] content = new byte[(int) readFrom.length()];

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(readFrom);
            stream.read(content);

            String s = new String(content);
            s = s.substring(1, s.length() - 1);
            String[] split = s.split(", ");
            if (split.length == 5) {
                viewModel.setAttendeeID(split[0]);
                viewModel.setProfileName(split[1]);
                viewModel.setProfilePhone(split[2]);
                viewModel.setProfileEmail(split[3]);
                viewModel.setProfileBio(split[4]);
                // Create a storage reference from our app
            }
            if (split.length >= 1) {
                viewModel.setAttendeeID(split[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}