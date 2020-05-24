package com.example.academymanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText register_email, register_password, register_confirm_password;
    private Button register_button_register;
    private TextView register_button_login;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        register_confirm_password = findViewById(R.id.register_confirm_password);

        register_button_register = findViewById(R.id.register_button_register);
        register_button_login = findViewById(R.id.register_button_login);

        register_button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = register_email.getText().toString();
                String pwd = register_password.getText().toString();
                String confirm_pwd = register_confirm_password.getText().toString();
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
