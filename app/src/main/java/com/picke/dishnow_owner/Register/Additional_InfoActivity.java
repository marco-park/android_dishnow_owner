package com.picke.dishnow_owner.Register;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.picke.dishnow_owner.Owner_User.UserAuthClass;
import com.picke.dishnow_owner.R;

public class Additional_InfoActivity extends AppCompatActivity {

    private UserAuthClass userAuthClass;
    private EditText Eownerbirth;
    private RadioButton Rsex_man;
    private RadioButton Rsex_woman;
    private Button BnextButton;
    private String Ssex="man";
    private TextView Tsex_man;
    private TextView Tsex_woman;
    private RadioGroup radioGroup;
    private boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional__info);

        Eownerbirth = findViewById(R.id.additional_info__ownerbirth);
        Rsex_man = findViewById(R.id.additional_info_sex_man_radiobutton);
        Rsex_woman = findViewById(R.id.additional_info_sex_woman_radiobutton);
        BnextButton = findViewById(R.id.additional_info_next_button);
        radioGroup = findViewById(R.id.additional_info__radiogroup);
        Tsex_man = findViewById(R.id.additional_info__sex_man_text);
        Tsex_woman = findViewById(R.id.additional_info__sex_woman_text);

        Eownerbirth.getBackground().setColorFilter(getResources().getColor(R.color.color_bolder), PorterDuff.Mode.SRC_ATOP);

        Toolbar toolbar = findViewById(R.id.additional_info__toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userAuthClass = UserAuthClass.getInstance(getApplicationContext());

        Drawable image_ok = getApplicationContext().getResources().getDrawable(R.drawable.ic_iconmonstr_check_mark_15);
        image_ok.setBounds(60,0,0,0);
        Drawable image_no = getApplicationContext().getResources().getDrawable(R.drawable.ic_icon_x);
        image_no.setColorFilter(getResources().getColor(R.color.color_red),PorterDuff.Mode.SRC_ATOP);
        image_no.setBounds(60,0,0,0);

        RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = (group, checkedId) -> {
            if(checkedId == R.id.additional_info_sex_man_radiobutton){
                Ssex="man";
                Tsex_man.setTextColor(getResources().getColor(R.color.color_violet));
                Tsex_woman.setTextColor(getResources().getColor(R.color.color_bolder));

            }else{
                Ssex="woman";
                Tsex_man.setTextColor(getResources().getColor(R.color.color_bolder));
                Tsex_woman.setTextColor(getResources().getColor(R.color.color_violet));
            }
        };

        Eownerbirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(Eownerbirth.getText().toString().length()!=8){
                    flag=false;
                    Eownerbirth.getBackground().setColorFilter(getResources().getColor(R.color.color_red),PorterDuff.Mode.SRC_ATOP);
                    Eownerbirth.setCompoundDrawablesWithIntrinsicBounds(null, null, image_no, null);
                }else{
                    Eownerbirth.getBackground().setColorFilter(getResources().getColor(R.color.color_violet),PorterDuff.Mode.SRC_ATOP);
                    Eownerbirth.setCompoundDrawablesWithIntrinsicBounds(null, null, image_ok, null);
                    flag=true;
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        BnextButton.setOnClickListener(v -> {
            if(flag==true) {
                userAuthClass.setOwnersex(Ssex);
                userAuthClass.setOwnerbirth(Eownerbirth.getText().toString());
                startActivity(new Intent(Additional_InfoActivity.this, VerificationActivity.class));
            }
        });
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
