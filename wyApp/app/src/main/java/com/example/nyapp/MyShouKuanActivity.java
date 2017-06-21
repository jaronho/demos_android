package com.example.nyapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classes.User;
import com.example.util.MyMsgDialog;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import ASimpleCache.org.afinal.simplecache.ACache;
import okhttp3.Call;

public class MyShouKuanActivity extends BaseActivity {
    private LinearLayout weitainxie_layout, yitainxie_layout;
    private int flag;
    private ASimpleCache.org.afinal.simplecache.ACache mCache;
    private User user;
    private Button btn_zhifu, btn_yinhangka, btn_bangding;
    private EditText edit_kahao, edit_name, edit_kaihuhang, edit_kaihuhang1, edit_kaihuhang3, edit_kaihuhang4,
            edit_kaihuhang5;
    private RelativeLayout kaihu_layout, kaihu_layout2, kaihu_layout1, kaihu_layout3, kaihu_layout4, kaihu_layout5;
    private TextView text_mingzi, text_kahuhang2, text_kahao2, text_fangshi2;
    private int flag2 = 1;
    private LinearLayout layout_back;
    private TextView text_huname;
    private Boolean Result_resume;
    private JSONObject object_resume;
    private int cardType_resume;
    private MyMsgDialog mDialog;

    @Override
    public void initView() {

    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_shoukuan);
        flag = getIntent().getIntExtra("flag", 0);
        mCache = ACache.get(this);
        user = (User) mCache.getAsObject("user");
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layout_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        text_huname = (TextView) findViewById(R.id.text_huname);
        yitainxie_layout = (LinearLayout) findViewById(R.id.yitainxie_layout);
        weitainxie_layout = (LinearLayout) findViewById(R.id.weitainxie_layout);
        btn_zhifu = (Button) findViewById(R.id.btn_zhifu);
        btn_yinhangka = (Button) findViewById(R.id.btn_yinhangka);
        btn_bangding = (Button) findViewById(R.id.btn_bangding);
        edit_kahao = (EditText) findViewById(R.id.edit_kahao);
        text_mingzi = (TextView) findViewById(R.id.text_mingzi);
        text_kahuhang2 = (TextView) findViewById(R.id.text_kahuhang2);
        text_kahao2 = (TextView) findViewById(R.id.text_kahao2);
        text_fangshi2 = (TextView) findViewById(R.id.text_fangshi2);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_kaihuhang = (EditText) findViewById(R.id.edit_kaihuhang);
        kaihu_layout = (RelativeLayout) findViewById(R.id.kaihu_layout);
        kaihu_layout2 = (RelativeLayout) findViewById(R.id.kaihu_layout2);

        kaihu_layout1 = (RelativeLayout) findViewById(R.id.kaihu_layout1);
        edit_kaihuhang1 = (EditText) findViewById(R.id.edit_kaihuhang1);

        kaihu_layout3 = (RelativeLayout) findViewById(R.id.kaihu_layout3);
        edit_kaihuhang3 = (EditText) findViewById(R.id.edit_kaihuhang3);

        kaihu_layout4 = (RelativeLayout) findViewById(R.id.kaihu_layout4);
        edit_kaihuhang4 = (EditText) findViewById(R.id.edit_kaihuhang4);

        kaihu_layout5 = (RelativeLayout) findViewById(R.id.kaihu_layout5);
        edit_kaihuhang5 = (EditText) findViewById(R.id.edit_kaihuhang5);

