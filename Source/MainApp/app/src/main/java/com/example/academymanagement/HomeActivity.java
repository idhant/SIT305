package com.example.academymanagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView homeWelcome, homeDescription, homeOptions;

    private Button homeCustomerLoginButton, homeCustomerRegisterButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeWelcome = findViewById(R.id.activity_home_welcome_message);
        homeDescription = findViewById(R.id.activity_home_application_description);
        homeOptions = findViewById(R.id.activity_home_interaction_options);
        homeCustomerLoginButton = findViewById(R.id.activity_home_customer_login_button);
        homeCustomerRegisterButton = findViewById(R.id.activity_home_customer_register_button);


        homeCustomerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });

        homeCustomerRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, RegisterActivity.class));
            }
        });
    }

}

/*

    android:background="#00FF62"

 */
