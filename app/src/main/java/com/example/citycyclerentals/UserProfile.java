package com.example.citycyclerentals;

import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class UserProfile extends AppCompatActivity {
    TextView txt_UserName, txt_UserEmail;
    TextView txt_Bicycle, txt_Location, txt_Plan, txt_Amount, txt_Date, txt_Time, txt_numPlan;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = "UserProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txt_UserName = findViewById(R.id.TXT_UserName);
        txt_UserEmail = findViewById(R.id.TXT_UserEmail);
        txt_Bicycle = findViewById(R.id.TXT_Bicycle);
        txt_Location = findViewById(R.id.TXT_Location);
        txt_Plan = findViewById(R.id.TXT_Plan);
        txt_Amount = findViewById(R.id.TXT_Amount);
        txt_Date = findViewById(R.id.TXT_Date);
        txt_Time = findViewById(R.id.TXT_Time);
        txt_numPlan = findViewById(R.id.TXT_numPlan);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadUserProfile();
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // 1. Fetch User Profile Data
            DocumentReference userRef = db.collection("Users").document(userId);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Changed "Full Name" to "FullName"
                        String fullName = document.getString("FullName");
                        String email = currentUser.getEmail();

                        txt_UserName.setText(fullName);
                        txt_UserEmail.setText(email);
                    } else {
                        Log.d(TAG, "No such document");
                        txt_UserName.setText("User data not found");
                        txt_UserEmail.setText(currentUser.getEmail());
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    txt_UserName.setText("Error loading user data");
                }
            });

            // 2. Fetch Most Recent Ride Data
            db.collection("Rides")
                    .whereEqualTo("userId", userId)  // Filter rides for the current user
                    .orderBy("timestamp", Query.Direction.DESCENDING) // Order by timestamp, most recent first
                    .limit(1)  // Get only the most recent ride
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentSnapshot rideDoc = querySnapshot.getDocuments().get(0); // Get the first (and only) document

                                String bikeType = rideDoc.getString("bikeType");
                                String location = rideDoc.getString("location");
                                String numPlan = rideDoc.getString("numPlan");
                                String date = rideDoc.getString("date");
                                String time = rideDoc.getString("time");
                                String amount = rideDoc.getString("amount");
                                String plan = rideDoc.getString("plan");

                                // Update TextViews with ride data (check for nulls to be safe)
                                txt_Bicycle.setText(bikeType != null ? bikeType : "-");
                                txt_Location.setText(location != null ? location : "-");
                                txt_numPlan.setText(numPlan != null ? numPlan : "-");
                                txt_Date.setText(date != null ? date : "-");
                                txt_Time.setText(time != null ? time : "-");
                                txt_Amount.setText(amount != null ? amount : "-");
                                txt_Plan.setText(plan != null ? plan : "-");

                            } else {
                                Log.d(TAG, "No rides found for user");
                                // Display a message indicating no rides found or clear the fields
                                txt_Bicycle.setText("No rides found");
                                txt_Location.setText("-");
                                txt_numPlan.setText("-");
                                txt_Date.setText("-");
                                txt_Time.setText("-");
                                txt_Amount.setText("-");
                                txt_Plan.setText("-");
                            }
                        } else {
                            Log.w(TAG, "Error getting ride data", task.getException());
                            // Display an error message
                            txt_Bicycle.setText("Error loading ride data");
                            txt_Location.setText("-");
                            txt_numPlan.setText("-");
                            txt_Date.setText("-");
                            txt_Time.setText("-");
                            txt_Amount.setText("-");
                            txt_Plan.setText("-");
                        }
                    });
        } else {
            txt_UserName.setText("No user signed in");
            txt_UserEmail.setText("");
            // Clear other fields or set default values if needed.
            txt_Bicycle.setText("-");
            txt_Location.setText("-");
            txt_numPlan.setText("-");
            txt_Date.setText("-");
            txt_Time.setText("-");
            txt_Amount.setText("-");
            txt_Plan.setText("-");
        }
    }
}