        if (flag == 1) {
            weitainxie_layout.setVisibility(View.GONE);
            yitainxie_layout.setVisibility(View.VISIBLE);
            Map<String, String> map = new TreeMap<>();
            map.put("loginKey", user.getUser_Name());
            map.put("deviceId", MyApplication.sUdid);
            MyOkHttpUtils
                    .getData(UrlContact.URL_STRING + "/api/UserCenter/GetBillingInformation", map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            MyToastUtil.showShortMessage("服务器连接失败!");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Boolean Result = jsonObject.getBoolean("Result");
                                if (Result) {
                                    JSONObject object = new JSONObject(jsonObject.getString("Data"));
                                    int cardType = object.getInt("CardType");
                                    if (cardType == 1) {
                                        text_fangshi2.setText("银行卡");
                                        kaihu_layout2.setVisibility(View.VISIBLE);
                                        text_kahao2.setText(object.getString("AccountCard"));
                                        text_mingzi.setText(object.getString("AccountName"));
                                        text_kahuhang2.setText(object.getString("AccountBank"));
                                        text_huname.setText("银行卡账号");
                                    } else {
                                        kaihu_layout2.setVisibility(View.GONE);
                                        text_fangshi2.setText("支付宝");
                                        text_huname.setText("支付宝账号");
                                        text_kahao2.setText(object.getString("AccountCard"));
                                        text_mingzi.setText(object.getString("AccountName"));
                                    }

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
            weitainxie_layout.setVisibility(View.VISIBLE);
            yitainxie_layout.setVisibility(View.GONE);

        }
        btn_zhifu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                btn_zhifu.setBackgroundColor(Color.parseColor("#4b90cb"));
                btn_yinhangka.setBackgroundColor(Color.parseColor("#ffffff"));
                flag2 = 1;
                // if (!Result_resume) {
                edit_kahao.setHint("支付宝账号");
                edit_kahao.setText("");
                edit_name.setHint("姓名");
                edit_name.setText("");
                // }else {
                try {
                    if (cardType_resume == 2) {
                        edit_kahao.setText(object_resume.getString("AccountCard"));
                        edit_name.setText(object_resume.getString("AccountName"));
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // }
                kaihu_layout.setVisibility(View.GONE);
                kaihu_layout1.setVisibility(View.GONE);
                kaihu_layout3.setVisibility(View.GONE);
                kaihu_layout4.setVisibility(View.GONE);
                kaihu_layout5.setVisibility(View.GONE);
            }
        });
        btn_yinhangka.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                btn_zhifu.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_yinhangka.setBackgroundColor(Color.parseColor("#4b90cb"));
                flag2 = 2;
                // if (!Result_resume) {
                edit_kahao.setHint("银行借记卡（储蓄卡）帐号");
                edit_kahao.setText("");

                edit_name.setHint("姓名");
                edit_name.setText("");

                edit_kaihuhang.setHint("开户银行");
                edit_kaihuhang.setText("");

                edit_kaihuhang1.setHint("省");
                edit_kaihuhang1.setText("");

                edit_kaihuhang3.setHint("市");
                edit_kaihuhang3.setText("");

                edit_kaihuhang4.setHint("县");
                edit_kaihuhang4.setText("");

                edit_kaihuhang5.setHint("支行");
                edit_kaihuhang5.setText("");
                // }else {
                try {
                    if (cardType_resume == 1) {

                        edit_kahao.setText(object_resume.getString("AccountCard"));
                        edit_name.setText(object_resume.getString("AccountName"));
                        edit_kaihuhang.setText(object_resume.getString("AccountBank"));
                        edit_kaihuhang1.setText(object_resume.getString("AccountProvince"));
                        edit_kaihuhang3.setText(object_resume.getString("AccountCounty"));
                        edit_kaihuhang4.setText(object_resume.getString("AccountCity"));
                        edit_kaihuhang5.setText(object_resume.getString("AccountSubBank"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // }
                kaihu_layout.setVisibility(View.VISIBLE);
                kaihu_layout1.setVisibility(View.VISIBLE);
                kaihu_layout3.setVisibility(View.VISIBLE);
                kaihu_layout4.setVisibility(View.VISIBLE);
                kaihu_layout5.setVisibility(View.VISIBLE);
            }
        });
        btn_bangding.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Map<String, String> params2 = new HashMap<String, String>();
                String message = "";
                params2.put("IsPurchasing", "false");
                params2.put("loginKey", user.getUser_Name());
                params2.put("deviceId", MyApplication.sUdid);
                params2.put("AccountCard", edit_kahao.getText().toString());
                params2.put("AccountName", edit_name.getText().toString());
                if (flag2 == 1) {
                    params2.put("CardType", 2 + "");
                    params2.put("AccountBank", "支付宝");
                    message = "支付宝账号：" + edit_kahao.getText().toString() + "\n" +
                            "姓名：" + edit_name.getText().toString();
                    if (edit_kahao.getText().toString().equals("") || edit_name.getText().toString().equals("")) {
                        Toast.makeText(MyShouKuanActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    } else {
                        showDialog(message, params2);
                    }
                } else {
                    params2.put("CardType", 1 + "");
                    params2.put("AccountBank", edit_kaihuhang.getText().toString());
                    params2.put("AccountProvince", edit_kaihuhang1.getText().toString());
                    params2.put("AccountCity", edit_kaihuhang3.getText().toString());
                    params2.put("AccountCounty", edit_kaihuhang4.getText().toString());
                    params2.put("AccountSubBank", edit_kaihuhang5.getText().toString());

                    message = "银行卡号：" + edit_kahao.getText().toString() + "\n" +
                            "开户银行：" + edit_kaihuhang.getText().toString() +
                            edit_kaihuhang1.getText().toString() +
                            edit_kaihuhang3.getText().toString() +
                            edit_kaihuhang4.getText().toString() +
                            edit_kaihuhang5.getText().toString() + "\n" +
                            "姓名：" + edit_name.getText().toString();
                    if (edit_kahao.getText().toString().equals("") ||
                            edit_name.getText().toString().equals("") ||
                            edit_kaihuhang.getText().toString().equals("") ||
                            edit_kaihuhang1.getText().toString().equals("") ||
                            edit_kaihuhang3.getText().toString().equals("") ||
                            edit_kaihuhang4.getText().toString().equals("") ||
                            edit_kaihuhang5.getText().toString().equals("")
                            ) {
                        Toast.makeText(MyShouKuanActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    } else {
                        showDialog(message, params2);
                    }
                }


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user != null) {
            Map<String, String> map = new TreeMap<>();
            map.put("loginKey", user.getUser_Name());
            map.put("deviceId", MyApplication.sUdid);

            MyProgressDialog.show(MyShouKuanActivity.this, true, true);
            MyOkHttpUtils
                    .getData(UrlContact.URL_STRING + "/api/usercenter/BillInfo", map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            MyToastUtil.showShortMessage("服务器连接失败!");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            MyProgressDialog.cancel();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Result_resume = jsonObject.getBoolean("Result");
                                if (Result_resume) {

                                    object_resume = new JSONObject(jsonObject.getString("Data"));
                                    cardType_resume = object_resume.getInt("CardType");
                                    if (cardType_resume == 1) {
                                        btn_zhifu.setBackgroundColor(Color.parseColor("#ffffff"));
                                        btn_yinhangka.setBackgroundColor(Color.parseColor("#4b90cb"));
                                        flag2 = 2;
                                        edit_kahao.setHint("银行借记卡（储蓄卡）帐号");
                                        edit_name.setHint("姓名");
                                        edit_kaihuhang.setHint("开户银行");
                                        edit_kaihuhang1.setHint("省");
                                        edit_kaihuhang3.setHint("市");
                                        edit_kaihuhang4.setHint("县");
                                        edit_kaihuhang5.setHint("支行");
                                        edit_kahao.setText(object_resume.getString("AccountCard"));
                                        edit_name.setText(object_resume.getString("AccountName"));
                                        kaihu_layout.setVisibility(View.VISIBLE);
                                        edit_kaihuhang.setText(object_resume.getString("AccountBank"));
                                        kaihu_layout1.setVisibility(View.VISIBLE);
                                        edit_kaihuhang1.setText(object_resume.getString("AccountProvince"));
                                        kaihu_layout3.setVisibility(View.VISIBLE);
                                        edit_kaihuhang3.setText(object_resume.getString("AccountCounty"));

                                        kaihu_layout4.setVisibility(View.VISIBLE);
                                        edit_kaihuhang4.setText(object_resume.getString("AccountCity"));

                                        kaihu_layout5.setVisibility(View.VISIBLE);
                                        edit_kaihuhang5.setText(object_resume.getString("AccountSubBank"));


                                    } else {
                                        btn_zhifu.setBackgroundColor(Color.parseColor("#4b90cb"));
                                        btn_yinhangka.setBackgroundColor(Color.parseColor("#ffffff"));
                                        flag2 = 1;
                                        edit_kahao.setHint("支付宝账号");
                                        edit_name.setHint("姓名");
                                        edit_kahao.setText(object_resume.getString("AccountCard"));
                                        edit_name.setText(object_resume.getString("AccountName"));
                                        kaihu_layout.setVisibility(View.GONE);
                                        kaihu_layout1.setVisibility(View.GONE);
                                        kaihu_layout3.setVisibility(View.GONE);
                                        kaihu_layout4.setVisibility(View.GONE);
                                        kaihu_layout5.setVisibility(View.GONE);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    private void showDialog(String message, final Map<String, String> params2) {
        mDialog = new MyMsgDialog(this, "您要保存的收款信息是：", message, new MyMsgDialog.ConfirmListener() {
            @Override
            public void onClick() {
                mDialog.dismiss();
                MyOkHttpUtils
                        .postData(UrlContact.URL_STRING + "/api/UserCenter/BillingInformation", params2)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                MyToastUtil.showShortMessage("服务器连接失败!");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Boolean Result = jsonObject.getBoolean("Result");
                                    String message = jsonObject.getString("Message");
                                    if (Result) {
                                        MyToastUtil.showShortMessage(message + "申请已提交");
                                        finish();
                                    } else {
                                        MyToastUtil.showShortMessage(message);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                finish();
            }
        }, null);
        mDialog.show();
    }
}
