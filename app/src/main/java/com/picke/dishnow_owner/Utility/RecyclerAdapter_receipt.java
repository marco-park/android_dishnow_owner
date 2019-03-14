package com.picke.dishnow_owner.Utility;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.picke.dishnow_owner.Owner_User.ReservationClass;
import com.picke.dishnow_owner.R;

import java.util.ArrayList;

public class RecyclerAdapter_receipt extends RecyclerView.Adapter<RecyclerAdapter_receipt.ItemViewHolder> {

    private ArrayList<ReservationClass> listData = new ArrayList<>();

    @NonNull
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_receipt, parent, false);
        return new ItemViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    public int getItemCount() {
        return listData.size();
    }

    public void addItem(ReservationClass data) {
        listData.add(data);
    }

    public void removeItem(int i){listData.remove(i);}

    public void clearItem(){
        listData.clear();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView Trestime;
        private TextView Tpeople;
        private TextView Tuid;
        private TextView Tday;

        ItemViewHolder(View itemView) {
            super(itemView);
            Trestime = itemView.findViewById(R.id.recycle_receipt_userarrivetime);
            Tpeople = itemView.findViewById(R.id.recycle_receipt_usernumber);
            Tuid = itemView.findViewById(R.id.recycle_receipt_userid);
            Tday = itemView.findViewById(R.id.recycle_receipt_userarriveday);
        }

        void onBind(ReservationClass data) {
            String restimehour = data.getTime().substring(0,2);
            //String restimemin = data.getTime().substring(3,5);
            Trestime.setText(restimehour);
            Tday.setText(data.getDate());
            Tpeople.setText(data.getPeople()+"명");
            Tuid.setText(data.getUid());
        }
    }
}