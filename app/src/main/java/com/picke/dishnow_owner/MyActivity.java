package com.picke.dishnow_owner;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.picke.dishnow_owner.Utility.CustomDialog;

public class MyActivity extends AppCompatActivity {

    private LinearLayout Lreservation;
    private ImageView Ireservation;
    private TextView Treservation;

    private LinearLayout Lwaitmatching;
    private ImageView Iwaitmatching;
    private TextView Twaitmatching;
    private LinearLayout Lmy;
    private ImageView Imy;
    private TextView Tmy;
    private LinearLayout Llogout;
    Context context;

    int ckl = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        Lreservation = findViewById(R.id.my_rescompletelayout);
        Ireservation = findViewById(R.id.main_reservation_imageview);
        Treservation = findViewById(R.id.main_reservation_txtview);

        Lwaitmatching = findViewById(R.id.my_waitmatchinglayout);
        Iwaitmatching = findViewById(R.id.main_wait_matching_imageview);
        Twaitmatching = findViewById(R.id.main_wait_matching_txtview);

        Lmy = findViewById(R.id.my_mymenulayout);
        Imy = findViewById(R.id.main_my_imageview);
        Tmy = findViewById(R.id.main_my_txtview);

        Llogout = findViewById(R.id.my_logout_layout);


        Ireservation.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder),PorterDuff.Mode.SRC_ATOP);
        Treservation.setTextColor(getResources().getColor(R.color.color_bolder));

        Iwaitmatching.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder),PorterDuff.Mode.SRC_ATOP);
        Twaitmatching.setTextColor(getResources().getColor(R.color.color_bolder));

        Imy.getBackground().setColorFilter(getResources().getColor(R.color.color_violet),PorterDuff.Mode.SRC_ATOP);
        Tmy.setTextColor(getResources().getColor(R.color.color_violet));

        Lwaitmatching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this,OnWaitActivity.class);
                startActivity(intent);
                overridePendingTransition(R.xml.anim_slide_in_right, R.xml.anim_slide_out_left);
                finish();
                overridePendingTransition(R.xml.anim_slide_in_left, R.xml.anim_slide_out_right);

            }
        });
        Lreservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.xml.anim_slide_in_left, R.xml.anim_slide_out_right);
                finish();
                overridePendingTransition(R.xml.anim_slide_in_right, R.xml.anim_slide_out_left);


            }
        });

        //로그아웃 레이아웃 클릭 시
        Llogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog customDialog = new CustomDialog(MyActivity.this);
                customDialog.callFunction();
            }
        });


    }
}