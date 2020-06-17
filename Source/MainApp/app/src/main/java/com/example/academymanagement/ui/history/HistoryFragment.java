package com.example.academymanagement.ui.history;

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
import com.example.academymanagement.models.History;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryFragment extends Fragment{

    // variable to store the customer details retrieved from the database
    private ArrayList<History> historyArrayList;

    private History history;

    // Firestore database reference
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // variable to store the email of current logged-in user
    private String logEmail;

    // TAG variable for debugging
    private static final String TAG="HistoryFragment:";

    // database reference to the current logged-in user details
    private CollectionReference colRefHistory;

    // variables for history class
    private String creditCategory = "Credits";
    private String pointsCategory = "Points";

    private TextView textView;

    private TableLayout tableLayout;
    private TextView tableRowTime, tableRowCategory, tableRowChange;
    private TextView tableRowTimeHeading, tableRowCategoryHeading, tableRowChangeHeading;
    private TableRow tableRowHeading;
    private int tableRowIndex = 1;

    private SimpleDateFormat simpleDateFormat;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_history, container, false);

        textView = root.findViewById(R.id.text_history);
        tableLayout = root.findViewById(R.id.fragment_history_table_layout);
        tableLayout.setStretchAllColumns(true);

        CheckCurrentUser();

        // Storing the path of collection(history)/document(email of user)/collection(details)/document(automated ID)
        colRefHistory = db.collection("history").document(logEmail).collection("details");

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

    public void SetTableHeadings() {
        tableRowHeading = new TableRow(getActivity());
        tableRowTimeHeading = new TextView(getActivity());
        tableRowCategoryHeading = new TextView(getActivity());
        tableRowChangeHeading = new TextView(getActivity());

        TableRow.LayoutParams layoutParamsHeading = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        tableRowHeading.setLayoutParams(layoutParamsHeading);

        tableRowTimeHeading.setText("Time: ");
        tableRowTimeHeading.setTextColor(Color.WHITE);
        tableRowTimeHeading.setGravity(Gravity.CENTER);
        tableRowTimeHeading.setTextSize(20);
        tableRowHeading.addView(tableRowTimeHeading);
        tableRowCategoryHeading.setText("Category: ");
        tableRowCategoryHeading.setTextColor(Color.WHITE);
        tableRowCategoryHeading.setGravity(Gravity.CENTER);
        tableRowCategoryHeading.setTextSize(20);
        tableRowHeading.addView(tableRowCategoryHeading);
        tableRowChangeHeading.setText("Change: ");
        tableRowChangeHeading.setTextColor(Color.WHITE);
        tableRowChangeHeading.setGravity(Gravity.CENTER);
        tableRowChangeHeading.setTextSize(20);
        tableRowHeading.addView(tableRowChangeHeading);

        tableLayout.addView(tableRowHeading, 0);
    }

    public void CheckDatabaseOnce() {
        colRefHistory.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        SetTableHeadings();

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                history = document.toObject(History.class);
                                Log.d(TAG, "History db Time: " + history.getTime());
                                Log.d(TAG, "History db Category: " + history.getCategory());
                                Log.d(TAG, "History db change: " + history.getChange());

                                TableRow row = new TableRow(getActivity());
                                tableRowTime = new TextView(getActivity());
                                tableRowCategory = new TextView(getActivity());
                                tableRowChange = new TextView(getActivity());

                                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                //TableRow.LayoutParams margin = new TableRow.LayoutParams(TableRow.LayoutParams)
                                row.setLayoutParams(layoutParams);

                                Long date = history.getTime().getTime();
                                Date newdate = new Date(date);
                                simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                String dateText = simpleDateFormat.format(newdate);
                                //int newdate = history.getTime().getDate();
                                Log.d(TAG, "date:" + date);
                                Log.d(TAG, "conv date: " + dateText);

                                tableRowTime.setText(dateText);
                                tableRowTime.setTextColor(Color.WHITE);
                                tableRowTime.setGravity(Gravity.CENTER);
                                tableRowTime.setTextSize(18);
                                row.addView(tableRowTime);
                                tableRowCategory.setText(history.getCategory());
                                tableRowCategory.setTextColor(Color.WHITE);
                                tableRowCategory.setGravity(Gravity.CENTER);
                                tableRowCategory.setTextSize(18);
                                row.addView(tableRowCategory);
                                int change = history.getChange();
                                tableRowChange.setText("" + change);
                                tableRowChange.setTextColor(Color.WHITE);
                                tableRowChange.setGravity(Gravity.CENTER);
                                tableRowChange.setTextSize(18);
                                row.addView(tableRowChange);

                                //tableLayout.addView(row);

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
    /*
    public void CheckDatabaseRealtime(){
        colRefHistory.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (queryDocumentSnapshots!= null && queryDocumentSnapshots.exists()) {
                    for (queryDocumentSnapshots.getDocuments()) {

                    }
                    customer = documentSnapshot.toObject(Customer.class);
                    if(customer != null){
                        Log.d(TAG, "Customer db email:" + customer.getEmail());
                        Log.d(TAG, "Customer db credits:" + customer.getCredits());
                        Log.d(TAG, "Customer db username:" + customer.getUsername());
                        Log.d(TAG, "Customer db points:" + customer.getPoints());
                        //TODO: Set this string using strings.xml
                        currentAmountText.setText("Current available Credits: " + customer.getCredits());
                        currentCredits = customer.getCredits();
                        currentPoints = customer.getPoints();
                    }
                }
                else {
                    Log.d(TAG, "Customer db details: null");
                }
            }
        });
    }

     */

}
