package com.inlog.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.inlog.ecommerce.R;

public class ordersummeryActivity extends AppCompatActivity {

    ImageView mImgCheck;
    Button continueshoppingid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordersummery);
        mImgCheck = (ImageView) findViewById(R.id.imageView);
        continueshoppingid = (Button) findViewById(R.id.continueshoppingid);
        ((Animatable) mImgCheck.getDrawable()).start();


        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((Animatable) mImgCheck.getDrawable()).start();
            }
        }, 2000);

        continueshoppingid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ordersummeryActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                finish();
            }
        });

    }
    public void onBackPressed(){
        Intent i = new Intent(ordersummeryActivity.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
        finish();
    }
}