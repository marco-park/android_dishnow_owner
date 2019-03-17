package com.picke.dishnow_owner.Utility;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.picke.dishnow_owner.Owner_User.ReceiptClass;
import com.picke.dishnow_owner.Owner_User.ReservationClass;
import com.picke.dishnow_owner.R;

import java.util.ArrayList;

public class RecyclerAdapter_receipt extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ReceiptClass> listData = new ArrayList<>();

    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case 0:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_receipt, parent, false);
                return new ItemViewHolder(view1);

            case 1:
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_receipt_date,parent,false);
                return new textViewHolder(view2);

                default:return null;
        }
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case 0:
                ItemViewHolder holder1 = (ItemViewHolder)holder;
                holder1.onBind(listData.get(position));
                break;

            case 1:
                textViewHolder holder2 = (textViewHolder)holder;
                holder2.onBind(listData.get(position));
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.valueOf(listData.get( position ).getFlag());
    }

    public int getItemCount() {
        return listData.size();
    }

    public void addItem(ReceiptClass data) {
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView Trestime;
        private TextView Tpeople;
        private TextView Tuid;

        ItemViewHolder(View itemView) {
            super(itemView);
            Trestime = itemView.findViewById(R.id.recycle_receipt_userarrivetime);
            Tpeople = itemView.findViewById(R.id.recycle_receipt_usernumber);
            Tuid = itemView.findViewById(R.id.recycle_receipt_userid);
        }

        void onBind(ReceiptClass data) {
            Tuid.setText(data.getUser_id());
            Tpeople.setText(data.getRes_people()+"명");
            Trestime.setText(data.getRes_time().substring(0,2)+"시 "+data.getRes_time().substring(3,5)+"분");
        }
    }
    class textViewHolder extends RecyclerView.ViewHolder{
        private TextView Tdate;

        public textViewHolder(@NonNull View itemView) {
            super(itemView);
            Tdate = itemView.findViewById(R.id.receipt_date_txtview);
        }
        void onBind(ReceiptClass data) {
            Tdate.setText(data.getRes_date());

        }
    }
}