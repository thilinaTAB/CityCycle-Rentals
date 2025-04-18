package com.example.citycyclerentals;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    ImageView imgbtn_Back;
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
        imgbtn_Back = findViewById(R.id.IMGBTN_Back);

        imgbtn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, UserDashboard.class);
                startActivity(intent);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadUserProfile();
        loadRideInfo();
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = db.collection("Users").document(userId);
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String fullName = document.getString("FullName");
                        if (fullName == null) {
                            fullName = document.getString("Full Name");
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

    private void loadRideInfo() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("RideHistory")
                    .document(userId)
                    .collection("rides")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                if (document != null) {
                                    String bicycleType = document.getString("bicycleType");
                                    String location = document.getString("location");
                                    String plan = document.getString("plan");
                                    String amount = document.getString("amount");
                                    String date = document.getString("date");

                                    txt_Bicycle.setText(bicycleType);
                                    txt_Location.setText(location);
                                    txt_Plan.setText(plan);
                                    txt_Amount.setText(amount);
                                    txt_Date.setText(date);

                                    if (bicycleType != null && !bicycleType.isEmpty()) {
                                        view_MyRide.setVisibility(View.VISIBLE);
                                        view_NoRideData.setVisibility(View.GONE);
                                    } else {
                                        view_MyRide.setVisibility(View.GONE);
                                        view_NoRideData.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    // Handle case where document is null. Maybe log a warning
                                    Log.w(TAG, "Most recent ride document is null.");
                                    view_MyRide.setVisibility(View.GONE);
                                    view_NoRideData.setVisibility(View.VISIBLE);
                                }

                            } else {
                                Log.d(TAG, "No ride history found for user");
                                view_MyRide.setVisibility(View.GONE);
                                view_NoRideData.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.e(TAG, "Error loading ride info: ",task.getException());
                            view_MyRide.setVisibility(View.GONE);
                            view_NoRideData.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }
}