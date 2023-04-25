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
import com.li.gddisease.pojo.DiseaseReturnPojo;

import org.w3c.dom.Text;

import java.util.List;

import util.MyUtil;
import util.ToastUtil;


public class LinearRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private OnItemClickListener mlistener;
    private AppDatabase db;
    private List<DiseaseReturnPojo> list;

    public interface OnItemClickListener {
        void OnClick(int pos);
    }

    public LinearRecycleViewAdapter(Context context, OnItemClickListener listener, List<DiseaseReturnPojo> mList) {
        this.mContext = context;
        this.mlistener = listener;
        this.list = mList;
    }

    class LinearViewHolder extends  RecyclerView.ViewHolder {
        private TextView mid;
        private TextView place;
        private TextView type;
        private TextView status;
        private TextView longitude;
        private TextView latitude;
        private TextView date;

        public TextView getMid() {
            return mid;
        }
        public TextView getPlace() {
            return place;
        }
        public TextView getType() {
            return type;
        }
        public TextView getStatus() {
            return status;
        }
        public TextView getLongitude() {
            return longitude;
        }
        public TextView getLatitude() {
            return latitude;
        }
        public TextView getDate() {
            return date;
        }
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            mid = itemView.findViewById(R.id.item_id);
            place = itemView.findViewById(R.id.item_place);
            type = itemView.findViewById(R.id.item_type);
            status = itemView.findViewById(R.id.item_status);
            longitude = itemView.findViewById(R.id.item_longtitude);
            latitude = itemView.findViewById(R.id.item_latitude);
            date = itemView.findViewById(R.id.item_date);
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
        DiseaseReturnPojo disease = list.get(position);
        mHolder.getMid().setText(disease.getId()+"");
        mHolder.getPlace().setText(disease.getPlace());
        mHolder.getType().setText(MyUtil.ConvertType_toString(disease.getType()));
        mHolder.getLatitude().setText((double) disease.getLatitude() + "");
        mHolder.getLongitude().setText((double) disease.getLongitude() + "");
        mHolder.getStatus().setText(MyUtil.convertStatus_toString(disease.getStatus()));
        mHolder.getDate().setText(disease.getDate().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.OnClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list.size() == 0)
            ToastUtil.showMsg(mContext, "没有数据");
        return list.size();
    }
}
