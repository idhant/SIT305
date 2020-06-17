package com.example.academymanagement.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment {

    private Customer customer;

    // Firestore database reference
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference docRefCustomer;

    // TAG variable for debugging
    private static final String TAG="HomeFragment:";

    // variable to store the email of current logged-in user
    private String logEmail;

    private TextView userName, userEmail, userCredits, userPoints, textView;

    private ImageView userPhoto;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        textView = root.findViewById(R.id.text_home);
        userName = root.findViewById(R.id.fragment_home_name);
        userEmail = root.findViewById(R.id.fragment_home_email);
        userCredits = root.findViewById(R.id.fragment_home_credits);
        userPoints = root.findViewById(R.id.fragment_home_points);
        userPhoto = root.findViewById(R.id.fragment_home_image);

        CheckCurrentUser();

        // Storing the path of collection(customers)/document(email of user)
        docRefCustomer = db.collection("customers").document(logEmail);

        CheckDatabaseRealtime();

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

    // Checks the changes to the document real time and updates the credit fields
    private void CheckDatabaseRealtime(){
        docRefCustomer.addSnapshotListener(new EventListener<DocumentSnapshot>() {
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
                        Log.d(TAG, "Customer db points:" + customer.getPoints());

                        //TODO: Set this string using strings.xml
                        userName.setText("User Name: " + customer.getUsername());
                        userEmail.setText("User Email: " + customer.getEmail());
                        userCredits.setText("Currency Credits: " + customer.getCredits());
                        userPoints.setText("Reward Points: " + customer.getPoints());
                    }
                }
                else {
                    Log.d(TAG, "Customer db details: null");
                }
            }
        });
    }
}
