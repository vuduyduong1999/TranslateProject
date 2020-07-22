package com.example.translateproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    //Variables
    Animation topAnimation, botAnimation;
    ImageView ivLogo;
    TextView tvProduct, tvAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        //Animation
        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        botAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks

        ivLogo = findViewById(R.id.iv_logo);
        tvProduct = findViewById(R.id.tvProduct);
        tvAndroid = findViewById(R.id.tvAndroid);

        ivLogo.setAnimation(topAnimation);
        tvProduct.setAnimation(botAnimation);
        tvAndroid.setAnimation(botAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);
    }
}
