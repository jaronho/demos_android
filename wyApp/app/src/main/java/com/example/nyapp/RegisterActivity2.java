package com.example.nyapp;


import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class RegisterActivity2 extends BaseActivity{
	private EditText edit_yanzhengma;
	public static RegisterActivity2 registerActivity2;
	private Button btn_xiayibu2;
	private TextView text_tishi,text_regist;
	private String urlString= UrlContact.URL_STRING+"/api/User/GetRegistMobileVCode?mobile=";
	private String phone;
	private LinearLayout layout_back;
	private int daigou;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.register_2);
		initView();
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		text_regist=(TextView) findViewById(R.id.text_regist);
		daigou=getIntent().getIntExtra("daigou", 0);
//		if (daigou==2) {
//			text_regist.setText("注册代购");
//		}else if(daigou == 1){
			text_regist.setText("注册");
//		}
		registerActivity2=this;
		layout_back=(LinearLayout) findViewById(R.id.layout_back);
		layout_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				finish();
			}
		});
		phone=getIntent().getStringExtra("phone");
		text_tishi=(TextView) findViewById(R.id.text_tishi);
		text_tishi.setText("验证码短信已发送到手机"+phone+",请输入收到的验证码");
		btn_xiayibu2=(Button) findViewById(R.id.btn_xiayibu2);
		edit_yanzhengma=(EditText) findViewById(R.id.edit_yanzhengma);
		btn_xiayibu2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (edit_yanzhengma.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), "验证码不能为空", 0).show();
					
				}else {
					String string= UrlContact.URL_STRING+"/api/User/ValidateRegistVCode?mobileVCode="+edit_yanzhengma.getText().toString()+"&deviceId="+MyApplication.sUdid;
					MyProgressDialog.show(RegisterActivity2.this, true, true);
					Map<String, String> params2 = new HashMap<String, String>();
					HttpconnectionUtil.uploadFile(
							RegisterActivity2.this,
							new ReturnResult2() {

								@Override
								public void getResult(String result) {
									// TODO Auto-generated method stub
									MyProgressDialog.cancel();
									try {
										if (result.equals("error")) {
											Toast.makeText(getApplicationContext(), "网络断开连接！", 0).show();
										}if (result.equals("")) {
											Toast.makeText(getApplicationContext(), "服务器异常！", 0).show();
										}if (!result.equals("")&&!result.equals("error")) {
										JSONObject jsonObject=new JSONObject(result);
										Boolean Result=jsonObject.getBoolean("Result");
										if (Result) {
											String message=jsonObject.getString("Message");
//											Toast.makeText(getApplicationContext(), message, 0).show();
											Intent intent=new Intent(RegisterActivity2.this,RegisterActivity3.class);
											intent.putExtra("code", edit_yanzhengma.getText().toString());
											intent.putExtra("phone", phone);
											intent.putExtra("daigou", daigou);
											startActivityForResult(intent, 33);
											finish();
											
										}else {
											String message=jsonObject.getString("Message");
											Toast.makeText(getApplicationContext(), message, 0).show();
										}
										}

									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
								}
							}, Method.GET,
							string,
							params2);
				
				}
			}
		});
		tv=(TextView) findViewById(R.id.tv);
		 jishi=120;
		 tv.setEnabled(false);
            timer=new Timer();
            timer.schedule(new TimerTask()
            {
                    
                    @Override
                    public void run()
                    {
                            handler.sendEmptyMessage(jishi--);
                    }
            }, 0, 1000);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				urlString=urlString+phone+"&deviceId="+MyApplication.sUdid;
				MyProgressDialog.show(RegisterActivity2.this, true, true);
				Map<String, String> params2 = new HashMap<String, String>();
				HttpconnectionUtil.uploadFile(
						RegisterActivity2.this,
						new ReturnResult2() {

							@Override
							public void getResult(String result) {
								// TODO Auto-generated method stub
								MyProgressDialog.cancel();
								try {
									if (result.equals("error")) {
										Toast.makeText(getApplicationContext(), "网络断开连接！", 0).show();
									}if (result.equals("")) {
										Toast.makeText(getApplicationContext(), "服务器异常！", 0).show();
									}if (!result.equals("")&&!result.equals("error")) {
									JSONObject jsonObject=new JSONObject(result);
									Boolean Result=jsonObject.getBoolean("Result");
									if (Result) {
										String message=jsonObject.getString("Message");
										Toast.makeText(getApplicationContext(), message, 0).show();
										
									}else {
										String message=jsonObject.getString("Message");
										Toast.makeText(getApplicationContext(), message, 0).show();
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
				 jishi=120;
				 tv.setEnabled(false);
		            timer=new Timer();
		            timer.schedule(new TimerTask()
		            {
		                    
		                    @Override
		                    public void run()
		                    {
		                            handler.sendEmptyMessage(jishi--);
		                    }
		            }, 0, 1000);
			}
		});
	}

    private String code;//验证码
    private Timer timer;//计时器
    private TextView tv;
    int jishi=60;
    
    private Handler  handler=new Handler(){
    public void handleMessage(android.os.Message msg) {
            if(msg.what==0){
                    tv.setEnabled(true);
                    tv.setText("重新获取验证码");
                    timer.cancel();
                    }else{
                            tv.setText(msg.what+"秒后重新获取验证码");
                    }
    };
    };
    
    @Override
    public void onDestroy()
    {
            if(timer!=null)
                    timer.cancel();
            super.onDestroy();
    }


}
