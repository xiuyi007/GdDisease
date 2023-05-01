package com.li.gddisease;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.li.gddisease.common.baseActivity;
import com.li.gddisease.entity.User;

import util.ToastUtil;

public class LoginActivity extends baseActivity {
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private User mUser;
    private AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

    }

    public void init()
    {
        db = AppDatabase.getInstance(getApplicationContext());
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_username.getText().toString();
                String passwd = et_password.getText().toString();
                if (!name.isEmpty() && !passwd.isEmpty())
                {
                    User userByName = db.userDao().getUserByName(name);
                    if (userByName != null)
                    {
                        if (userByName.getPassword().equals(passwd)) {
                            //用户名存在，密码相等，登录成功
                            ToastUtil.showMsg(getApplicationContext(), "登录成功");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userId", userByName.getId() + "");
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            ToastUtil.showMsg(getApplicationContext(), "用户名或密码错误");
                        }
                    }
                    else
                    {
                        ToastUtil.showMsg(getApplicationContext(), "用户名或密码错误");
                    }
                }
                else
                {
                    ToastUtil.showMsg(getApplicationContext(), "请输入用户名或密码");
                }
            }
        });
    }
}