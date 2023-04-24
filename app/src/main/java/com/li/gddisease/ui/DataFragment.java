package com.li.gddisease.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.li.gddisease.AppDatabase;
import com.li.gddisease.MainActivity;
import com.li.gddisease.R;
import com.li.gddisease.dto.DiseaseChosenDto;
import com.li.gddisease.entity.Disease;
import com.li.gddisease.entity.User;
import com.li.gddisease.ui.adapter.DatePickerFragment;
import com.li.gddisease.ui.adapter.LinearRecycleViewAdapter;

import java.util.List;

import util.SqlHelper;
import util.ToastUtil;

public class DataFragment extends Fragment implements AdapterView.OnItemSelectedListener, DatePickerFragment.DateListener {
    private Spinner place_spinner;
    private Spinner type_spinner;
    private Spinner status_spinner;
    private AppCompatButton mBtnDate;
    private DiseaseChosenDto disease;
    private AppDatabase db;
    private RecyclerView mRv;
    private List<Disease> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_data_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        mRv = view.findViewById(R.id.rv_main);
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mRv.setAdapter(new LinearRecycleViewAdapter(getContext(), new LinearRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void OnClick(int pos) {
                Toast.makeText(getContext(), "click" + pos, Toast.LENGTH_SHORT).show();
            }
        }, list));
    }


    private void init(View view) {
        mBtnDate = view.findViewById(R.id.btn_date);
        mBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getParentFragmentManager(), "DatePicker");
            }
        });
        place_spinner = view.findViewById(R.id.place_choose);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.place_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        place_spinner.setAdapter(adapter);

        type_spinner = view.findViewById(R.id.type_choose);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        type_spinner.setAdapter(adapter2);

        status_spinner = view.findViewById(R.id.status_choose);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(),
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        status_spinner.setAdapter(adapter3);
        place_spinner.setTag("place");
        status_spinner.setTag("status");
        type_spinner.setTag("type");
        place_spinner.setOnItemSelectedListener(DataFragment.this);
        status_spinner.setOnItemSelectedListener(DataFragment.this);
        type_spinner.setOnItemSelectedListener(DataFragment.this);
        db = AppDatabase.getInstance(getActivity());

        StringBuilder s = new StringBuilder("select * from Disease");
        disease = new DiseaseChosenDto();
        String sql = SqlHelper.disease_sql(disease);
        SupportSQLiteQuery sqLiteQuery = new SimpleSQLiteQuery(sql);

/*        if (all.getValue() != null && !all.getValue().isEmpty())
            for (User user : all.getValue()) {
                Log.d("user", user.getUsername());
            }
        else
            ToastUtil.showMsg(getActivity(), "no info");*/
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (disease == null)
            disease = new DiseaseChosenDto("鄞州区", 0, 0);
        else
        {
            switch (parent.getTag().toString())
            {
                case "place":
                    disease.setPlace(parent.getItemAtPosition(position).toString());
                    break;
                case "type":
                    int tmp = 0;
                    switch (parent.getItemAtPosition(position).toString())
                    {
                        case "线裂":
                            tmp = 1;
                            break;
                        case "坑洞":
                            tmp = 2;
                            break;
                        case "跳车":
                            tmp = 3;
                            break;
                        case "错台":
                            tmp = 4;
                            break;
                        case "沉陷":
                            tmp = 5;
                            break;
                    }
                    disease.setType(tmp);
                case "status":
                    int status = 0;
                    switch (parent.getItemAtPosition(position).toString())
                    {
                        case "修复":
                            status = 1;
                            break;
                        case "未修复":
                            status = 2;
                            break;
                    }
                    disease.setStatus(status);
            }

            Log.d("mDisease", disease.toString());

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void click(String s) {
        mBtnDate.setText(s);
    }


}
