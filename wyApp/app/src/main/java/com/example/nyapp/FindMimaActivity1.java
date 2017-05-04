package com.example.nyapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;

public class FindMimaActivity1 extends BaseActivity {
    private Button find1_next;
    private LinearLayout layout_back;
    private ImageRequest imageRequest;
    private ImageView image_code;
    private EditText text_findname, text_findcode;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.find1);
        image_code = (ImageView) findViewById(R.id.image_findcode);
        imageRequest = new ImageRequest(
                UrlContact.URL_STRING + "/api/User/GetVCode" + "?deviceId=" + MyApplication.sUdid,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        image_code.setImageBitmap(response);
                    }
                }, 0, 0, Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MyVolley.getRequestQueue().add(imageRequest);
        image_code.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MyVolley.getRequestQueue().add(imageRequest);

            }
        });
        text_findname = (EditText) findViewById(R.id.text_findname);
        text_findcode = (EditText) findViewById(R.id.text_findcode);
        find1_next = (Button) findViewById(R.id.find1_next);
        find1_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (text_findname.getText().toString().equals("")) {
                    MyToastUtil.showShortMessage("用户名不能为空！");

                } else {
                    if (text_findcode.getText().toString().equals("")) {
                        MyToastUtil.showShortMessage("验证码不能为空！");
                    } else {
                        Map<String, String> params2 = new TreeMap<>();
                        params2.put("userName", text_findname.getText().toString());
                        params2.put("deviceId", MyApplication.sUdid);
                        params2.put("Code", text_findcode.getText().toString());
                        MyOkHttpUtils
                                .postData(UrlContact.URL_STRING + "/api/user/FindPwdVerifyUser", params2)
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        MyToastUtil.showShortMessage("服务器连接失败!");
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        try {
                                            JSONObject object = new JSONObject(response);
                                            if (object.getBoolean("Result")) {

                                                    Intent intent = new Intent(FindMimaActivity1.this, FindMimaActivity2.class);
                                                    intent.putExtra("phone", object.getString("Message"));
                                                    intent.putExtra("username", text_findname.getText().toString());
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                MyToastUtil.showShortMessage(object.getString("Message"));
                                                }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }
                }
            }
        });
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layout_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    @Override
    public void initView() {

    }

}
