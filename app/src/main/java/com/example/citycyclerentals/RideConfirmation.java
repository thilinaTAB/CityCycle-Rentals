package com.example.citycyclerentals;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RideConfirmation extends AppCompatActivity {

    TextView txt_bikeType, txt_location, txt_plan, txt_amount;
    EditText etxt_date, etxt_time, etxt_numPlan;
    Button btn_confirm, btn_cancel;
    Calendar calendar = Calendar.getInstance();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_confirmation);

        txt_bikeType = findViewById(R.id.TXT_Bicycle);
        txt_location = findViewById(R.id.TXT_Location);
        etxt_date = findViewById(R.id.ETXT_Date);
        etxt_time = findViewById(R.id.ETXT_Time);
        btn_confirm = findViewById(R.id.BTN_Confirm);
        btn_cancel = findViewById(R.id.BTN_Cancel);
        etxt_numPlan = findViewById(R.id.ETXT_numPlan);
        txt_plan = findViewById(R.id.TXT_Plan);
        txt_amount = findViewById(R.id.TXT_Amount);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Make EditText non-editable and clickable
        etxt_date.setFocusable(false);
        etxt_date.setClickable(true);
        etxt_date.setOnClickListener(v -> showDatePickerDialog());

        etxt_time.setFocusable(false);
        etxt_time.setClickable(true);
        etxt_time.setOnClickListener(v -> showTimePickerDialog());

        // Get the bike type here, inside onCreate
        String BikeType = getIntent().getStringExtra("bikeType");
        txt_bikeType.setText(BikeType + " bicycle");

        // Get the location here, inside onCreate
        String Location = getIntent().getStringExtra("Location");
        txt_location.setText(Location);

        // Get the plan here, inside onCreate
        String Plan = getIntent().getStringExtra("Plan");
        txt_plan.setText(Plan);

        // Get the price here, inside onCreate
        int basePrice = getIntent().getIntExtra("Price", -1);

        calculateAndUpdateTotalPrice(basePrice);

        etxt_numPlan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateAndUpdateTotalPrice(basePrice); // Recalculate on text change
            }
        });


        btn_confirm.setOnClickListener(v ->{
            Toast.makeText(RideConfirmation.this, "Ride confirmed and saved!", Toast.LENGTH_SHORT).show();
            Intent goConfirm = new Intent(RideConfirmation.this, SplashActivityConfirm.class);
            goConfirm.putExtra("BicycleType", txt_bikeType.getText().toString());
            goConfirm.putExtra("Location", txt_location.getText().toString());
            goConfirm.putExtra("Plan", etxt_numPlan.getText().toString()+" "+txt_plan.getText().toString());
            goConfirm.putExtra("Amount", txt_amount.getText().toString());
            goConfirm.putExtra("Date", etxt_date.getText().toString()+" "+etxt_time.getText().toString());
            startActivity(goConfirm);
            finish();
        });

    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(year, monthOfYear, dayOfMonth);
                    updateDateInView();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    updateTimeInView();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // is24HourView:  true for 24-hour format, false for 12-hour with AM/PM
        );
        timePickerDialog.show();
    }

    private void updateTimeInView() {
        String myFormat = "HH:mm"; // Include HH:mm in the format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etxt_time.setText(sdf.format(calendar.getTime()));
    }

    private void updateDateInView() {
        String myFormat = "dd/MM/yyyy"; // Choose your desired format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etxt_date.setText(sdf.format(calendar.getTime()));

    }

    private void calculateAndUpdateTotalPrice(int basePrice) {
        int multiplier = 0;
        try {
            String numPlanText = etxt_numPlan.getText().toString();
            if (!numPlanText.isEmpty()) {
                multiplier = Integer.parseInt(numPlanText);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number of plans. Using default value (1).", Toast.LENGTH_SHORT).show();
        }

        int totalPrice = basePrice * multiplier;

        if (basePrice != -1) {
            txt_amount.setText("LKR " + String.valueOf(totalPrice) + ".00");
        } else {
            txt_amount.setText("-");
        }
    }

    private void mainMenu() {
        Intent goDash = new Intent(this, UserDashboard.class);
        startActivity(goDash);
        finish();
    }
}