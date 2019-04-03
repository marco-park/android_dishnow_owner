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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.picke.dishnow_owner.Call.BookedActivity;
import com.picke.dishnow_owner.Call.CallActivity;
import com.picke.dishnow_owner.Owner_User.ReservationArrayClass;
import com.picke.dishnow_owner.Owner_User.ReservationClass;
import com.picke.dishnow_owner.Owner_User.UserAuthClass;
import com.picke.dishnow_owner.Owner_User.UserInfoClass;
import com.picke.dishnow_owner.Utility.RecyclerAdapter_reserved;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.IO;
import io.socket.client.Socket;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "claor123";
    public Intent intent;
    static public Socket mSocket;
    String res_id;
    private Vibrator vibrator;
    private LinearLayout Lreservation;
    private ImageView Ireservation;
    private TextView Treservation;

    private LinearLayout Lwaitmatching;
    private ImageView Iwaitmatching;
    private TextView Twaitmatching;

    private LinearLayout Lmy;
    private ImageView Imy;
    private TextView Tmy;
    private TextView Thomeshow;
    private TimerTask timerTask;
    private Timer timer;

    private UserInfoClass userInfoClass;
    private ReservationArrayClass reservationArrayClass;
    private ArrayList<ReservationClass> res_list;
    private ArrayList<ReservationClass> final_list;
    private RecyclerAdapter_reserved adapter_reserved;
    private UserAuthClass userAuthClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        Thomeshow = findViewById(R.id.main_show);

        Lreservation = findViewById(R.id.main_rescompletelayout);
        Ireservation = findViewById(R.id.main_reservation_imageview);
        Treservation = findViewById(R.id.main_reservation_txtview);

        Lwaitmatching = findViewById(R.id.main_waitmatchinglayout);
        Iwaitmatching = findViewById(R.id.main_wait_matching_imageview);
        Twaitmatching = findViewById(R.id.main_wait_matching_txtview);

        Lmy = findViewById(R.id.main_mymenulayout);
        Imy = findViewById(R.id.main_my_imageview);
        Tmy = findViewById(R.id.main_my_txtview);

        Ireservation.getBackground().setColorFilter(getResources().getColor(R.color.color_violet),PorterDuff.Mode.SRC_ATOP);
        Treservation.setTextColor(getResources().getColor(R.color.color_violet));

        Iwaitmatching.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder),PorterDuff.Mode.SRC_ATOP);
        Twaitmatching.setTextColor(getResources().getColor(R.color.color_bolder));

        Imy.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder),PorterDuff.Mode.SRC_ATOP);
        Tmy.setTextColor(getResources().getColor(R.color.color_bolder));

        userInfoClass = UserInfoClass.getInstance(getApplicationContext());
        userAuthClass = UserAuthClass.getInstance(getApplicationContext());
        SharedPreferences shared_id = getSharedPreferences("shared_id", Activity.MODE_PRIVATE);
        res_id = shared_id.getString("id",null);
        res_list = ReservationArrayClass.getInstance(getApplicationContext()).getresArray();
        final_list = ReservationArrayClass.getInstance(getApplicationContext()).getrfinArray();
        reservationArrayClass = ReservationArrayClass.getInstance(getApplicationContext());

        RecyclerView recyclerView = findViewById(R.id.main_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(
                HomeActivity.this, instanceIdResult -> {
                    String newToken = instanceIdResult.getToken();
                    userAuthClass.setOwnertoken(newToken);
                    userInfoClass.setOwnertoken(newToken);
                });

        adapter_reserved = new RecyclerAdapter_reserved();
        recyclerView.setAdapter(adapter_reserved);

        try {
            mSocket = IO.socket("http://ec2-18-218-206-167.us-east-2.compute.amazonaws.com:3000");
            mSocket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                JsonObject prejsonobject = new JsonObject();
                prejsonobject.addProperty("res_id",res_id);
                prejsonobject.addProperty("res_token",userInfoClass.getOwnertoken());
                prejsonobject.addProperty("is_call","true");
                JSONObject jsonObject_id = null;
                try{
                    jsonObject_id = new JSONObject(prejsonobject.toString());
                }catch(Exception e){
                    e.printStackTrace();
                }
                mSocket.emit("res_id", jsonObject_id);
            }).on("user_call", (Object... objects) -> {
                vibrator.vibrate(2000);
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
                ringtone.play();

                JsonParser jsonParsers = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParsers.parse(objects[0].toString());
                runOnUiThread(()-> {
                            Intent intent = new Intent(HomeActivity.this, CallActivity.class);
                            String people = jsonObject.get("user_people").toString();
                            people = people.substring(1, people.length() - 1);
                            String time = jsonObject.get("user_time").toString();
                            time = time.substring(1, time.length() - 1);
                            String uid = jsonObject.get("user_id").toString();
                            uid = uid.substring(1, uid.length() - 1);
                            intent.putExtra("user_people", people);
                            intent.putExtra("user_time", time);
                            intent.putExtra("user_id", uid);
                            intent.putExtra("user_sec",jsonObject.get("user_sec").toString());
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
                uid = uid.substring(1,uid.length()-1);
                reservationArrayClass.fin_add(reservationArrayClass.get_resclass(uid));
                try{
                    reservationArrayClass.res_delete(uid);
                }catch (Exception e){
                    e.printStackTrace();
                }
                String user_people = jsonObject.get("user_people").toString();
                user_people = user_people.substring(1,user_people.length()-1);
                String user_arrive_sec = jsonObject.get("user_arrive_sec").toString();
                user_arrive_sec = user_arrive_sec.substring(1,user_arrive_sec.length()-1);
                Intent intent1 = new Intent(HomeActivity.this, BookedActivity.class);
                intent1.putExtra("user_people",user_people);
                intent1.putExtra("user_arrive_sec",user_arrive_sec);
                startActivity(intent1);
                finish();
            });
            mSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Lwaitmatching.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,OnWaitActivity.class);
            startActivity(intent);
            overridePendingTransition(R.xml.anim_slide_in_left, R.xml.anim_slide_out_right);
            finish();
            overridePendingTransition(R.xml.anim_slide_in_right, R.xml.anim_slide_out_left);

        });
        Lmy.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,MyActivity.class);
            startActivity(intent);
            overridePendingTransition(R.xml.anim_slide_in_left, R.xml.anim_slide_out_right);
            finish();
            overridePendingTransition(R.xml.anim_slide_in_right ,R.xml.anim_slide_out_left);
        });

        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread( () -> {
                    //adapter비워주기
                    try{adapter_reserved.clearItem();}
                    catch (Exception e ){e.printStackTrace();}
                    getData();
                    //리스팅
                    if (final_list.size() != 0) {
                        for (int i = 0; i < final_list.size(); i++) {
                            long now = System.currentTimeMillis() / 1000; //현재시간 저장
                            long arrivetime = Long.valueOf(final_list.get( i ).getArriveSec() );

                            if (now - arrivetime >= 10*60) {
                                try {
                                    final_list.get(i).setItemViewBType();
                                    adapter_reserved.setItemViewBType( i );
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    adapter_reserved.notifyDataSetChanged();
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 100, 500);
    }

    private void getData() {
        for(int i=0;i<final_list.size();i++){
            ReservationClass reservationClass = new ReservationClass();
            reservationClass.setTime( final_list.get( i ).getTime() );
            reservationClass.setPeople( final_list.get( i ).getPeople() );
            reservationClass.setUid( final_list.get( i ).getUid() );
            reservationClass.setNowtime( final_list.get( i ).getNowtime() );
            reservationClass.setArriveSec( final_list.get( i ).getArriveSec() );

            if(final_list.get(i).getItemViewType()==1)
                reservationClass.setItemViewBType();
            else if(final_list.get(i).getItemViewType()==0)
                reservationClass.setItemViewAType();

            adapter_reserved.addItem( reservationClass );
        }
        if(adapter_reserved.getItemCount()!=0) Thomeshow.setText("");
        else Thomeshow.setText("내역이 없습니다.");

    }

    @Override
    public void onPause(){
        super.onPause();
        timerTask.cancel();
        timer.cancel();
        mSocket.disconnect();
    }

    @Override
    public void onResume(){
        super.onResume();
        JsonObject prejsonobject = new JsonObject();
        prejsonobject.addProperty("res_id", res_id);
        prejsonobject.addProperty("res_token",userInfoClass.getOwnertoken());
        prejsonobject.addProperty("is_call","true");

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
