package com.michio.ten_sh.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.michio.ten_sh.util.event.RefreshEvent;

import de.greenrobot.event.EventBus;


public class TimeService extends Service {


    private CheckTimeTask checkTimeTask;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        if (checkTimeTask == null) {
            checkTimeTask = new TimeService.CheckTimeTask();
            checkTimeTask.SafeStart();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.MODE_PRIVATE;
    }


    public class CheckTimeTask extends SafeThread {

        @Override
        public void run() {
            try {

                while (this.isRunFlg) {

                    EventBus.getDefault().post(new RefreshEvent());

                    sleep(50000);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }


    }


}