package com.michio.ten_sh.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.michio.ten_sh.MyApplication;
import com.michio.ten_sh.R;
import com.michio.ten_sh.util.event.RefreshEvent;
import com.michio.ten_sh.view.FlashChargeView;
import com.michoi.db.entity.RecordBean;
import com.zj.public_lib.ui.BaseActivity;
import com.zj.public_lib.view.HeaderLayout;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * @author Administrator
 */
public class MainActivity extends BaseActivity {


    private TextView tv_showTime;
    private TextView tv_curTime;
    private TextView tv_start;
    private FlashChargeView chargeView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        tv_showTime = this.findViewById(R.id.tv_showTime);
        tv_curTime = this.findViewById(R.id.tv_curTime);
        tv_start = this.findViewById(R.id.tv_start);
        chargeView = this.findViewById(R.id.chargeView);
    }

    @Override
    protected void initData(Bundle bundle) {
        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String way_name = MyApplication.sp.getString("way_name", "");
                if (TextUtils.isEmpty(way_name)) {
                    AddGreatThingActivity.startActivity(MainActivity.this);
                    return;
                }

                if (isStarting) {
                    isStarting = false;
                    tv_start.setText("开始");
                    setView(true);
                } else {
                    RecordBean bean = new RecordBean();
                    Calendar mCalendar = Calendar.getInstance();
                    int year = mCalendar.get(Calendar.YEAR);
                    int monty = mCalendar.get(Calendar.MONTH);
                    int day = mCalendar.get(Calendar.DAY_OF_MONTH);
                    int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                    int minute = mCalendar.get(Calendar.MINUTE);
                    bean.setYear(year);
                    bean.setMonth(monty);
                    bean.setDay(day);
                    bean.setHour(hour);
                    bean.setMinute(minute);
                    bean.setStartTime(System.currentTimeMillis());
                    bean.setEndTime(0L);
                    MyApplication.dataHelper.insertRecord(bean);
                    isStarting = true;
                    tv_curTime.setText("未开始");
                    tv_start.setText("结束");
                    setView(false);
                }
            }
        });
    }


    public void onEventMainThread(RefreshEvent event) {
        Log.e("Me", "刷新时间");
        setView(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        final String way_name = MyApplication.sp.getString("way_name", "");
        initTitleLeftAndRight(TextUtils.isEmpty(way_name) ? "添加事件" : way_name, TextUtils.isEmpty(way_name) ? "添加" : "记录", new HeaderLayout.onRightClickListener() {

            @Override
            public void onClick() {
                if (TextUtils.isEmpty(way_name)) {
                    AddGreatThingActivity.startActivity(MainActivity.this);
                } else {
                    RecordListActivity.startActivity(MainActivity.this);
                }
            }
        });
        setView(false);
    }


    public void handleAllRecord(boolean isCloseAll) {

        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int monty = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        List<RecordBean> recordBeans = MyApplication.dataHelper.queryBuilderUnfinish();
        for (int i = 0; i < recordBeans.size(); i++) {
            RecordBean bean = recordBeans.get(i);
            if (bean.getYear() != year || bean.getMonth() != monty || bean.getDay() != day) {//不是今天
                if (bean.getHour() == 23) {
                    bean.setEndTime(getTimeTemp(bean.getYear(), bean.getMonth(), bean.getDay(), bean.getHour(), 59));
                } else {
                    bean.setEndTime(getTimeTemp(bean.getYear(), bean.getMonth(), bean.getDay(), bean.getHour() + 1, bean.getMinute()));
                }
                if (bean.getEndTime() > bean.getStartTime()) {
                    MyApplication.dataHelper.updateRecord(bean);
                } else {
                    MyApplication.dataHelper.deleteRecord(bean);
                }
            } else {//是今天
                long curTime = System.currentTimeMillis();
                long startTime = bean.getStartTime();
                if (curTime - 60 * 60 * 1000 > startTime) {//超过一个小时了
                    bean.setEndTime(startTime + 60 * 60 * 1000);
                    MyApplication.dataHelper.updateRecord(bean);
                } else {
                    if (isCloseAll) {
                        if (curTime - startTime > 5 * 60 * 1000) {
                            bean.setEndTime(curTime);
                            if (bean.getEndTime() > bean.getStartTime()) {
                                MyApplication.dataHelper.updateRecord(bean);
                            } else {
                                MyApplication.dataHelper.deleteRecord(bean);
                            }
                        } else {
                            Toast.makeText(this, "5分钟以内不计时", Toast.LENGTH_SHORT).show();
                            MyApplication.dataHelper.deleteRecord(bean);
                        }
                    }
                }

            }

        }
    }

    public long getTimeTemp(int year, int month, int day, int hour, int minute) {
        String timeTemp = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + "00";
        //获取指定时间的时间戳，除以1000说明得到的是秒级别的时间戳（10位）
        long time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(timeTemp, new ParsePosition(0)).getTime();
        return time;
    }

    public long getAllTime() {
        List<RecordBean> recordBeans = MyApplication.dataHelper.listAllRecord();
        if (recordBeans == null || recordBeans.size() == 0) {
            return 0;
        }
        long allTime = 0;
        for (int i = 0; i < recordBeans.size(); i++) {
            RecordBean recordBean = recordBeans.get(i);
            if (recordBean.getEndTime() != 0) {
                allTime = allTime + recordBean.getEndTime() - recordBean.getStartTime();
            }
        }
        return allTime;
    }

    boolean isStarting;

    public void setView(boolean iscloseAll) {
        Log.e("ME", "setView =" + iscloseAll);
        handleAllRecord(iscloseAll);
        long allTime = getAllTime();
        long allMinite = allTime / (1000 * 60);
        long allHour = allMinite / 60;
        tv_showTime.setText("已累计:  " + allHour + "小时 + " + (allMinite % 60) + "分钟");


        List<RecordBean> unfinishList = MyApplication.dataHelper.queryBuilderUnfinish();
        if (unfinishList.size() > 1) {
            tv_curTime.setText("数据有误");
            isStarting = false;
            tv_start.setText("开始");
            setView(true);
            chargeView.setVisibility(View.GONE);

        } else if (unfinishList.size() == 0) {
            isStarting = false;
            tv_start.setText("开始");
            tv_curTime.setText("未开始");
            chargeView.setVisibility(View.GONE);
        } else {
            isStarting = true;
            tv_start.setText("结束");
            long curTime = System.currentTimeMillis();
            long time = curTime - unfinishList.get(0).getStartTime();
            long allMinute = time / (60 * 1000);
            tv_curTime.setText(allMinute + " 分钟");
            chargeView.setVisibility(View.VISIBLE);
            chargeView.startChargeAnimator();
            chargeView.setPower((int) (allMinute * 1.0 * 100) / 60);
        }
    }

    long time;

    @Override
    public void onBackPressed() {

        long curTime = System.currentTimeMillis();
        if (curTime - time < 2000) {
            time = 0;
            finish();
        } else {
            Toast.makeText(this, "再按一下退出", Toast.LENGTH_SHORT).show();
            time = curTime;
        }
    }
}
