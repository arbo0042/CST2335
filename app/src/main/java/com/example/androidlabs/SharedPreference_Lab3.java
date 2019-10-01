package com.example.androidlabs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SharedPreference_Lab3 extends AppCompatActivity {

    /**
     * @param savedInstanceState
     */
    //@Override
    protected void OnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab3);

        EditText editText = findViewById(R.id.emailInput);



        SharedPreferences prefs = getSharedPreferences("activity_main_lab3" , MODE_PRIVATE);

        String previous = prefs.getString("Reserve Name", "Default Value");

        editText.setText(previous);


    }
}

