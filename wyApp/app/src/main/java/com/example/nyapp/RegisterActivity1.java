package com.example.nyapp;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.example.util.HttpconnectionUtil;
import com.example.util.UrlContact;
import com.example.util.HttpconnectionUtil.ReturnResult2;
import com.example.util.MyProgressDialog;

public class RegisterActivity1 extends BaseActivity{
	private EditText edit_phone;
	public static RegisterActivity1 registerActivity1;
	private String urlString= UrlContact.URL_STRING+"/api/User/GetRegistMobileVCode?mobile=";
	private Button btn_huoqu;
	private LinearLayout layout_back;
	private int daigou;
	private TextView text_regist;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.register_1);
		initView();
		
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		text_regist=(TextView) findViewById(R.id.text_regist);
		daigou=getIntent().getIntExtra("daigou", 0);
//		if (daigou==2) {
//			text_regist.setText("注册");
//		}else if(daigou == 1){
			text_regist.setText("注册");
//		}
		registerActivity1=this;
		layout_back=(LinearLayout) findViewById(R.id.layout_back);
		layout_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				setResult(11, intent);
				finish();
			}
		});
		edit_phone=(EditText) findViewById(R.id.edit_phone);
		btn_huoqu=(Button) findViewById(R.id.btn_huoqu);
		btn_huoqu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String phone=edit_phone.getText().toString().trim();
//				Intent intent=new Intent(RegisterActivity1.this,RegisterActivity2.class);
//				intent.putExtra("phone", phone);
//				startActivity(intent);
				if (phone.equals("")) {
					Toast.makeText(getApplicationContext(), "手机不能为空", Toast.LENGTH_SHORT).show();
				} else {
					if (isMobile(phone)) {
						urlString=urlString+phone+"&deviceId="+MyApplication.sUdid;
						MyProgressDialog.show(RegisterActivity1.this, true, true);
						Map<String, String> params2 = new HashMap<String, String>();
						HttpconnectionUtil.uploadFile(
								RegisterActivity1.this,
								new ReturnResult2() {

									@Override
									public void getResult(String result) {
										// TODO Auto-generated method stub
										MyProgressDialog.cancel();
										try {
											if (result.equals("error")) {
												Toast.makeText(getApplicationContext(), "网络断开连接！", Toast.LENGTH_SHORT).show();
											}if (result.equals("")) {
												Toast.makeText(getApplicationContext(), "服务器异常！", Toast.LENGTH_SHORT).show();
											}if (!result.equals("")&&!result.equals("error")) {
											JSONObject jsonObject=new JSONObject(result);
											Boolean Result=jsonObject.getBoolean("Result");
											if (Result) {
												String message=jsonObject.getString("Message");
												Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
												Intent intent=new Intent(RegisterActivity1.this,RegisterActivity2.class);
												intent.putExtra("phone", phone);
												intent.putExtra("daigou", daigou);
												startActivityForResult(intent, 22);
												finish();
												
											}else {
												String message=jsonObject.getString("Message");
												Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
											}
											
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
									}
								}, Method.GET,
								urlString,
								params2);
					}else {
						Toast.makeText(getApplicationContext(), "手机格式不正确", Toast.LENGTH_SHORT).show();

					}
				}
			}
		});
	}
	
	
	public static boolean isMobile(String mobiles){
		String regExp = "^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9])|(14[0-9])|(16[0-9]))\\d{8}$";  
		  
		Pattern p = Pattern.compile(regExp);  
		  
	Matcher m = p.matcher(mobiles);  
		  
	return m.find();//boolean  
	}
	
}
