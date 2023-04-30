package com.li.gddisease;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.li.gddisease.dao.UserDao;
import com.li.gddisease.entity.User;
import com.li.gddisease.ui.DataFragment;
import com.li.gddisease.ui.MapFragment;
import com.li.gddisease.ui.MeFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView mTvMap, mTvData, mTvHome;
    private int id;
    private AppDatabase db;

    public int getUserId()
    {
        return id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        id = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, MapFragment.class, null)
                    .commit();
        }
        init();
        setClickListener();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_view, new MapFragment()).commitAllowingStateLoss();

    }

    private void init() {
        db = AppDatabase.getInstance(this);
        mTvMap = findViewById(R.id.map);
        mTvData = findViewById(R.id.data);
        mTvHome = findViewById(R.id.home);
    }
    private void setClickListener()
    {
        mTvMap.setOnClickListener(this);
        mTvData.setOnClickListener(this);
        mTvHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        String tag = null;
        switch (v.getId())
        {
            case R.id.map:
                fragment = new MapFragment();
                tag = "map";
                break;
            case R.id.data:
                fragment = new DataFragment();
                tag = "data";
                break;
            case R.id.home:
                tag = "home";
                fragment = new MeFragment();
                break;
        }
        fragmentTransaction.replace(R.id.fragment_container_view, fragment, tag).commitAllowingStateLoss();
    }
}