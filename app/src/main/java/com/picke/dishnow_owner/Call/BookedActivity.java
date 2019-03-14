package com.picke.dishnow_owner.Call;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.picke.dishnow_owner.HomeActivity;
import com.picke.dishnow_owner.Owner_User.ReservationArrayClass;
import com.picke.dishnow_owner.Owner_User.ReservationClass;
import com.picke.dishnow_owner.Owner_User.UserInfoClass;
import com.picke.dishnow_owner.R;

public class BookedActivity extends AppCompatActivity {

    private Button Bconfirm;
    private TextView Tplace;
    private TextView Tpeople;
    private TextView Ttime;
    private ReservationArrayClass reservationArrayClass;
    private UserInfoClass userInfoClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked);
        Tplace = findViewById(R.id.booked_place_txtview);
        Bconfirm = findViewById(R.id.booked_confirmbutton);
        Tpeople = findViewById(R.id.booked_people_textview);
        Ttime = findViewById(R.id.booked_arrive_time_textview);
        reservationArrayClass = ReservationArrayClass.getInstance(getApplicationContext());
        userInfoClass = UserInfoClass.getInstance(getApplicationContext());

        Tplace.setText(userInfoClass.getResname());
        Intent intent = getIntent();

        try {
            if (intent.getStringExtra("push").length()!=0) {
                String uid = intent.getStringExtra("user_id");
                ReservationClass reservationClass = reservationArrayClass.get_resclass(uid);
                reservationArrayClass.fin_add(reservationClass);
                reservationArrayClass.res_delete(uid);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Tpeople.setText(intent.getStringExtra("user_people"));
        Ttime.setText(Long.toString(
                (Long.valueOf(intent.getStringExtra("user_arrive_sec"))-System.currentTimeMillis()/1000)/60));

        Bconfirm.setOnClickListener(v -> {
            startActivity(new Intent(BookedActivity.this, HomeActivity.class));
            finish();
        });
    }
}
