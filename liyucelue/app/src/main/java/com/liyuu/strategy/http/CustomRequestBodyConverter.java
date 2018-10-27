package com.liyuu.strategy.http;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.liyuu.strategy.BuildConfig;
import com.liyuu.strategy.app.App;
import com.liyuu.strategy.app.AppConfig;
import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.util.AES;
import com.liyuu.strategy.util.DateUtil;
import com.liyuu.strategy.util.LogUtil;
import com.liyuu.strategy.util.PreferenceUtils;
import com.liyuu.strategy.util.UserInfoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by hlw on 2018/5/16.
 * http 请求参数封装
 */

public class CustomRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        Map<String, Object> params = (Map<String, Object>) value;
        String optAct = (String) params.get("opact");
        String oriParam = transformers(addParams(params));
        String deStr = AES.encrypt(oriParam, AppConfig.AES_KEY);
        Map<String, Object> request = new HashMap<>();
        request.put("en_data", deStr);
        request.put("opact", optAct);
        request.put("version", BuildConfig.APP_VERSION);
        request.put("en_key", AppConfig.EN_KEY);
        request.put("device_type", AppConfig.DEVICE_TYPE);
        String requestStr = transformers(request);
        LogUtil.d(LogUtil.TAG + " : " + AppConfig.API_IP + " : " + optAct + " : " + oriParam + " \n" + requestStr);
        return RequestBody.create(MEDIA_TYPE, requestStr);
    }

    private Map<String, Object> addParams(Map<String, Object> pair) {
        if (pair == null) pair = new HashMap<>();
        pair.put("tms", DateUtil.getNowTime());
        pair.put("imei", PreferenceUtils.getString(SPKeys.FILE_COMMON, SPKeys.COMMON_UUID));
        pair.put("platform", AppConfig.PLATFORM);
        pair.put("source", App.getInstance().getAppChannel());
//        pair.put("source", "yingyongbao");
        String appModel = Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.RELEASE;
        pair.put("app_model", appModel);
        pair.put("xgpush_device", UserInfoUtil.getPushToken());
        return pair;
    }

    private String transformers(Map<String, ?> params) {
        JSONObject object = new JSONObject();
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            try {
                object.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return object.toString();
    }

}
