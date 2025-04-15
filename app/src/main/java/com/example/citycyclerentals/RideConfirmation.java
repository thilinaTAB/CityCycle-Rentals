package com.example.citycyclerentals;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RideConfirmation extends AppCompatActivity {

    TextView txt_bikeType, txt_location;
    EditText etxt_date;
    Button btn_confirm, btn_cancel;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_confirmation);

        txt_bikeType = findViewById(R.id.TXT_Bicycle);
        txt_location = findViewById(R.id.TXT_Location);
        etxt_date = findViewById(R.id.ETXT_Date);
        btn_confirm = findViewById(R.id.BTN_Confirm);
        btn_cancel = findViewById(R.id.BTN_Cancel);

        // Make EditText non-editable and clickable
        etxt_date.setFocusable(false);
        etxt_date.setClickable(true);
        etxt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Get the bike type here, inside onCreate
        String BikeType = getIntent().getStringExtra("bikeType");
        txt_bikeType.setText(BikeType + " bicycle");

        // Get the location here, inside onCreate
        String Location = getIntent().getStringExtra("Location");
        txt_location.setText(Location);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            Intent goConfirm = new Intent(RideConfirmation.this, SplashActivityConfirm.class);
            @Override
            public void onClick(View v) {
                startActivity(goConfirm);
            }

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

    private void updateDateInView() {
        String myFormat = "dd/MM/yyyy"; // Choose your desired format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etxt_date.setText(sdf.format(calendar.getTime()));
    }
}