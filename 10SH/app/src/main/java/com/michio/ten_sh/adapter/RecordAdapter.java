package com.michio.ten_sh.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.michio.ten_sh.R;
import com.michio.ten_sh.db.DataUtils;
import com.michoi.db.entity.RecordBean;
import com.zj.public_lib.ui.BaseArrayListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecordAdapter extends BaseArrayListAdapter {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private ArrayList<RecordBean> beans;

    public RecordAdapter(Context context, ArrayList<RecordBean> beans) {
        super(context, beans);
        this.beans = beans;
    }

    @Override
    public int getContentView() {
        return R.layout.item_recored;
    }

    @Override
    public void onInitView(View view, int position) {
        RecordBean bean = beans.get(position);
        TextView tv_startTime = (TextView) get(view, R.id.tv_startTime);
        TextView tv_endTime = (TextView) get(view, R.id.tv_endTime);
        TextView tv_allTime = (TextView) get(view, R.id.tv_allTime);
        tv_startTime.setText("" + dateFormat.format(Long.valueOf(bean.getStartTime())));
        if (bean.getEndTime() == null || bean.getEndTime() == 0) {
            tv_endTime.setText("尚未结束");
        } else {
            tv_endTime.setText("" + dateFormat.format(Long.valueOf(bean.getEndTime())));
            long time = bean.getEndTime() - bean.getStartTime();
            long allMinute = time / (60 * 1000);
            tv_allTime.setText("" + allMinute + " 分钟");
        }
    }
}
