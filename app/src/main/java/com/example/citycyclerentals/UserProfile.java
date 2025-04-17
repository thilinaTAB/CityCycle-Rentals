package com.example.citycyclerentals;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
    TextView txt_Bicycle, txt_Location, txt_Plan, txt_Amount, txt_Date;

    CardView view_MyRide, view_NoRideData;
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
        view_MyRide = findViewById(R.id.View_MyRide);
        view_NoRideData = findViewById(R.id.View_NoRideData);

        String bicycleType = getIntent().getStringExtra("BicycleType");

        if (bicycleType != null && !bicycleType.isEmpty()) {
            txt_Bicycle.setText(bicycleType);
            view_MyRide.setVisibility(View.VISIBLE);
            view_NoRideData.setVisibility(View.GONE);
        } else {
            view_MyRide.setVisibility(View.GONE);
            view_NoRideData.setVisibility(View.VISIBLE);
        }

        String BicycleType = getIntent().getStringExtra("BicycleType");
        String Location = getIntent().getStringExtra("Location");
        String Plan = getIntent().getStringExtra("Plan");
        String Amount = getIntent().getStringExtra("Amount");
        String Date = getIntent().getStringExtra("Date");

        txt_Bicycle.setText(BicycleType);
        txt_Location.setText(Location);
        txt_Plan.setText(Plan);
        txt_Amount.setText(Amount);
        txt_Date.setText(Date);

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
                        // Attempt to get "FullName" first, fall back to "Full Name" if not found
                        String fullName = document.getString("FullName");
                        if (fullName == null) {
                            fullName = document.getString("Full Name"); // Try with space
                            Log.w(TAG, "Field 'FullName' not found, falling back to 'Full Name'");
                        }

                        String email = currentUser.getEmail();

                        txt_UserName.setText(fullName != null ? fullName : "Name not available");
                        txt_UserEmail.setText(email);
                    } else {
                        Log.d(TAG, "No such user document");
                        txt_UserName.setText("User data not found");
                        txt_UserEmail.setText(currentUser.getEmail());
                    }
                } else {
                    Log.e(TAG, "Error loading user data: ", task.getException());
                    txt_UserName.setText("Error loading user data");
                }
            });
    }

    }
}