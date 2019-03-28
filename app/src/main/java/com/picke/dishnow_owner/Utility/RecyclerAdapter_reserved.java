package com.picke.dishnow_owner.Utility;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.picke.dishnow_owner.Owner_User.ReservationArrayClass;
import com.picke.dishnow_owner.Owner_User.ReservationClass;
import com.picke.dishnow_owner.Owner_User.UserInfoClass;
import com.picke.dishnow_owner.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RecyclerAdapter_reserved extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ReservationClass> listData = new ArrayList<>();
    private ArrayList<ReservationClass> reservationClassArrayList;
    private RequestQueue requestQueue;
    private Context context;
    private NotArrivedDialog notArrivedDialog;
    private UserInfoClass userInfoClass;

    private long time;
    private Date date;

    public Context getContext() {
        return context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // set the Context here
        context = parent.getContext();
        reservationClassArrayList = ReservationArrayClass.getInstance( getContext() ).getrfinArray();
        requestQueue = VolleySingleton.getmInstance( getContext() ).getRequestQueue();

        if (viewType == 0) {
            View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.recycle_item_reserved, parent, false );
            return new AHolder( v );
        } else {
            View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.recycle_item_noarrive_reserved, parent, false );
            return new BHolder( v );
        }
        //포지션에 따른 viewtype 지정
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        //늦었다면 바꿔줘야함 setViewBTyoe 호출 ->1로

        userInfoClass = UserInfoClass.getInstance(getContext());

        switch (viewHolder.getItemViewType()) {
            case 0:
                AHolder aHolder = (AHolder) viewHolder;
                long diff =System.currentTimeMillis()/1000-Long.valueOf(listData.get(i).getArriveSec());
                aHolder.onBind(listData.get(i));

                ((AHolder) viewHolder).Aarrive.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        String day;
                        day = GetDay();
                        final StringRequest StringRequest1 = new StringRequest( Request.Method.POST, "http://dishnow.kr/ResHistoryPush.php", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d( "meenseek", error.getLocalizedMessage() );
                            }
                        } ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put( "m_res_id", userInfoClass.getuId() );
                                params.put( "m_user_id",listData.get(i).getUid() );
                                params.put( "m_user_name", "prototype" );
                                params.put("m_res_name", userInfoClass.getResname());
                                params.put("m_res_people", listData.get(i).getPeople());
                                params.put("m_res_date", day);
                                params.put("m_res_time", listData.get(i).getTime());
                                return params;
                            }
                        };
                        reservationClassArrayList.remove(i);
                        requestQueue.add(StringRequest1);
                    }
                });
                break;
            case 1:
                BHolder bHolder = (BHolder) viewHolder;
                bHolder.onBind( listData.get(i));
                ((BHolder) viewHolder).Barrive.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        String day;
                        day = GetDay();
                        final StringRequest StringRequest2 = new StringRequest( Request.Method.POST, "http://dishnow.kr/ResHistoryPush.php", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d( "meenseek", error.getLocalizedMessage() );
                            }
                        } ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put( "m_res_id", userInfoClass.getuId() );
                                params.put( "m_user_id",listData.get(i).getUid() );
                                params.put( "m_user_name", "prototype" );
                                params.put("m_res_name", userInfoClass.getResname());
                                params.put("m_res_people", listData.get(i).getPeople());
                                params.put("m_res_date", day);
                                params.put("m_res_time", listData.get(i).getTime());
                                return params;
                            }
                        };
                        reservationClassArrayList.remove( i );
                        requestQueue.add(StringRequest2);
                    }
                });

                ((BHolder)viewHolder).Bnoarrive.setOnClickListener(new OnSingleClickListener() {
                    private Context mContext;
                    private DialogInterface dlg;

                    @Override
                    public void onSingleClick(View v) {
                        NotArrivedDialog notArrivedDialog  = new NotArrivedDialog( getContext() );
                        notArrivedDialog.setPosition( i );
                        notArrivedDialog.callFunction();
                    }
                });

                break;
            default:
                Log.d("asd","default");
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        return listData.get( position ).getItemViewType();
    }

    //안늦었을때
    public class AHolder extends RecyclerView.ViewHolder {
        private TextView Trestime;
        private TextView Tcurrenttime;
        private TextView Tpeople;
        private TextView Tuid;
        private Button Aarrive;
        private RelativeLayout Llayout;
        private TextView Tprefixtime;

        //안늦었을때
        public AHolder(View view) {
            super( view );
            Trestime = itemView.findViewById( R.id.item_user_arrivetime );
            Tcurrenttime = itemView.findViewById( R.id.item_user_calltime );
            Tpeople = itemView.findViewById( R.id.item_user_number );
            Tuid = itemView.findViewById( R.id.item_userid );
            Aarrive = itemView.findViewById( R.id.recycle_item_reserved_arrive_button );
            Llayout = itemView.findViewById(R.id.reserved_linearlayout);
            Tprefixtime = itemView.findViewById(R.id.item_reserved_prefixtime);
        }

        void onBind(ReservationClass data) {
            String restimehour = data.getTime().substring( 0, 2 );
            String restimemin = data.getTime().substring( 3, 5 );
            String nowtimehour = data.getNowtime().substring( 0, 2 );
            String nowtimemin = data.getNowtime().substring( 3, 5 );
            Trestime.setText( restimehour + "시 " + restimemin + "분" );
            Tcurrenttime.setText( nowtimehour + ":" + nowtimemin );
            Tpeople.setText( data.getPeople() + "명" );
            Tuid.setText( data.getUid() );
        }

        void onBind2(ReservationClass data){
            String restimehour = data.getTime().substring( 0, 2 );
            String restimemin = data.getTime().substring( 3, 5 );
            String nowtimehour = data.getNowtime().substring( 0, 2 );
            String nowtimemin = data.getNowtime().substring( 3, 5 );
            Trestime.setText( restimehour + "시 " + restimemin + "분" );
            Tcurrenttime.setText( nowtimehour + ":" + nowtimemin );
            Tpeople.setText( data.getPeople() + "명" );
            Tuid.setText( data.getUid() );

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Llayout.setBackground(ContextCompat.getDrawable(context,R.drawable.item_nowtime));
                    Aarrive.setBackground(ContextCompat.getDrawable(context,R.drawable.hollow_button_light_bolder));
                }else{
                    Llayout.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.item_nowtime));
                    Aarrive.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.hollow_button_light_bolder));

                }
                Tcurrenttime.setTextColor(ContextCompat.getColor(context,R.color.color_grey));
                Tprefixtime.setTextColor(ContextCompat.getColor(context,R.color.color_grey));

        }
    }

    //요청시간 10분 초과시
    public class BHolder extends RecyclerView.ViewHolder {
        private TextView Trestime;
        private TextView Tcurrenttime;
        private TextView Tpeople;
        private TextView Tuid;
        private Button Barrive;
        private Button Bnoarrive;

        public BHolder(View view) {
            super( view );
            Trestime = itemView.findViewById( R.id.item_user_arrivetime );
            Tcurrenttime = itemView.findViewById( R.id.item_user_calltime );
            Tpeople = itemView.findViewById( R.id.item_user_number );
            Tuid = itemView.findViewById( R.id.item_userid );

            Barrive = itemView.findViewById( R.id.recycle_item_arrive_button );
            Bnoarrive = itemView.findViewById( R.id.recycle_item_noarrive_button );
        }

        void onBind(ReservationClass data) {
            String restimehour = data.getTime().substring( 0, 2 );
            String restimemin = data.getTime().substring( 3, 5 );
            String nowtimehour = data.getNowtime().substring( 0, 2 );
            String nowtimemin = data.getNowtime().substring( 3, 5 );
            String arrivesec = data.getArriveSec();

            Trestime.setText( restimehour + "시 " + restimemin + "분" );
            Tcurrenttime.setText( nowtimehour + ":" + nowtimemin );
            Tpeople.setText( data.getPeople() + "명" );
            Tuid.setText( data.getUid() );
        }

    }

    public void addItem(ReservationClass data) {
        listData.add( data );
    }

    public void removeItem(int i) {
        listData.remove( i );
    }

    public void clearItem() {
        listData.clear();
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    //늦었을때 B로바뀜 도착 A 안도착 B
    public void setItemViewAType(int i) {
        listData.get( i ).setItemViewAType();
    }

    public void setItemViewBType(int i) {
        listData.get( i ).setItemViewBType();
    }


    public class NotArrivedDialog {
        private Context mContext;
        int cklogoutok = 0;
        int position = 0;
        private DialogInterface dlg;

        public void setPosition(int i){position = i;}

        public int getPosition() {
            return position;
        }

        public NotArrivedDialog(Context context) {
            mContext = context;
        }

        public void callFunction() {

            // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
            final Dialog dlg = new Dialog( mContext );

            // 액티비티의 타이틀바를 숨긴다.
            dlg.requestWindowFeature( Window.FEATURE_NO_TITLE );

            // 커스텀 다이얼로그의 레이아웃을 설정한다.
            dlg.setContentView( R.layout.dlg_not_arrived );

            // 커스텀 다이얼로그를 노출한다.
            dlg.show();

            // 커스텀 다이얼로그의 각 위젯들을 정의한다.
            final Button Bok = (Button) dlg.findViewById( R.id.dlg_not_arrived_ok_button);
            final Button Bcancle = (Button) dlg.findViewById( R.id.dlg_not_arrived_cancle_button);

            //미도착했으면 DB에 미도착 내역 저장
            Bok.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final StringRequest StringRequest = new StringRequest( Request.Method.POST, "http://dishnow.kr/NotArrived.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d( "meenseek", error.getLocalizedMessage() );
                        }
                    } ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put( "m_res_id", userInfoClass.getuId() );
                            params.put( "m_user_id",  listData.get(position).getUid());
                            params.put( "m_user_name", "prototype" );
                            return params;
                        }
                    };
                    // 커스텀 다이얼로그를 종료한다.

                    dlg.dismiss();
                    requestQueue.add(StringRequest);
                    reservationClassArrayList.remove( getPosition() );
                }
            } );

            Bcancle.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 커스텀 다이얼로그를 종료한다.
                    dlg.dismiss();
                }
            } );
        }
    }

    public String GetDay(){
        long now = System.currentTimeMillis();
        date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String Sgettime = sdf.format(date);
        String Syear = Sgettime.substring(0,4);
        String Smonth = Sgettime.substring(5,7);
        String Sday = Sgettime.substring(8);

        /*
        String weekDay;
        // SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US); // 특정 언어로 출력하고 싶은 경우
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.KOREA);
        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());


        Sgettime = Smonth + "/" + Sday + " (" + weekDay + ")";
        */
        return Syear+"."+Smonth+"."+Sday;
    }

    public abstract class OnSingleClickListener implements View.OnClickListener{
        private static final long MIN_CLICK_INTERVAL=1000;
        private long mLastClickTime;
        public abstract void onSingleClick(View v);

        @Override
        public final void onClick(View v) {
            long currentClickTime= SystemClock.uptimeMillis();
            long elapsedTime=currentClickTime-mLastClickTime;
            mLastClickTime=currentClickTime;

            // 중복 클릭인 경우
            if(elapsedTime<=MIN_CLICK_INTERVAL){
                return;
            }

            // 중복 클릭아 아니라면 추상함수 호출
            onSingleClick(v);
        }
    }
}