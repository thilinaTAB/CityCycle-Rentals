package com.example.citycyclerentals;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;  // Import the Looper class

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {  // Use the correct Handler constructor
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, 2000); // 2 seconds delay
    }
}