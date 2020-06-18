package com.example.academymanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SnookerAcademy extends AppCompatActivity {

    // Variables for the objects
    private TextView homeWelcome, homeDescription, homeOptions;
    private Button homeCustomerLoginButton, homeCustomerRegisterButton;

    // Tag for log and debugging
    private static final String TAG="Snooker Academy Home: ";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // referencing the objects
        AddVariableReferences();

        // Defines onclick listeners on both buttons and the code which redirects the user on success
        // or shows error
        SetButtonListeners();
    }

    // This function assigns the private variables the object ids to use in other functions
    public void AddVariableReferences(){
        homeWelcome = findViewById(R.id.activity_home_welcome_message);
        homeDescription = findViewById(R.id.activity_home_application_description);
        homeOptions = findViewById(R.id.activity_home_interaction_options);
        homeCustomerLoginButton = findViewById(R.id.activity_home_customer_login_button);
        homeCustomerRegisterButton = findViewById(R.id.activity_home_customer_register_button);
    }

    // Sets on click listeners on both the buttons. Redirects the user appropriately
    public void SetButtonListeners(){
        homeCustomerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Login Button clicked, opening LoginActivity.");
                startActivity(new Intent(SnookerAcademy.this, LoginActivity.class));
            }
        });

        homeCustomerRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Registration Button clicked, opening RegisterActivity.");
                startActivity(new Intent(SnookerAcademy.this, RegisterActivity.class));
            }
        });
    }
}

/*
    android:background="#00FF62"

 */
