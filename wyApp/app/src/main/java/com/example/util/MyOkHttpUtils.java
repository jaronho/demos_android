package com.example.util;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.util.Map;

/**
 * Created by NY on 2017/3/30.
 * 网络请求
 */

public class MyOkHttpUtils {

    public static GetBuilder getData(String url, Map<String, String> map) {
        GetBuilder builder = OkHttpUtils.get().url(url);
        for (String key : map.keySet()) {
            builder.addParams(key, map.get(key));
        }
        return builder;
    }

    public static PostFormBuilder postData(String url, Map<String, String> map) {
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        for (String key : map.keySet()) {
            builder.addParams(key, map.get(key));
        }
        return builder;
    }
}
