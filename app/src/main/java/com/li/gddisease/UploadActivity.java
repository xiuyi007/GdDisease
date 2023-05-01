package com.li.gddisease;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.li.gddisease.common.baseActivity;
import com.li.gddisease.entity.Disease;

import java.sql.Date;
import java.text.SimpleDateFormat;

import util.MyUtil;
import util.ToastUtil;

public class UploadActivity extends baseActivity {
    private EditText mEtLongitude;
    private EditText mEtLatitude;
    private EditText mEtDescription;
    private RadioGroup radioGroup;
    private RadioGroup radioGroup_place;
    private Button mBtnGo;
    private Disease disease;
    private String place;
    private int type;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        getDb();
        init();

    }

    public boolean validateInput()
    {
        String s = mEtLatitude.getText().toString();
        String s1 = mEtLongitude.getText().toString();
        if (s.isEmpty() || s1.isEmpty())
            return false;
        return true;
    }
    public void init()
    {
        mBtnGo = findViewById(R.id.button_upload);
        mEtLongitude = findViewById(R.id.edit_text_disease_longitude);
        mEtLatitude = findViewById(R.id.edit_text_disease_latitude);
        mEtDescription = findViewById(R.id.edit_text_disease_description);
        radioGroup_place = findViewById(R.id.radioGroup_place);
        radioGroup = findViewById(R.id.radioGroup);
        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateInput())
                {
                    ToastUtil.showMsg(UploadActivity.this, "请填写经纬度");
                    return;
                }
                makeData();
                Long aLong = db.diseaseDao().insertDisease(disease);
                if (aLong > 0)
                {
                    ToastUtil.showMsg(UploadActivity.this, "插入成功");

                }
                else
                {
                    ToastUtil.showMsg(UploadActivity.this, "插入失败");
                }
                Intent intent = new Intent(UploadActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        radioGroup_place.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                RadioButton rb = radioGroup_place.findViewById(i);
                place = rb.getText().toString();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = radioGroup.findViewById(i);
                String s = rb.getText().toString();
                type = MyUtil.ConvertType_toInt(s);
            }
        });


    }

    private void getDb()
    {
        db = AppDatabase.getInstance(getApplication());
    }

    public void makeData()
    {
        Date date = MyUtil.getDate();
        disease = new Disease();

        disease.setLatitude(Double.parseDouble(mEtLatitude.getText().toString()));
        disease.setLongitude(Double.parseDouble(mEtLongitude.getText().toString()));
        disease.setPlace(place);
        disease.setType(type);
        disease.setDescription(mEtDescription.getText().toString());
        disease.setDate(date);
        db.diseaseDao().insertDiseases(disease);
    }
}