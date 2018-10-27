package com.liyuu.strategy.model.db;



import io.realm.DynamicRealm;
import io.realm.RealmMigration;

/**
 * realm数据库迁移
 * realm相关资料中可查看demo
 * Realm db中包含的表格
 * {@link com.liyuu.strategy.model.bean.SearchStockBean}
 * {@link com.liyuu.strategy.model.bean.CustomSelectStockBean}
 */

public class MyRealmMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
//        LogUtil.i("Realm oldVersion " + oldVersion + " newVersion " + newVersion);
//        RealmSchema schema = realm.getSchema();

//        if (oldVersion == 0) {
//            schema.create("SearchStockBean")
//                    .addPrimaryKey("addTime")
//                    .addField("sname", String.class)
//                    .addField("symbol", String.class);
//        }

        //table字段更改事例
//        if (oldVersion == 1) {
//            RealmObjectSchema personSchema = schema.get("SearchStockBean");
//            // Combine 'firstName' and 'lastName' in a new field called 'fullName'
//            personSchema
//                    .addField("addTest", String.class)//, 第三个参数 必须与新增的item标注一致 列如添加了@required 则需要填入FieldAttribute.REQUIRED
//                    .transform(new RealmObjectSchema.Function() {
//                        @Override
//                        public void apply(DynamicRealmObject obj) {
//                            obj.set("addTest", String.valueOf(obj.getLong("addTime")));
//                        }
//                    });
//            oldVersion++;
//        }
        //table新增事例
//        if (oldVersion==1){
//            schema.create("TestBean")
//                    .addField("test", String.class);
//        }
    }
}
