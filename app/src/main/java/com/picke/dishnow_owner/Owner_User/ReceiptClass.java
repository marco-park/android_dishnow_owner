package com.picke.dishnow_owner.Owner_User;

import android.content.Context;

import java.util.ArrayList;

public class ReceiptClass {

    private static ReceiptClass reciptClass = null;
    private ArrayList<ReservationClass> ReciptArray;

    public ReceiptClass(Context context){
        ReciptArray = new ArrayList<>();
    }

    public static ReceiptClass getInstance(Context context){
        if(reciptClass==null){
            reciptClass = new ReceiptClass(context);
        }
        return reciptClass;
    }

    public ArrayList<ReservationClass> getresArray(){
        return ReciptArray;
    }

    public void res_add(ReservationClass reservationClass){
        ReciptArray.add(reservationClass);
    }

    public void res_delete(String uid){
        for(int i=0;i<ReciptArray.size();i++){
            if(uid.equals(ReciptArray.get(i).getUid())){
                ReciptArray.remove(i);
                break;
            }
        }
    }

    public ReservationClass get_resclass(String uid){
        for(int i=0;i<ReciptArray.size();i++){
            if(uid.equals(ReciptArray.get(i).getUid())){
                return ReciptArray.get(i);
            }
        }
        return null;
    }
}