package com.example.androidlabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.androidlabs.R.id.imageButton;

public class ProfileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    ImageButton mImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        // Creates a space for information to be saved if the page is paused
        // Calls information back from the onPause data
        //Retrieves email address already input in the MainActivity Screen

        Intent dataFromPreviousPage = getIntent();
        String emailFromMainActivity;
        emailFromMainActivity = dataFromPreviousPage.getStringExtra("emailAddress");

        EditText editText = findViewById(R.id.emailInput);
        editText.setText(emailFromMainActivity);

        //Sets the Image button and creates link to the Dispatch Take Picture Intent
        mImageButton = findViewById(imageButton);
        ImageButton pictureButton = (ImageButton) findViewById(R.id.imageButton);
        if (pictureButton != null) {
            pictureButton.setOnClickListener(clk -> {
                this.dispatchTakePictureIntent();
            });
        }
        Log.e(ACTIVITY_NAME, "In function onCreate");


    Button chatButton = (Button)findViewById(R.id.buttonChat);

    // Create a Click Listener  which will create an Intent
    // Also Assigns information with .putExtra to be called by another class if needed
        if (chatButton !=null)
            chatButton.setOnClickListener(v -> {
            Intent goToChatRoom = new Intent(ProfileActivity.this, ChatRoomActivity.class);
             startActivity(goToChatRoom);
        });
    }// End of OnCreate
    //Creates an Intent to use the built in camera of the Andriod Phone
         private void dispatchTakePictureIntent() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
        Log.e(ACTIVITY_NAME, "In function onActivityResult");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In function onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In function onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function onPauses");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In function onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function onDestroy");
    }


}

