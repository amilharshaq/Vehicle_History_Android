package com.example.vehicle_history;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {
    // Declare the DrawerLayout, NavigationView, and Toolbar
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    EditText e1;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the activity_main layout
        setContentView(R.layout.activity_main);

        // Initialize the DrawerLayout, Toolbar, and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);

        // Create an ActionBarDrawerToggle to handle
        // the drawer's open/close state
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        // Add the toggle as a listener to the DrawerLayout
        drawerLayout.addDrawerListener(toggle);

        // Synchronize the toggle's state with the linked DrawerLayout
        toggle.syncState();





        e1 = findViewById(R.id.editTextText);
        b1 = findViewById(R.id.button4);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String reg_no = e1.getText().toString();

                Intent i = new Intent(getApplicationContext(), View_HIstory.class);
                i.putExtra("regno",reg_no);
                startActivity(i);


            }
        });





        // Set a listener for when an item in the NavigationView is selected
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // Called when an item in the NavigationView is selected.
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle the selected item based on its ID
                if (item.getItemId() == R.id.nav_account) {

                    Intent i = new Intent(getApplicationContext(), SearchServiceCenter.class);
                    startActivity(i);

                }

                if (item.getItemId() == R.id.nav_settings) {

                    Intent i = new Intent(getApplicationContext(), ViewBookings.class);
                    startActivity(i);


                }

                if (item.getItemId() == R.id.nav_logout) {
                    // Show a Toast message for the Logout item

                    Intent i = new Intent(getApplicationContext(), ip.class);
                    startActivity(i);

                    Toast.makeText(MainActivity.this,
                            "You are Logged Out", Toast.LENGTH_SHORT).show();
                }

                // Close the drawer after selection
                drawerLayout.closeDrawers();
                // Indicate that the item selection has been handled
                return true;
            }


        });

        // Add a callback to handle the back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            // Called when the back button is pressed.
            @Override
            public void handleOnBackPressed() {
                // Check if the drawer is open
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    // Close the drawer if it's open
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // Finish the activity if the drawer is closed
                    finish();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }


}
