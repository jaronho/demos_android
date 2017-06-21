package com.example.nyapp;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.example.classes.User;
import com.example.util.HttpconnectionUtil;
import com.example.util.HttpconnectionUtil.ReturnResult2;
import com.example.util.MyProgressDialog;
import com.example.util.UrlContact;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import ASimpleCache.org.afinal.simplecache.ACache;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import net.sourceforge.simcpux.Constants;

public class WXPayActivity extends Activity {
	
	private IWXAPI api;
	private ASimpleCache.org.afinal.simplecache.ACache mCache;
	private User user;
	private String url;
	private Button wxpay;
	
	@SuppressLint("NewApi")
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wxpay);
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		mCache = ACache.get(this);
		user = (User) mCache.getAsObject("user");
		wxpay = (Button) findViewById(R.id.wxpay);
		url = UrlContact.URL_STRING+"/PaySubmit/PayIndex?orderid="+"314"+"&paytype=5&Type=4&loginKey="+user.getUser_Name()  +"&deviceId="+MyApplication.sUdid;
		final boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		
			wxpay.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
//				Toast.makeText(WXPayActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
					if (isPaySupported) {
					getWXpay();
					} else {
						Toast.makeText(WXPayActivity.this, "您的微信暂不支持微信支付功能或者您的手机未安装微信", Toast.LENGTH_SHORT).show();
					}
				}
			});
		

		
	}
	public static String JSONTokener(String in) {  
        // consume an optional byte order mark (BOM) if it exists  
        if (in != null && in.startsWith("\ufeff")) {  
        in = in.substring(1);  
        }  
        return in;  
   }
	
//获取微信支付的数据
	public void getWXpay(){
		 MyProgressDialog.show(WXPayActivity.this,true, true);
		Map<String, String> params2 = new HashMap<String, String>();
		HttpconnectionUtil.uploadFile(WXPayActivity.this, new ReturnResult2() {

			@Override
			public void getResult(String result) {
				// TODO Auto-generated method stub
				MyProgressDialog.cancel();
				try {
					if (result.equals("error")) {
						Toast.makeText(getApplicationContext(), "网络断开连接！", 0).show();
					}
					if (result.equals("")) {
						Toast.makeText(getApplicationContext(), "服务器异常！", 0).show();
					}
					if (!result.equals("") && !result.equals("error")) {
						if (result != null && result.length() > 0) {
							String content = new String(result);
							content = JSONTokener(content);
				        	JSONObject json = new JSONObject(content); 
				        	if(null != json && !json.has("retcode") ){
								PayReq req = new PayReq();
								//req.appId = "wxf8b4f85f3a794e77";  // ������appId
								String appid = json.getString("appid");
								IWXAPI wxApi = WXAPIFactory.createWXAPI(WXPayActivity.this, appid, true);
					            wxApi.registerApp(appid);
								req.appId			= json.getString("appid");
								req.partnerId		= json.getString("partnerid");
								req.prepayId		= json.getString("prepayid");
								req.nonceStr		= json.getString("noncestr");
								req.timeStamp		= json.getString("timestamp");
								req.packageValue    = "Sign=WXPay";
								req.sign			= json.getString("sign");
								req.extData			= "app data"; // optional		
//								Toast.makeText(WXPayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
								// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
								api.sendReq(req);
							}else{
								Log.d("PAY_GET", "返回错误"+json.getString("retmsg"));
					        	Toast.makeText(WXPayActivity.this, "返回错误"+json.getString("retmsg"), Toast.LENGTH_SHORT).show();
							}
						}else{
				        	Log.d("PAY_GET", "服务器请求错误");
				        	Toast.makeText(WXPayActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
				        }
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, Method.GET, url, params2);
	}
	
}
