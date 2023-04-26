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

import java.util.List;

import util.MyUtil;
import util.ToastUtil;

public class MeLinearRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private int rvId;
    private MeLinearRecycleViewAdapter.OnItemClickListener mlistener;
    private OnDoneItemClickListener doneListener;
    private AppDatabase db;
    private List<Disease> list;

    public interface OnItemClickListener {
        void OnClick(int id);
    }

    public interface OnDoneItemClickListener {
        void On_done_Click(int id);
    }
    public MeLinearRecycleViewAdapter(Context context, OnItemClickListener listener, List<Disease> mList, int mId) {
        this.mContext = context;
        this.mlistener = listener;
        this.list = mList;
        this.rvId = mId;
    }
    public MeLinearRecycleViewAdapter(Context context, OnDoneItemClickListener listener, List<Disease> mList, int mId) {
        this.mContext = context;
        this.doneListener = listener;
        this.list = mList;
        this.rvId = mId;
    }

    class LinearViewHolderMe extends RecyclerView.ViewHolder {
        private TextView mid;
        private TextView place;
        private TextView type;
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
        public TextView getLongitude() {
            return longitude;
        }
        public TextView getLatitude() {
            return latitude;
        }
        public TextView getDate() {
            return date;
        }
        public LinearViewHolderMe(@NonNull View itemView) {
            super(itemView);
            mid = itemView.findViewById(R.id.item_id);
            place = itemView.findViewById(R.id.item_place);
            type = itemView.findViewById(R.id.item_type);
            longitude = itemView.findViewById(R.id.item_longtitude);
            latitude = itemView.findViewById(R.id.item_latitude);
            date = itemView.findViewById(R.id.item_date);
        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MeLinearRecycleViewAdapter.LinearViewHolderMe(LayoutInflater.from(mContext).inflate(R.layout.layout_item_disease_me, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MeLinearRecycleViewAdapter.LinearViewHolderMe mHolder = (LinearViewHolderMe) holder;
        Disease disease = list.get(position);
        mHolder.getMid().setText(disease.getId()+"");
        mHolder.getPlace().setText(disease.getPlace());
        mHolder.getType().setText(MyUtil.ConvertType_toString(disease.getType()));
        mHolder.getLatitude().setText((double) disease.getLatitude() + "");
        mHolder.getLongitude().setText((double) disease.getLongitude() + "");
        mHolder.getDate().setText(disease.getDate().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tvId= (TextView)view.findViewById(R.id.item_id);
                int id = Integer.parseInt(tvId.getText().toString());
                if (rvId == R.id.me_rv_target)
                    mlistener.OnClick(id);
                else
                    doneListener.On_done_Click(id);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list.size() == 0)
        {
            if (rvId == R.id.me_rv_ok)
                ToastUtil.showMsg(mContext, "你没有已经解决的病害");
            else if (rvId == R.id.me_rv_target)
                ToastUtil.showMsg(mContext, "暂无任务");
        }
        return list.size();
    }
}
