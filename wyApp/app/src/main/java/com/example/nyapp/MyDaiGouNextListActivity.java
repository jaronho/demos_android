package com.example.nyapp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import ASimpleCache.org.afinal.simplecache.ACache;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.example.classes.MyShopper;
import com.example.classes.MyShopperJson;
import com.example.classes.User;
import com.example.util.HttpconnectionUtil;
import com.example.util.HttpconnectionUtil.ReturnResult2;
import com.example.util.MyProgressDialog;
import com.example.util.UrlContact;

public class MyDaiGouNextListActivity extends Activity {
	private ListView listView;
	private LinearLayout layout_back;
	private ASimpleCache.org.afinal.simplecache.ACache mCache;
	private User user;
	private String uString;
	private MyShopper myShopper;
	private ArrayList<MyShopper> shoppers;
	private int index;
	DecimalFormat ddf1 = new DecimalFormat("#.00");

	// private MyShopper myShopper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mydaigou_nextlist);
		mCache = ACache.get(this);
		user = (User) mCache.getAsObject("user");

		uString = UrlContact.URL_STRING + "/api/usercenter/MyShopper?loginKey="
				+ user.getUser_Name() + "&deviceId=" + MyApplication.sUdid;
		listView = (ListView) findViewById(R.id.listView1);

		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		layout_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		new getNetWork().execute();
	}

	/**
	 * 主要用来获取我的代购人的信息
	 * 
	 * @return
	 */
	public class getNetWork extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			run();
			return null;
		}
	}

	public void run() {

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		MyProgressDialog.show(MyDaiGouNextListActivity.this, true, true);
		Map<String, String> params2 = new HashMap<String, String>();
		HttpconnectionUtil.uploadFile(MyDaiGouNextListActivity.this,
				new ReturnResult2() {

					@Override
					public void getResult(String result) {
						MyProgressDialog.cancel();
						if (result.equals("error")) {
							Toast.makeText(getApplicationContext(), "网络断开连接！",
									0).show();
						}
						if (result.equals("")) {
							Toast.makeText(getApplicationContext(), "服务器异常！", 0)
									.show();
						}
						if (!result.equals("") && !result.equals("error")) {
							try {
								JSONObject jsonObject = new JSONObject(result);

								if (jsonObject.getBoolean("Result")) {
									shoppers = MyShopperJson
											.getMyShopper(result);
									Map<String, Object> map = new HashMap<String, Object>();
									for (int i = 0; i < shoppers.size(); i++) {
										map = new HashMap<String, Object>();
										map.put("username", shoppers.get(i)
												.getUser_Name());
										if (shoppers.get(i).getMoney() < 1) {
											map.put("money","0"+ ddf1.format(shoppers.get(i).getMoney()));
										} else {
											map.put("money", ddf1.format(shoppers.get(i).getMoney()));
										}
										list.add(map);
									}
									if (list != null && list.size() > 0) {
										SimpleAdapter adapter = new SimpleAdapter(getApplication(),list,
												R.layout.mydaigou_nextlist_item,
												new String[] { "username","money" },
												new int[] {R.id.text_daigou_name,R.id.text_daigou_price });
										listView.setAdapter(adapter);
										listView.setEmptyView(findViewById(R.id.empty12));
									}

								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					}
				}, Method.GET, uString, params2);

	}

}
