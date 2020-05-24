package com.example.academymanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText register_email, register_password, register_confirm_password, register_username;
    private Button register_button_register;
    private TextView register_button_login;

    private FirebaseAuth firebaseAuth;

    // Access a Cloud Firestore instance from your Activity
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        register_confirm_password = findViewById(R.id.register_confirm_password);
        register_username = findViewById(R.id.register_username);

        register_button_register = findViewById(R.id.register_button_register);
        register_button_login = findViewById(R.id.register_button_login);

        register_button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                //else if(confirm_pwd != pwd){
                    //Toast.makeText(RegisterActivity.this, "Passwords dont match", Toast.LENGTH_SHORT).show();
                //}
                else  if(email.isEmpty() && pwd.isEmpty() && confirm_pwd.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty() && confirm_pwd.isEmpty())){
                    firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Registration Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Map<String, Object> user = new HashMap<>();
                                user.put("email", email);
                                user.put("name", username);
                                // Add a new document with a generated ID
                                db.collection("users")
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(RegisterActivity.this, "DocumentSnapshot added with ID: " + documentReference.getId(),Toast.LENGTH_SHORT).show();
                                                //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterActivity.this, "Error adding document", Toast.LENGTH_SHORT).show();
                                                //Log.w(TAG, "Error adding document", e);
                                            }
                                        });
                                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegisterActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }
            }
        });

        register_button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
