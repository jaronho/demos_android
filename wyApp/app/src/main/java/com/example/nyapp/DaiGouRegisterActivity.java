package com.example.nyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.example.classes.User;
import com.example.classes.UserJsonUtil;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

import ASimpleCache.org.afinal.simplecache.ACache;
import okhttp3.Call;

public class DaiGouRegisterActivity extends BaseActivity {
    private RadioGroup shoukuanGroup;
    private EditText edit_kahao, edit_name, edit_kaihuhang, edit_kaihuhang1, edit_kaihuhang2, edit_kaihuhang3, edit_kaihuhang4;
    private RelativeLayout kaihu_layout, kaihu_layout1, kaihu_layout2, kaihu_layout3, kaihu_layout4;
    private RadioButton radiozhifubao, radioyinhangka;
    private Button btn_xiayibu;
    private String phone, code, password, povince, citys, areas;
    private ASimpleCache.org.afinal.simplecache.ACache mCache;
    private LinearLayout layout_back;

    @Override
    public void initView() {

    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.daigouregist);
        phone = getIntent().getStringExtra("phone");
        code = getIntent().getStringExtra("code");
        password = getIntent().getStringExtra("password");
        povince = getIntent().getStringExtra("povince");
        citys = getIntent().getStringExtra("citys");
        areas = getIntent().getStringExtra("areas");
        shoukuanGroup = (RadioGroup) findViewById(R.id.shoukuanGroup);
        edit_kahao = (EditText) findViewById(R.id.edit_kahao);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_kaihuhang = (EditText) findViewById(R.id.edit_kaihuhang);
        kaihu_layout = (RelativeLayout) findViewById(R.id.kaihu_layout);
        radiozhifubao = (RadioButton) findViewById(R.id.radiozhifubao);
        radioyinhangka = (RadioButton) findViewById(R.id.radioyinhangka);
        btn_xiayibu = (Button) findViewById(R.id.btn_xiayibu);


        kaihu_layout1 = (RelativeLayout) findViewById(R.id.kaihu_layout1);
        edit_kaihuhang1 = (EditText) findViewById(R.id.edit_kaihuhang1);

        kaihu_layout2 = (RelativeLayout) findViewById(R.id.kaihu_layout2);
        edit_kaihuhang2 = (EditText) findViewById(R.id.edit_kaihuhang2);

        kaihu_layout3 = (RelativeLayout) findViewById(R.id.kaihu_layout3);
        edit_kaihuhang3 = (EditText) findViewById(R.id.edit_kaihuhang3);

