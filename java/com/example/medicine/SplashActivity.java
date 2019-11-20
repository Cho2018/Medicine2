package com.example.medicine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(1000);

            Intent intent = new Intent(this, PasswordActivity.class);
            startActivity(intent);
            finish();
        } catch (InterruptedException e) {
        }
    }
}
