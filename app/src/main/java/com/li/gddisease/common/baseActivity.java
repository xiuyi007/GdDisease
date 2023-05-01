package com.li.gddisease.common;

import androidx.appcompat.app.AppCompatActivity;

public abstract class baseActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            // 当前 Activity 是任务的根 Activity，即应用程序中的最后一个 Activity
            // 不执行任何操作，保留在当前 Activity
        } else {
            // 当前 Activity 不是任务的根 Activity，即还有其他 Activity 在任务中
            // 返回上一个 Activity
            super.onBackPressed();
        }

    }
}
