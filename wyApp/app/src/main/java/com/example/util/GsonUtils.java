package com.example.util;

import com.google.gson.Gson;

/**
 * Created by NY on 2017/3/13.
 * Gson单例化
 */

public class GsonUtils {
    private static Gson mGson;

    public static Gson getInstance() {
        if (mGson == null) {
            mGson = new Gson();
        }
        return mGson;
    }
}
