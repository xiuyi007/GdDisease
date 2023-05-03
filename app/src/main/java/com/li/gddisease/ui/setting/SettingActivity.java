package com.li.gddisease.ui.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.li.gddisease.LoginActivity;
import com.li.gddisease.R;

public class SettingActivity extends AppCompatActivity {

    private Button mBtn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mBtn_logout = findViewById(R.id.btn_logout);
        mBtn_logout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
}