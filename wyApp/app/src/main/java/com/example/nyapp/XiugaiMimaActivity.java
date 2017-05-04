package com.example.nyapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageRequest;
import com.example.classes.User;
import com.example.util.HttpconnectionUtil;
import com.example.util.MyProgressDialog;
import com.example.util.UrlContact;
import com.example.util.HttpconnectionUtil.ReturnResult2;

import ASimpleCache.org.afinal.simplecache.ACache;

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
import android.widget.Toast;

public class XiugaiMimaActivity extends BaseActivity {
    private EditText edit_dangqianpassword, edit_newpassword, edit_querennewpassword, xiugai_code;
    private ImageView image_code;
    private ASimpleCache.org.afinal.simplecache.ACache mCache;
    private User user;
    private Button btn_xiugai;
    ImageRequest imageRequest;
    private LinearLayout layout_back;

    @Override
    public void initView() {

    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.xiugaimima);
        mCache = ACache.get(this);
        user = (User) mCache.getAsObject("user");
        if (user == null) {

        } else {
        }
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layout_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        edit_dangqianpassword = (EditText) findViewById(R.id.edit_dangqianpassword);
        edit_newpassword = (EditText) findViewById(R.id.edit_newpassword);
        edit_querennewpassword = (EditText) findViewById(R.id.edit_querennewpassword);
        xiugai_code = (EditText) findViewById(R.id.xiugai_code);
        btn_xiugai = (Button) findViewById(R.id.btn_xiugai);
        image_code = (ImageView) findViewById(R.id.image_code);
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
                // TODO Auto-generated method stub
                MyVolley.getRequestQueue().add(imageRequest);

            }
        });
        btn_xiugai.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String oldpassword = edit_dangqianpassword.getText().toString();
                String newpassword = edit_newpassword.getText().toString();
                String querennewpassword = edit_querennewpassword.getText().toString();
                String code = xiugai_code.getText().toString();
                if (!oldpassword.equals("") && !newpassword.equals("") && !querennewpassword.equals("") && !code.equals("")) {
                    if (newpassword.equals(querennewpassword)) {
                        String uString = UrlContact.URL_STRING + "/api/UserCenter/ChangePwd?loginKey=" + user.getUser_Name() + "&deviceId=" + MyApplication.sUdid + "&vCode=" + xiugai_code.getText().toString() + "&oldPwd=" + encryption(oldpassword).toUpperCase() + "&newPwd=" + encryption(newpassword).toUpperCase();
                        MyProgressDialog.show(XiugaiMimaActivity.this, true, true);
                        Map<String, String> params2 = new HashMap<String, String>();
                        HttpconnectionUtil.uploadFile(
                                XiugaiMimaActivity.this,
                                new ReturnResult2() {

                                    @Override
                                    public void getResult(String result) {
                                        MyProgressDialog.cancel();
                                        try {
                                            if (result.equals("error")) {
                                                Toast.makeText(getApplicationContext(), "网络断开连接！", Toast.LENGTH_SHORT).show();
                                            }
                                            if (result.equals("")) {
                                                Toast.makeText(getApplicationContext(), "服务器异常！", Toast.LENGTH_SHORT).show();
                                            }
                                            if (!result.equals("") && !result.equals("error")) {
                                                if (new JSONObject(result).getBoolean("Result")) {
                                                    Toast.makeText(getApplicationContext(), new JSONObject(result).getString("Message"), Toast.LENGTH_SHORT).show();
                                                    User user = new User();
                                                    mCache.put("loginString", "false");
                                                    mCache.put("user", user);
                                                    Intent intent = new Intent(XiugaiMimaActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), new JSONObject(result).getString("Message"), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, Method.GET,
                                uString,
                                params2);
                    } else {
                        Toast.makeText(getApplicationContext(), "两次新密码不一致请重新输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请全部填写", Toast.LENGTH_SHORT).show();
                }

            }
        });

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
