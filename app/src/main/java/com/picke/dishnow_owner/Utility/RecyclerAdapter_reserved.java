package com.picke.dishnow_owner.Utility;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.picke.dishnow_owner.Owner_User.ReservationArrayClass;
import com.picke.dishnow_owner.Owner_User.ReservationClass;
import com.picke.dishnow_owner.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerAdapter_reserved extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ReservationClass> listData = new ArrayList<>();
    private ArrayList<ReservationClass> reservationClassArrayList;
    private RequestQueue requestQueue;
    private Context context;
    private NotArrivedDialog notArrivedDialog;


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

        switch (viewHolder.getItemViewType()) {
            case 0:
                AHolder aHolder = (AHolder) viewHolder;
                aHolder.onBind( listData.get( i ) );
                ((AHolder) viewHolder).Aarrive.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reservationClassArrayList.remove( i );
                    }
                } );
                break;
            case 1:
                BHolder bHolder = (BHolder) viewHolder;
                bHolder.onBind( listData.get( i ) );
                ((BHolder) viewHolder).Barrive.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reservationClassArrayList.remove( i );
                    }
                } );

                ((BHolder)viewHolder).Bnoarrive.setOnClickListener( new View.OnClickListener() {
                    private Context mContext;
                    private DialogInterface dlg;

                    @Override
                    public void onClick(View v) {
                        // 호출할 다이얼로그 함수를 정의한다.
                        NotArrivedDialog notArrivedDialog  = new NotArrivedDialog( getContext() );
                        notArrivedDialog.setPosition( i );
                        notArrivedDialog.callFunction();
                    }
                } );
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

        //안늦었을때
        public AHolder(View view) {
            super( view );
            Trestime = itemView.findViewById( R.id.item_user_arrivetime );
            Tcurrenttime = itemView.findViewById( R.id.item_user_calltime );
            Tpeople = itemView.findViewById( R.id.item_user_number );
            Tuid = itemView.findViewById( R.id.item_userid );
            Aarrive = itemView.findViewById( R.id.recycle_item_reserved_arrive_button );
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
    }

    //요청시간 10분 초과시
    public class BHolder extends RecyclerView.ViewHolder {
        private TextView Trestime;
        private TextView Tcurrenttime;
        private TextView Tpeople;
        private TextView Tuid;
        private TextView Tnoarrive;
        private Button Barrive;
        private Button Bnoarrive;

        public BHolder(View view) {
            super( view );
            Trestime = itemView.findViewById( R.id.item_user_arrivetime );
            Tcurrenttime = itemView.findViewById( R.id.item_user_calltime );
            Tpeople = itemView.findViewById( R.id.item_user_number );
            Tuid = itemView.findViewById( R.id.item_userid );

            Tnoarrive = itemView.findViewById( R.id.recycle_item_noarrive_txtview );
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
            dlg.setContentView( R.layout.custom_dialog );

            // 커스텀 다이얼로그를 노출한다.
            dlg.show();

            // 커스텀 다이얼로그의 각 위젯들을 정의한다.
            final Button Bok = (Button) dlg.findViewById( R.id.custom_logout_ok_button );
            final Button Bcancle = (Button) dlg.findViewById( R.id.custom_logout_cancle_button );
            final TextView TnotArrived = (TextView) dlg.findViewById( R.id.custom_dialog_textView );
            TnotArrived.setText( "미도착 했습니까?" );

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
                            params.put( "m_res_id", listData.get( getPosition()).getUid() );
                            params.put( "m_user_id", "1" );
                            params.put( "m_user_name", "하위" );
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

}
