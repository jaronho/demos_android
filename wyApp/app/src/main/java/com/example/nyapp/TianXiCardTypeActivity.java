package com.example.nyapp;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.example.classes.User;
import com.example.util.HttpconnectionUtil;
import com.example.util.MyProgressDialog;
import com.example.util.UrlContact;
import com.example.util.HttpconnectionUtil.ReturnResult2;

import ASimpleCache.org.afinal.simplecache.ACache;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class TianXiCardTypeActivity extends BaseActivity{
	private LinearLayout weitainxie_layout,yitainxie_layout;
	private int flag;
	 private ASimpleCache.org.afinal.simplecache.ACache mCache;
	 private User user;
	private String urString= UrlContact.URL_STRING+"/api/UserCenter/GetBillingInformation?loginKey=";
	private Button btn_zhifu,btn_yinhangka,btn_bangding,btn_bangding2;
	private EditText edit_kahao,edit_name,edit_kaihuhang;
	private RelativeLayout kaihu_layout,kaihu_layout2;
	private TextView text_zhaopin,text_zhaopin2,text_mingzi,text_kahuhang2,text_kahao2,text_fangshi2;
	private int flag2=1;
	private LinearLayout layout_back;
	private CheckBox checkBox1,checkBox2;
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tianxiecard);
		flag=getIntent().getIntExtra("flag", 0);
		mCache = ACache.get(this);
		user=(User) mCache.getAsObject("user");
		if (user==null) {

		}else {
		}
		checkBox1=(CheckBox) findViewById(R.id.checkBox1);
		checkBox2=(CheckBox) findViewById(R.id.checkBox2);
		layout_back=(LinearLayout) findViewById(R.id.layout_back);
		layout_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		text_zhaopin=(TextView) findViewById(R.id.text_zhaopin);
		text_zhaopin2=(TextView) findViewById(R.id.text_zhaopin2);
		yitainxie_layout=(LinearLayout) findViewById(R.id.yitainxie_layout);
		weitainxie_layout=(LinearLayout) findViewById(R.id.weitainxie_layout);
		btn_zhifu=(Button) findViewById(R.id.btn_zhifu);
		btn_yinhangka=(Button) findViewById(R.id.btn_yinhangka);
		btn_bangding=(Button) findViewById(R.id.btn_bangding);
		btn_bangding2=(Button) findViewById(R.id.btn_bangding2);
		edit_kahao=(EditText) findViewById(R.id.edit_kahao);
		text_mingzi=(TextView) findViewById(R.id.text_mingzi);
		text_kahuhang2=(TextView) findViewById(R.id.text_kahuhang2);
		text_kahao2=(TextView) findViewById(R.id.text_kahao2);
		text_fangshi2=(TextView) findViewById(R.id.text_fangshi2);
		edit_name=(EditText) findViewById(R.id.edit_name);
		edit_kaihuhang=(EditText) findViewById(R.id.edit_kaihuhang);
		kaihu_layout=(RelativeLayout) findViewById(R.id.kaihu_layout);
		kaihu_layout2=(RelativeLayout) findViewById(R.id.kaihu_layout2);
		if (flag==1) {
			weitainxie_layout.setVisibility(View.GONE);
			yitainxie_layout.setVisibility(View.VISIBLE);
			urString=urString+user.getUser_Name()+"&deviceId="+MyApplication.sUdid;
			MyProgressDialog.show(TianXiCardTypeActivity.this, true, true);
			Map<String, String> params2 = new HashMap<String, String>();
			HttpconnectionUtil.uploadFile(
					TianXiCardTypeActivity.this,
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
									int cardtype=new JSONObject(jsonObject.getString("Data")).getInt("CardType");
									if (cardtype==1) {
										text_fangshi2.setText("银行卡");
										kaihu_layout2.setVisibility(View.VISIBLE);
										text_kahao2.setText(new JSONObject(jsonObject.getString("Data")).getString("AccountCard"));
										text_mingzi.setText(new JSONObject(jsonObject.getString("Data")).getString("AccountName"));
										text_kahuhang2.setText(new JSONObject(jsonObject.getString("Data")).getString("AccountBank"));

									}else {
										kaihu_layout2.setVisibility(View.GONE);

										text_fangshi2.setText("支付宝");
										text_kahao2.setText(new JSONObject(jsonObject.getString("Data")).getString("AccountCard"));
										text_mingzi.setText(new JSONObject(jsonObject.getString("Data")).getString("AccountName"));
									}
									
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
					urString,
					params2);
		}else{
			weitainxie_layout.setVisibility(View.VISIBLE);
			yitainxie_layout.setVisibility(View.GONE);
			
		}
		btn_zhifu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				btn_zhifu.setBackgroundColor(Color.parseColor("#4b90cb"));
				btn_yinhangka.setBackgroundColor(Color.parseColor("#ffffff"));
				flag2=1;
				edit_kahao.setHint("支付宝账号");
				edit_kahao.setText("");
				edit_name.setHint("姓名");
				edit_name.setText("");
				kaihu_layout.setVisibility(View.GONE);
			}
		});
		btn_yinhangka.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				btn_zhifu.setBackgroundColor(Color.parseColor("#ffffff"));
				btn_yinhangka.setBackgroundColor(Color.parseColor("#4b90cb"));
				flag2=2;
				edit_kahao.setHint("银行卡账号");
				edit_kahao.setText("");
				edit_name.setHint("姓名");
				edit_name.setText("");
				kaihu_layout.setVisibility(View.VISIBLE);
				edit_kaihuhang.setHint("开户行");
				edit_kaihuhang.setText("");
			}
		});
		text_zhaopin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(TianXiCardTypeActivity.this,TuanGouActivity.class);
				intent.putExtra("payurl", UrlContact.URL_STRING+ "/act/daigou/rm.html");
				startActivity(intent);
			}
		});	
		text_zhaopin2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(TianXiCardTypeActivity.this,TuanGouActivity.class);
				intent.putExtra("payurl", UrlContact.URL_STRING+"/act/daigou/rm.html");
				startActivity(intent);
			}
		});	
		btn_bangding.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (checkBox1.isChecked()) {
					MyProgressDialog.show(TianXiCardTypeActivity.this, true, true);
					Map<String, String> params2 = new HashMap<String, String>();
					   params2.put("IsPurchasing", "true");
					   params2.put("loginKey", user.getUser_Name());
					   params2.put("deviceId", MyApplication.sUdid);
					   if (flag2==1) {
						   params2.put("CardType", 2+"");
						   params2.put("AccountBank","支付宝");
					   }else {
						   params2.put("CardType", 1+"");
						   params2.put("AccountBank",edit_kaihuhang.getText().toString());
					   }
					 
					
					   params2.put("AccountCard",edit_kahao.getText().toString());
					   params2.put("AccountName",edit_name.getText().toString());
		               	HttpconnectionUtil.uploadFile(
		               			TianXiCardTypeActivity.this,
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
												Toast.makeText(getApplicationContext(), message+"申请已提交", 0).show();
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
		           				}, Method.POST,
		           				UrlContact.URL_STRING+"/api/UserCenter/BillingInformation",
		           				params2);
				}else {
					Toast.makeText(getApplicationContext(), "请勾选农一网农一网农资代购人员招聘办法！", 0).show();
				}
				
			}
		});
		btn_bangding2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (checkBox2.isChecked()) {
					String uString= UrlContact.URL_STRING+"/api/UserCenter/DoPurchasingApply?loginKey="+user.getUser_Name()+"&deviceId="+MyApplication.sUdid;
					MyProgressDialog.show(TianXiCardTypeActivity.this, true, true);
					Map<String, String> params2 = new HashMap<String, String>();
					HttpconnectionUtil.uploadFile(
							TianXiCardTypeActivity.this,
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
							uString,
							params2);
				}
				else {
					Toast.makeText(getApplicationContext(), "请勾选农一网农一网农资代购人员招聘办法！", 0).show();
				}
			}
		});
		
	}
}
