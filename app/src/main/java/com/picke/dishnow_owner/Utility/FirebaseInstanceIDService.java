package com.picke.dishnow_owner.Utility;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService{
    private static final String TAG = "MyFirebaseInstanceIDService";

    @SuppressLint("LongLogTag")
    @Override
    public void onTokenRefresh(){
        String refreshdToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"Refresh_Token"+refreshdToken);
        sendRegistrationToServer(refreshdToken);
    }

    private void sendRegistrationToServer(String token){

    }
}
