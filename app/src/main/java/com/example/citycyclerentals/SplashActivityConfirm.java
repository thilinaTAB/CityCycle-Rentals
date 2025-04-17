package com.example.citycyclerentals;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SplashActivityConfirm extends AppCompatActivity {

    String BicycleType, Location, Plan, Amount, Date;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_splash);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        BicycleType = getIntent().getStringExtra("BicycleType");
        Location = getIntent().getStringExtra("Location");
        Plan = getIntent().getStringExtra("Plan");
        Amount = getIntent().getStringExtra("Amount");
        Date = getIntent().getStringExtra("Date");

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                saveRideInfo();
            }
        }, 3000); // 3 seconds delay
    }

    private void saveRideInfo() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Create a new ride info object
            Map<String, Object> rideInfo = new HashMap<>();
            rideInfo.put("bicycleType", BicycleType);
            rideInfo.put("location", Location);
            rideInfo.put("plan", Plan);
            rideInfo.put("amount", Amount);
            rideInfo.put("date", Date);

            // Add a new document with the user ID
            db.collection("RideInfo").document(userId)
                    .set(rideInfo)
                    .addOnSuccessListener(aVoid -> {
                        // Data saved successfully, now navigate to UserProfile
                        Intent splash = new Intent(SplashActivityConfirm.this, UserProfile.class);
                        splash.putExtra("BicycleType", BicycleType);
                        splash.putExtra("Location", Location);
                        splash.putExtra("Plan", Plan);
                        splash.putExtra("Amount", Amount);
                        splash.putExtra("Date", Date);
                        startActivity(splash);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "ERROR. Try later", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}