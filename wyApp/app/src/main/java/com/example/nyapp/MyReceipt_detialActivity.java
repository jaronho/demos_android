package com.example.nyapp;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.example.classes.User;
import com.example.util.MyProgressDialog;
import com.example.util.UrlContact;
import com.example.util.HttpconnectionUtil.ReturnResult2;

import ASimpleCache.org.afinal.simplecache.ACache;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyReceipt_detialActivity extends BaseActivity implements OnClickListener {
	private ASimpleCache.org.afinal.simplecache.ACache mCache;
	private User user;
	private String loginString;
	private EditText youji_address, shouhuo_name, shouhuo_phone, edit_gongsi;
	private TextView money_sum;
	private Button fapiao_tijiao;
	private RadioGroup radioGroup, fapiaoGroup;
	private RelativeLayout relat_gongsi;
	private LinearLayout layout_back;
	private String title = "个人";
	private String fapiao_content = "明细";
	private int fapiao_ct = 2;// "1"代表农药，"2"代表明细
	private String Total_Money;
	private String OrderIds;
	Map<String, String> params = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myreceipt_next);
		findViewById(R.id.myreceipt).setOnClickListener(this);
		money_sum = (TextView) findViewById(R.id.money_sum);
		youji_address = (EditText) findViewById(R.id.youji_address);
		shouhuo_name = (EditText) findViewById(R.id.shouhuo_name);
		shouhuo_phone = (EditText) findViewById(R.id.shouhuo_phone);
		fapiao_tijiao = (Button) findViewById(R.id.fapiao_tijiao);
		Total_Money = getIntent().getStringExtra("Total_Money");
		OrderIds = getIntent().getStringExtra("OrderIds");
		money_sum.setText(Total_Money);
		fapiao_tijiao.setOnClickListener(this);
		relat_gongsi = (RelativeLayout) findViewById(R.id.relat_gongsi);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		fapiaoGroup = (RadioGroup) findViewById(R.id.fapiaoGroup);
		edit_gongsi = (EditText) findViewById(R.id.edit_gongsi);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		layout_back.setOnClickListener(this);
		mCache = ACache.get(this);
		loginString = mCache.getAsString("loginString");
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton ra = (RadioButton) MyReceipt_detialActivity.this.findViewById(radioButtonId);
				title = ra.getText().toString();
				if (radioButtonId == R.id.radiodanwei) {
					relat_gongsi.setVisibility(View.VISIBLE);
				} else {
					relat_gongsi.setVisibility(View.GONE);
				}
			}
		});
		fapiaoGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton fa = (RadioButton) MyReceipt_detialActivity.this.findViewById(radioButtonId);
				fapiao_content = fa.getText().toString();
				if (fapiao_ct == 1) {
					fapiao_ct = 2;
				} else if (fapiao_ct == 2) {
					fapiao_ct = 1;
				}
				// 更新文本内容，以符合选中项
			}
		});
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	public static boolean isMobile(String mobiles) {
		String regExp = "^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9])|(14[0-9])|(16[0-9]))\\d{8}$";

		Pattern p = Pattern.compile(regExp);

		Matcher m = p.matcher(mobiles);

		return m.find();// boolean
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.myreceipt:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;
		case R.id.fapiao_tijiao:
			if (loginString == null) {
				loginString = "false";
				mCache.put("loginString", loginString);
			} else {

				if (loginString.equals("true")) {
					user = (User) mCache.getAsObject("user");
					if (relat_gongsi.getVisibility() == View.VISIBLE) {
						if (edit_gongsi.getText().toString().equals("")) {
							Toast.makeText(this, "公司抬头不可为空", Toast.LENGTH_SHORT).show();
						} else {
							title = edit_gongsi.getText().toString();
						}
					}
					if (youji_address.getText().toString().equals("")) {
						Toast.makeText(this, "邮寄地址不可以为空", Toast.LENGTH_SHORT).show();
					} else if (shouhuo_name.getText().toString().equals("")) {
						Toast.makeText(this, "收货人姓名不可以为空", Toast.LENGTH_SHORT).show();
					} else if (shouhuo_phone.getText().toString().equals("")) {
						Toast.makeText(this, "收货人电话不可以为空", Toast.LENGTH_SHORT).show();
					} else {
						if (isMobile(shouhuo_phone.getText().toString())) {
							// 提交信息
							params.put("Invoice_Title", title);
							params.put("Invoice_Type", fapiao_ct + "");
							params.put("Total_Price", Total_Money);
							params.put("Receive_Address", youji_address.getText().toString());
							params.put("Receive_Name", shouhuo_name.getText().toString());
							params.put("Receive_Phone", shouhuo_phone.getText().toString());
							params.put("orderIds", OrderIds);
							params.put("loginKey", user.getUser_Name());
							params.put("deviceId", MyApplication.sUdid);
							setReceipt(params);
						} else {
							Toast.makeText(this, "手机格式不正确", Toast.LENGTH_SHORT).show();
						}
					}
				}else {
					Intent intent = new Intent(MyReceipt_detialActivity.this, LoginActivity.class);
					startActivity(intent);
				}
			}
			break;
		case R.id.layout_back:
			finish();
			break;
		}
	}

	private void setReceipt(Map<String, String> params) {
		MyProgressDialog.show(MyReceipt_detialActivity.this, true, true);
		com.example.util.HttpconnectionUtil.uploadFile(getApplicationContext(), new ReturnResult2() {

			@Override
			public void getResult(String result) {
				// TODO Auto-generated method stub
				MyProgressDialog.cancel();
				if (result.equals("error")) {
					Toast.makeText(getApplicationContext(), "网络断开连接！", 0).show();
				}
				if (result.equals("")) {
					Toast.makeText(getApplicationContext(), "服务器异常！", 0).show();
				}
				if (!result.equals("") && !result.equals("error")) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(result);

						Boolean Result = jsonObject.getBoolean("Result");
						String message = jsonObject.getString("Message");
						if (Result) {
							finish();
						}
						Toast.makeText(MyReceipt_detialActivity.this, message, Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}, Method.POST, UrlContact.URL_STRING + "/api/Usercenter/AddInvoice", params);
	}
}
