package com.example.nyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class FindMimaActivity4 extends BaseActivity{
	private Button find4_next;
	 private LinearLayout layout_back;
		@Override
		protected void onCreate(Bundle arg0) {
			// TODO Auto-generated method stub
			super.onCreate(arg0);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.find4);
			find4_next=(Button) findViewById(R.id.find4_next);
			find4_next.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//					Intent intent=new Intent(FindMimaActivity4.this,MainActivity.class);
//					startActivity(intent);
					finish();
					
				}
			});
			layout_back=(LinearLayout) findViewById(R.id.layout_back);
	   		layout_back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}

}
