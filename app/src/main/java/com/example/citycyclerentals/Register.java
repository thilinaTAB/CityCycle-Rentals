package com.example.citycyclerentals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Register extends AppCompatActivity {

    TextView txt_btnLogin;

    EditText etxt_Name, etxt_Email, etxt_Password;

    boolean valid = false;

    Button btn_Register;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txt_btnLogin = findViewById(R.id.TXT_btnLogin);
        etxt_Name = findViewById(R.id.ETXT_Name);
        etxt_Email = findViewById(R.id.ETXT_Email);
        etxt_Password = findViewById(R.id.ETXT_Password);
        btn_Register = findViewById(R.id.BTN_Register);

        txt_btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveLogin = new Intent(getApplicationContext(), Login.class);
                startActivity(moveLogin);
            }
        });

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(etxt_Name);
                checkField(etxt_Email);
                checkField(etxt_Password);

            }
        });
    }

    public boolean checkField(EditText ex) {
        if(ex.getText().toString().isEmpty()){
            ex.setError("Required this field");
            valid = false;
        }else{
            valid = true;
        }


        return valid;
    }
}