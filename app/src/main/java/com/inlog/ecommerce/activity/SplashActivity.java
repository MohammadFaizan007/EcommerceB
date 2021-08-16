package com.inlog.ecommerce.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.inlog.ecommerce.R;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {
    Animation animFadeIn;
    LinearLayout linearLayout;
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private final int PERMISSION_ALL = 1;
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.animation_fade_in);
        animFadeIn.setAnimationListener(this);
        linearLayout = (LinearLayout) findViewById(R.id.layout_linear);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.startAnimation(animFadeIn);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    public void onAnimationEnd(Animation animation) {
        final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (hasPermissions(SplashActivity.this, PERMISSIONS)) {
            navigateToLogin();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMISSIONS, PERMISSION_ALL);//ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, PERMISSION_ALL);
            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        //under Implementation
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL:
                if (hasPermissions(context, permissions)) {
                    navigateToLogin();
                } else {
                    //final String[] PERMISSIONS = {android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.CALL_PHONE, android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(PERMISSIONS, PERMISSION_ALL);
                    }
                }
                break;
        }
    }

    private void navigateToLogin() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            SharedPreferences sharedPrefs = getSharedPreferences("globalloginvalues", MODE_PRIVATE);
            SharedPreferences.Editor ed;
            if(!sharedPrefs.contains("globalid"))
            {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                finish();
            }
            else
            {
                SharedPreferences sharedPreft = getSharedPreferences("shopidkey", MODE_PRIVATE);
                if(!sharedPreft.contains("shopid"))
                {
                    Intent i = new Intent(SplashActivity.this, mapactivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                    finish();
                }
                else
                {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                    finish();
                }

            }

        }, SPLASH_DISPLAY_LENGTH);
    }
}