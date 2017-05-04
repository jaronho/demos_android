package com.example.nyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RegisterActivity4 extends BaseActivity{
	private TextView text_zhanghao;
	private Button btn_gouwu,btn_wanshan;
	private String phone;
	private LinearLayout layout_back;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_4);
		phone=getIntent().getStringExtra("phone");
		text_zhanghao=(TextView) findViewById(R.id.text_zhanghao);
		text_zhanghao.setText("用户名为："+phone);
		btn_gouwu=(Button) findViewById(R.id.btn_gouwu);
		btn_wanshan=(Button) findViewById(R.id.btn_wanshan);
		layout_back=(LinearLayout) findViewById(R.id.layout_back);
		btn_gouwu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(RegisterActivity4.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		layout_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(RegisterActivity4.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		btn_wanshan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(RegisterActivity4.this, UserInfoActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}
}
