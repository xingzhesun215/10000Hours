package com.michio.ten_sh.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.michio.ten_sh.MyApplication;
import com.michio.ten_sh.R;
import com.michio.ten_sh.adapter.RecordAdapter;
import com.michoi.db.entity.RecordBean;
import com.zj.public_lib.ui.BaseActivity;
import com.zj.public_lib.utils.PublicUtil;
import com.zj.public_lib.view.ScrollListView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Administrator on 2018/2/24.
 */

public class RecordListActivity extends BaseActivity {

    private PtrClassicFrameLayout ptrclassicframelayout;

    private ScrollListView listview;

    private ArrayList<RecordBean> recordBeens = new ArrayList<>();
    private RecordAdapter customerAdapter;

    private int page = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recordlist;
    }

    @Override
    protected void initView() {
        initTitleLeft("全部记录");
        ptrclassicframelayout = this.findViewById(R.id.ptrclassicframelayout);
        listview = this.findViewById(R.id.listview);
    }

    @Override
    protected void initData(Bundle bundle) {

        PublicUtil.setHeadView(this, ptrclassicframelayout);
        ptrclassicframelayout.setLoadingMinTime(1000);
        ptrclassicframelayout.setMode(PtrFrameLayout.Mode.BOTH);
        ptrclassicframelayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(final PtrFrameLayout frame) {
                page++;
                getCompanyList();
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                page = 0;
                getCompanyList();
            }
        });

        customerAdapter = new RecordAdapter(this, recordBeens);
        listview.setAdapter(customerAdapter);
        ptrclassicframelayout.autoRefresh();
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, RecordListActivity.class));
    }

    private void getCompanyList() {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("pageSize", "10");

        List<RecordBean> recordBeans = MyApplication.dataHelper.listRecordsByPageBySize(20, page);

        ptrclassicframelayout.refreshComplete();
        try {
            if (page == 0) {
                recordBeens.clear();
            }
            if (recordBeans != null) {
                recordBeens.addAll(recordBeans);
                notifyDataSetChanged();
            }
        } catch (Exception e) {

        }

    }


    /**
     * 统一操作
     */
    public void notifyDataSetChanged() {
        customerAdapter.notifyDataSetChanged();
    }
}
