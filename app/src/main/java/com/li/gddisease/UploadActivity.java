package com.li.gddisease;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class UploadActivity extends AppCompatActivity {
    private EditText mEtLongitude;
    private EditText mEtLatitude;
    private RadioGroup radioGroup;
    private RadioGroup radioGroup_place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        init();

    }

    public void init()
    {
        mEtLongitude = findViewById(R.id.edit_text_disease_longitude);
        mEtLatitude = findViewById(R.id.edit_text_disease_latitude);
        radioGroup_place = findViewById(R.id.radioGroup_place);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup_place.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = radioGroup.findViewById(checkedId);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = radioGroup.findViewById(i);
            }
        });
    }

    public void makeData()
    {

    }
}