package com.example.academymanagement.ui.leaderboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.example.academymanagement.models.History;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LeaderboardFragment extends Fragment{

    // variable for the customer class object
    private Customer customer;

    // Firestore database reference
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // variable to store the email of current logged-in user
    private String logEmail;

    // TAG variable for debugging
    private static final String TAG = "Leader Fragment: ";

    // variable to store the collection reference
    private CollectionReference colRefCustomer;

    // variables for customer class
    private String pointsCategory = "Points";

    // variables for the object references
    private TextView textView;
    private TableLayout tableLayout;
    private TextView tableRowNumber, tableRowUser, tableRowPoints;
    private TextView tableRowNumberHeading, tableRowUserHeading, tableRowPointsHeading;
    private TableRow tableRowHeading;
    private int tableRowIndex = 1;

    // variable for date conversion
    private SimpleDateFormat simpleDateFormat;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        // setting the object references
        textView = root.findViewById(R.id.text_leaderboard);
        tableLayout = root.findViewById(R.id.fragment_leaderboard_table_layout);
        tableLayout.setStretchAllColumns(true);

        // Check if the user has already logged in, if true direct to customeractivity
        CheckCurrentUser();

        // Storing the path of collection(customers)/document(email of user)
        colRefCustomer = db.collection("customers");

        // Checks the database at the start for information to be filled in the table
        CheckDatabaseOnce();

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

    // This function sets the table headings as row with index 0
    public void SetTableHeadings() {
        tableRowHeading = new TableRow(getActivity());
        tableRowNumberHeading = new TextView(getActivity());
        tableRowUserHeading = new TextView(getActivity());
        tableRowPointsHeading = new TextView(getActivity());

        TableRow.LayoutParams layoutParamsHeading = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        tableRowHeading.setLayoutParams(layoutParamsHeading);
        tableRowHeading.setPadding(5,5,5,5);

        tableRowNumberHeading .setText("Serial No: ");
        tableRowNumberHeading .setTextColor(Color.WHITE);
        tableRowNumberHeading .setGravity(Gravity.CENTER);
        tableRowNumberHeading .setTextSize(20);
        tableRowNumberHeading .setPadding(5,5,5,5);
        //tableRowTimeHeading.setBackground(Color.GRAY);
        tableRowHeading.addView(tableRowNumberHeading);

        tableRowUserHeading.setText("User name: ");
        tableRowUserHeading.setTextColor(Color.WHITE);
        tableRowUserHeading.setGravity(Gravity.CENTER);
        tableRowUserHeading.setTextSize(20);
        tableRowUserHeading.setPadding(5,5,5,5);
        tableRowHeading.addView(tableRowUserHeading);

        tableRowPointsHeading .setText("Points: ");
        tableRowPointsHeading .setTextColor(Color.WHITE);
        tableRowPointsHeading .setGravity(Gravity.CENTER);
        tableRowPointsHeading .setTextSize(20);
        tableRowPointsHeading .setPadding(5,5,5,5);
        tableRowHeading.addView(tableRowPointsHeading );

        tableLayout.addView(tableRowHeading, 0);
    }

    // This function checks the data base for information and puts it in the table
    public void CheckDatabaseOnce() {
        colRefCustomer.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        SetTableHeadings();
                        Log.d(TAG, "Headings set");
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Task successful");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                customer = document.toObject(Customer.class);
                                Log.d(TAG, tableRowIndex + ": Customer db email:" + customer.getEmail());
                                Log.d(TAG, tableRowIndex + ": Customer db credits:" + customer.getCredits());
                                Log.d(TAG, tableRowIndex + ": Customer db username:" + customer.getUsername());
                                Log.d(TAG, tableRowIndex + ": Customer db points:" + customer.getPoints());

                                TableRow row = new TableRow(getActivity());
                                tableRowNumber = new TextView(getActivity());
                                tableRowUser = new TextView(getActivity());
                                tableRowPoints = new TextView(getActivity());

                                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                row.setLayoutParams(layoutParams);
                                row.setPadding(5,5,5,5);

                                tableRowNumber.setText("" + tableRowIndex + ": ");
                                tableRowNumber.setTextColor(Color.WHITE);
                                tableRowNumber.setGravity(Gravity.CENTER);
                                tableRowNumber.setTextSize(18);
                                tableRowNumber.setPadding(5,5,5,5);
                                row.addView(tableRowNumber);

                                tableRowUser.setText(customer.getUsername());
                                tableRowUser.setTextColor(Color.WHITE);
                                tableRowUser.setGravity(Gravity.CENTER);
                                tableRowUser.setTextSize(18);
                                tableRowUser.setPadding(5,5,5,5);
                                row.addView(tableRowUser);

                                tableRowPoints.setText("" + customer.getPoints());
                                tableRowPoints.setTextColor(Color.WHITE);
                                tableRowPoints.setGravity(Gravity.CENTER);
                                tableRowPoints.setTextSize(18);
                                tableRowPoints.setPadding(5,5,5,5);
                                row.addView(tableRowPoints);

                                tableLayout.addView(row,tableRowIndex);
                                tableRowIndex++;
                            }
                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
