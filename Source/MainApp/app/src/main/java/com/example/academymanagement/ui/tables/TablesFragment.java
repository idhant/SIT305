package com.example.academymanagement.ui.tables;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.academymanagement.R;
import com.example.academymanagement.models.Tables;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class TablesFragment extends Fragment {

    // Access a Cloud Firestore instance from your Activity
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // variables to store the Tables Class objects
    private Tables tableOneObject, tableTwoObject, tableThreeObject, tableFourObject;

    // variables to store the image button references
    private ImageButton tableOne, tableTwo, tableThree, tableFour;

    // variables to store the textview references
    private TextView textOne, textTwo, textThree, textFour;

    // variables to store document references
    private DocumentReference docRefTableOne, docRefTableTwo, docRefTableThree, docRefTableFour;

    // variable to check vacant or occupied status of tables
    private String updateStatus;

    // TAG variable for debugging
    private static final String TAG = "Tables Fragment: ";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tables, container, false);

        // Storing references to the private variables
        tableOne = root.findViewById(R.id.fragment_tables_one);
        tableTwo = root.findViewById(R.id.fragment_tables_two);
        tableThree = root.findViewById(R.id.fragment_tables_three);
        tableFour = root.findViewById(R.id.fragment_tables_four);
        textOne = root.findViewById(R.id.fragment_tables_text_one);
        textTwo = root.findViewById(R.id.fragment_tables_text_two);
        textThree = root.findViewById(R.id.fragment_tables_text_three);
        textFour = root.findViewById(R.id.fragment_tables_text_four);

        // Storing the path of collection(tables)/document(table name)
        docRefTableOne = db.collection("tables").document("tableOne");
        docRefTableTwo = db.collection("tables").document("tableTwo");
        docRefTableThree = db.collection("tables").document("tableThree");
        docRefTableFour = db.collection("tables").document("tableFour");

        //CheckDataBaseOnce();
        // Checks the database for table related values. If there is any change updates it as well.
        CheckDatabaseRealtime();

        return root;
    }

    // Checks the changes to the document real time and updates the tables fields.
    private void CheckDatabaseRealtime() {
        docRefTableOne.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    tableOneObject = documentSnapshot.toObject(Tables.class);
                    if (tableOneObject != null) {
                        Log.d(TAG, "Tables db name:" + tableOneObject.getTablename());
                        Log.d(TAG, "Tables db price:" + tableOneObject.getTableprice());
                        Log.d(TAG, "Tables db status:" + tableOneObject.getTablestatus());
                        if (tableOneObject.getTablestatus()) {
                            tableOne.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_enabled));
                            tableOne.setBackground(null);
                            updateStatus = "Occupied";
                        } else {
                            tableOne.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_disabled));
                            tableOne.setBackground(null);
                            updateStatus = "Vacant";
                        }
                        textOne.append("Table One" + "\n" + "Table Price: " + tableOneObject.getTableprice() + "\n" + "Table Status: " + updateStatus);
                    }
                } else {
                    Log.d(TAG, "Table db details: null");
                }
            }
        });

        docRefTableTwo.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    tableTwoObject = documentSnapshot.toObject(Tables.class);
                    if (tableTwoObject != null) {
                        Log.d(TAG, "Tables db name:" + tableTwoObject.getTablename());
                        Log.d(TAG, "Tables db price:" + tableTwoObject.getTableprice());
                        Log.d(TAG, "Tables db status:" + tableTwoObject.getTablestatus());
                        if (tableTwoObject.getTablestatus()) {
                            tableTwo.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_enabled));
                            tableTwo.setBackground(null);
                            updateStatus = "Occupied";
                        } else {
                            tableTwo.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_disabled));
                            tableTwo.setBackground(null);
                            updateStatus = "Vacant";
                        }
                        textTwo.append("Table Two" + "\n" + "Table Price: " + tableTwoObject.getTableprice() + "\n" + "Table Status: " + updateStatus);
                    }
                } else {
                    Log.d(TAG, "Table db details: null");
                }
            }
        });

        docRefTableThree.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    tableThreeObject = documentSnapshot.toObject(Tables.class);
                    if (tableThreeObject != null) {
                        Log.d(TAG, "Tables db name:" + tableThreeObject.getTablename());
                        Log.d(TAG, "Tables db price:" + tableThreeObject.getTableprice());
                        Log.d(TAG, "Tables db status:" + tableThreeObject.getTablestatus());
                        if (tableThreeObject.getTablestatus()) {
                            tableThree.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_enabled));
                            tableThree.setBackground(null);
                            updateStatus = "Occupied";
                        } else {
                            tableThree.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_disabled));
                            tableThree.setBackground(null);
                            updateStatus = "Vacant";
                        }
                        textThree.append("Table Three" + "\n" + "Table Price: " + tableThreeObject.getTableprice() + "\n" + "Table Status: " + updateStatus);
                    }
                } else {
                    Log.d(TAG, "Table db details: null");
                }
            }
        });

        docRefTableFour.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    tableFourObject = documentSnapshot.toObject(Tables.class);
                    if (tableFourObject != null) {
                        Log.d(TAG, "Tables db name:" + tableFourObject.getTablename());
                        Log.d(TAG, "Tables db price:" + tableFourObject.getTableprice());
                        Log.d(TAG, "Tables db status:" + tableFourObject.getTablestatus());
                        if (tableFourObject.getTablestatus()) {
                            tableFour.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_enabled));
                            tableFour.setBackground(null);
                            updateStatus = "Occupied";
                        } else {
                            tableFour.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_disabled));
                            tableFour.setBackground(null);
                            updateStatus = "Vacant";
                        }
                        textFour.append("Table Four" + "\n" + "Table Price: " + tableFourObject.getTableprice() + "\n" + "Table Status: " + updateStatus);
                    }
                } else {
                    Log.d(TAG, "Table db details: null");
                }
            }
        });
    }

}
    /*
    public void RespondServerResponse(int tablenumber){

        String status;
        switch (tablenumber){
            case 1:
                if (tableOneObject.getTablestatus()){
                    tableOne.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_enabled));
                    tableOne.setBackground(null);
                    status = "Occupied";
                }
                else{
                    tableOne.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_disabled));
                    tableOne.setBackground(null);
                    status = "Vacant";
                }
                textOne.append("Table One"+ "\n" + "Table Price: "+ tableOneObject.getTableprice() + "\n" + "Table Status: " + status);
                break;

            case 2:
                if (tableTwoObject.getTablestatus()){
                    tableTwo.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_enabled));
                    tableTwo.setBackground(null);
                    status = "Occupied";
                }
                else{
                    tableTwo.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_disabled));
                    tableTwo.setBackground(null);
                    status = "Vacant";
                }
                textTwo.append("Table Two"+ "\n" + "Table Price: " + tableTwoObject.getTableprice() + "\n" + "Table Status: " + status);
                break;

            case 3:
                if (tableThreeObject.getTablestatus()){
                    tableThree.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_enabled));
                    tableThree.setBackground(null);
                    status = "Occupied";
                }
                else{
                    tableThree.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_disabled));
                    tableThree.setBackground(null);
                    status = "Vacant";
                }
                textThree.append("Table Three" + "\n" + "Table Price:" + tableThreeObject.getTableprice() + "\n" + "Table Status: " + status);
                break;

            case 4:
                if (tableFourObject.getTablestatus()){
                    tableFour.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_enabled));
                    tableFour.setBackground(null);
                    status = "Occupied";
                }
                else{
                    tableFour.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_disabled));
                    tableFour.setBackground(null);
                    status = "Vacant";
                }
                textFour.append("Table Four" + "\n" + "Table Price:" + tableFourObject.getTableprice() + "\n" + "Table Status: " + status);
                break;
        }
    }
     */

    /*
    public void CheckDataBaseOnce(){
        docRefTableOne = db.collection("tables").document("tableOne");
        docRefTableOne.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tableOneObject = documentSnapshot.toObject(Tables.class);
                Log.d(TAG, "Table One Name:" + tableOneObject.getTablename());
                Log.d(TAG, "Table One Price:" + tableOneObject.getTableprice());
                Log.d(TAG, "Table One Status:" + tableOneObject.getTablestatus());
            }
        });

        docRefTableTwo = db.collection("tables").document("tableTwo");
        docRefTableTwo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tableTwoObject = documentSnapshot.toObject(Tables.class);
                Log.d(TAG, "Table Two Name:" + tableTwoObject.getTablename());
                Log.d(TAG, "Table Two Price:" + tableTwoObject.getTableprice());
                Log.d(TAG, "Table Two Status:" + tableTwoObject.getTablestatus());
            }
        });

        docRefTableThree = db.collection("tables").document("tableThree");
        docRefTableThree.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tableThreeObject = documentSnapshot.toObject(Tables.class);
                Log.d(TAG, "Table Three Name:" + tableThreeObject.getTablename());
                Log.d(TAG, "Table Three Price:" + tableThreeObject.getTableprice());
                Log.d(TAG, "Table Three Status:" + tableThreeObject.getTablestatus());
            }
        });

        docRefTableFour = db.collection("tables").document("tableFour");
        docRefTableFour.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tableFourObject = documentSnapshot.toObject(Tables.class);
                Log.d(TAG, "Table Four Name:" + tableFourObject.getTablename());
                Log.d(TAG, "Table Four Price:" + tableFourObject.getTableprice());
                Log.d(TAG, "Table Four Status:" + tableFourObject.getTablestatus());
            }
        });
    }
    */

