package com.example.academymanagement.ui.reward;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.academymanagement.LoginActivity;
import com.example.academymanagement.R;
import com.example.academymanagement.models.Customer;
import com.example.academymanagement.models.History;
import com.example.academymanagement.models.Rewards;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;
import java.util.GregorianCalendar;

public class RewardFragment extends Fragment{

    // variables to store the customer details retrieved from the database
    private Rewards rewardOneObject, rewardTwoObject, rewardThreeObject;

    // variables to store history details and push them to firestore
    private History creditHistory, pointsHistory;

    // variable to store customer reference
    private Customer customer;

    //variables to store the virtual rewards
    final private int rewardOneCredits = 30;
    final private int rewardTwoCredits = 150;
    final private int rewardThreeCredits = 300;

    // variables for history class
    private String creditCategory = "Credits";
    private String pointsCategory = "Points";

    // Firestore database reference
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // variable to store the current time and push it as a parameter in the history contructor
    private Date currentTime;

    // variable to store the email of current logged-in user
    private String logEmail;

    // TAG variable for debugging
    private static final String TAG="Reward Fragment: ";

    // database reference to the reward details
    private DocumentReference docRefRewardOne, docRefRewardTwo, docRefRewardThree, docRefCustomer;

    // database reference to the collection
    private CollectionReference colRefHistory;

    private TextView textView;

    // variables to store image button references
    private ImageButton rewardOneButton, rewardTwoButton, rewardThreeButton;

    // variables to store text view references
    private TextView rewardOneText, rewardTwoText, rewardThreeText, rewardPointsText;

    // variables to keep track of points and credits
    private int currentPoints;
    private int addCredits;
    private int remainingPoints;
    private int addPoints;

