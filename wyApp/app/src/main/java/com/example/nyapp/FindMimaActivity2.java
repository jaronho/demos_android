package com.example.nyapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.util.MyOkHttpUtils;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

public class FindMimaActivity2 extends BaseActivity {
    private Button find2_next;
    private LinearLayout layout_back;
    private String phone;
    private String username;
    private EditText text_findname2, text_findcode2, text_findphone;
    private Button find_phonecode2;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.find2);
        phone = getIntent().getStringExtra("phone");
        username = getIntent().getStringExtra("username");
        text_findname2 = (EditText) findViewById(R.id.text_findname2);
        text_findcode2 = (EditText) findViewById(R.id.text_findcode2);
        text_findphone = (EditText) findViewById(R.id.text_findphone);
        text_findname2.setText(username);
        text_findphone.setText(phone);
        text_findname2.setFocusableInTouchMode(false);
        text_findname2.setCursorVisible(false);
        text_findname2.setEnabled(false);
        text_findphone.setFocusableInTouchMode(false);
        text_findphone.setCursorVisible(false);
        text_findphone.setEnabled(false);
        find_phonecode2 = (Button) findViewById(R.id.find_phonecode2);
        find_phonecode2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Map<String, String> params2 = new HashMap<String, String>();
                params2.put("userName", username);
                params2.put("deviceId", MyApplication.sUdid);
                MyOkHttpUtils
                        .postData(UrlContact.URL_STRING + "/api/user/FindPwdSendMVCode", params2)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                MyToastUtil.showShortMessage("服务器连接失败!");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    jishi = 120;
                                    find_phonecode2.setEnabled(false);
                                    find_phonecode2.setBackgroundColor(Color.parseColor("#b5b5b5"));
                                    timer = new Timer();
                                    timer.schedule(new TimerTask() {

                                        @Override
                                        public void run() {
                                            handler.sendEmptyMessage(jishi--);
                                        }
                                    }, 0, 1000);
                                    JSONObject object = new JSONObject(response);
                                    if (object.getBoolean("Result")) {
                                        MyToastUtil.showShortMessage(object.getString("Message"));
                                    } else {
                                        if (object.getString("Message").equals("Error")) {
                                            Intent intent = new Intent(FindMimaActivity2.this, FindMimaActivity1.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            MyToastUtil.showShortMessage(object.getString("Message"));
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
        find2_next = (Button) findViewById(R.id.find2_next);
        find2_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (text_findcode2.getText().toString().equals("")) {
                    MyToastUtil.showShortMessage("验证码不能为空！");
                } else {
                    Map<String, String> params2 = new HashMap<String, String>();
                    params2.put("userName", username);
                    params2.put("deviceId", MyApplication.sUdid);
                    params2.put("Code", text_findcode2.getText().toString());
                    MyOkHttpUtils
                            .postData(UrlContact.URL_STRING + "/api/user/FindPwdVerifyCode", params2)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    MyToastUtil.showShortMessage("服务器连接失败!");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    try {
//									System.out.println("res=------------------->"+result);
                                        JSONObject object = new JSONObject(response);
                                        if (object.getBoolean("Result")) {

                                                Intent intent = new Intent(FindMimaActivity2.this, FindMimaActivity3.class);
                                                intent.putExtra("username", username);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                if (object.getString("Message").equals("Error")) {
                                                    Intent intent = new Intent(FindMimaActivity2.this, FindMimaActivity1.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    MyToastUtil.showShortMessage(object.getString("Message"));

                                                }
                                            }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
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

    private Timer timer;//计时器
    int jishi = 60;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                find_phonecode2.setEnabled(true);
                find_phonecode2.setText("发送验证码");
                find_phonecode2.setBackgroundColor(Color.parseColor("#ff4b00"));
                timer.cancel();
            } else {
                find_phonecode2.setText(msg.what + "秒后重新获取验证码");
            }
        }

        ;
    };

    @Override
    public void onDestroy() {
        if (timer != null)
            timer.cancel();
        super.onDestroy();
    }
}
