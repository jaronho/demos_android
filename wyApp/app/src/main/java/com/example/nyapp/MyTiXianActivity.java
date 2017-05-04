package com.example.nyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.example.classes.TiXianJsonUtil;
import com.example.classes.Tixian;
import com.example.classes.TixianItem;
import com.example.classes.User;
import com.example.util.HttpconnectionUtil;
import com.example.util.HttpconnectionUtil.ReturnResult2;
import com.example.util.MyProgressDialog;
import com.example.util.UrlContact;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ASimpleCache.org.afinal.simplecache.ACache;

public class MyTiXianActivity extends BaseActivity{

	 private ASimpleCache.org.afinal.simplecache.ACache mCache;
	 private User user;
	private Button btn_bangding,btn_daigou;
	private TextView text_tishi;
	private String uString= UrlContact.URL_STRING+"/api/UserCenter/FinanceTakeList?loginKey=";
	private Tixian tixian;
	private Button btn_chakan;
	private RelativeLayout tixian_layout;
	private ListView listView1;
	private LinearLayout bangding_yicang;
	private LinearLayout layout_back;
	private TextView empty6;
	DecimalFormat ddf1 = new DecimalFormat("#.00");
	@Override
	public void initView() {

	}
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mytixian);
		mCache = ACache.get(this);
		user=(User) mCache.getAsObject("user");
		empty6=(TextView) findViewById(R.id.empty6);
		layout_back=(LinearLayout) findViewById(R.id.layout_back);
		layout_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		tixian_layout=(RelativeLayout) findViewById(R.id.tixian_layout);
		listView1=(ListView) findViewById(R.id.listView1);
		tixian_layout.setVisibility(View.GONE);
		listView1.setVisibility(View.GONE);
		empty6.setVisibility(View.GONE);
		btn_chakan=(Button) findViewById(R.id.btn_chakan);
		btn_bangding=(Button) findViewById(R.id.btn_bangding);
		text_tishi=(TextView) findViewById(R.id.text_tishi);
		bangding_yicang=(LinearLayout) findViewById(R.id.bangding_yicang);
		uString=uString+user.getUser_Name()+"&deviceId="+MyApplication.sUdid+"&pageIndex=1"+"&pageSize=200";
		
		
		btn_bangding.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			if (!tixian.isIsFillAccount()) {
				Intent intent=new Intent(MyTiXianActivity.this,MyShouKuanActivity.class);
				intent.putExtra("flag", 2);
				startActivity(intent);
			}
					
					
				
			}
		});
		btn_chakan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MyTiXianActivity.this,MyShouKuanActivity.class);
				intent.putExtra("flag", 1);
				startActivity(intent);
			}
		});
	}
	class Myadapters extends BaseAdapter{
		List<TixianItem> tixianItems;
		public Myadapters(List<TixianItem> tixianItems){
			this.tixianItems=tixianItems;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tixianItems.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			Viewholder viewholder=null;
			if (convertView==null) {
				convertView=LayoutInflater.from(MyTiXianActivity.this).inflate(R.layout.tixian_item, null);
				viewholder=new Viewholder();
				viewholder.text_money=(TextView) convertView.findViewById(R.id.text_money);
				viewholder.text_state=(TextView) convertView.findViewById(R.id.text_state);
				viewholder.text_kaishi=(TextView) convertView.findViewById(R.id.text_kaishi);
				viewholder.text_jieshu=(TextView) convertView.findViewById(R.id.text_jieshu);
				convertView.setTag(viewholder);
			}else {
				viewholder=(Viewholder) convertView.getTag();
			}
			if (tixianItems.get(arg0).getEndTime().equals("null")) {
				viewholder.text_jieshu.setText("");
			}else {
				viewholder.text_jieshu.setText(tixianItems.get(arg0).getEndTime());
			}
			if (tixianItems.get(arg0).getTakeMoney()>=1) {
				viewholder.text_money.setText(ddf1.format(tixianItems.get(arg0).getTakeMoney())+"元");
				
			} else {
				viewholder.text_money.setText("0"+ddf1.format(tixianItems.get(arg0).getTakeMoney())+"元");

			}
			viewholder.text_state.setText(tixianItems.get(arg0).getState());
			viewholder.text_kaishi.setText(tixianItems.get(arg0).getAddTime());
			return convertView;
		}
		class Viewholder {
			TextView text_money;
			TextView text_state;
			TextView text_kaishi;
			TextView text_jieshu;
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MyProgressDialog.show(MyTiXianActivity.this, true, true);
		Map<String, String> params2 = new HashMap<String, String>();
		HttpconnectionUtil.uploadFile(
				MyTiXianActivity.this,
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
								tixian=TiXianJsonUtil.getTixian(result);
								if (tixian.isIsFillAccount()) {
									tixian_layout.setVisibility(View.VISIBLE);
									bangding_yicang.setVisibility(View.GONE);
									listView1.setVisibility(View.VISIBLE);
									empty6.setVisibility(View.VISIBLE);
									text_tishi.setVisibility(View.GONE);
									btn_bangding.setVisibility(View.GONE);
									List<TixianItem> tixianItems = tixian.getTixianItems();
									if (tixianItems!=null) {
										Myadapters aMyadapters=new Myadapters(tixianItems);
										listView1.setAdapter(aMyadapters);
									}
									listView1.setEmptyView(empty6);
								}else {
//									"你还没有绑定收款方式！"
									text_tishi.setText(tixian.getBindAccountMessage());
									btn_bangding.setVisibility(View.VISIBLE);
									bangding_yicang.setVisibility(View.VISIBLE);
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
				uString,
				params2);
	}
}
