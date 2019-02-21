package com.picke.dishnow_owner.Utility;
import android.content.Context;
import android.support.annotation.NonNull;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

import com.picke.dishnow_owner.Owner_User.ReservationArrayClass;
import com.picke.dishnow_owner.Owner_User.ReservationClass;
import com.picke.dishnow_owner.R;

        import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    private ArrayList<ReservationClass> listData = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
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

        ItemViewHolder(View itemView) {
            super(itemView);
            Trestime = itemView.findViewById(R.id.item_user_arrivetime);
            Tcurrenttime = itemView.findViewById(R.id.item_user_calltime);
            Tpeople = itemView.findViewById(R.id.item_user_number);
            Tuid = itemView.findViewById(R.id.item_userid);
        }

        void onBind(ReservationClass data) {
           Trestime.setText(data.getTime());
           Tcurrenttime.setText(data.getNowtime());
           Tpeople.setText(data.getPeople());
           Tuid.setText(data.getUid());
        }
    }
}