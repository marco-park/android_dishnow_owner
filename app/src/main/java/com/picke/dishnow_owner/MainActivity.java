package com.picke.dishnow_owner;

import android.content.Context;
import android.content.Intent;
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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.picke.dishnow_owner.Owner_User.ReservationClass;
import com.picke.dishnow_owner.Owner_User.UserInfoClass;
import com.picke.dishnow_owner.Utility.RecyclerAdapter;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;


public class MainActivity extends AppCompatActivity {

    String feed_url = "http://claor123.cafe24.com/Callout.php";
    private static final String TAG = "claor123";
    public String start;
    boolean Start = false;
    public Intent intent;
    private TextView tv;
    static public Socket mSocket;
    Handler handler = null;
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
    //

    private UserInfoClass userInfoClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.main_show);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

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
        res_id = userInfoClass.getuId();

        try {
            mSocket = IO.socket("http://ec2-18-218-206-167.us-east-2.compute.amazonaws.com:3000");
            mSocket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                JsonObject prejsonobject = new JsonObject();
                prejsonobject.addProperty("res_id",res_id);
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
                runOnUiThread(()->{
                        Intent intent = new Intent(MainActivity.this, PopupCallActivity.class);
                        intent.putExtra("user_numbers", jsonObject.get("user_numbers").toString());
                        intent.putExtra("user_time", jsonObject.get("user_time").toString());
                        intent.putExtra("user_id", jsonObject.get("user_id").toString());
                        startActivity(intent);
                        finish();
                });
            });
            mSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Lwaitmatching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,OnWaitActivity.class);
                startActivity(intent);
                overridePendingTransition(R.xml.anim_slide_in_right, R.xml.anim_slide_out_left);
                finish();
                overridePendingTransition(R.xml.anim_slide_in_left, R.xml.anim_slide_out_right);

            }
        });
        Lmy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.xml.anim_slide_in_right ,R.xml.anim_slide_out_left);
                finish();
                overridePendingTransition(R.xml.anim_slide_in_left, R.xml.anim_slide_out_right);

            }
        });
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
        prejsonobject.addProperty("res_id", res_id);
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
