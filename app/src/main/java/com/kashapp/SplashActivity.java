package com.kashapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private ImageView SplashLogo;
    private int SPLASH_TIME_OUT = 1000;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SplashLogo = (ImageView) findViewById(R.id.splash_logo);

        firebaseAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splash = new Intent(SplashActivity.this, SignUpActivity.class);
                startActivity(splash);
                finish();
            }
        }, SPLASH_TIME_OUT);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        SplashLogo.startAnimation(animation);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            SendUserToSignUpActivity();
        } else {
            SendUserToMainActivity();
        }
    }

    private void SendUserToSignUpActivity() {
        Intent mainIntent = new Intent(SplashActivity.this, SignUpActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
