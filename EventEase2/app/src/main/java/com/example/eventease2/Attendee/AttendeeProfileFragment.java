package com.example.eventease2.Attendee;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.eventease2.R;
import com.example.eventease2.RoleChooseActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;


/**
 * A fragment that displays and manages the attendee's profile information.
 * <p>
 * This fragment allows attendees to view and edit their profile details such as name, phone number,
 * email, and bio. Attendees can also upload or delete their profile picture.
 * <p>
 * Additionally, attendees can enable geolocation tracking to share their current coordinates with
 * event organizers, if enabled by the organizer.
 * <p>
 * Dependencies:
 * <p>
 * This fragment relies on Firebase Firestore for storing attendee data and Firebase Storage for
 * storing profile pictures. It also uses Picasso for image loading and Glide for image uploading.
 * <p>
 * Note: This fragment assumes that the attendee's profile picture is stored in Firebase Storage
 * under a folder named "profilepics" with the attendee's ID as the filename.
 * <p>
 * Permissions:
 * <p>
 * This fragment requests location permission to enable geolocation tracking. Users need to grant
 * location permission for this feature to work properly. However, this was not fully implemented.
 *
 * @author Sean Sebheyer
 */
public class AttendeeProfileFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FirebaseStorage storage;
    private StorageReference storageRef, pathReference;
    private Button attendeeSaveChanges;
    private FirebaseFirestore appDb;
    private EditText attendeeNameText, attendeePhoneText, attendeeEmailText, attendeeBioText;
    private ImageButton attendeeImage;
    private Uri selectedImage;
    private final String event;
    private final String organizer;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_profile, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(AttendeeItemViewModel.class);

        TextView back = view.findViewById(R.id.home_profile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the AppEvent activity
                Intent intent = new Intent(getActivity(), RoleChooseActivity.class);
                startActivity(intent);
            }
        });
        firebase();
        getEditProfile(view);
        setTextFromModel();

        // Set up checkbox listener
        checkBox = view.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isGeoLocationEnabled = isChecked;
            if (isChecked) {
                requestLocationPermission();
            } else {
                stopLocationUpdates();
            }
        });
        attendeeSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setModelItems();
                Toast.makeText(getContext(), "Profile Saved!", Toast.LENGTH_SHORT).show();
                if (!Objects.equals(event, "")) {
                    addAttendeeData();

                }
            }
        });
        ImageButton gallery = view.findViewById(R.id.attendeeProfileImage);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 3);

            }
        });
        Button deleteButton = view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable defaultPhoto = getResources().getDrawable(R.drawable.upload_image_button);
                gallery.setImageDrawable(defaultPhoto);

                viewModel.setProfileImage(null);
                storage = FirebaseStorage.getInstance();
                storageRef = storage.getReference();
                StorageReference attendeeProfile = storageRef.child("profilepics/" + viewModel.getAttendeeID());

                attendeeProfile.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("DeletePhoto", "Photo successfully deleted");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DeletePhoto", "Photo NOT deleted");
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            ImageButton imageButton = getView().findViewById(R.id.attendeeProfileImage);
            imageButton.setImageURI(selectedImage);
        }
    }

    /**
     * Adds attendee data to the Firebase Firestore database.
     * <p>
     * This method creates a HashMap containing the attendee's name, email, and phone number,
     * and then stores this data in the Firestore database under the "Attendees" collection.
     */
    public void addAttendeeData() {
        HashMap<String, String> data = new HashMap<>();
        data.put("Name", viewModel.getProfileName());
        data.put("Email", viewModel.getProfileEmail());
        data.put("Phone", viewModel.getProfilePhone());
        attendeeCollect.document(viewModel.getAttendeeID()).set(data);

    }

    /**
     * On create, the fragment will find the texts and image.
     *
     * @param view The view of the Fragment attendee Profile so it can correctly match with the text and image
     *             fields.
     */
    public void getEditProfile(View view) {
        attendeeSaveChanges = view.findViewById(R.id.AttendeeAddChanges);
        attendeeNameText = view.findViewById(R.id.editProfileName);
        attendeePhoneText = view.findViewById(R.id.editTextPhone2);
        attendeeEmailText = view.findViewById(R.id.editProfileEmail);
        attendeeImage = view.findViewById(R.id.attendeeProfileImage);
        attendeeBioText = view.findViewById(R.id.editBioText);
        if (!Objects.equals(viewModel.getProfileName(), "") && viewModel.getProfileImage() == null) {
            String identiconURL = "http://www.gravatar.com/avatar/" + viewModel.getProfileName() + "?s=55&d=identicon&r=PG";
            Picasso.get().load(identiconURL).into(attendeeImage);

        }
    }

    /**
     * Sets the data entered by the attendee into the ViewModel for later use.
     * <p>
     * This method retrieves the text entered into EditText fields and the selected image
     * (if any) from the ImageButton, and sets this data into the ViewModel, ensuring that
     * changes made by the user are stored for later use.
     */
    public void setModelItems() {
        viewModel.setProfileName(attendeeNameText.getText().toString());
        viewModel.setProfileEmail(attendeeEmailText.getText().toString());
        viewModel.setProfilePhone(attendeePhoneText.getText().toString());
        viewModel.setProfileBio(attendeeBioText.getText().toString());

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        StorageReference attendeeProfile = storageRef.child("profilepics/" + viewModel.getAttendeeID());
        UploadTask uploadTask = null;

        if (selectedImage != null) {
            viewModel.setProfileImage(selectedImage);
            uploadTask = attendeeProfile.putFile(selectedImage);
        } else {
            //Below does not update to the firebase since I don't know how to switch imageview to URI
            String identiconURL = "http://www.gravatar.com/avatar/" + viewModel.getProfileName() + "?s=55&d=identicon&r=PG";
            Picasso.get().load(identiconURL).into(attendeeImage);
            uploadTask = attendeeProfile.putFile(Uri.parse(identiconURL));
        }
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "pic did not upload!");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "pic did upload!");
            }
        });

    }

    /**
     * Retrieves and sets the attendee's profile information from the ViewModel.
     * <p>
     * This method sets the text of EditText fields and the image of the ImageButton
     * to match the data stored in the ViewModel, ensuring consistency with the displayed
     * profile information.
     */
    public void setTextFromModel() {
        attendeeNameText.setText(viewModel.getProfileName());
        attendeePhoneText.setText(viewModel.getProfilePhone());
        attendeeEmailText.setText(viewModel.getProfileEmail());
        attendeeBioText.setText(viewModel.getProfileBio());
        if (viewModel.getProfileImage() == null) {
            getFirebaseProfileImage();
        } else {
            attendeeImage.setImageURI(viewModel.getProfileImage());
        }

    }

    /**
     * Initializes Firebase components necessary for communication with the database.
     * <p>
     * This method initializes the FirebaseFirestore instance and sets up the CollectionReference
     * for storing attendee data under the appropriate organizer and event.
     */
    public void firebase() {
        if (!Objects.equals(event, "")) {
            appDb = FirebaseFirestore.getInstance();

            attendeeCollect = appDb.collection("Organizer").document(organizer)
                    .collection("Events")
                    .document(event)
                    .collection("Attendees");
        }
    }

    /**
     * Retrieves the attendee's profile image from Firebase Storage.
     * <p>
     * This method retrieves the profile picture stored in Firebase Storage and sets it
     * as the image for the ImageButton in the fragment's layout.
     */
    public void getFirebaseProfileImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        pathReference = storageRef.child("profilepics").child(viewModel.getAttendeeID());

        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                attendeeImage.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

    /**
     * Requests permission for accessing the device's location.
     * <p>
     * This method requests the ACCESS_FINE_LOCATION permission from the user to enable
     * geolocation tracking. If the permission is granted, location updates are started.
     * However, geolocation was not fully implemented.
     */
    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // If the permission is not granted, request it
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                // If the permission is already granted, start location updates
                requestLocationUpdates();
            }
        }
    }

    /**
     * Handles the result of the location permission request.
     * <p>
     * This method is called when the user responds to the location permission request.
     * If the permission is granted, location updates are started; otherwise, a message
     * is displayed indicating that location permission was denied.
     * However, geolocation was not fully implemented.
     *
     * @param requestCode  The code originally supplied to requestPermissions().
     * @param permissions  The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                Toast.makeText(getContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Starts location updates if location permission is granted.
     * <p>
     * This method requests location updates from the LocationManager if the ACCESS_FINE_LOCATION
     * permission is granted by the user. Location updates provide the current coordinates of
     * the device, which can be shared with event organizers if geolocation tracking is enabled.
     * However, geolocation was not fully implemented.
     */
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
                public void onProviderEnabled(@NonNull String provider) {
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    /**
     * Stops location updates.
     * <p>
     * This method removes location updates previously requested from the LocationManager.
     * It is called when geolocation tracking is disabled or when the fragment is destroyed.
     * However, geolocation was not fully implemented.
     */
    private void stopLocationUpdates() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}