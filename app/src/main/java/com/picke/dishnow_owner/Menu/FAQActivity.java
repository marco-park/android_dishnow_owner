package com.picke.dishnow_owner.Menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.widget.TextView;

import com.picke.dishnow_owner.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FAQActivity extends AppCompatActivity {
    private TextView Tkakaolink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_faq );

        Tkakaolink = findViewById( R.id.faq_kakao_txtview );

        Toolbar toolbar = findViewById(R.id.faq_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Linkify.TransformFilter mTransform = (match, url) -> "";
        Pattern pattern = Pattern.compile("클릭");
        Linkify.addLinks(Tkakaolink, pattern, "http://pf.kakao.com/_exdxdvj",null,mTransform);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}