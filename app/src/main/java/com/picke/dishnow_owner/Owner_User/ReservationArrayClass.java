package com.picke.dishnow_owner.Owner_User;

import android.content.Context;

import java.util.ArrayList;

public class ReservationArrayClass {
    private static ReservationArrayClass reservationArrayClass = null;
    private ArrayList<ReservationClass> ReservationArray;   // 수락 누른 사용자들
    private ArrayList<ReservationClass> FinishedArray;      // 예약 성사된 사용자들

    public ReservationArrayClass(Context context){
        ReservationArray = new ArrayList<>();
        FinishedArray = new ArrayList<>();
    }

    public static ReservationArrayClass getInstance(Context context){
        if(reservationArrayClass==null){
            reservationArrayClass = new ReservationArrayClass(context);
        }
        return reservationArrayClass;
    }

   public ArrayList<ReservationClass> getresArray(){
        return ReservationArray;
   }
   public ArrayList<ReservationClass> getrfinArray() { return FinishedArray;}

   public void res_add(ReservationClass reservationClass){
        ReservationArray.add(reservationClass);
   }

   public void res_delete(String uid){
        for(int i=0;i<ReservationArray.size();i++){
            if(uid.equals(ReservationArray.get(i).getUid())){
                ReservationArray.remove(i);
                break;
            }
        }
   }

   public ReservationClass get_resclass(String uid){
        for(int i=0;i<ReservationArray.size();i++){
            if(uid.equals(ReservationArray.get(i).getUid())){
                return ReservationArray.get(i);
            }
        }
        return null;
   }

   public void fin_add(ReservationClass reservationClass){
        FinishedArray.add(reservationClass);
   }

   public void fin_delete(String uid){
        for(int i=0;i<ReservationArray.size();i++){
            if(uid.equals(ReservationArray.get(i).getUid())){
                ReservationArray.remove(i);
                break;
            }
        }
   }

   public ReservationClass get_finclass(String uid){
        for(int i=0;i<FinishedArray.size();i++){
            if(uid.equals(FinishedArray.get(i).getUid())){
                return FinishedArray.get(i);
            }
        }
        return null;
   }
}
