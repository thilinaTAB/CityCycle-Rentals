package com.example.citycyclerentals;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;  // Import the Looper class

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivityConfirm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_splash );

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splash = new Intent(SplashActivityConfirm.this, UserDashboard.class);
                startActivity(splash);
                finish();
            }
        }, 3000); // 3 seconds delay
    }
}