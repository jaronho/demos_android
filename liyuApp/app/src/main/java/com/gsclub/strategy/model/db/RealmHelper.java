package com.gsclub.strategy.model.db;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gsclub.strategy.app.App;
import com.gsclub.strategy.util.SystemUtil;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * 持久化数据实现的类
 * 注入提供给module
 */
public class RealmHelper implements DBHelper {

    private static final String DB_NAME = "myRealm.realm";
    private RealmConfiguration realmConfiguration;

    @Inject
    RealmHelper() {
        Context context = App.getInstance();
        Realm.init(context);
        int versionCode = SystemUtil.getVersionCode(context);
        // The RealmConfiguration is created using the builder pattern.
        // The Realm file will be located in Context.getFilesDir() with name "myrealm.realm"
        //很重要的一点是 Realm 实例是线程单例化的，也就是说多次在同一线程调用静态构建器会返回同一 Realm 实例。
        /*RealmConfiguration config = */
        realmConfiguration = new RealmConfiguration.Builder()
                .name(DB_NAME)//数据库名称
//                .encryptionKey(getKey())//加密
                .schemaVersion(versionCode)//当前数据库版本（自定义，便于数据库升级数据迁移）
//                .modules(new MySchemaModule())//指定只添加的数据
                .migration(new MyRealmMigration())//数据库迁移方法
                .build();
    }

    private Realm getCustomRealm() {
        return Realm.getInstance(realmConfiguration);
    }

    @Override
    public List<? extends RealmObject> dbQueryList(@NonNull Class<? extends RealmObject> clazz, String queryField, Sort sortType) {
        Realm realm = getCustomRealm();
        RealmResults<? extends RealmObject> realmResults = realm.where(clazz).findAll();
        if (!TextUtils.isEmpty(queryField))
            realmResults = realmResults.sort(queryField, sortType);
        List<? extends RealmObject> arrayList = realm.copyFromRealm(realmResults);
        realm.close();
        return arrayList;
    }

    @Override
    public List<? extends RealmObject> dbQueryEqualList(@NonNull Class<? extends RealmObject> clazz, @NonNull String queryField, @NonNull String equal, Sort sortType) {
        Realm realm = getCustomRealm();
        RealmResults<? extends RealmObject> realmResults = realm.where(clazz).equalTo(queryField, equal).findAll();
        if (!TextUtils.isEmpty(queryField))
            realmResults = realmResults.sort(queryField, sortType);
        List<? extends RealmObject> arrayList = realm.copyFromRealm(realmResults);
        realm.close();
        return arrayList;
    }

    @Override
    public void dbDeleteTable(Class<? extends RealmObject> clazz) {
        Realm realm = getCustomRealm();
        realm.beginTransaction();
        realm.delete(clazz);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public void dbDeleteData(final Class<? extends RealmObject> clazz, final String queryStr, final Object o) {
        Realm realm = getCustomRealm();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                RealmResults<? extends RealmObject> realmResults = null;

                if (o instanceof String)
                    realmResults = realm.where(clazz).equalTo(queryStr, (String) o).findAll();
                else if (o instanceof Integer)
                    realmResults = realm.where(clazz).equalTo(queryStr, (Integer) o).findAll();
                else if (o instanceof Double)
                    realmResults = realm.where(clazz).equalTo(queryStr, (Double) o).findAll();
                else if (o instanceof Float)
                    realmResults = realm.where(clazz).equalTo(queryStr, (Float) o).findAll();
                else if (o instanceof Long)
                    realmResults = realm.where(clazz).equalTo(queryStr, (Long) o).findAll();
                else if (o instanceof Date)
                    realmResults = realm.where(clazz).equalTo(queryStr, (Date) o).findAll();
                if (realmResults != null)
                    realmResults.deleteAllFromRealm(); // Delete and remove object directly
            }
        });

        realm.close();
    }

    @Override
    public void dbInsertData(RealmObject data) {
        Realm realm = getCustomRealm();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(data);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public RealmObject dbQueryDataByField(Class<? extends RealmObject> clazz, @NonNull String queryField, @NonNull String queryData) {
        Realm realm = getCustomRealm();
        RealmQuery<? extends RealmObject> realmResults = realm.where(clazz).equalTo(queryField, queryData);
        if (realmResults.count() == 0 || realmResults.findFirst() == null) {
            realm.close();
            return null;
        }
        RealmObject result = realm.copyFromRealm(realmResults.findFirst());
        realm.close();
        return result;
    }

}
