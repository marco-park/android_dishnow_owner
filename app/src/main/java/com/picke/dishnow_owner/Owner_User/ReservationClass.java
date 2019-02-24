package com.picke.dishnow_owner.Owner_User;

public class ReservationClass {
    public String getTime() {
        return time;
    }

    public String getPeople() {
        return people;
    }

    public String getUid() {
        return uid;
    }

    public String getNowtime() {
        return nowtime;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setNowtime(String nowtime) {
        this.nowtime = nowtime;
    }

    private String time;
    private String people;
    private String uid;
    private String nowtime;
}
