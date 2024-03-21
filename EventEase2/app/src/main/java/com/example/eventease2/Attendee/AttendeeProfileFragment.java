package com.example.eventease2.Attendee;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import android.Manifest;
import com.example.eventease2.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

/**
 * This fragments shows the user the empty profile unless saved changes were updated.
 */
public class AttendeeProfileFragment extends Fragment {

    private Button attendeeSaveChanges;
    private FirebaseFirestore appDb;
    private EditText attendeeNameText,attendeePhoneText,attendeeEmailText,attendeeBioText;
    private ImageButton attendeeImage;
    private Uri selectedImage;
    private String event,organizer;
    private AttendeeItemViewModel viewModel;
    private CollectionReference attendeeCollect;
    private CheckBox checkBox;
    private boolean isGeoLocationEnabled = false;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public AttendeeProfileFragment() {
        event = "";
        organizer = "";
    }

    public AttendeeProfileFragment(String eventID, String organizerID) {
        this.event = eventID;
        this.organizer = organizerID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_profile, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(AttendeeItemViewModel.class);

        firebase();
        getEditProfile(view);
        setTextFromModel();

        // Set up checkbox listener
        checkBox = view.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isGeoLocationEnabled = isChecked;
            if (isChecked) {
                requestLocationUpdates();
            } else {
                stopLocationUpdates();
            }
        });

        // Set up save changes button listener
        attendeeSaveChanges.setOnClickListener(v -> {
            setModelItems();
            if(!Objects.equals(event, "")) {
                addAttendeeData();
            }
        });

        // Set up gallery button listener
        ImageButton gallery = view.findViewById(R.id.attendeeProfileImage);
        gallery.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i,3);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            selectedImage = data.getData();
            ImageButton imageButton = getView().findViewById(R.id.attendeeProfileImage);
            imageButton.setImageURI(selectedImage);
        }
    }

    /**
     * This function creates a hashmap, and then stores the name, email, and phone to the firebase
     * Still need functionality of storing their photos to the database.
     */
    public void addAttendeeData(){
        HashMap<String,String> data = new HashMap<>();
        data.put("Name", viewModel.getProfileName());
        data.put("Email", viewModel.getProfileEmail());
        data.put("Phone", viewModel.getProfilePhone());
        attendeeCollect.document(viewModel.getAttendeeID()).set(data);
    }

    /**
     * On create, the fragment will find the texts and image.
     */
    public void getEditProfile(View view){
        attendeeSaveChanges = view.findViewById(R.id.AttendeeAddChanges);
        attendeeNameText = view.findViewById(R.id.editProfileName);
        attendeePhoneText = view.findViewById(R.id.editTextPhone2);
        attendeeEmailText = view.findViewById(R.id.editProfileEmail);
        attendeeImage = view.findViewById(R.id.attendeeProfileImage);
        attendeeBioText = view.findViewById(R.id.editBioText);
    }

    /**
     * When called, set the current text of the edit text field and image to the viewModel for
     * later use.
     */
    public void setModelItems(){
        viewModel.setProfileName(attendeeNameText.getText().toString());
        viewModel.setProfileEmail(attendeeEmailText.getText().toString());
        viewModel.setProfilePhone(attendeePhoneText.getText().toString());
        viewModel.setProfileBio(attendeeBioText.getText().toString());
        if(selectedImage != null){
            viewModel.setProfileImage(selectedImage);
        }
    }

    /**
     * Set the text that was saved on the viewModel when users switch back and forth from their
     * profile to other fragments.
     */
    public void setTextFromModel(){
        attendeeNameText.setText(viewModel.getProfileName());
        attendeePhoneText.setText(viewModel.getProfilePhone());
        attendeeEmailText.setText(viewModel.getProfileEmail());
        attendeeBioText.setText(viewModel.getProfileBio());
        attendeeImage.setImageURI(viewModel.getProfileImage());
    }

    /**
     * Firebase implementation necessary for communicating with the database.
     */
    public void firebase(){
        if(!Objects.equals(event, "")){
            appDb = FirebaseFirestore.getInstance();

            attendeeCollect = appDb.collection("Organizer").document(organizer)
                    .collection("Events")
                    .document(event)
                    .collection("Attendees");
        }
    }

    private void requestLocationUpdates() {
        if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    if (isGeoLocationEnabled) {
                        // Display current coordinates in a toast message
                        Toast.makeText(getContext(), "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {}

                @Override
                public void onProviderDisabled(@NonNull String provider) {}

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    private void stopLocationUpdates() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
