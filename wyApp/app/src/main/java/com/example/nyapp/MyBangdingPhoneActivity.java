package com.example.nyapp;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classes.User;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ASimpleCache.org.afinal.simplecache.ACache;
import okhttp3.Call;

public class MyBangdingPhoneActivity extends BaseActivity {
    private int flag;
    private String mobileString;
    private TextView text_tishi;
    private LinearLayout bangding_layout;
    private Button btn_bangding;
    private EditText edit_bangdingphone, edit_code;
    private ASimpleCache.org.afinal.simplecache.ACache mCache;
    private User user;
    private TextView fasong_code;
    private LinearLayout layout_back;
    private ImageView image_duihao;

    @Override
    public void initView() {

    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bangdingphone);
        mCache = ACache.get(this);
        user = (User) mCache.getAsObject("user");
        if (user == null) {

        } else {
        }
        image_duihao = (ImageView) findViewById(R.id.image_duihao);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layout_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        edit_bangdingphone = (EditText) findViewById(R.id.edit_bangdingphone);
        bangding_layout = (LinearLayout) findViewById(R.id.bangding_layout);
        text_tishi = (TextView) findViewById(R.id.text_tishi);
        btn_bangding = (Button) findViewById(R.id.btn_bangding);
        fasong_code = (TextView) findViewById(R.id.fasong_code);
        edit_code = (EditText) findViewById(R.id.edit_code);
        flag = getIntent().getIntExtra("bangding", 0);
        if (flag == 1) {

        } else {
            mobileString = getIntent().getStringExtra("phone");
            text_tishi.setVisibility(View.VISIBLE);
            bangding_layout.setVisibility(View.GONE);
            image_duihao.setVisibility(View.VISIBLE);
            text_tishi.setText("你已经绑定手机" + mobileString);
        }
        fasong_code.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (edit_bangdingphone.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "手机不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    String pString = edit_bangdingphone.getText().toString().trim();
                    if (isMobile(pString)) {
                        MyProgressDialog.show(MyBangdingPhoneActivity.this, true, true);
                        Map<String, String> params2 = new TreeMap<>();
                        params2.put("loginKey", user.getUser_Name());
                        params2.put("deviceId", MyApplication.sUdid);
                        params2.put("mobile", edit_bangdingphone.getText().toString());
                        params2.put("bind", "true");
                        MyOkHttpUtils
                                .getData(UrlContact.URL_STRING + "/api/UserCenter/SendVCode", params2)
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        MyProgressDialog.cancel();
                                        MyToastUtil.showShortMessage("服务器连接失败!");
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        MyProgressDialog.cancel();
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String message = jsonObject.getString("Message");
                                            MyToastUtil.showShortMessage(message);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    } else {
                        MyToastUtil.showShortMessage("手机格式不正确！");
                    }

                }

            }
        });
        btn_bangding.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (edit_code.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    MyProgressDialog.show(MyBangdingPhoneActivity.this, true, true);
                    Map<String, String> params2 = new TreeMap<>();
                    params2.put("loginKey", user.getUser_Name());
                    params2.put("deviceId", MyApplication.sUdid);
                    params2.put("vCode", edit_code.getText().toString());
                    MyOkHttpUtils
                            .getData(UrlContact.URL_STRING + "/api/UserCenter/BindMobile", params2)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    MyProgressDialog.cancel();
                                    MyToastUtil.showShortMessage("服务器连接失败!");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    MyProgressDialog.cancel();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Boolean Result = jsonObject.getBoolean("Result");
                                        String message = jsonObject.getString("Message");
                                        if (Result) {
                                            user.setMobile(edit_bangdingphone.getText().toString());
                                            mCache.put("user", user);
                                            finish();
                                        }
                                        MyToastUtil.showShortMessage(message);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }

            }
        });

    }

    public static boolean isMobile(String mobiles) {
        String regExp = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

        Pattern p = Pattern.compile(regExp);

        Matcher m = p.matcher(mobiles);

        return m.find();//boolean
    }


}
