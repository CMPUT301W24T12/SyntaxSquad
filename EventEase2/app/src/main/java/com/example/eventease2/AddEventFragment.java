package com.example.eventease2;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This is the class to add a event into the event list
 */
public class AddEventFragment extends AppCompatActivity {
    private ImageView imageView;
    private TextView eventNameView;
    private TextView descriptionView;
    private  TextView locationView;
    private CheckBox isAbleLocationTrackingView;
    private EditText durationView;

    private Button generateButton;

    private static final int REQUEST_IMAGE_PICK = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image_page);

        imageView = findViewById(R.id.imageButton);
        eventNameView = findViewById(R.id.editTextText);
        descriptionView = findViewById(R.id.editTextText2);
        locationView = findViewById(R.id.editTextText3);
        isAbleLocationTrackingView = findViewById(R.id.enable_location_checkbox);
        durationView = findViewById(R.id.editTextText4);
        generateButton = findViewById(R.id.button2);

        //get the event info to make an event
        //Image image = imageView.getImageAlpha();

        // let the user click to upload an image
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    /**
     * Define a launcher for picking images
     */
    ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                // Handle the picked image URI here
                if (uri != null) {
                    // Set the selected file to the image view
                    imageView.setImageURI(uri);
                }
            }
    );

    /**
     * Call the launcher to select image
     */
    void selectImage() {
        pickImageLauncher.launch("image/*");
    }


}
