package com.li.gddisease.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.li.gddisease.AppDatabase;
import com.li.gddisease.LoginActivity;
import com.li.gddisease.MainActivity;
import com.li.gddisease.R;
import com.li.gddisease.dao.DiseaseDao;
import com.li.gddisease.dao.HandleDao;
import com.li.gddisease.dto.DiseaseChosenDto;
import com.li.gddisease.dto.UserDiseaseDto;
import com.li.gddisease.entity.Disease;
import com.li.gddisease.entity.Handle;
import com.li.gddisease.pojo.DiseaseReturnPojo;
import com.li.gddisease.ui.account.AccountActivity;
import com.li.gddisease.ui.adapter.LinearRecycleViewAdapter;
import com.li.gddisease.ui.adapter.MeLinearRecycleViewAdapter;
import com.li.gddisease.ui.message.MessageActivity;
import com.li.gddisease.ui.setting.SettingActivity;

import org.w3c.dom.Text;

import java.util.List;

import util.SqlHelper;
import util.ToastUtil;

public class MeFragment extends Fragment implements MeLinearRecycleViewAdapter.OnItemClickListener, MeLinearRecycleViewAdapter.OnDoneItemClickListener {
    private RecyclerView mRv;
    private AppDatabase db;
    private DiseaseDao diseaseDao;
    private List<Disease> list_target;
    private List<Disease> list_done;
    private TextView tv_me_head;
    private TextView tv_me_message;
    private TextView tv_me_setting;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_me_fragment, container, false);
        return view;
    }

    public void refresh()
    {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_view, new MeFragment());
        transaction.commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        getDb();
        getDao();
        setName(view);
        setData(1);
        setData(2);
        setRecycleView(view, list_target, R.id.me_rv_target);
        setRecycleView(view, list_done, R.id.me_rv_ok);

    }

    private void init(View view)
    {
        tv_me_head = view.findViewById(R.id.me_head);
        tv_me_message = view.findViewById(R.id.me_message);
        tv_me_setting = view.findViewById(R.id.me_setting);
        setListener();
    }

    private void setListener()
    {
        tv_me_setting.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SettingActivity.class);
            startActivity(intent);
        });
        tv_me_message.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MessageActivity.class);
            startActivity(intent);
        });
        tv_me_head.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AccountActivity.class);
            startActivity(intent);
        });
    }

    private void setName(View view)
    {
        tv_me_head.setText(db.userDao().getUserById(getUserId()).getUsername());
    }

    //1获取处理中的数据，2获取处理完的数据
    private void setData(int status)
    {

        UserDiseaseDto dto = new UserDiseaseDto();
        dto.setStatus(status);
        dto.setUserId(getUserId());
        if (status == 1)
        {
            list_target = diseaseDao.getDisease_user_status(dto.getUserId(),dto.getStatus());
        }
        else if(status == 2)
        {
            list_done = diseaseDao.getDisease_user_status(dto.getUserId(),dto.getStatus());
        }
    }

    private void setRecycleView(View view, List<Disease> list, int id)
    {
        mRv = view.findViewById(id);
        mRv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        if (id == R.id.me_rv_target)
            mRv.setAdapter(new MeLinearRecycleViewAdapter(getContext(), (MeLinearRecycleViewAdapter.OnItemClickListener) this, list, id));
        else
            mRv.setAdapter(new MeLinearRecycleViewAdapter(getContext(), (MeLinearRecycleViewAdapter.OnDoneItemClickListener) this, list, id));
    }

    private void getDb()
    {
        db = AppDatabase.getInstance(getActivity());
    }

    private void getDao()
    {
        diseaseDao = db.diseaseDao();
    }


    //图速度，有问题的话再改回调吧
    private int getUserId()
    {
        MainActivity activity = (MainActivity)getActivity();
        return activity.getUserId();
    }
    private void setDiseaseTarget(int id)
    {
        HandleDao handleDao = db.handleDao();
        handleDao.updateDiseasesStatus(id, 1);
    }

    private void setDiseaseDone(int id)
    {
        HandleDao handleDao = db.handleDao();
        handleDao.updateDiseasesStatus(id, 2);
    }
    /*
        id是点击的病害的id号,数据库中的。
     */
    @Override
    public void OnClick(int id) {
        String[] choice = new String[] {"标记为已处理", "修改"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("选择你要进行的操作").setIcon(R.drawable.rabbit)
                .setSingleChoiceItems(choice, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0)
                        {
                            setDiseaseDone(id);
                            dialog.dismiss();
                        }
                        refresh();
                    }
                }).setCancelable(true).show();
//        onViewCreated();
    }

    @Override
    public void On_done_Click(int id) {
        String[] choice = new String[] {"标记为处理中"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("请确认").setIcon(R.drawable.rabbit)
                .setSingleChoiceItems(choice, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setDiseaseTarget(id);
                        dialog.dismiss();
                        refresh();
                    }
                }).setCancelable(true).show();
    }
}
