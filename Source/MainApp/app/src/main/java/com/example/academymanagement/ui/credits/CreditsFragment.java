package com.example.academymanagement.ui.credits;

import android.os.Bundle;
import android.os.Handler;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.academymanagement.LoginActivity;
import com.example.academymanagement.MainActivity;
import com.example.academymanagement.R;
import com.example.academymanagement.models.Customer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreditsFragment extends Fragment {

    private Customer customer;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String logName, logEmail;
    private static final String TAG="CreditsFragment";

    private int currentCredits;
    private int addCredits;
    private boolean safeToAdd;
    private DocumentReference documentReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_credits, container, false);

        final TextView textView = root.findViewById(R.id.text_credits);
        final EditText purchaseAmountValue = root.findViewById(R.id.fragment_credits_credits_purchase_amount);
        final TextView currentAmountText = root.findViewById(R.id.fragment_credits_credits_current_text);
        final TextView purchaseAmountText = root.findViewById(R.id.fragment_credits_credits_purchase_text);
        final ImageButton purchaseAmountButton = root.findViewById(R.id.fragment_credits_credits_purchase_button);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            logEmail = user.getEmail();
            Log.d(TAG, "Customer Email:" + logEmail);
        }

        documentReference = db.collection("customer").document(logEmail);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                customer = documentSnapshot.toObject(Customer.class);
                if(customer != null)
                    Log.d(TAG, "Customer details:" + customer.getEmail());
            }
        });

        textView.setText("Credit Summary");
        purchaseAmountText.setText("Enter the amount of credits you want to purchase:");

        //TODO: Implement async function instead
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(customer != null) {
                    currentAmountText.setText("Current available Credits: " + customer.getCredits());
                    currentCredits = customer.getCredits();
                }
            }
        }, 3000);


        purchaseAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(purchaseAmountValue != null)  {
                    addCredits = Integer.parseInt(purchaseAmountValue.getText().toString());
                    if(addCredits >= 0) {
                        safeToAdd = true;
                        addCredits = currentCredits + addCredits;
                    }
                    else{
                        safeToAdd = false;
                        Toast.makeText(getActivity(), "Entry field is negative!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    safeToAdd = false;
                    Toast.makeText(getActivity(), "Entry field is null!", Toast.LENGTH_SHORT).show();
                }

                if (safeToAdd){
                    documentReference.update("credits", addCredits)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Added Credits: " + addCredits);
                                    Toast.makeText(getActivity(), "Added Credits: " + addCredits, Toast.LENGTH_SHORT).show();
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
            }
        });

        return root;
    }
}
