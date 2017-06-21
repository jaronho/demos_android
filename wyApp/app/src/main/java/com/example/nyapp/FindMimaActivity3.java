package com.example.nyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.util.MyOkHttpUtils;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class FindMimaActivity3 extends BaseActivity {
    private Button find3_next;
    private LinearLayout layout_back;
    private EditText text_findpass1, text_findpass2;
    private String username = "";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.find3);
        username = getIntent().getStringExtra("username");
        text_findpass1 = (EditText) findViewById(R.id.text_findpass1);
        text_findpass2 = (EditText) findViewById(R.id.text_findpass2);
        find3_next = (Button) findViewById(R.id.find3_next);
        find3_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String password1 = text_findpass1.getText().toString();
                String password2 = text_findpass2.getText().toString();
                if (!password1.equals("") && !password2.equals("")) {
                    if (password1.equals(password2)) {
                        if (password1.toCharArray().length < 6) {
                            MyToastUtil.showShortMessage("密码不能小于6位");
                        } else {

                            Map<String, String> params2 = new HashMap<String, String>();
                            params2.put("userName", username);
                            params2.put("deviceId", MyApplication.sUdid);
                            params2.put("password", encryption(password1).toUpperCase());
                            MyOkHttpUtils
                                    .postData(UrlContact.URL_STRING + "/api/user/FindPwdSetPwd", params2)
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
                                                if (object
                                                        .getBoolean("Result")) {

                                                    Intent intent = new Intent(
                                                            FindMimaActivity3.this,
                                                            FindMimaActivity4.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    String message = object.getString("Message");
                                                    if (message.equals("Error")) {
                                                        Intent intent = new Intent(FindMimaActivity3.this, FindMimaActivity1.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        MyToastUtil.showShortMessage(message);

                                                    }
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "两次密码输入不一致",
                                Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "新密码和确认密码都不能为空！",
                            Toast.LENGTH_SHORT).show();
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

    public String encryption(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }
}
