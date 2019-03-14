package com.picke.dishnow_owner.Owner_User;

public class ReservationClass {

    public ReservationClass(){
        this.itemViewType = 0;
    }

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

    public void setItemViewAType() {
        this.itemViewType = 0;
    }
    //늦음
    public void setItemViewBType()
    {
        this.itemViewType = 1;
    }

    public int getItemViewType() {
        return itemViewType;
    }

    private String time;
    private String people;
    private String uid;
    private String nowtime;
    private String username;
    private int itemViewType;
    private String arriveSec;
    private long nowsecond;
    private String date;

    public void setDate(String date){this.date = date;}

    public String getDate(){return date;}

    public long getNowsecond() {
        return nowsecond;
    }

    public void setArriveSec(String arriveSec){this.arriveSec = arriveSec;}

    public String getArriveSec(){return arriveSec;}

    public void setUsername(String username){this.username = username;}

    public String getUsername(){return username;}

    public void setNowsecond(long nowsecond) {
        this.nowsecond = nowsecond;
    }

}
