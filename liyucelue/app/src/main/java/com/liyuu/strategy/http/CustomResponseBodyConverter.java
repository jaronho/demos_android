package com.liyuu.strategy.http;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.liyuu.strategy.app.AppConfig;
import com.liyuu.strategy.util.AES;
import com.liyuu.strategy.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class CustomResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = convertStreamToString(value.byteStream());
//        LogUtil.d(LogUtil.TAG, "response:" + response);
        String newResponse = response;//先给response赋一个初值，以免下面解析异常出现null的情况，同时如果为明文时，即为此值
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("en_data")) { //密文
                String en_data = jsonObject.optString("en_data");
                newResponse = AES.decrypt(en_data, AppConfig.AES_KEY);
            }
            LogUtil.i(LogUtil.TAG + " newResponse:" + newResponse);

            JSONObject obj = new JSONObject(newResponse);
            if (obj.optInt("code") != 1000) {
                obj.remove("data");
            }
            return readResponse(obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            //解析错误仍将response传递出去，否则无法提示解析错误，因为这里是子线程
            return readResponse(newResponse);
        } finally {
            value.close();
        }
    }

    private T readResponse(String response) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(new StringReader(response));
        return adapter.read(jsonReader);
    }


    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
