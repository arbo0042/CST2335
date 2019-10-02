package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab3);
        SharedPreferences prefs = getSharedPreferences("shared_preferences" , MODE_PRIVATE);
        // The following will keep information that was already input by the user
        // if the Pause button is pressed
        // Create a new string to hold the User Input for email address
        //Call the pref variable and get the string from the emailAddress in activity_main_lab3
        // create a EditText variable to return the emailAddress to
        // Set the Text of the emailAddress back to the dataEmail variable
        String dataEmail = new String();
        prefs.getString("emailAddress", dataEmail);
        EditText editText = findViewById(R.id.emailInput);
        editText.setText(dataEmail);


        String dataPassword = new String();
        prefs.getString("password", dataPassword);
        TextView editView = findViewById(R.id.password);
        editView.setText(dataPassword);

        // Create a Button Object
        Button loginButton = (Button)findViewById(R.id.loginButton);

        // Create a Click Listener  which will create an Intent
        // Also Assigns information with .putExtra to be called by another class if needed
        if (loginButton !=null){
            loginButton.setOnClickListener(clk -> {
              Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
              goToProfile.putExtra("emailAddress",editText.getText().toString());
              startActivity(goToProfile);
           });
        }

    } // End of OnCreate

    // Creates a place to hold the users input information if the page is paused
    @Override
    protected void onPause(){
        super.onPause();

        SharedPreferences pref = getSharedPreferences("activity_main_lab3" , MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

       EditText editText1 = findViewById(R.id.emailInput);
       editor.putString("emailAddress",editor.toString());

       TextView editText2 = findViewById(R.id.password);
       editor.putString ("password", editText1.getText().toString());

        editor.commit();



    }
    }
