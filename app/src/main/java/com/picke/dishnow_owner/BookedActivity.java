package com.picke.dishnow_owner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BookedActivity extends AppCompatActivity {

    private Button Bconfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked);

        Bconfirm = findViewById(R.id.booked_confirmbutton);
        Bconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookedActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
}
