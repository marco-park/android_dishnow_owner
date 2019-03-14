package com.picke.dishnow_owner.Register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.picke.dishnow_owner.Owner_User.UserAuthClass;
import com.picke.dishnow_owner.R;
import com.picke.dishnow_owner.SigninActivity;

public class FindemailActivity extends AppCompatActivity {

    private Button Bgobackbutton;
    private TextView Townername;
    private TextView Towneremail;
    private UserAuthClass userAuthClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findemail);

        Bgobackbutton = findViewById(R.id.findemail_gobackbutton);
        Towneremail = findViewById(R.id.findemail_auth_email);
        Townername = findViewById(R.id.findemail_auth_name);

        userAuthClass = UserAuthClass.getInstance(getApplicationContext());

        Townername.setText(userAuthClass.getOwnername());
        Towneremail.setText(userAuthClass.getOwnerid());

        Bgobackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindemailActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });
    }
}