        kaihu_layout4 = (RelativeLayout) findViewById(R.id.kaihu_layout4);
        edit_kaihuhang4 = (EditText) findViewById(R.id.edit_kaihuhang4);


        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layout_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        shoukuanGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int radioButtonId = arg0.getCheckedRadioButtonId();
                if (radioButtonId == R.id.radiozhifubao) {
                    kaihu_layout.setVisibility(View.GONE);
                    kaihu_layout1.setVisibility(View.GONE);
                    kaihu_layout2.setVisibility(View.GONE);
                    kaihu_layout3.setVisibility(View.GONE);
                    kaihu_layout4.setVisibility(View.GONE);
                    edit_kahao.setText("");
                    edit_name.setText("");
                    edit_kahao.setHint("支付宝账号");
                } else {
                    edit_kahao.setHint("银行卡账号");
                    edit_kahao.setText("");
                    edit_name.setText("");
                    edit_kaihuhang.setText("");
                    kaihu_layout.setVisibility(View.VISIBLE);

                    edit_kaihuhang1.setHint("省");
                    edit_kaihuhang1.setText("");
                    kaihu_layout1.setVisibility(View.VISIBLE);

                    edit_kaihuhang2.setHint("市");
                    edit_kaihuhang2.setText("");
                    kaihu_layout2.setVisibility(View.VISIBLE);

                    edit_kaihuhang3.setHint("县");
                    edit_kaihuhang3.setText("");
                    kaihu_layout3.setVisibility(View.VISIBLE);


                    edit_kaihuhang4.setHint("支行");
                    edit_kaihuhang4.setText("");
                    kaihu_layout4.setVisibility(View.VISIBLE);
                }
            }
        });
        btn_xiayibu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (radiozhifubao.isChecked()) {
                    if (!edit_kahao.getText().toString().equals("") && !edit_name.getText().toString().equals("")) {
                        MyProgressDialog.show(DaiGouRegisterActivity.this, true, true);

                        Map<String, String> params2 = new TreeMap<>();
                        params2.put("deviceId", MyApplication.sUdid);
                        params2.put("User_Name", phone);
                        params2.put("User_Pwd", password);
                        params2.put("VCode", code);
                        params2.put("txtProvince", povince);
                        params2.put("txtCity", citys);
                        params2.put("txtCounty", areas);
                        params2.put("CardType", 2 + "");
                        params2.put("Account_Bank", "支付宝");
                        params2.put("Account_Card", edit_kahao.getText().toString());
                        params2.put("Account_Name", edit_name.getText().toString());

                        MyOkHttpUtils
                                .postData(UrlContact.URL_STRING + "/api/User/PurchasingRegist",params2)
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
                                            if (Result) {
//													String message=jsonObject.getString("Message");
//													Toast.makeText(getApplicationContext(), message, 0).show();
                                                User user = UserJsonUtil.getUser(response);

                                                Intent intent = new Intent(DaiGouRegisterActivity.this, RegisterActivity4.class);
                                                intent.putExtra("phone", phone);
                                                startActivityForResult(intent, 22);
                                                mCache = ACache.get(DaiGouRegisterActivity.this);
                                                mCache.put("loginString", "true");
                                                mCache.put("user", user);
                                                finish();

                                            } else {
                                                String message = jsonObject.getString("Message");
                                                MyToastUtil.showShortMessage(message);
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    } else {
                        MyToastUtil.showShortMessage("请全部填写!");
                    }

                } else {
                    if (!edit_kahao.getText().toString().equals("") && !edit_name.getText().toString().equals("") && !edit_kaihuhang.getText().toString().equals("")) {
                        Map<String, String> params2 = new TreeMap<>();
                        params2.put("deviceId", MyApplication.sUdid);
                        params2.put("User_Name", phone);
                        params2.put("User_Pwd", password);
                        params2.put("VCode", code);
                        params2.put("txtProvince", povince);
                        params2.put("txtCity", citys);
                        params2.put("txtCounty", areas);
                        params2.put("CardType", 1 + "");
                        String temp = edit_kaihuhang.getText().toString() +
                                edit_kaihuhang1.getText().toString() +
                                edit_kaihuhang2.getText().toString() +
                                edit_kaihuhang3.getText().toString() +
                                edit_kaihuhang4.getText().toString();
                        params2.put("Account_Bank", temp);
                        params2.put("Account_Card", edit_kahao.getText().toString());
                        params2.put("Account_Name", edit_name.getText().toString());

                        MyOkHttpUtils
                                .postData(UrlContact.URL_STRING + "/api/User/PurchasingRegist",params2)
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
                                            if (Result) {
//													String message=jsonObject.getString("Message");
//													Toast.makeText(getApplicationContext(), message, 0).show();
                                                User user = UserJsonUtil.getUser(response);

                                                Intent intent = new Intent(DaiGouRegisterActivity.this, RegisterActivity4.class);
                                                intent.putExtra("phone", phone);
                                                startActivityForResult(intent, 22);
                                                mCache = ACache.get(DaiGouRegisterActivity.this);
                                                mCache.put("loginString", "true");
                                                mCache.put("user", user);
                                            } else {
                                                String message = jsonObject.getString("Message");
                                                MyToastUtil.showShortMessage(message);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    } else {
                        MyToastUtil.showShortMessage("请全部填写");
                    }
                }
            }
        });
    }
}
