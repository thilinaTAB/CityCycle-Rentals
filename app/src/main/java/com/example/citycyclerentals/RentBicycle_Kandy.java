package com.example.citycyclerentals;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RentBicycle_Kandy extends AppCompatActivity {

    Button btn_classic, btn_cityBike, btn_cruiser, btn_folding;
    Button btn_hybrid, btn_touring, btn_cruiser1, btn_folding1;
    Button btn_bmx, btn_mountain, btn_roadBike, btn_electric;

    TextView txt_classic, txt_cityBike, txt_cruiser, txt_folding;
    TextView txt_hybrid,txt_touring, txt_cruiser1, txt_folding1;
    TextView txt_bmx, txt_mountain, txt_roadBike, txt_electric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rent_bicycle_kandy);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

        setButtonOnClickListener(btn_classic, txt_classic);
        setButtonOnClickListener(btn_cityBike, txt_cityBike);
        setButtonOnClickListener(btn_cruiser, txt_cruiser);
        setButtonOnClickListener(btn_folding, txt_folding);
        setButtonOnClickListener(btn_hybrid, txt_hybrid);
        setButtonOnClickListener(btn_touring, txt_touring);
        setButtonOnClickListener(btn_cruiser1, txt_cruiser1);
        setButtonOnClickListener(btn_folding1, txt_folding1);
        setButtonOnClickListener(btn_bmx, txt_bmx);
        setButtonOnClickListener(btn_mountain, txt_mountain);
        setButtonOnClickListener(btn_roadBike, txt_roadBike);
        setButtonOnClickListener(btn_electric, txt_electric);
    }


    private void setButtonOnClickListener(Button button, TextView textView) {
        button.setOnClickListener(v -> {
            try {
                int currentValue = Integer.parseInt(textView.getText().toString());
                if (currentValue > 0) {
                    textView.setText(String.valueOf(currentValue - 1));
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


    private void showNotAvailableDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Not Available")
                .setMessage("This bicycle type is currently not available.")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}