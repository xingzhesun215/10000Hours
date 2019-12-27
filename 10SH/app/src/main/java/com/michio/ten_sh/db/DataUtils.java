package com.michio.ten_sh.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.michoi.db.dao.RecordBeanDao;
import com.michoi.db.entity.RecordBean;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;


/**
 * 完成对某一张表的具体操作
 * Created by LGL on 2016/7/2.
 */
public class DataUtils {

    private DaoManager daoManager;

    //构造方法
    public DataUtils(Context context) {
        daoManager = DaoManager.getInstance();
        daoManager.initManager(context);
    }

    /**
     * 对数据库中RecordBean表的插入操作
     *
     * @param recordBean
     * @return
     */
    public boolean insertRecord(RecordBean recordBean) {
        boolean flag = false;
        flag = daoManager.getDaoSession().insert(recordBean) != -1 ? true : false;
        return flag;
    }

    public void updateRecord(RecordBean recordBean) {
        daoManager.getDaoSession().update(recordBean);
    }

    /**
     * 删除所有数据
     */
    public void deleteAllRecord() {
        daoManager.getDaoSession().getRecordBeanDao().deleteAll();
    }

    public void deleteRecord(RecordBean bean){
        daoManager.getDaoSession().getRecordBeanDao().delete(bean);
    }

    /**
     * 批量插入
     *
     * @return
     */
    public boolean inserMultRecords(final List<RecordBean> actionBeens) {
        //标识
        boolean flag = false;
        try {
            //插入操作耗时
            daoManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (RecordBean bean : actionBeens) {
                        daoManager.getDaoSession().insertOrReplace(bean);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 全部查询
     *
     * @return
     */
    public List<RecordBean> listAllRecord() {
        return daoManager.getDaoSession().loadAll(RecordBean.class);
    }


    public List<RecordBean> listRecordsByPageBySize(int size,int page){
        QueryBuilder<RecordBean> queryBuilder = daoManager.getDaoSession().queryBuilder(RecordBean.class);
        return queryBuilder.offset(page * size).limit(size).list();
    }

    /**
     * QueryBuilder
     */
    public List<RecordBean> queryBuilderUnfinish() {
        //查询构建器
        QueryBuilder<RecordBean> queryBuilder = daoManager.getDaoSession().queryBuilder(RecordBean.class);
        return queryBuilder.where(RecordBeanDao.Properties.EndTime.eq(new Long(0))).list();
    }


}
