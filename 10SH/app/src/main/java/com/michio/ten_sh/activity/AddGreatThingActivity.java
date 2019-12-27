package com.michio.ten_sh.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.michio.ten_sh.MyApplication;
import com.michio.ten_sh.R;
import com.zj.public_lib.ui.BaseActivity;
import com.zj.public_lib.view.HeaderLayout;

public class AddGreatThingActivity extends BaseActivity {

    private EditText et_add_way;
    private TextView tv_lock;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_addgreatthing;
    }

    @Override
    protected void initView() {
        et_add_way = this.findViewById(R.id.et_add_way);
        initTitleLeftAndRight("添加事项", "确定", new HeaderLayout.onRightClickListener() {
            @Override
            public void onClick() {
                if (MyApplication.sp.getBoolean("way_lock", true) && !TextUtils.isEmpty(MyApplication.sp.getString("way_name", ""))) {
                    Toast.makeText(AddGreatThingActivity.this, "不能修改,被锁定", Toast.LENGTH_SHORT).show();
                    return;
                }
                String way = et_add_way.getText().toString().trim();
                String way_name = MyApplication.sp.getString("way_name", "");
                if (TextUtils.isEmpty(way)) {
                    return;
                }
                if (!way_name.equals(way)) {
                    MyApplication.sp.edit().putString("way_name", way).putBoolean("way_lock", true).commit();
                    MyApplication.dataHelper.deleteAllRecord();
                    Toast.makeText(AddGreatThingActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    MyApplication.sp.edit().putBoolean("way_lock", true).commit();
                    Toast.makeText(AddGreatThingActivity.this, "已锁定", Toast.LENGTH_SHORT).show();
                    tv_lock.setText("已锁定");
                }

            }
        });
        tv_lock = this.findViewById(R.id.tv_lock);
        boolean isLock = MyApplication.sp.getBoolean("way_lock", false);
        if (isLock) {
            tv_lock.setText("已锁定");
        } else {
            tv_lock.setText("未锁定");
        }
        tv_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String way_name = MyApplication.sp.getString("way_name", "");
                if (TextUtils.isEmpty(way_name)) {
                    return;
                }
                MyApplication.sp.edit().putBoolean("way_lock", false).commit();
                Toast.makeText(AddGreatThingActivity.this, "已解锁", Toast.LENGTH_SHORT).show();
                tv_lock.setText("未锁定");
            }
        });
    }

    @Override
    protected void initData(Bundle bundle) {

    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AddGreatThingActivity.class));
    }
}
