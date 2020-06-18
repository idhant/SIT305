package com.example.academymanagement;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academymanagement.models.Customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/*
*Breakdown of different views:
* content_main.xml = defines the space for fragment
* app_bar_main.xml = defines the toolbar on top of the screen, the floating button and the fragment
* space from the content_main.xml
* nav_header_main.xml = defines the layout of the header part of the nav view
* activity_main_drawer.xml = defines the objects in the nav view
* main.xml = defines the settings dropdown on the toolbar
* activity_main.xml = defines the space for the app_bar_main and the navigation_View
 */

public class CustomerActivity extends AppCompatActivity {

    // Declaring a appbarconfig variable to add the options in the appbar
    private AppBarConfiguration mAppBarConfiguration;

    // Firebase variables
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button logoutButton;

    private String logName;
    private String logEmail;

    private static final String TAG="CustomerActivity";
    Customer customer;

    private DocumentReference docRefCustomer;

    private TextView navName;
    private TextView navUsername;
    private ImageView navImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a reference to the toolbar in app_bar_main.xml which acts as a action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if( firebaseUser != null ){
                    Toast.makeText(CustomerActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(CustomerActivity.this, CustomerActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(CustomerActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };

        // When customer clicks this it should show a upwards animation fragment which lists any promotions.
        // Create a reference to the floatactionbutton in app_bar_main.xml
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(CustomerActivity.this, LoginActivity.class);
                startActivity(intToMain);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*
        logoutButton = findViewById(R.id.action_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(CustomerActivity.this, LoginActivity.class);
                startActivity(intToMain);
            }
        });
        */


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            logName = user.getDisplayName();
            logEmail = user.getEmail();
            //Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }

        docRefCustomer = db.collection("customers").document(logEmail);

        CheckDatabaseRealtime();

        // Create a reference to the drawer layout in activity_main.xml
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) drawer.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        // Create a reference to the navView in activity_main.xml
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Create a reference to the header of nav bar
        View headerView = navigationView.getHeaderView(0);
        // Append username and email of current user.
        navName = headerView.findViewById(R.id.menu_name);
        navUsername = headerView.findViewById(R.id.menu_username);
        navImage = headerView.findViewById(R.id.menu_photo);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_credits,
                R.id.nav_history,
                R.id.nav_tables,
                R.id.nav_leaderboard,
                R.id.nav_reward,
                R.id.nav_settings,
                R.id.nav_contact_information)
                .setDrawerLayout(drawer)
                .build();

        // Reference to the host fragment in the content_main.xml which goes in app_bar_main.xml
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Sets the actionbar with the fragment
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // Checks the changes to the document real time and updates the credit fields
    private void CheckDatabaseRealtime() {
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
                    if (customer != null) {
                        Log.d(TAG, "Customer db email:" + customer.getEmail());
                        Log.d(TAG, "Customer db credits:" + customer.getCredits());
                        Log.d(TAG, "Customer db username:" + customer.getUsername());
                        Log.d(TAG, "Customer db points:" + customer.getPoints());

                        navName.setText(customer.getUsername());
                        navUsername.setText(logEmail);
                        navImage.setImageResource(R.drawable.ic_menu_user_photo);
                    }
                } else {
                    Log.d(TAG, "Customer db details: null");
                }
            }
        });
    }
}
