package com.example.citycyclerentals;

import android.annotation.SuppressLint;
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

public class Login extends AppCompatActivity {

    TextView txt_btnRegister;
    EditText etxt_Email,etxt_Password;
    Button btn_Login;
    boolean valid = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txt_btnRegister = findViewById(R.id.TXT_btnRegister);
        etxt_Email = findViewById(R.id.ETXT_Email);
        etxt_Password = findViewById(R.id.ETXT_Password);
        btn_Login = findViewById(R.id.BTN_Login);


        txt_btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveReg =new Intent(getApplicationContext(), Register.class);
                startActivity(moveReg);
            }
        });

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(etxt_Email);
                checkField(etxt_Password);
            }
        });
    }
    public boolean checkField(EditText ex){
        if (ex.getText().toString().isEmpty()){
            ex.setError("Fill This");
            valid = false;
        }else{
            valid = true;
        }
        return valid;
    }
}