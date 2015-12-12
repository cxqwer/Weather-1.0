package com.alex.weather10.launch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;

import com.alex.weather10.R;


public class ActivitySplash extends AppCompatActivity {
    private static long SPLASH_SCREEN_DELAY_MILLIS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Splash);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(ActivitySplash.this, MainActivity.class);
                ActivitySplash.this.startActivity(mainIntent);
                ActivitySplash.this.finish();
            }
        }, SPLASH_SCREEN_DELAY_MILLIS);
    }
}
