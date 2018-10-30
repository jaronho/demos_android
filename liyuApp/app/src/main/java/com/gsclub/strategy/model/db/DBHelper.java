package com.gsclub.strategy.model.db;

import android.support.annotation.NonNull;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.Sort;

/**
 * 持久化数据的操作接口
 */
public interface DBHelper {
    /**
     * 查询表格数据
     *
     * @param clazz      查询的表格
     * @param queryField 查询表格的排序字段（无需排序，传空）
     * @param sortType   排序的方式
     * @return 查询得到的列表
     */
    List<? extends RealmObject> dbQueryList(
            @NonNull Class<? extends RealmObject> clazz, String queryField, Sort sortType);


    /**
     * 查询表格数据
     *
     * @param clazz      查询的表格
     * @param queryField 查询表格的排序字段
     * @param sortType   排序的方式
     * @param equal      查询表格中字段queryField 为 equal的数据
     * @return 查询得到的列表
     */
    List<? extends RealmObject> dbQueryEqualList(
            @NonNull Class<? extends RealmObject> clazz, @NonNull String queryField, @NonNull String equal, Sort sortType);

    /**
     * 删除表格数据
     *
     * @param clazz 删除的表格
     */
    void dbDeleteTable(Class<? extends RealmObject> clazz);

    /**
     * 删除表中数据
     *
     * @param clazz    删除数据的表格
     * @param queryStr 删除数据依据的字段
     * @param o        删除数据依据字段的具体值
     */
    void dbDeleteData(Class<? extends RealmObject> clazz, final String queryStr, final Object o);

    /**
     * 向表格中插入数据
     *
     * @param data 插入的数据
     */
    void dbInsertData(RealmObject data);

    /**
     * 查询数据是否存在并返回
     *
     * @param clazz      查询的表
     * @param queryField 查询依据表的字段
     * @param queryData  查询的数据
     */
    RealmObject dbQueryDataByField(Class<? extends RealmObject> clazz, @NonNull String queryField, @NonNull String queryData);
}
