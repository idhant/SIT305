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
import androidx.appcompat.app.AppCompatActivity;

import com.example.academymanagement.models.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    // Private variables for the text fields
    private EditText register_email, register_password, register_confirm_password, register_username;

    // Private variables for the buttons
    private Button register_button_register;
    private TextView register_button_login;

    // private variable for the progress bar
    private ProgressBar register_progress_bar;

    // Firebase variables
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    // Access a Cloud Firestore instance from your Activity
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // TAG for debugging and log entries
    private static final String TAG="Register Activity: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Firebase reference for authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Check if the user has already logged in, if true direct to customeractivity
        CheckLoggedInUser();

        // Variable references
        AddVariableReferences();

        // Set the progress bar invisible
        register_progress_bar.setVisibility(View.INVISIBLE);

        // Defines onclick listeners on both buttons and the code which redirects the user on success
        // or shows error
        SetButtonListeners();
    }

    // On back button press Redirect user back to the Snooker Activity home
    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, SnookerAcademy.class));
        super.onBackPressed();
    }

    // This function assigns the private varibles the object ids to use in other functions
    public void AddVariableReferences(){
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        register_confirm_password = findViewById(R.id.register_confirm_password);
        register_username = findViewById(R.id.register_username);
        register_button_register = findViewById(R.id.register_button_register);
        register_button_login = findViewById(R.id.register_button_login);
        register_progress_bar = findViewById(R.id.register_progessbar);
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
                    Toast.makeText(RegisterActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    startActivity( new Intent(RegisterActivity.this, CustomerActivity.class));
                }
                else{
                    Log.d(TAG,"Please Login.");
                    Toast.makeText(RegisterActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    // on click listener on register button which takes the input from the user
    // checks if the credentials are there and appropriate
    // uses firebase to register the user and redirect if successful or throw a message
    // on click listener on the login button to redirect to login activity
    public void SetButtonListeners(){
        register_button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Register Button Clicked!");
                final String email = register_email.getText().toString();
                String pwd = register_password.getText().toString();
                String confirm_pwd = register_confirm_password.getText().toString();
                final String username = register_username.getText().toString();

                if(email.isEmpty()){
                    register_email.setError("Please enter email id");
                    register_email.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    register_password.setError("Please enter your password");
                    register_password.requestFocus();
                }
                else if(confirm_pwd.isEmpty()){
                    register_confirm_password.setError("Please confirm your password");
                    register_confirm_password.requestFocus();
                }
                else if(username.isEmpty()){
                    register_username.setError("Please enter a username");
                    register_username.requestFocus();
                }
                else  if(email.isEmpty() && pwd.isEmpty() && confirm_pwd.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty() && confirm_pwd.isEmpty())){
                    firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Log.d(TAG,"Registration Unsuccessful, Please Try Again!");
                                Toast.makeText(RegisterActivity.this,"Registration Unsuccessful, Please Try Again!",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                register_progress_bar.setVisibility(View.VISIBLE);
                                int credits = 0;
                                int points = 0;
                                Customer customer = new Customer(username, email, credits, points);
                                db.collection("customers").document(email).set(customer)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot added with ID: " + username);
                                                Toast.makeText(RegisterActivity.this, "DocumentSnapshot added with ID: " + username,Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                register_progress_bar.setVisibility(View.INVISIBLE);
                                                Log.d(TAG, "Error adding document");
                                                Toast.makeText(RegisterActivity.this, "Error adding document", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                startActivity(new Intent(RegisterActivity.this, CustomerActivity.class));
                            }
                        }
                    });
                }
                else{
                    Log.d(TAG,"Error Occurred!");
                    Toast.makeText(RegisterActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Opening the login activity
        register_button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Login Button Clicked!, redirecting the user.");
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }
}
