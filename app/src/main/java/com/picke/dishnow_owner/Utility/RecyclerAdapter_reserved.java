package com.picke.dishnow_owner.Utility;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchUIUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.picke.dishnow_owner.Owner_User.ReservationClass;
import com.picke.dishnow_owner.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerAdapter_reserved extends RecyclerView.Adapter<RecyclerAdapter_reserved.ItemViewHolder>{

    private ArrayList<ReservationClass> listData = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_reserved, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.onBind(listData.get(i));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(ReservationClass data) {
        listData.add(data);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView Trestime;
        private TextView Tcurrenttime;
        private TextView Tpeople;
        private TextView Tuid;


        public ItemViewHolder(View view) {
            super(view);
            Trestime = itemView.findViewById(R.id.item_user_arrivetime);
            Tcurrenttime = itemView.findViewById(R.id.item_user_calltime);
            Tpeople = itemView.findViewById(R.id.item_user_number);
            Tuid = itemView.findViewById(R.id.item_userid);
        }

        void onBind(ReservationClass data) {
            String restimehour = data.getTime().substring(0, 2);
            String restimemin = data.getTime().substring(3, 5);
            String nowtimehour = data.getNowtime().substring(0, 2);
            String nowtimemin = data.getNowtime().substring(3, 5);
            Trestime.setText(restimehour + "시 " + restimemin + "분");
            Tcurrenttime.setText(nowtimehour + ":" + nowtimemin);
            Tpeople.setText(data.getPeople() + "명");
            Tuid.setText(data.getUid());
        }
    }

}
