package com.example.examprativce;

import static com.example.examprativce.R.id.app_name1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.examprativce.ui.main.SplashActivity2Fragment;

public class SplashActivity2 extends AppCompatActivity {
    private TextView appName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity2);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SplashActivity2Fragment.newInstance())
                    .commitNow();
        }

        appName = findViewById(R.id.app_name1);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);
        appName.setAnimation(animation);

        new Thread() {

            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        }.start();
    }
}