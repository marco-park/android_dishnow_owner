package com.picke.dishnow_owner;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.picke.dishnow_owner.Owner_User.ReservationArrayClass;
import com.picke.dishnow_owner.Owner_User.ReservationClass;
import com.picke.dishnow_owner.Utility.RecyclerAdapter;

import java.util.ArrayList;

public class OnWaitActivity extends AppCompatActivity {

    private LinearLayout Lreservation;
    private ImageView Ireservation;
    private TextView Treservation;

    private LinearLayout Lwaitmatching;
    private ImageView Iwaitmatching;
    private TextView Twaitmatching;

    private LinearLayout Lmy;
    private ImageView Imy;
    private TextView Tmy;

    private ArrayList<ReservationClass> arrayList;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_wait);

        Lreservation = findViewById(R.id.onwait_rescompletelayout);
        Ireservation = findViewById(R.id.main_reservation_imageview);
        Treservation = findViewById(R.id.main_reservation_txtview);

        Lwaitmatching = findViewById(R.id.onwait_waitmatchinglayout);
        Iwaitmatching = findViewById(R.id.main_wait_matching_imageview);
        Twaitmatching = findViewById(R.id.main_wait_matching_txtview);

        Lmy = findViewById(R.id.onwait_mymenulayout);
        Imy = findViewById(R.id.main_my_imageview);
        Tmy = findViewById(R.id.main_my_txtview);

        Ireservation.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder),PorterDuff.Mode.SRC_ATOP);
        Treservation.setTextColor(getResources().getColor(R.color.color_bolder));

        Iwaitmatching.getBackground().setColorFilter(getResources().getColor(R.color.color_violet),PorterDuff.Mode.SRC_ATOP);
        Twaitmatching.setTextColor(getResources().getColor(R.color.color_violet));

        Imy.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder),PorterDuff.Mode.SRC_ATOP);
        Tmy.setTextColor(getResources().getColor(R.color.color_bolder));

        RecyclerView recyclerView = findViewById(R.id.onwait_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        getData();

        Lmy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnWaitActivity.this,MyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.xml.anim_slide_in_right, R.xml.anim_slide_out_left);
                finish();
                overridePendingTransition(R.xml.anim_slide_in_left, R.xml.anim_slide_out_right);
            }
        });
        Lreservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnWaitActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.xml.anim_slide_in_left, R.xml.anim_slide_out_right);
                finish();
                overridePendingTransition(R.xml.anim_slide_in_right, R.xml.anim_slide_out_left);
            }
        });
    }
    private void getData() {
        arrayList = ReservationArrayClass.getInstance(getApplicationContext()).getresArray();
        for(int i=0;i<arrayList.size();i++){
            ReservationClass reservationClass = new ReservationClass();
            reservationClass.setTime(arrayList.get(i).getTime());
            reservationClass.setPeople(arrayList.get(i).getPeople());
            reservationClass.setUid(arrayList.get(i).getUid());
            adapter.addItem(reservationClass);
        }
        adapter.notifyDataSetChanged();
    }
}
