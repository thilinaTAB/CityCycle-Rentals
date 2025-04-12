package com.example.citycyclerentals;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class UserDashboard extends AppCompatActivity {
    ImageView img_signOut,img_bicycleCategory;
    TextView txtBTN_signOut, txtBTN_bicycleCategory;
    FirebaseAuth fauth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize FirebaseAuth
        fauth = FirebaseAuth.getInstance();

        img_signOut = findViewById(R.id.IMG_signOut);
        txtBTN_signOut = findViewById(R.id.TXTBTN_signOut);
        img_bicycleCategory = findViewById(R.id.IMG_bicycleCategory);
        txtBTN_bicycleCategory = findViewById(R.id.TXTBTN_bicycleCategory);

        img_bicycleCategory.setOnClickListener(v -> {
            Intent movetoList = new Intent(getApplicationContext(), BicycleList.class);
            startActivity(movetoList);
        });
        txtBTN_bicycleCategory.setOnClickListener(v -> {
            Intent movetoList = new Intent(getApplicationContext(), BicycleList.class);
            startActivity(movetoList);
        });

        // Sign Out Click Listeners
        View.OnClickListener signOutClickListener = v -> showSignOutConfirmationDialog();
        img_signOut.setOnClickListener(signOutClickListener);
        txtBTN_signOut.setOnClickListener(signOutClickListener);

    }

    private void showSignOutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", (dialog, which) -> signOut())
                .setNegativeButton("No", null) // Do nothing on "No"
                .show();
    }

    private void signOut() {
        fauth.signOut();
        // Redirect to Login Activity
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish(); // Close the current activity
    }
}