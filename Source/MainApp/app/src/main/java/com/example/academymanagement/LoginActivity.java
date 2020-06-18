package com.example.academymanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // private variables for text fields and buttons
    private EditText login_email, login_password;
    private Button login_button_login;
    private TextView login_button_register;

    // private variable for the progress bar
    private ProgressBar login_progress_bar;

    // Firebase variables
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    // TAG for debugging and log entries
    private static final String TAG="Login Activity: ";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase reference for authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Check if the user has already logged in, if true direct to customeractivity
        CheckLoggedInUser();

        // Variable references
        AddVariableReferences();

        // Set the progress bar invisible
        login_progress_bar.setVisibility(View.INVISIBLE);

        // Defines onclick listeners on both buttons and the code which redirects the user on success
        // or shows error
        SetButtonListeners();
    }

    // Start a auth listener on start up
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    // On back button press Redirect user back to the Snooker Activity home
    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, SnookerAcademy.class));
        super.onBackPressed();
    }

    // This function assigns the private varibles the object ids to use in other functions
    public void AddVariableReferences(){
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_button_login = findViewById(R.id.login_button_login);
        login_button_register = findViewById(R.id.login_button_register);
        login_progress_bar =findViewById(R.id.login_progessbar);
    }

    // This function checks if the user is authenticated, if he is redirects to customer activity
    // else continue
    public void CheckLoggedInUser(){
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if( firebaseUser != null ){
                    Log.d(TAG,"User is already logged In.");
                    Toast.makeText(LoginActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, CustomerActivity.class));
                }
                else{
                    Log.d(TAG,"Please Login.");
                    Toast.makeText(LoginActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    // on click listener on login button which takes the input from the user
    // checks if the credentials are there and appropriate
    // uses firebase to sign in the user and redirect if successful or throw a message
    // on click listener on the registration button to redirect to registration activity
    public void SetButtonListeners(){
        login_button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Login Button Clicked!");
                String email = login_email.getText().toString();
                String pwd = login_password.getText().toString();
                if(email.isEmpty()){
                    login_email.setError("Please enter email id");
                    login_email.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    login_password.setError("Please enter your password");
                    login_password.requestFocus();
                }
                else  if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty())){
                    login_progress_bar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"Login Error, Please Login Again!",Toast.LENGTH_SHORT).show();
                                Log.d(TAG,"Login Error, Please Login Again!");
                                login_progress_bar.setVisibility(View.INVISIBLE);
                            }
                            else{
                                Log.d(TAG, "Login Successful, redirecting the user.");
                                Intent intToHome = new Intent(LoginActivity.this, CustomerActivity.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Error Occurred!");
                }
            }
        });

        login_button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Register Button Clicked!, redirecting the user.");
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}
