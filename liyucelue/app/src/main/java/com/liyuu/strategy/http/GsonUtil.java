package com.liyuu.strategy.http;

/**
 * Created by hlw on 2017/12/21.
 */

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.liyuu.strategy.app.StockField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GsonUtil {

    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtil() {
    }

    public static Gson getInstance() {
        return gson;
    }

    /**
     * 转成json
     */
    public static String toString(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    /**
     * 转成bean
     */
    public static <T> T gsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

//    /**
//     * 转成list
//     * 泛型在编译期类型被擦除导致报错(不能使用)
//     *
//     * @param gsonString
//     * @param cls
//     * @return
//     */
//    private static <T> List<T> gsonToList(String gsonString, Class<T> cls) {
//        List<T> list = null;
//        if (gson != null) {
//            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
//            }.getType());
//        }
//        return list;
//    }


    public static <T> List<T> fromJsonList(String json, Class<T> clazz) {
        List<T> lst = new ArrayList<>();
        try {
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (final JsonElement elem : array) {
                lst.add(new Gson().fromJson(elem, clazz));
            }
        } catch (Exception e) {
        }
        return lst;
    }

    /**
     * 转成list中有map的
     */
    public static <T> List<Map<String, T>> gsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * 转成map的
     */
    public static <T> Map<String, T> gsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }

    /**
     * LinkedTreeMap to MyClass
     * the method is resultless
     * 方法无效
     */
    public static <T> ArrayList<T> convert(Class<T> bean, ArrayList list) {
        ArrayList result = new ArrayList<>();
        //获取bean对象内的属性：
        Field[] fields = bean.getDeclaredFields();
        //循环遍历list,获取linkedTreeMap
        for (int i = 0; i < fields.length; i++) {
            LinkedTreeMap map = (LinkedTreeMap) list.get(i);
            try {
                T instance = (T) bean.newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(instance, map.get(field.getName()));
                }
                result.add(instance);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    public static JsonObject getJsonObject(@NonNull String data, @NonNull String... strings) {
        JsonParser jsonParser = new JsonParser();
        JsonObject all = (JsonObject) jsonParser.parse(data);

        JsonObject[] jsonObjects = new JsonObject[strings.length];
        for (int i = 0, size = strings.length; i < size; i++) {
            jsonObjects[i] = new JsonObject();
            if (i == 0) {
                if (!all.has(strings[i])) return null;
                jsonObjects[i] = all.getAsJsonObject(strings[i]);
            } else {
                if (!jsonObjects[i - 1].has(strings[i])) return null;
                jsonObjects[i] = jsonObjects[i - 1].getAsJsonObject(strings[i]);
            }
        }
        return jsonObjects[strings.length - 1];
    }

    public static String arrayGetString(JsonArray array, int position) {
        if (position < 0)
            return null;
        String str = "";
        if (array.size() > position && !array.get(position).isJsonNull())
            str = array.get(position).getAsString();
        return str;
    }

    public static String arrayGetString(JsonArray array, int position, String defStr) {
        if (position < 0)
            return null;
        String str = defStr;
        if (array.size() > position && !array.get(position).isJsonNull())
            str = array.get(position).getAsString();
        return str;
    }

    public static float arrayGetFloat(JsonArray array, int position) {
        if (position < 0)
            return 0.f;
        float f = 0.f;
        if (array.size() > position && !array.get(position).isJsonNull())
            f = array.get(position).getAsFloat();
        return f;
    }

    public static float arrayGetFloat(JsonArray array, int position, float defFloat) {
        if (position < 0)
            return 0.f;
        float f = defFloat;
        if (array.size() > position && !array.get(position).isJsonNull())
            f = array.get(position).getAsFloat();
        return f;
    }

    public static double arrayGetDouble(JsonArray array, int position) {
        if (position < 0)
            return 0.f;
        double d = 0.f;
        if (array.size() > position && !array.get(position).isJsonNull())
            d = array.get(position).getAsDouble();
        return d;
    }

    public static double arrayGetDouble(JsonArray array, int position, double defDouble) {
        if (position < 0)
            return 0.f;
        double d = defDouble;
        if (array.size() > position && !array.get(position).isJsonNull())
            d = array.get(position).getAsDouble();
        return d;
    }

    public static int arrayGetInt(JsonArray array, int position) {
        if (position < 0)
            return 0;
        int i = 0;
        if (array.size() > position && !array.get(position).isJsonNull())
            i = array.get(position).getAsInt();
        return i;
    }

    public static int getStockFieldsPosition(JsonObject snapshot, @StockField String field) {
        if (!snapshot.has("fields")) return 0;
        JsonArray array = snapshot.get("fields").getAsJsonArray();
        int size = array.size();
        for (int i = 0; i < size; i++) {
            String string = GsonUtil.arrayGetString(array, i);
            if (field.equals(string))
                return i;
        }
        return -1;
    }

}
