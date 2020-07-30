package com.example.translateproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MenuActivity extends AppCompatActivity {
    ImageView logo ;
    LinearLayout lotitle,lor1,lor2;
    Animation top,bot;
    CardView cvDich, cvThem, cvLichSu, cvYoutube;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //Hooks
        logo = findViewById(R.id.imlogoDB);
        lotitle = findViewById(R.id.loTitle);

        lor1 = findViewById(R.id.loLine1);
        lor2 = findViewById(R.id.loLine2);

        top = AnimationUtils.loadAnimation(this, R.anim.top_animation_db);
        bot = AnimationUtils.loadAnimation(this,R.anim.bottom_animation_db);

        logo.setAnimation(top);
        lotitle.setAnimation(top);
        lor1.setAnimation(bot);
        lor2.setAnimation(bot);

        cvDich = findViewById(R.id.cvDich);
        cvThem = findViewById(R.id.cvThem);
        cvLichSu = findViewById(R.id.cvLichsu);
        cvYoutube = findViewById(R.id.cvYoutube);

        cvDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.putExtra("menu",1);
                startActivity(intent);
            }
        });

        cvThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.putExtra("menu",2);
                startActivity(intent);

            }
        });
        cvLichSu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.putExtra("menu",3);
                startActivity(intent);
            }
        });
        cvYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.putExtra("menu",4);
                startActivity(intent);
            }
        });


    }
}
