package com.example.academymanagement.ui.credits;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.academymanagement.LoginActivity;
import com.example.academymanagement.R;
import com.example.academymanagement.models.Customer;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class CreditsFragment extends Fragment {

    // variable to store the customer details retrieved from the database
    private Customer customer;

    // Firestore database reference
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // variable to store the email of current logged-in user
    private String logEmail;

    // TAG variable for debugging
    private static final String TAG="CreditsFragment:";

    // variables to store current and to be added credits
    private int currentCredits;
    private int addCredits;

    // boolean to check if all conditions are met for adding credits
    private boolean safeToAdd;

    // database reference to the current logged-in user details
    private DocumentReference documentReference;

    // variables for layout objects
    private TextView textView;
    private EditText purchaseAmountValue;
    private TextView currentAmountText;
    private TextView purchaseAmountText;
    private ImageButton purchaseAmountButton;

    // on create view function for the fragment
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_credits, container, false);

        // Storing references to the private variables
        textView = root.findViewById(R.id.text_credits);
        purchaseAmountValue = root.findViewById(R.id.fragment_credits_credits_purchase_amount);
        currentAmountText = root.findViewById(R.id.fragment_credits_credits_current_text);
        purchaseAmountText = root.findViewById(R.id.fragment_credits_credits_purchase_text);
        purchaseAmountButton = root.findViewById(R.id.fragment_credits_credits_purchase_button);

        CheckCurrentUser();

        // Storing the path of collection(customers)/document(email of user)
        documentReference = db.collection("customers").document(logEmail);

        //CheckDatabaseOnce();
        // retrieving data whenever change is detected in any fields
        CheckDatabaseRealtime();

        //TODO: Set these strings using strings.xml
        // Setting the text for fields
        textView.setText("Credit Summary");
        purchaseAmountText.setText("Enter the amount of credits you want to purchase:");

        // making background of vector image white
        purchaseAmountButton.setBackground(null);

        // Listening to clicks on the add credits button
        // If all addition conditions are met (value not null, value not negative), database is updated
        purchaseAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addCredits = Integer.parseInt(purchaseAmountValue.getText().toString());
                    if(addCredits >= 0) {
                        safeToAdd = true;
                        addCredits = currentCredits + addCredits;
                    }
                    else{
                        safeToAdd = false;
                        Toast.makeText(getActivity(), "Entry field is negative!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Entry field is negative!");
                    }
                }
                catch (NumberFormatException ex){
                    safeToAdd = false;
                    Toast.makeText(getActivity(), "Entry field is null!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, ex.toString());
                }

                if (safeToAdd){
                    documentReference.update("credits", addCredits)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Added Credits: " + addCredits);
                                    Toast.makeText(getActivity(), "Added Credits: " + addCredits, Toast.LENGTH_SHORT).show();
                                    purchaseAmountValue.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failed to add credits.");
                                    Toast.makeText(getActivity(), "Failed to add credits!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    Log.d(TAG, "Conditions to addition of new credits not met.");
                    Toast.makeText(getActivity(), "Conditions to addition of new credits not met.", Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(getActivity(),LoginActivity.class));
        }
    }

    // Checks the changes to the document real time and updates the credit fields
    private void CheckDatabaseRealtime(){
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    customer = documentSnapshot.toObject(Customer.class);
                    if(customer != null){
                        Log.d(TAG, "Customer db email:" + customer.getEmail());
                        Log.d(TAG, "Customer db credits:" + customer.getCredits());
                        Log.d(TAG, "Customer db username:" + customer.getUsername());
                        //TODO: Set this string using strings.xml
                        currentAmountText.setText("Current available Credits: " + customer.getCredits());
                        currentCredits = customer.getCredits();
                    }
                }
                else {
                    Log.d(TAG, "Customer db details: null");
                }
            }
        });
    }

    private void CheckDatabaseOnce(){
        // Retrieving data from the database and storing in the Customer class object
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                customer = documentSnapshot.toObject(Customer.class);
                if(customer != null){
                    Log.d(TAG, "Customer db details:" + customer.getEmail());
                    Log.d(TAG, "Customer db details:" + customer.getCredits());
                    Log.d(TAG, "Customer db details:" + customer.getUsername());
                    Log.d(TAG, "Customer current Credits:" + customer.getCredits());
                    //TODO: Set this string using strings.xml
                    currentAmountText.setText("Current available Credits: " + customer.getCredits());
                    currentCredits = customer.getCredits();
                }
            }
        });
    }
}
