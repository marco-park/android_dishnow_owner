package com.picke.dishnow_owner.Utility;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.picke.dishnow_owner.Owner_User.UserInfoClass;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketService extends Service {
    private Socket mSocket;
    private String res_id;

    public void connectSocket(){
        res_id = UserInfoClass.getInstance(getApplicationContext()).getuId();
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
                JsonParser jsonParsers = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParsers.parse(objects[0].toString());

                });

            mSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroy() {
        mSocket.disconnect();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
