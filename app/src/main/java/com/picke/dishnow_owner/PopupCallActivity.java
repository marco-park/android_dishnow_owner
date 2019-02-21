package com.picke.dishnow_owner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.picke.dishnow_owner.Owner_User.ReservationArrayClass;
import com.picke.dishnow_owner.Owner_User.ReservationClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PopupCallActivity extends AppCompatActivity {
    private TextView Ttimehour;
    private TextView Ttimemin;
    private TextView Tpeople;
    private Button Baccept;
    private Button Breject;
    private ReservationArrayClass reservationArrayClass;
    private ArrayList<ReservationClass>arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_call);

        Ttimehour = findViewById(R.id.popup_call_hourtext);
        Ttimemin = findViewById(R.id.popup_call_mintext);
        Tpeople = findViewById(R.id.popup_call_peoplenumber);
        Baccept = findViewById(R.id.popup_call_acceptbutton);
        Breject = findViewById(R.id.popup_call_rejectbutton);

        Intent intent = getIntent();
        Tpeople.setText(intent.getStringExtra("user_numbers"));
        Ttimehour.setText(intent.getStringExtra("user_time"));
        arrayList = ReservationArrayClass.getInstance(getApplicationContext()).getresArray();

        Baccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1= new Intent(PopupCallActivity.this,OnWaitActivity.class);
                ReservationClass reservationClass = new ReservationClass();
                reservationClass.setTime(intent.getStringExtra("user_numbers"));
                reservationClass.setPeople(intent.getStringExtra("user_people"));
                reservationClass.setUid(intent.getStringExtra("user_id"));
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                String getTime = sdf.format(date);
                reservationClass.setNowtime(getTime);
                arrayList.add(reservationClass);
                startActivity(intent1);
                finish();
            }
        });

        Breject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PopupCallActivity.this,MainActivity.class));
                finish();
            }
        });
    }
}
