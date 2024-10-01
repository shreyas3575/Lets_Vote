package com.example.letsvote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new MyThread().start();
    }

    class MyThread extends Thread
    {
        @Override
        public void run() {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            startActivity(new Intent(SplashScreen.this, IntroActivity.class));
            finish();
        }
    }
}