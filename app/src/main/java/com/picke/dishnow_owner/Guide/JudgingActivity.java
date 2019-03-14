package com.picke.dishnow_owner.Guide;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.picke.dishnow_owner.Owner_User.UserInfoClass;
import com.picke.dishnow_owner.R;
import com.picke.dishnow_owner.SigninActivity;
import com.picke.dishnow_owner.Utility.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class JudgingActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private UserInfoClass userInfoClass;
    final String feed_url = "http://claor123.cafe24.com/Judging.php";
    private Button Bokaybutton;
    private String res_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judging);

        requestQueue = VolleySingleton.getmInstance(getApplicationContext()).getRequestQueue();
        userInfoClass = UserInfoClass.getInstance(getApplicationContext());

        Bokaybutton = findViewById(R.id.judging_okaybutton);
        SharedPreferences shared_id = getSharedPreferences("shared_id", Activity.MODE_PRIVATE);
        res_id = shared_id.getString("id",null);

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, feed_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("m_id",res_id);
                return params;
            }
        };

        requestQueue.add(stringRequest);

        Bokaybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences auto =  getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor autologin = auto.edit();
                autologin.clear();
                autologin.commit();
                Intent intent = new Intent(JudgingActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
