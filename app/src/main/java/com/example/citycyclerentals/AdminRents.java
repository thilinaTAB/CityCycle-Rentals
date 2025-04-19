package com.example.citycyclerentals;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AdminRents extends AppCompatActivity {

    private TextView txt_All;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_rents);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txt_All = findViewById(R.id.TXT_All);

        loadAllRideHistory();
    }

    private void loadAllRideHistory() {
        db.collection("RideHistory")
                .get()
                .addOnSuccessListener(userDocuments -> {
                    final StringBuilder allRideData = new StringBuilder();
                    if (userDocuments.isEmpty()) {
                        txt_All.setText("No ride history found.");
                        return;
                    }

                    final int[] userCount = {0}; // To track completed user data retrievals
                    final int totalUsers = userDocuments.size();

                    for (QueryDocumentSnapshot userDocument : userDocuments) {
                        String userId = userDocument.getId();
                        allRideData.append("User ID: ").append(userId).append("\n");

                        db.collection("RideHistory").document(userId).collection("rides")
                                .get()
                                .addOnSuccessListener(rideDocuments -> {
                                    for (QueryDocumentSnapshot rideDocument : rideDocuments) {
                                        allRideData.append("\tRide: ").append(rideDocument.getData().toString()).append("\n");
                                    }
                                    userCount[0]++; // Increment the counter

                                    if (userCount[0] == totalUsers) {
                                        // All users' data is retrieved; update the TextView
                                        txt_All.setText(allRideData.toString());
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.w("AdminRents", "Error getting rides for user " + userId, e);
                                    allRideData.append("\tError getting rides for this user.\n");
                                    userCount[0]++;

                                    if (userCount[0] == totalUsers) {
                                        // Update TextView even if there are errors, to show partial data and errors
                                        txt_All.setText(allRideData.toString());
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w("AdminRents", "Error getting users from RideHistory", e);
                    txt_All.setText("Error retrieving ride history.");
                });
    }
}