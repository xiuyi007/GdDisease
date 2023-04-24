package com.li.gddisease.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.li.gddisease.AppDatabase;
import com.li.gddisease.R;
import com.li.gddisease.entity.Disease;

import java.util.List;


public class LinearRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private OnItemClickListener mlistener;
    private AppDatabase db;
    private List<Disease> list;

    public interface OnItemClickListener {
        void OnClick(int pos);
    }

    public LinearRecycleViewAdapter(Context context, OnItemClickListener listener, List<Disease> mList) {
        this.mContext = context;
        this.mlistener = listener;
        this.list = mList;
    }

    class LinearViewHolder extends  RecyclerView.ViewHolder {
        private TextView id;
        private TextView place;
        private TextView type;
        private TextView status;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.item_id);
            place = itemView.findViewById(R.id.item_place);
            type = itemView.findViewById(R.id.item_type);
            status = itemView.findViewById(R.id.item_status);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_disease, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LinearViewHolder mHolder = (LinearViewHolder) holder;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mlistener.OnClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        db = AppDatabase.getInstance(mContext.getApplicationContext());
        return db.userDao().getALL().size();
    }
}
