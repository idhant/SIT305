package com.example.academymanagement.ui.tables;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.academymanagement.R;
import com.example.academymanagement.models.Tables;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.ContentValues.TAG;

public class TablesFragment extends Fragment {

    // Access a Cloud Firestore instance from your Activity
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Tables tableOneObject, tableTwoObject, tableThreeObject, tableFourObject;

    private ImageButton tableOne, tableTwo, tableThree, tableFour;

    private TextView textOne, textTwo, textThree, textFour;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tables, container, false);

        CheckServerResponse();

        tableOne = root.findViewById(R.id.fragment_tables_one);
        tableTwo = root.findViewById(R.id.fragment_tables_two);
        tableThree = root.findViewById(R.id.fragment_tables_three);
        tableFour = root.findViewById(R.id.fragment_tables_four);

        textOne = root.findViewById(R.id.fragment_tables_text_one);
        textTwo = root.findViewById(R.id.fragment_tables_text_two);
        textThree = root.findViewById(R.id.fragment_tables_text_three);
        textFour = root.findViewById(R.id.fragment_tables_text_four);

        tableOne.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_disabled));
        tableOne.setBackground(null);
        tableTwo.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_disabled));
        tableTwo.setBackground(null);
        tableThree.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_disabled));
        tableThree.setBackground(null);
        tableFour.setImageDrawable(getResources().getDrawable(R.drawable.ic_fragment_tables_disabled));
        tableFour.setBackground(null);

        //TODO: Implement async function instead of handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RespondServerResponse(1);
                RespondServerResponse(2);
                RespondServerResponse(3);
                RespondServerResponse(4);
            }
        }, 3000);

        return root;
    }

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

    public void CheckServerResponse(){
        DocumentReference docRefTableOne = db.collection("tables").document("tableOne");
        docRefTableOne.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tableOneObject = documentSnapshot.toObject(Tables.class);
                Log.d(TAG, "Table One Name:" + tableOneObject.getTablename());
                Log.d(TAG, "Table One Price:" + tableOneObject.getTableprice());
                Log.d(TAG, "Table One Status:" + tableOneObject.getTablestatus());
            }
        });

        DocumentReference docRefTableTwo = db.collection("tables").document("tableTwo");
        docRefTableTwo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tableTwoObject = documentSnapshot.toObject(Tables.class);
                Log.d(TAG, "Table Two Name:" + tableTwoObject.getTablename());
                Log.d(TAG, "Table Two Price:" + tableTwoObject.getTableprice());
                Log.d(TAG, "Table Two Status:" + tableTwoObject.getTablestatus());
            }
        });

        DocumentReference docRefTableThree = db.collection("tables").document("tableThree");
        docRefTableThree.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tableThreeObject = documentSnapshot.toObject(Tables.class);
                Log.d(TAG, "Table Three Name:" + tableThreeObject.getTablename());
                Log.d(TAG, "Table Three Price:" + tableThreeObject.getTableprice());
                Log.d(TAG, "Table Three Status:" + tableThreeObject.getTablestatus());
            }
        });

        DocumentReference docRefTableFour = db.collection("tables").document("tableFour");
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
}
