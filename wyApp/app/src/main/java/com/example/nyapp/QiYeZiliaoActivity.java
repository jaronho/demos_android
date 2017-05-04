package com.example.nyapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.example.classes.ManufLianXi;
import com.example.classes.ManufLianXiJsonUtil;
import com.example.util.HttpconnectionUtil.ReturnResult2;
import com.example.util.UrlContact;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QiYeZiliaoActivity extends BaseActivity{
	private String urlString= UrlContact.URL_STRING+"/api/product/GetMoreContact?ManufId=";
	private int ManufId;
	private ManufLianXi manufLianXi;
	private TextView text_address,text_phone,text_work;
	private LinearLayout qqlist,layout_back;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.qiyeziliao);
		ManufId=getIntent().getIntExtra("ManufId", 0);
		urlString=urlString+ManufId;
		text_address=(TextView) findViewById(R.id.text_address);
		text_phone=(TextView) findViewById(R.id.text_phone);
		text_work=(TextView) findViewById(R.id.text_work);
		qqlist=(LinearLayout) findViewById(R.id.qqlist);
		layout_back=(LinearLayout) findViewById(R.id.layout_back);
		layout_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		 Map<String,String> params = new HashMap<String, String>();
		com.example.util.HttpconnectionUtil.uploadFile(QiYeZiliaoActivity.this, new ReturnResult2() {
			
			@Override
			public void getResult(String result) {
				// TODO Auto-generated method stub
				if (result.equals("error")) {
					Toast.makeText(getApplicationContext(), "网络断开连接！", Toast.LENGTH_SHORT).show();
				}if (result.equals("")) {
					Toast.makeText(getApplicationContext(), "服务器异常！", Toast.LENGTH_SHORT).show();
				}if (!result.equals("")&&!result.equals("error")) {
					try {
						if (new JSONObject(result).getBoolean("Result")) {
							manufLianXi=ManufLianXiJsonUtil.getManufLianXi(result);
							text_address.setText(manufLianXi.getManufAddress());
							text_phone.setText(manufLianXi.getManufPhone());
							text_work.setText(manufLianXi.getWorkTime());
							if (manufLianXi.getQQList()==null) {
								
							}else {
								for (int i = 0; i < manufLianXi.getQQList().size(); i++) {
									View view=LayoutInflater.from(QiYeZiliaoActivity.this).inflate(R.layout.qq_item, null);
									TextView textView1=(TextView) view.findViewById(R.id.text_kefu);
									TextView textView2=(TextView) view.findViewById(R.id.text_qq);
									textView1.setText("客服QQ"+i);
									textView2.setText(manufLianXi.getQQList().get(i));
									qqlist.addView(view);
								}
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}
			}
		}, Method.GET, urlString, params);
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}

}
