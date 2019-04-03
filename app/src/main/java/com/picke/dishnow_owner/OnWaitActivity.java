package com.picke.dishnow_owner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.picke.dishnow_owner.Call.BookedActivity;
import com.picke.dishnow_owner.Call.CallActivity;
import com.picke.dishnow_owner.Owner_User.ReservationArrayClass;
import com.picke.dishnow_owner.Owner_User.ReservationClass;
import com.picke.dishnow_owner.Owner_User.UserInfoClass;
import com.picke.dishnow_owner.Utility.RecyclerAdapter_onwait;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.IO;
import io.socket.client.Socket;

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

    private String res_id;
    private Vibrator vibrator;
    static public Socket mSocket;
    private ArrayList<ReservationClass> arrayList;
    private RecyclerAdapter_onwait adapter;
    private UserInfoClass userInfoClass;
    private ReservationArrayClass reservationArrayClass;
    private TextView Tonwaitshow;
    private Timer timer;
    private TimerTask timerTask;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_wait);
        Tonwaitshow = findViewById(R.id.onwait_show);

        Lreservation = findViewById(R.id.onwait_rescompletelayout);
        Ireservation = findViewById(R.id.onwait_reservation_imageview);
        Treservation = findViewById(R.id.onwait_reservation_txtview);

        Lwaitmatching = findViewById(R.id.onwait_waitmatchinglayout);
        Iwaitmatching = findViewById(R.id.onwiat_matching_imageview);
        Twaitmatching = findViewById(R.id.onwait_matching_txtview);

        floatingActionButton = findViewById(R.id.on_wait_floating_btn_call);

        Lmy = findViewById(R.id.onwait_mymenulayout);
        Imy = findViewById(R.id.onwait_my_imageview);
        Tmy = findViewById(R.id.onwait_my_txtview);

        Ireservation.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder),PorterDuff.Mode.SRC_ATOP);
        Treservation.setTextColor(getResources().getColor(R.color.color_bolder));

        Iwaitmatching.getBackground().setColorFilter(getResources().getColor(R.color.color_violet),PorterDuff.Mode.SRC_ATOP);
        Twaitmatching.setTextColor(getResources().getColor(R.color.color_violet));

        Imy.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder),PorterDuff.Mode.SRC_ATOP);
        Tmy.setTextColor(getResources().getColor(R.color.color_bolder));

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        userInfoClass = UserInfoClass.getInstance(getApplicationContext());
        SharedPreferences shared_id = getSharedPreferences("shared_id", Activity.MODE_PRIVATE);
        res_id = shared_id.getString("id",null);

        RecyclerView recyclerView = findViewById(R.id.onwait_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        reservationArrayClass = ReservationArrayClass.getInstance(getApplicationContext());
        arrayList = ReservationArrayClass.getInstance(getApplicationContext()).getresArray();
        SharedPreferences push = getSharedPreferences("v1.0", Activity.MODE_PRIVATE);

        if(push.getString("background","true").equals("true")) {
            floatingActionButton.getBackground().setColorFilter(getResources().getColor(R.color.color_violet),PorterDuff.Mode.SRC_ATOP);
            floatingActionButton.setBackgroundDrawable( getResources().getDrawable(R.drawable.ic_icon_call_white));
        }else {
            floatingActionButton.getBackground().setColorFilter(getResources().getColor(R.color.color_red),PorterDuff.Mode.SRC_ATOP);
            floatingActionButton.setBackgroundDrawable( getResources().getDrawable(R.drawable.ic_icon_call_white) );
        }

        floatingActionButton.setOnClickListener(v -> {
            floatingActionButton.getBackground().setColorFilter(getResources().getColor(R.color.color_red),PorterDuff.Mode.SRC_ATOP);
            floatingActionButton.setBackgroundDrawable( getResources().getDrawable(R.drawable.ic_icon_call_white));
            if(push.getString("background","true").equals("true")) {
                SharedPreferences.Editor epush1 = push.edit();
                epush1.putString("background", "false");
                epush1.commit();
                JsonObject prejsonobject = new JsonObject();
                prejsonobject.addProperty("res_id", res_id);
                prejsonobject.addProperty("res_token",userInfoClass.getOwnertoken());
                prejsonobject.addProperty("is_call",push.getString("background","false"));
                JSONObject jsonObject_id = null;
                try {
                    jsonObject_id = new JSONObject(prejsonobject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mSocket.emit("res_id", jsonObject_id);
                mSocket.connect();
                Toast.makeText(getApplicationContext(),"콜을 중지합니다.",Toast.LENGTH_SHORT).show();
            }else {
                floatingActionButton.getBackground().setColorFilter(getResources().getColor(R.color.color_violet),PorterDuff.Mode.SRC_ATOP);
                floatingActionButton.setBackgroundDrawable( getResources().getDrawable(R.drawable.ic_icon_call_white) );
                SharedPreferences.Editor epush2 = push.edit();
                epush2.putString("background", "true");
                epush2.commit();
                JsonObject prejsonobject = new JsonObject();
                prejsonobject.addProperty("res_id", res_id);
                prejsonobject.addProperty("res_token",userInfoClass.getOwnertoken());
                prejsonobject.addProperty("is_call",push.getString("background","true"));
                JSONObject jsonObject_id = null;
                try {
                    jsonObject_id = new JSONObject(prejsonobject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mSocket.emit("res_id", jsonObject_id);
                mSocket.connect();
                Toast.makeText(getApplicationContext(),"콜을 재개합니다.",Toast.LENGTH_SHORT).show();
            }
        });

        try {
            mSocket = IO.socket("http://ec2-18-218-206-167.us-east-2.compute.amazonaws.com:3000");
            mSocket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                JsonObject prejsonobject = new JsonObject();
                prejsonobject.addProperty("res_id",res_id);
                prejsonobject.addProperty("res_token",userInfoClass.getOwnertoken());
                prejsonobject.addProperty("is_call",push.getString("background","true"));
                JSONObject jsonObject_id = null;
                try{
                    jsonObject_id = new JSONObject(prejsonobject.toString());
                }catch(Exception e){
                    e.printStackTrace();
                }
                mSocket.emit("res_id", jsonObject_id);
            }).on("user_call", (Object... objects) -> {
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
                ringtone.play();

                JsonParser jsonParsers = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParsers.parse(objects[0].toString());
                runOnUiThread(()->{
                    Intent intent = new Intent(OnWaitActivity.this, CallActivity.class);
                    String people = jsonObject.get("user_people").toString();
                    people = people.substring(1,people.length()-1);
                    String time = jsonObject.get("user_time").toString();
                    time = time.substring(1,time.length()-1);
                    String uid = jsonObject.get("user_id").toString();
                    uid = uid.substring(1,uid.length()-1);
                    intent.putExtra("user_people", people);
                    intent.putExtra("user_time", time);
                    intent.putExtra("user_id", uid);
                    intent.putExtra("user_sec", jsonObject.get("user_sec").toString());
                    intent.putExtra("user_arrive_sec",jsonObject.get("user_arrive_sec").toString());
                    startActivity(intent);
                    finish();
                });
            }).on("final_call",(Object... objects)-> {
                    vibrator.vibrate(2000);
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
                    ringtone.play();
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(objects[0].toString());
                    String uid = jsonObject.get("user_id").toString();
                    uid = uid.substring(1, uid.length() - 1);
                    reservationArrayClass.fin_add(reservationArrayClass.get_resclass(uid));
                    try{
                        reservationArrayClass.res_delete(uid);

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    String user_people = jsonObject.get("user_people").toString();
                    user_people = user_people.substring(1,user_people.length()-1);
                    String user_arrive_sec = jsonObject.get("user_arrive_sec").toString();
                    user_arrive_sec = user_arrive_sec.substring(1,user_arrive_sec.length()-1);

                    Intent intent1 = new Intent(OnWaitActivity.this, BookedActivity.class);
                    intent1.putExtra("user_people",user_people);
                    intent1.putExtra("user_arrive_sec",user_arrive_sec);
                    startActivity(intent1);
                    finish();
            });
            mSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new RecyclerAdapter_onwait();
        recyclerView.setAdapter(adapter);

        timerTask= new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(()-> {
                    adapter.clearItem();
                    getData();
                    arrayList = ReservationArrayClass.getInstance(getApplicationContext()).getresArray();
                    long now = System.currentTimeMillis()/1000;
                    for(int i=0;i<arrayList.size();i++){
                        if((now-arrayList.get(i).getNowsecond())>=10*60){
                            reservationArrayClass.res_delete(arrayList.get(i).getUid());
                            adapter.removeItem(i);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if(adapter.getItemCount()!=0){
                        Tonwaitshow.setText("");
                    }else{
                        Tonwaitshow.setText("내역이 없습니다.");
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,100,1000*3);


        Lmy.setOnClickListener(v -> {
            Intent intent = new Intent(OnWaitActivity.this,MyActivity.class);
            startActivity(intent);
            overridePendingTransition(R.xml.anim_slide_in_left, R.xml.anim_slide_out_right);
            finish();
            overridePendingTransition(R.xml.anim_slide_in_right, R.xml.anim_slide_out_left);

        });
        Lreservation.setOnClickListener(v -> {
            Intent intent = new Intent(OnWaitActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.xml.anim_slide_in_right, R.xml.anim_slide_out_left);
            finish();
            overridePendingTransition(R.xml.anim_slide_in_left, R.xml.anim_slide_out_right);
        });
    }
    private void getData() {
        arrayList = ReservationArrayClass.getInstance(getApplicationContext()).getresArray();
        for(int i=0;i<arrayList.size();i++){
            ReservationClass reservationClass = new ReservationClass();
            reservationClass.setTime(arrayList.get(i).getTime());
            reservationClass.setPeople(arrayList.get(i).getPeople());
            reservationClass.setUid(arrayList.get(i).getUid());
            reservationClass.setNowtime(arrayList.get(i).getNowtime());
            reservationClass.setNowsecond(arrayList.get(i).getNowsecond());
            adapter.addItem(reservationClass);
        }
        adapter.notifyDataSetChanged();
        if(adapter.getItemCount()!=0){
            Tonwaitshow.setText("");
        }else{
            Tonwaitshow.setText("내역이 없습니다.");
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        mSocket.disconnect();
        timer.cancel();
        timerTask.cancel();
    }

    @Override
    public void onResume(){
        super.onResume();
        JsonObject prejsonobject = new JsonObject();
        prejsonobject.addProperty("res_id", res_id);
        prejsonobject.addProperty("res_token",userInfoClass.getOwnertoken());
        prejsonobject.addProperty("is_call",getSharedPreferences("v1.0",MODE_PRIVATE).getString("background","true"));
        JSONObject jsonObject_id = null;
        try {
            jsonObject_id = new JSONObject(prejsonobject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSocket.emit("res_id", jsonObject_id);
        mSocket.connect();
    }
}