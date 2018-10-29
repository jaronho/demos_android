package com.liyuu.strategy.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.Pair;

import com.liyuu.strategy.app.App;
import com.liyuu.strategy.app.SPKeys;


/**
 * 对 SharePrefence 的简单封装
 */

public final class PreferenceUtils {

    public static int getInt(String fileName, @SPKeys String key) {
        return getInt(fileName, key, 0);
    }

    public static int getInt(String fileName, @SPKeys String key, int defaultValue) {
        return get(fileName).getInt(key, defaultValue);
    }

    public static void put(String fileName, @SPKeys String key, int value) {
        getEditor(fileName).putInt(key, value).apply();
    }

    public static String getString(String fileName, @SPKeys String key) {
        return getString(fileName, key, null);
    }

    public static String getString(String fileName, @SPKeys String key, String defaultValue) {
        return get(fileName).getString(key, defaultValue);
    }

    public static void put(String fileName, @SPKeys String key, String value) {
        getEditor(fileName).putString(key, value).apply();
    }

    public static boolean getBoolean(String fileName, @SPKeys String key) {
        return getBoolean(fileName, key, false);
    }

    public static boolean getBoolean(String fileName, @SPKeys String key, boolean defaultValue) {
        return get(fileName).getBoolean(key, defaultValue);
    }

    public static void put(String fileName, @SPKeys String key, boolean value) {
        getEditor(fileName).putBoolean(key, value).apply();
    }

    public static void putInt(String fileName, Pair<String, Integer>... pairs) {
        if(pairs != null) {
            SharedPreferences.Editor editor = getEditor(fileName);
            for(Pair<String, Integer> pair : pairs) {
                editor.putInt(pair.first, pair.second);
            }
            editor.commit();
        }
    }

    public static void putBool(String fileName, Pair<String, Boolean>... pairs) {
        if(pairs != null) {
            SharedPreferences.Editor editor = getEditor(fileName);
            for(Pair<String, Boolean> pair : pairs) {
                editor.putBoolean(pair.first, pair.second);
            }
            editor.commit();
        }
    }

    public static void putString(String fileName, Pair<String, String>... pairs) {
        if(pairs != null) {
            SharedPreferences.Editor editor = getEditor(fileName);
            for(Pair<String, String> pair : pairs) {
                editor.putString(pair.first, pair.second);
            }
            editor.commit();
        }
    }

    public static void cleanUserInfo(){
        SharedPreferences.Editor editor =  getEditor(SPKeys.FILE_USER_INFO);
        editor.clear();
        editor.commit();
    }

    public static void cleanAd(){
        SharedPreferences.Editor editor =  getEditor(SPKeys.FILE_AD);
        editor.clear();
        editor.commit();
    }

    private PreferenceUtils(){}

    public static SharedPreferences get(String name) {
        Context context = App.getInstance();
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor(String name) {
        return get(name).edit();
    }
}
