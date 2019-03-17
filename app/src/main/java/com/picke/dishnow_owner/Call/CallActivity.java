package com.picke.dishnow_owner.Call;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.picke.dishnow_owner.HomeActivity;
import com.picke.dishnow_owner.OnWaitActivity;
import com.picke.dishnow_owner.Owner_User.ReservationArrayClass;
import com.picke.dishnow_owner.Owner_User.ReservationClass;
import com.picke.dishnow_owner.Owner_User.UserInfoClass;
import com.picke.dishnow_owner.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.IO;
import io.socket.client.Socket;

public class CallActivity extends AppCompatActivity {
    private TextView Ttimehour;
    private TextView Ttimemin;
    private TextView Ttimesecond;

    private TimerTask second;
    private final Handler handler = new Handler();

    int timer_sec;
    int count = 0;

    private TextView Tpeople;
    private Button Baccept;
    private Button Breject;
    private ReservationArrayClass reservationArrayClass;
    private ArrayList<ReservationClass>arrayList;
    private UserInfoClass userInfoClass;
    private Socket mSocket;
    private String res_id;
    private long lsecond;
    private ArrayList<ReservationClass>final_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_call);
        timerStart();

        Ttimehour = findViewById(R.id.popup_call_hourtext);
        Ttimemin = findViewById(R.id.popup_call_mintext);
        Ttimesecond = findViewById(R.id.popup_call_60secondtext);

        Tpeople = findViewById(R.id.popup_call_peoplenumber);
        Baccept = findViewById(R.id.popup_call_acceptbutton);
        Breject = findViewById(R.id.popup_call_rejectbutton);

        Intent intent = getIntent();
        Tpeople.setText(intent.getStringExtra("user_people"));
        String resTime = intent.getStringExtra("user_time");
        String ressecond = intent.getStringExtra("user_sec");

        final_list = ReservationArrayClass.getInstance(getApplicationContext()).getrfinArray();

        Log.d("meenseek",ressecond);

        try{
            lsecond = Long.valueOf(ressecond);

            timer_sec = (int) (System.currentTimeMillis()/1000 - lsecond);
            if(timer_sec>=60){
                startActivity(new Intent(CallActivity.this, HomeActivity.class));
                finish();
            }else{
                timer_sec = (60 - timer_sec);
            }
        }
        catch(Exception e){
            timer_sec=60;
            e.printStackTrace();
        }

        String resTimehour = resTime.substring(0,2);
        String resTimemin = resTime.substring(3,5);
        Ttimehour.setText(resTimehour);
        Ttimemin.setText(resTimemin);
        arrayList = ReservationArrayClass.getInstance(getApplicationContext()).getresArray();

        userInfoClass = UserInfoClass.getInstance(getApplicationContext());
        SharedPreferences shared_id = getSharedPreferences("shared_id", Activity.MODE_PRIVATE);
        res_id = shared_id.getString("id",null);

        try {
            mSocket = IO.socket("http://ec2-18-218-206-167.us-east-2.compute.amazonaws.com:3000");
            mSocket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                JsonObject prejsonobject = new JsonObject();
                prejsonobject.addProperty("res_id",res_id);
                prejsonobject.addProperty("res_token",userInfoClass.getOwnertoken());
                JSONObject jsonObject_id = null;
                try{
                    jsonObject_id = new JSONObject(prejsonobject.toString());
                }catch(Exception e){
                    e.printStackTrace();
                }
                mSocket.emit("res_id", jsonObject_id);
            });
            mSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Baccept.setOnClickListener(v -> {
            timer_sec=-1;
            Intent intent1= new Intent(CallActivity.this, OnWaitActivity.class);
            ReservationClass reservationClass = new ReservationClass();
            reservationClass.setTime(intent.getStringExtra("user_time"));
            reservationClass.setPeople(intent.getStringExtra("user_people"));
            reservationClass.setUid(intent.getStringExtra("user_id"));
            reservationClass.setArriveSec(intent.getStringExtra("user_arrive_sec"));
            long rnow = System.currentTimeMillis();
            reservationClass.setNowsecond(lsecond);

            Date date = new Date(rnow);

            JsonObject prejsonobject = new JsonObject();
            prejsonobject.addProperty("res_id",res_id);
            prejsonobject.addProperty("user_id",intent.getStringExtra("user_id"));
            prejsonobject.addProperty("res_name",userInfoClass.getResname());

            JSONObject jsonObject_id = null;
            try {
                jsonObject_id = new JSONObject(prejsonobject.toString());
                mSocket.emit("res_yes",jsonObject_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            reservationClass.setNowtime(sdf.format(date));
            arrayList.add(reservationClass);
            startActivity(intent1);
            finish();
        });

        Breject.setOnClickListener(v -> {
            timer_sec=-1;
            startActivity(new Intent(CallActivity.this, HomeActivity.class));
            finish();
        });
    }

    public void timerStart() {
        timer_sec = 60;
        second = new TimerTask() {
            @Override
            public void run() {
                Update();
                timer_sec--;
                if(timer_sec==0)
                {
                    startActivity(new Intent(CallActivity.this, HomeActivity.class));
                    finish();
                    timer_sec=-1;
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 1000);
    }

    protected void Update() {
        Runnable updater = () -> Ttimesecond.setText(""+timer_sec);
        handler.post(updater);
    }

    @Override
    public void onPause(){
        super.onPause();
        mSocket.disconnect();
    }
    @Override
    public void onResume(){
        super.onResume();
        JsonObject prejsonobject = new JsonObject();
        prejsonobject.addProperty("res_token",userInfoClass.getOwnertoken());
        prejsonobject.addProperty("res_id", res_id);
        JSONObject jsonObject_id = null;
        try {
            jsonObject_id = new JSONObject(prejsonobject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject finalJsonObject_id = jsonObject_id;
        mSocket.emit("res_id", finalJsonObject_id);
        mSocket.connect();
    }
}
