package com.michio.ten_sh;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.michio.ten_sh.db.DataUtils;
import com.michio.ten_sh.server.TimeService;
import com.tencent.bugly.crashreport.CrashReport;

public class MyApplication extends Application {
    public static DataUtils dataHelper;
    public static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "43b2f6132f", false);
        dataHelper = new DataUtils(this);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        startService(new Intent(this, TimeService.class));
    }
}