    // variables to keep track of point and credit change
    private int pointsChange, creditsChange;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_reward, container, false);

        // setting the object references
        textView = root.findViewById(R.id.text_reward);
        rewardOneButton = root.findViewById(R.id.fragment_reward_reward_one_button);
        rewardTwoButton = root.findViewById(R.id.fragment_reward_reward_two_button);
        rewardThreeButton = root.findViewById(R.id.fragment_reward_reward_three_button);
        rewardOneText = root.findViewById(R.id.fragment_reward_reward_one_text);
        rewardTwoText = root.findViewById(R.id.fragment_reward_reward_two_text);
        rewardThreeText = root.findViewById(R.id.fragment_reward_reward_three_text);
        rewardPointsText = root.findViewById(R.id.fragment_reward_reward_points);

        // Check if the user has already logged in, if true direct to customeractivity
        CheckCurrentUser();

        // Setting the image background null
        rewardOneButton.setBackground(null);
        rewardTwoButton.setBackground(null);
        rewardThreeButton.setBackground(null);

        // Storing the path of collection(rewards)/document(email of user)
        docRefRewardOne = db.collection("rewards").document("rewardOne");
        docRefRewardTwo = db.collection("rewards").document("rewardTwo");
        docRefRewardThree = db.collection("rewards").document("rewardThree");
        docRefCustomer = db.collection("customers").document(logEmail);

        // Storing the path of collection(history)/document(email of user)/collection(details)
        colRefHistory = db.collection("history").document(logEmail).collection("details");

        textView.setText("List of redeemable rewards.");

        // retrieving data whenever change is detected in any fields
        CheckDatabaseRealtime();

        // calling the button listener to define the on click events
        SetButtonListener();

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

    // Sets the button listeners
    private void SetButtonListener(){
        rewardOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPoints < rewardOneObject.getPrice()){
                    Log.d(TAG, "Insufficient points, purchase more credits to gain points!");
                    Toast.makeText(getActivity(), "Insufficient points, purchase more credits to gain points!", Toast.LENGTH_SHORT).show();
                }
                else{
                    // NOTE: In a real app the user would be issued some kind of token as proof that points were spent.(For physical rewards)
                    // The customer would then show this token which will be verified by the attendant and reward will be given to the user.
                    // For the purpose of demonstrating the feature the customer is credited by virtual currency.
                    Log.d(TAG, "You are eligible for this reward, your account has been credited " + rewardOneCredits + "credits!");
                    Toast.makeText(getActivity(), "You are eligible for this reward, your account has been credited " + rewardOneCredits + "credits!", Toast.LENGTH_SHORT).show();

                    remainingPoints = currentPoints - rewardOneObject.getPrice();
                    pointsChange = -rewardOneObject.getPrice();
                    addCredits = customer.getCredits() + rewardOneCredits;
                    creditsChange = + rewardOneCredits;

                    currentTime = GregorianCalendar.getInstance().getTime();
                    creditHistory = new History(currentTime, creditCategory, creditsChange, logEmail);
                    pointsHistory = new History(currentTime, pointsCategory, pointsChange, logEmail);

                    docRefCustomer.update("points", remainingPoints, "credits", addCredits)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Remaining Reward Points: " + remainingPoints);
                                    Log.d(TAG, "Credits after redemption: " + addCredits);
                                    Toast.makeText(getActivity(), "Remaining Reward Points: " + remainingPoints, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getActivity(), "Credits after redemption: " + addCredits, Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failed to redeem rewards.");
                                    Toast.makeText(getActivity(), "Failed to redeem rewards.", Toast.LENGTH_SHORT).show();
                                }
                            });

                    colRefHistory.add(creditHistory)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "Credits history recorded.");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failed to record credit history.");
                                }
                            });

                    colRefHistory.add(pointsHistory)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "Points history recorded.");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failed to record point history");
                                }
                            });
                }
            }
        });

        rewardTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPoints < rewardTwoObject.getPrice()){
                    Log.d(TAG, "Insufficient points, purchase more credits to gain points!");
                    Toast.makeText(getActivity(), "Insufficient points, purchase more credits to gain points!", Toast.LENGTH_SHORT).show();
                }
                else{
                    // NOTE: In a real app the user would be issued some kind of token as proof that points were spent.(For physical rewards)
                    // The customer would then show this token which will be verified by the attendant and reward will be given to the user.
                    // For the purpose of demonstrating the feature the customer is credited by virtual currency.
                    Log.d(TAG, "You are eligible for this reward, your account has been credited " + rewardTwoCredits + "credits!");
                    Toast.makeText(getActivity(), "You are eligible for this reward, your account has been credited " + rewardTwoCredits + "credits!", Toast.LENGTH_SHORT).show();

                    remainingPoints = currentPoints - rewardTwoObject.getPrice();
                    pointsChange = -rewardTwoObject.getPrice();
                    addCredits = customer.getCredits() + rewardTwoCredits;
                    creditsChange = + rewardTwoCredits;

                    currentTime = GregorianCalendar.getInstance().getTime();
                    creditHistory = new History(currentTime, creditCategory, creditsChange, logEmail);
                    pointsHistory = new History(currentTime, pointsCategory, pointsChange, logEmail);

                    docRefCustomer.update("points", remainingPoints, "credits", addCredits)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Remaining Reward Points: " + remainingPoints);
                                    Log.d(TAG, "Credits after redemption: " + addCredits);
                                    Toast.makeText(getActivity(), "Remaining Reward Points: " + remainingPoints, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getActivity(), "Credits after redemption: " + addCredits, Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failed to redeem rewards.");
                                    Toast.makeText(getActivity(), "Failed to redeem rewards.", Toast.LENGTH_SHORT).show();
                                }
                            });

                    colRefHistory.add(creditHistory)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "Credits history recorded.");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failed to record credit history.");
                                }
                            });

                    colRefHistory.add(pointsHistory)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "Points history recorded.");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failed to record point history");
                                }
                            });
                }
            }
        });

        rewardThreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPoints < rewardThreeObject.getPrice()){
                    Log.d(TAG, "Insufficient points, purchase more credits to gain points!");
                    Toast.makeText(getActivity(), "Insufficient points, purchase more credits to gain points!", Toast.LENGTH_SHORT).show();
                }
                else{
                    // NOTE: In a real app the user would be issued some kind of token as proof that points were spent.(For physical rewards)
                    // The customer would then show this token which will be verified by the attendant and reward will be given to the user.
                    // For the purpose of demonstrating the feature the customer is credited by virtual currency.
                    Log.d(TAG, "You are eligible for this reward, your account has been credited " + rewardThreeCredits + "credits!");
                    Toast.makeText(getActivity(), "You are eligible for this reward, your account has been credited " + rewardThreeCredits + "credits!", Toast.LENGTH_SHORT).show();

                    remainingPoints = currentPoints - rewardThreeObject.getPrice();
                    pointsChange = -rewardThreeObject.getPrice();
                    addCredits = customer.getCredits() + rewardThreeCredits;
                    creditsChange = + rewardThreeCredits;

                    currentTime = GregorianCalendar.getInstance().getTime();
                    creditHistory = new History(currentTime, creditCategory, creditsChange, logEmail);
                    pointsHistory = new History(currentTime, pointsCategory, pointsChange, logEmail);

                    docRefCustomer.update("points", remainingPoints, "credits", addCredits)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Remaining Reward Points: " + remainingPoints);
                                    Log.d(TAG, "Credits after redemption: " + addCredits);
                                    Toast.makeText(getActivity(), "Remaining Reward Points: " + remainingPoints, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getActivity(), "Credits after redemption: " + addCredits, Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failed to redeem rewards.");
                                    Toast.makeText(getActivity(), "Failed to redeem rewards.", Toast.LENGTH_SHORT).show();
                                }
                            });

                    colRefHistory.add(creditHistory)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "Credits history recorded.");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failed to record credit history.");
                                }
                            });

                    colRefHistory.add(pointsHistory)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "Points history recorded.");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failed to record point history");
                                }
                            });
                }
            }
        });
    }

    // Checks the changes to the document real time and updates the credit fields
    private void CheckDatabaseRealtime(){
        docRefRewardOne.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    rewardOneObject = documentSnapshot.toObject(Rewards.class);
                    if(rewardOneObject != null){
                        Log.d(TAG, "Reward db name:" + rewardOneObject.getName());
                        Log.d(TAG, "Reward db details:" + rewardOneObject.getDetails());
                        Log.d(TAG, "Reward db price:" + rewardOneObject.getPrice());
                        //TODO: Set this string using strings.xml
                        rewardOneText.setText("Reward Name: " + rewardOneObject.getName() + "\n" + "Reward Details: " + rewardOneObject.getDetails() + "\n" + "Reward Price: " +
                                rewardOneObject.getPrice());
                    }
                }
                else {
                    Log.d(TAG, "Reward db details: null");
                }
            }
        });

        docRefRewardTwo.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    rewardTwoObject = documentSnapshot.toObject(Rewards.class);
                    if(rewardTwoObject != null){
                        Log.d(TAG, "Reward db name:" + rewardTwoObject.getName());
                        Log.d(TAG, "Reward db details:" + rewardTwoObject.getDetails());
                        Log.d(TAG, "Reward db price:" + rewardTwoObject.getPrice());
                        //TODO: Set this string using strings.xml
                        rewardTwoText.setText("Reward Name: " + rewardTwoObject.getName() + "\n" + "Reward Details: " + rewardTwoObject.getDetails() + "\n" + "Reward Price: " +
                                rewardTwoObject.getPrice());
                    }
                }
                else {
                    Log.d(TAG, "Reward db details: null");
                }
            }
        });

        docRefRewardThree.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    rewardThreeObject = documentSnapshot.toObject(Rewards.class);
                    if(rewardTwoObject != null){
                        Log.d(TAG, "Reward db name:" + rewardThreeObject.getName());
                        Log.d(TAG, "Reward db details:" + rewardThreeObject.getDetails());
                        Log.d(TAG, "Reward db price:" + rewardThreeObject.getPrice());
                        //TODO: Set this string using strings.xml
                        rewardThreeText.setText("Reward Name: " + rewardThreeObject.getName() + "\n" + "Reward Details: " + rewardThreeObject.getDetails() + "\n" + "Reward Price: " +
                                rewardThreeObject.getPrice());
                    }
                }
                else {
                    Log.d(TAG, "Reward db details: null");
                }
            }
        });

        docRefCustomer.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot,
                                @androidx.annotation.Nullable FirebaseFirestoreException e) {
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
                        rewardPointsText.setText("Current available points: " + customer.getPoints());
                        currentPoints = customer.getPoints();
                    }
                }
                else {
                    Log.d(TAG, "Customer db details: null");
                }
            }
        });
    }
}
