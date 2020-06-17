package com.example.academymanagement.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.academymanagement.LoginActivity;
import com.example.academymanagement.R;
import com.example.academymanagement.models.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsFragment extends Fragment{

    private Customer customer;

    // Firestore database reference
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference docRefCustomer, docRefHistory;

    // TAG variable for debugging
    private static final String TAG="SettingsFragment:";

    // variable to store the email of current logged-in user
    private String logEmail;

    private TextView textView, textChoice;

    private EditText newUsername, newEmail, newPassword;

    private ImageView userPhoto;

    private Spinner choiceSpinner;

    private Button selectionButton;

    private String[] items = new String[]{"username", "password", "email"};

    private String selectedChoice;

    private boolean safeToUpdate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        textView = root.findViewById(R.id.text_settings);
        choiceSpinner = root.findViewById(R.id.fragment_settings_spinner);
        textChoice = root.findViewById(R.id.fragment_settings_select_spinner_option);
        newUsername = root.findViewById(R.id.fragment_settings_new_name);
        newEmail = root.findViewById(R.id.fragment_settings_new_email);
        newPassword = root.findViewById(R.id.fragment_settings_new_password);
        selectionButton = root.findViewById(R.id.fragment_settings_button);

        CheckCurrentUser();

        // Storing the path of collection(customers)/document(email of user)
        docRefCustomer = db.collection("customers").document(logEmail);
        docRefHistory = db.collection("history").document(logEmail);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        choiceSpinner.setAdapter(adapter);
        textView.setText("Settings");
        textChoice.setText("Please select a option from dropdown and fill out the appropriate fields to update details.");

        selectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedChoice = choiceSpinner.getSelectedItem().toString();
                if(selectedChoice != null){
                    if(selectedChoice == items[0])
                    {
                        if(newUsername != null){

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(newUsername.toString())
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Username updated!");
                                                Toast.makeText(getActivity(), "Username updated!", Toast.LENGTH_SHORT).show();
                                                safeToUpdate = true;
                                                Log.d(TAG,"Safe to update other references, firebase inside: " + safeToUpdate);
                                            }
                                            else{
                                                Log.d(TAG, "Couldn`t update username!");
                                                Toast.makeText(getActivity(), "Couldn`t update username!", Toast.LENGTH_SHORT).show();
                                                safeToUpdate = false;
                                            }
                                        }
                                    });

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(safeToUpdate){
                                        docRefCustomer.update("username", newUsername.getText().toString())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "Customer username updated!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d(TAG, "Couldn`t update Customer username!");
                                                    }
                                                });
                                    }
                                    else{
                                        Log.d(TAG,"Safe to update other references: " + safeToUpdate);
                                    }
                                }
                            }, 5000);

                        }
                        else{
                            Log.d(TAG, "Username field is empty!");
                            Toast.makeText(getActivity(), "Username field is empty!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(selectedChoice == items[1]){

                    }
                    if(selectedChoice == items[2]){

                    }
                }
                else{
                    Log.d(TAG, "No option selected!");
                    Toast.makeText(getActivity(), "No option selected!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    // Checking the current logged in user
    // If no user is logged-in redirects the user back to login page else continue
    private void CheckCurrentUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            logEmail = user.getEmail();
            Log.d(TAG, "Logged In Customer Email:" + logEmail);
        }
        else {
            Log.d(TAG, "User hasn`t logged-in, redirecting to login activity.");
            Toast.makeText(getActivity(), "User hasn`t logged-in, redirecting to login activity.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }
}
