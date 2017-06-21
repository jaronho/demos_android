package com.example.util;

import android.annotation.SuppressLint;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

import ASimpleCache.org.afinal.simplecache.ACache;

public class TianjiaCart {

    //加入购物车
    @SuppressLint("NewApi")
    public static void addCart(int id, String count, Context context) {
        JSONArray jsonArray;
        ASimpleCache.org.afinal.simplecache.ACache mCache;

        mCache = ACache.get(context);
        jsonArray = mCache.getAsJSONArray("testJsonArray");

        if (jsonArray == null) {
            jsonArray = new JSONArray();
            JSONObject yosonJsonObject = new JSONObject();

            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject;
                    jsonObject = jsonArray.getJSONObject(i);
                    if (id == jsonObject.getInt("Id")) {
                        count = Integer.valueOf(count) + Integer.valueOf(jsonObject.getString("Count")) + "";
                        try {
                            delete(i, jsonArray);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                yosonJsonObject.put("Id", id);
                yosonJsonObject.put("Count", count);
                jsonArray.put(yosonJsonObject);

                mCache.put("testJsonArray", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        } else {
            JSONArray newArray = new JSONArray();
            boolean isAdded = false;
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (id == jsonObject.getInt("Id")) {
                        isAdded = true;
                        count = Integer.valueOf(count) + Integer.valueOf(jsonObject.getString("Count")) + "";
                        jsonObject.put("Count", count);
                    }
                    newArray.put(jsonObject);
                }
                if (!isAdded) {
                    JSONObject newJsonObject = new JSONObject();
                    newJsonObject.put("Id", id);
                    newJsonObject.put("Count", count);
                    newArray.put(newJsonObject);
                }

                mCache.put("testJsonArray", newArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //修改购物车商品数量
    @SuppressLint("NewApi")
    public static void addCart2(int id, String count, Context context) {
        JSONArray jsonArray;
        ASimpleCache.org.afinal.simplecache.ACache mCache;

        mCache = ACache.get(context);
        jsonArray = mCache.getAsJSONArray("testJsonArray");

        if (jsonArray != null) {
            JSONArray newArray = new JSONArray();
            try {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (id == jsonObject.getInt("Id")) {
                        jsonObject.put("Count", count);
                    }
                    newArray.put(jsonObject);
                }
                mCache.put("testJsonArray", newArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void delete(int s, JSONArray jsonArray) throws Exception {
        if (s < 0)
            return;
        Field valuesField = JSONArray.class.getDeclaredField("values");
        valuesField.setAccessible(true);
        List<Object> values = (List<Object>) valuesField.get(jsonArray);
        if (s >= values.size())
            return;
        values.remove(s);
    }

    @SuppressLint("NewApi")
    public static int getShoppingNum(Context context) {
        JSONArray jsonArray;
        int num = 0;
        ASimpleCache.org.afinal.simplecache.ACache mCache;

        mCache = ACache.get(context);
        jsonArray = mCache.getAsJSONArray("testJsonArray");
        if (jsonArray != null && jsonArray.length() > 0) {
            num = jsonArray.length();
        }
        return num;
    }

    @SuppressLint("NewApi")
    public static int getShoppingNumById(Context context, int id) {
        int num = 0;
        JSONArray jsonArray;
        ASimpleCache.org.afinal.simplecache.ACache mCache;

        mCache = ACache.get(context);
        jsonArray = mCache.getAsJSONArray("testJsonArray");
        if (jsonArray != null && jsonArray.length() > 0) {
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int mId = jsonObject.getInt("Id");
                    if (mId == id) {
                        num = Integer.valueOf(jsonObject.getString("Count"));
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return num;
    }
}
