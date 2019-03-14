package com.picke.dishnow_owner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.picke.dishnow_owner.Guide.JudgingActivity;
import com.picke.dishnow_owner.Guide.RegisterGuideActivity;
import com.picke.dishnow_owner.Guide.TermsActivity;
import com.picke.dishnow_owner.Owner_User.UserAuthClass;
import com.picke.dishnow_owner.Owner_User.UserInfoClass;
import com.picke.dishnow_owner.Register.FindidpassActivity;
import com.picke.dishnow_owner.Utility.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class SigninActivity extends AppCompatActivity {

    private Button signupbutton;
    private Button signinbutton;
    private Button Bfindidpassword;
    private EditText Eidinput;
    private EditText Epasswordinput;
    private TextView wronginput;

    private String idinput;
    private String passwordinput;

    final int PERMISSION = 1;

    private RequestQueue requestQueue;
    private UserAuthClass userAuthClass;
    private UserInfoClass userInfoClass;

    private final String login_url = "https://claor123.cafe24.com/Login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        requestQueue = VolleySingleton.getmInstance(getApplicationContext()).getRequestQueue();
        userAuthClass = UserAuthClass.getInstance(getApplicationContext());
        userInfoClass = UserInfoClass.getInstance(getApplicationContext());

        //  TODO: Permission Check
        if(Build.VERSION.SDK_INT>=23&&ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_NUMBERS)
                !=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)
                !=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS)
                !=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)
                !=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this,Manifest.permission.WAKE_LOCK)
                !=PackageManager.PERMISSION_GRANTED
                )
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WAKE_LOCK
            }, PERMISSION);
        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(
                SigninActivity.this, instanceIdResult -> {
                    String newToken = instanceIdResult.getToken();
                    userAuthClass.setOwnertoken(newToken);
                    userInfoClass.setOwnertoken(newToken);
                });

        SharedPreferences auto =  getSharedPreferences("auto",Activity.MODE_PRIVATE);
        SharedPreferences shared_id = getSharedPreferences("shared_id",Activity.MODE_PRIVATE);
        String loginid,loginpassword,id,name,resauth;
        loginid = auto.getString("o_id",null);
        loginpassword = auto.getString("o_password",null);

        final StringRequest StringRequest2 = new StringRequest(Request.Method.POST, login_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                String resauth1 = jsonObject.getString("owner_resauth");
                String id1 = jsonObject.getString("uid");
                String resname = jsonObject.getString("res_name");

                if(success==true){
                    userAuthClass.setOwnerid(Eidinput.getText().toString());
                    userAuthClass.setOwnerpassword(Epasswordinput.getText().toString());
                    userAuthClass.setUid(id1);
                    userInfoClass.setuId(id1);
                    userInfoClass.setResname(resname);

                    Intent intent2 = new Intent(SigninActivity.this, HomeActivity.class);
                    Intent intent1 = new Intent(SigninActivity.this, JudgingActivity.class);
                    Intent intent0 = new Intent(SigninActivity.this, RegisterGuideActivity.class);

                    if(resauth1.equals("2")){
                        SharedPreferences.Editor autologin = auto.edit();
                        autologin.putString("o_resauth", resauth1);
                        autologin.commit();
                        startActivity(intent2);
                        finish();
                    }
                    else if(resauth1.equals("1")){
                        SharedPreferences.Editor autologin = auto.edit();
                        autologin.putString("o_resauth", resauth1);
                        autologin.commit();
                        startActivity(intent1);
                        finish();
                    }
                    else{
                        SharedPreferences.Editor autologin = auto.edit();
                        autologin.putString("o_resauth", resauth1);
                        autologin.commit();
                        startActivity(intent0);
                        finish();
                    }
                }else{
                    wronginput.setText("이메일 또는 비밀번호가 일치하지 않습니다.");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("spark123","errorj");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("spark123",error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("m_id",loginid);
                params.put("m_password",loginpassword);
                return params;
            }
        };

        if(loginid!=null&&loginpassword!=null){
           requestQueue.add(StringRequest2);        //TODO: auto login
        }

        signupbutton = findViewById(R.id.signin_signupButton);
        signinbutton = findViewById(R.id.signin_loginButton);
        Eidinput = findViewById(R.id.signin_idinput);
        Epasswordinput = findViewById(R.id.signin_passwordinput);
        wronginput = findViewById(R.id.signin_wronginput);
        Epasswordinput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        Bfindidpassword = findViewById(R.id.signin_findidButton);
        Epasswordinput.setTransformationMethod(PasswordTransformationMethod.getInstance());
        signupbutton.setText(Html.fromHtml("<u>"+"새로운 계정 만들기"+"</u>"));

        Eidinput.getBackground().setColorFilter(getResources().getColor(R.color.color_white),PorterDuff.Mode.SRC_ATOP);
        Epasswordinput.getBackground().setColorFilter(getResources().getColor(R.color.color_white),PorterDuff.Mode.SRC_ATOP);

        final StringRequest StringRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String resauth = jsonObject.getString("owner_resauth");
                    String id = jsonObject.getString("uid");
                    String resname = jsonObject.getString("res_name");

                    if(success==true){
                        userAuthClass.setOwnerid(Eidinput.getText().toString());
                        userAuthClass.setOwnerpassword(Epasswordinput.getText().toString());
                        userAuthClass.setUid(id);
                        userInfoClass.setuId(id);
                        userInfoClass.setResname(resname);
                        Intent intent2 = new Intent(SigninActivity.this, HomeActivity.class);
                        Intent intent1 = new Intent(SigninActivity.this,JudgingActivity.class);
                        Intent intent0 = new Intent(SigninActivity.this,RegisterGuideActivity.class);

                        SharedPreferences.Editor uid = shared_id.edit();
                        uid.putString("id",userAuthClass.getUid());
                        uid.commit();
                        SharedPreferences.Editor autologin = auto.edit();
                        autologin.putString("o_resauth", resauth);
                        autologin.putString("o_id", idinput);
                        autologin.putString("o_password", passwordinput);
                        autologin.commit();

                        if(resauth.equals("2")){
                            autologin.putString("o_resauth",resauth);
                            autologin.commit();
                            startActivity(intent2);
                            finish();
                        }
                        else if(resauth.equals("1")){
                            autologin.putString("o_resauth",resauth);
                            autologin.commit();
                            startActivity(intent1);
                            finish();
                        }
                        else{
                            autologin.putString("o_resauth",resauth);
                            autologin.commit();
                            startActivity(intent0);
                            finish();
                        }

                    }else{
                        wronginput.setText("이메일 또는 비밀번호가 일치하지 않습니다.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("spark123","errorj");
                }
            }
        }, error -> Log.d("spark123",error.getLocalizedMessage())) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("m_id",idinput);
                params.put("m_password",passwordinput);
                return params;
            }
        };

        signinbutton.setOnClickListener(v -> {
            idinput = Eidinput.getText().toString();
            passwordinput = Epasswordinput.getText().toString();
            requestQueue.add(StringRequest);
        });

        Bfindidpassword.setOnClickListener(v -> {
            startActivity(new Intent(SigninActivity.this, FindidpassActivity.class));
            finish();
        });

        signupbutton.setOnClickListener(v -> {
            userAuthClass.setOwnerid(Eidinput.getText().toString());
            userAuthClass.setOwnerpassword(Epasswordinput.getText().toString());
            Intent intent = new Intent(SigninActivity.this, TermsActivity.class);
            startActivity(intent);
        });
    }
}
