package com.example.citycyclerentals;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RentBicycle_Matale extends AppCompatActivity {

    String Location = "Matale";
    Button btn_classic, btn_cityBike, btn_cruiser, btn_folding;
    Button btn_hybrid, btn_touring, btn_cruiser1, btn_folding1;
    Button btn_bmx, btn_mountain, btn_roadBike, btn_electric;

    TextView txt_classic, txt_cityBike, txt_cruiser, txt_folding;
    TextView txt_hybrid, txt_touring, txt_cruiser1, txt_folding1;
    TextView txt_bmx, txt_mountain, txt_roadBike, txt_electric;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_bicycle_kandy);

        mDatabase = FirebaseDatabase.getInstance().getReference("bicycleAvailability_Matale");

        initializeViews();
        setButtonOnClickListeners();
        loadInitialData();
    }

    private void initializeViews() {
        btn_classic = findViewById(R.id.BTN_classic);
        btn_cityBike = findViewById(R.id.BTN_cityBike);
        btn_cruiser = findViewById(R.id.BTN_cruiser);
        btn_folding = findViewById(R.id.BTN_folding);
        btn_hybrid = findViewById(R.id.BTN_hybrid);
        btn_touring = findViewById(R.id.BTN_touring);
        btn_cruiser1 = findViewById(R.id.BTN_cruiser1);
        btn_folding1 = findViewById(R.id.BTN_folding1);
        btn_bmx = findViewById(R.id.BTN_bmx);
        btn_mountain = findViewById(R.id.BTN_mountain);
        btn_roadBike = findViewById(R.id.BTN_roadBike);
        btn_electric = findViewById(R.id.BTN_electric);

        txt_classic = findViewById(R.id.TXT_classic);
        txt_cityBike = findViewById(R.id.TXT_cityBike);
        txt_cruiser = findViewById(R.id.TXT_cruiser);
        txt_folding = findViewById(R.id.TXT_folding);
        txt_hybrid = findViewById(R.id.TXT_hybrid);
        txt_touring = findViewById(R.id.TXT_touring);
        txt_cruiser1 = findViewById(R.id.TXT_cruiser1);
        txt_folding1 = findViewById(R.id.TXT_folding1);
        txt_bmx = findViewById(R.id.TXT_bmx);
        txt_mountain = findViewById(R.id.TXT_mountain);
        txt_roadBike = findViewById(R.id.TXT_roadBike);
        txt_electric = findViewById(R.id.TXT_electric);
    }

    private void setButtonOnClickListeners() {
        setButtonOnClickListener(btn_classic, txt_classic, "classic");
        setButtonOnClickListener(btn_cityBike, txt_cityBike, "cityBike");
        setButtonOnClickListener(btn_cruiser, txt_cruiser, "cruiser");
        setButtonOnClickListener(btn_folding, txt_folding, "folding");
        setButtonOnClickListener(btn_hybrid, txt_hybrid, "hybrid");
        setButtonOnClickListener(btn_touring, txt_touring, "touring");
        setButtonOnClickListener(btn_cruiser1, txt_cruiser1, "cruiser1");
        setButtonOnClickListener(btn_folding1, txt_folding1, "folding1");
        setButtonOnClickListener(btn_bmx, txt_bmx, "bmx");
        setButtonOnClickListener(btn_mountain, txt_mountain, "mountain");
        setButtonOnClickListener(btn_roadBike, txt_roadBike, "roadBike");
        setButtonOnClickListener(btn_electric, txt_electric, "electric");
    }

    private void setButtonOnClickListener(Button button, TextView textView, String bikeType) {
        button.setOnClickListener(v -> {
            try {
                int currentValue = Integer.parseInt(textView.getText().toString());
                if (currentValue > 0) {
                    int newValue = currentValue - 1;
                    textView.setText(String.valueOf(newValue));
                    updateAvailability(bikeType, newValue); // Update Firebase
                    Intent gotoConfirm = new Intent(getApplicationContext(), RideConfirmation.class);
                    gotoConfirm.putExtra("bikeType", bikeType);
                    gotoConfirm.putExtra("Location", Location);
                    startActivity(gotoConfirm);
                } else if (currentValue == 0) {
                    textView.setText("-");
                    showNotAvailableDialog();
                }
            } catch (NumberFormatException e) {
                textView.setText("-");
                showNotAvailableDialog();
            }
        });
    }

    private void loadInitialData() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot bikeSnapshot : dataSnapshot.getChildren()) {
                        String bikeType = bikeSnapshot.getKey();
                        Integer availability = bikeSnapshot.getValue(Integer.class);
                        if (availability != null) {
                            updateTextView(bikeType, availability);
                        }
                    }
                } else {
                    initializeDefaultValues();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error loading data: " + databaseError.getMessage());
            }
        });
    }

    private void updateTextView(String bikeType, int availability) {
        TextView textView = null;
        switch (bikeType) {
            case "classic": textView = txt_classic; break;
            case "cityBike": textView = txt_cityBike; break;
            case "cruiser": textView = txt_cruiser; break;
            case "folding": textView = txt_folding; break;
            case "hybrid": textView = txt_hybrid; break;
            case "touring": textView = txt_touring; break;
            case "cruiser1": textView = txt_cruiser1; break;
            case "folding1": textView = txt_folding1; break;
            case "bmx": textView = txt_bmx; break;
            case "mountain": textView = txt_mountain; break;
            case "roadBike": textView = txt_roadBike; break;
            case "electric": textView = txt_electric; break;
        }
        if (textView != null) {
            textView.setText(String.valueOf(availability));
        }
    }


    private void initializeDefaultValues() {
        Map<String, Object> defaultValues = new HashMap<>();
        defaultValues.put("classic", 0);
        defaultValues.put("cityBike", 0);
        defaultValues.put("cruiser", 0);
        defaultValues.put("folding", 0);
        defaultValues.put("hybrid", 0);
        defaultValues.put("touring", 0);
        defaultValues.put("cruiser1", 0);
        defaultValues.put("folding1", 0);
        defaultValues.put("bmx", 0);
        defaultValues.put("mountain", 0);
        defaultValues.put("roadBike", 0);
        defaultValues.put("electric", 0);

        mDatabase.setValue(defaultValues)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Initialized with default values.");
                    // Update UI after initializing
                    for (Map.Entry<String, Object> entry : defaultValues.entrySet()) {
                        updateTextView(entry.getKey(), (Integer) entry.getValue());
                    }
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Error initializing: " + e.getMessage()));
    }


    private void updateAvailability(String bikeType, int newValue) {
        mDatabase.child(bikeType).setValue(newValue)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Updated " + bikeType + " to " + newValue))
                .addOnFailureListener(e -> Log.e("Firebase", "Error updating " + bikeType + ": " + e.getMessage()));
    }


    private void showNotAvailableDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Not Available")
                .setMessage("This bicycle type is currently not available.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}