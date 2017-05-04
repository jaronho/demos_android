package com.example.nyapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.classes.MyGPS;
import com.example.classes.User;
import com.example.classes.UserJsonUtil;
import com.example.util.MyMsgDialog;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

import ASimpleCache.org.afinal.simplecache.ACache;
import okhttp3.Call;

public class LoginActivity extends BaseActivity {
    //	private Button btn_trgister;
    private LinearLayout layout_back;
    private ImageView image_code;
    ImageRequest imageRequest;
    private EditText username, password, code;
    private Button login;
    private ACache mCache;
    private TextView btn_daigou;
    private int daigou;
    private TextView text_find;
    private MyGPS gps;
    private FragmentTransaction transaction;
    private FragmentManager manager;
    private User user;
    private MyMsgDialog mUpdateAddDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.login);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
//		btn_trgister = (Button) findViewById(R.id.btn_trgister);
        btn_daigou = (TextView) findViewById(R.id.btn_daigou);
        image_code = (ImageView) findViewById(R.id.image_code);
        imageRequest = new ImageRequest(UrlContact.URL_STRING
                + "/api/User/GetVCode" + "?deviceId=" + MyApplication.sUdid,
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
        image_code.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                MyVolley.getRequestQueue().add(imageRequest);

            }
        });
        btn_daigou.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this,
                        RegisterActivity1.class);
                intent.putExtra("daigou", 2);
                startActivityForResult(intent, 11);

            }
        });
        layout_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        username = (EditText) findViewById(R.id.username);

        password = (EditText) findViewById(R.id.password);
        password.setTransformationMethod(new PasswordTransformationMethod());//密码内容隐藏

        code = (EditText) findViewById(R.id.code);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (!username.getText().toString().equals("")
                        && !password.getText().toString().equals("")
                        && !code.getText().toString().equals("")) {
                    String password2 = encryption(password.getText().toString())
                            .toUpperCase();
                    String name = username.getText().toString();
                    try {
                        name = URLEncoder.encode(name, "UTF-8");
                    } catch (UnsupportedEncodingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    MyProgressDialog.show(LoginActivity.this, true, true);
                    Map<String, String> params2 = new TreeMap<>();
                    params2.put("User_Name", name);
                    params2.put("vCode", code.getText().toString());
                    params2.put("deviceId", MyApplication.sUdid);
                    params2.put("User_Pwd", password2);
                    MyOkHttpUtils
                            .getData(UrlContact.URL_STRING + "/api/User/Login", params2)
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
                                            user = UserJsonUtil.getUser(response);
                                            try {
                                                mCache = ACache.get(LoginActivity.this);
                                                mCache.put("loginString", "true");
                                                mCache.put("user", user);
                                                if (user.getArea_Name().equals("") || user.getArea_Name() == null) {

                                                } else {

                                                    String addres = user.getArea_Name();
                                                    addres = URLDecoder.decode(addres, "UTF-8");
                                                    gps = (MyGPS) mCache.getAsObject("gps");
                                                    if (gps == null) {
                                                        gps = new MyGPS();
                                                    }
                                                    gps.setProId(user.getProvince_Id());
                                                    gps.setCityId(user.getCity_Id());
                                                    gps.setCouId(user.getCounty_Id());
                                                    gps.setTownId(user.getTown_Id());
                                                    String add[] = addres.split(",");
                                                    gps.setProvinceName(add[0]);
                                                    gps.setCityName(add[1]);
                                                    gps.setCountyName(add[2]);
                                                    if (add.length > 3) {
                                                        gps.setTownName(add[3]);
                                                    } else {
                                                        gps.setTownName(null);
                                                    }
                                                    mCache.put("gpsString", "true");
                                                    mCache.put("gps", gps);
                                                }
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            if (message.equals("UpdateAddress")) {
                                                showDailog2("您的注册地址不完整，请完善注册地址");
                                            } else {
                                                finish();
                                            }

                                        } else {
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
        });
        text_find = (TextView) findViewById(R.id.text_find);
        text_find.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this,
                        FindMimaActivity1.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

    private void showDailog2(String message) {
        mUpdateAddDialog = new MyMsgDialog(this, "温馨提示", message,
                new MyMsgDialog.ConfirmListener() {
                    @Override
                    public void onClick() {
                        mUpdateAddDialog.dismiss();
                        finish();
                        Intent intent = new Intent(LoginActivity.this, TuanGouActivity.class);
                        intent.putExtra("payurl", UrlContact.WEB_URL_STRING + "/User/UpdateAddress?loginKey=" + user.getUser_Name() + "&deviceId=" + MyApplication.sUdid);
                        intent.putExtra("type", 7);
                        intent.putExtra("title", "完善地址信息");
                        startActivity(intent);
                    }
                }, null);
        mUpdateAddDialog.show();
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

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MyVolley.getRequestQueue().add(imageRequest);
    }
}
