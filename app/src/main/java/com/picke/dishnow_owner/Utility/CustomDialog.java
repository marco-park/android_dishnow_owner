package com.picke.dishnow_owner.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.picke.dishnow_owner.R;
import com.picke.dishnow_owner.SigninActivity;


public class CustomDialog{

    private Context mContext;


    int cklogoutok = 0;
    int cklogoutcancle = 0;
    private DialogInterface dlg;

    public CustomDialog(Context context) {
        mContext = context;
    }


    public void setLogoutOk(){
        // if(cklogoutok == 0)
        cklogoutok = 1;
//        else
//            cklogoutok = 0;
    }
    public int getLogoutOk(){
        return cklogoutok;
    }



    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(mContext);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.custom_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Button Bok = (Button) dlg.findViewById(R.id.custom_logout_ok_button);
        final Button Bcancle = (Button) dlg.findViewById(R.id.custom_logout_cancle_button);


        Bok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();

                SharedPreferences auto =  mContext.getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor autologin = auto.edit();
                autologin.clear();
                autologin.commit();

                Intent intent = new Intent(mContext, SigninActivity.class);
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
                // 커스텀 다이얼로그를 종료한다.

            }
        });

        Bcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }

}