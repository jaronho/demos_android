package com.example.nyapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.classes.DaiGouMingXi;
import com.example.classes.DaiGouMingXiJsonUtil;
import com.example.classes.Daigou;
import com.example.classes.Products1;
import com.example.classes.Statistics;
import com.example.classes.User;
import com.example.lanuchertest.wegdit.PanelView;
import com.example.util.MyMsgDialog;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyProgressDialog;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ASimpleCache.org.afinal.simplecache.ACache;
import okhttp3.Call;

public class MyDaiGouDetialNextActivity extends BaseActivity{
	private ASimpleCache.org.afinal.simplecache.ACache mCache;
	private User user;
	private String loginString;
	private Button text_takeintomoney;
	private TextView text_tishi;
	private String uString = UrlContact.URL_STRING
			+ "/api/usercenter/MyShopperDetail?loginKey=";
	private DaiGouMingXi daiGouMingXi;
	private TextView text_daigou_price,text_daigou_Revenue,text_daigou_todayrevenue,text_freeze,text_cancashout,text_cashingout,text_cashedOut;
	private ListView listView1;
//	private Button btn_chakan2;
	private DisplayImageOptions options = null;
	private LinearLayout layout_back;
	DecimalFormat ddf1 = new DecimalFormat("#.00");

	private PanelView panelView;
	private Context mContext;
	private MyMsgDialog mDialog;
	private MyMsgDialog mCantakeDialog;

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.benzhan_detial);
		options = new DisplayImageOptions.Builder()
				.displayer(new SimpleBitmapDisplayer()).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)

				.build();
		mContext = this;
		mCache = ACache.get(this);
		user = (User) mCache.getAsObject("user");

		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		layout_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
//		text_tishi = (TextView) findViewById(R.id.text_tishi);

		listView1 = (ListView) findViewById(R.id.listView_benzhan);


		

		
		/**
		 * 1、我的明细中的头信息
		 * 2、总代购金额、总代购收益、今日新增、冻结、未提现、已提现、体现中
		 */
		text_daigou_price = (TextView) findViewById(R.id.text_daigou_price);
		text_daigou_Revenue = (TextView) findViewById(R.id.text_daigou_Revenue);
		text_daigou_todayrevenue = (TextView) findViewById(R.id.text_daigou_todayrevenue);
		text_freeze = (TextView) findViewById(R.id.text_freeze);
		text_cancashout = (TextView) findViewById(R.id.text_cancashout);
		text_cashingout = (TextView) findViewById(R.id.text_cashingout);
		text_cashedOut = (TextView) findViewById(R.id.text_cashedOut);
		/**
		 * 1、判断是否转入余额
		 */
		text_takeintomoney = (Button) findViewById(R.id.text_takeintomoney);
		text_takeintomoney.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Map<String, String> map = new TreeMap<>();
				map.put("loginKey", user.getUser_Name());
				map.put("deviceId", MyApplication.sUdid);
				MyOkHttpUtils
						.getData(UrlContact.URL_STRING+"/api/usercenter/cantakeintomoney",map)
						.build()
						.execute(new StringCallback() {
							@Override
							public void onError(Call call, Exception e, int id) {
								MyToastUtil.showShortMessage("服务器连接失败!");
							}

							@Override
							public void onResponse(String response, int id) {
								try {
									JSONObject jsonObject=new JSONObject(response);
									Boolean Result=jsonObject.getBoolean("Result");
									if (Result) {
										String message=jsonObject.getString("Message");
										showDialog(message);
									}else {
										String message=jsonObject.getString("Message");
										showDialog2(message);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
			}
			
		});
		
	}
	/**
	 * 对话框提示是否转到余额
	 * @author nongyi-lenovo
	 *
	 */
	
	private void showDialog(String message){
		mDialog = new MyMsgDialog(this, "温馨提示", message, new MyMsgDialog.ConfirmListener() {
			@Override
			public void onClick() {
				mDialog.dismiss();
				Map<String, String> map = new TreeMap<>();
				map.put("loginKey", user.getUser_Name());
				map.put("deviceId", MyApplication.sUdid);
				MyOkHttpUtils
						.getData(UrlContact.URL_STRING+"/api/usercenter/takeintomoney",map)
						.build()
						.execute(new StringCallback() {
							@Override
							public void onError(Call call, Exception e, int id) {
								MyToastUtil.showShortMessage("服务器连接失败!");
							}

							@Override
							public void onResponse(String response, int id) {
								try {
									JSONObject jsonObject=new JSONObject(response);
									Boolean Result=jsonObject.getBoolean("Result");
									if (Result) {
										String message=jsonObject.getString("Message");
										showDialog2(message);
									}else {
										String message=jsonObject.getString("Message");
										showDialog2(message);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
			}
		}, new MyMsgDialog.CancelListener() {
			@Override
			public void onClick() {
				mDialog.dismiss();
			}
		});
		mDialog.show();
	}
	private void showDialog2(String message){
		mCantakeDialog = new MyMsgDialog(this, "温馨提示", message, new MyMsgDialog.ConfirmListener() {
			@Override
			public void onClick() {
				mCantakeDialog.dismiss();
				onStart();
			}
		}, null);
		mCantakeDialog.show();
	}

	class MyAdapters extends BaseAdapter {
		private List<Daigou> daigous;

		public MyAdapters(List<Daigou> daigous) {
			this.daigous = daigous;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return daigous.size();
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
		public View getView(final int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolders viewHolders = null;
			if (daiGouMingXi.getItems().get(arg0).getType()==5) {
				convertView = null;
				if (convertView == null) {
					
					convertView = LayoutInflater.from(MyDaiGouDetialNextActivity.this)
							.inflate(R.layout.benzhandaigou_item, null);
					viewHolders = new ViewHolders();
					viewHolders.text_is_or_not_tixian = (TextView) convertView
							.findViewById(R.id.text_is_or_not_tixian);
					viewHolders.text_promoney = (TextView) convertView
							.findViewById(R.id.text_promoney);
					viewHolders.text_daigoupeople_name = (TextView)convertView.findViewById(R.id.text_daigoupeople_name);
					viewHolders.daigou_money = (LinearLayout) convertView.findViewById(R.id.daigou_money);
					viewHolders.daigou_mingxi = (LinearLayout) convertView.findViewById(R.id.daigou_mingxi);

					convertView.setTag(viewHolders);
				} 
//				else {
//					viewHolders = (ViewHolders) convertView.getTag();
//				}
				viewHolders.daigou_mingxi.setVisibility(View.GONE);
				viewHolders.daigou_money.setVisibility(View.VISIBLE);
				viewHolders.text_is_or_not_tixian.setText(daigous.get(arg0).getState());
				viewHolders.text_daigoupeople_name.setText(daigous.get(arg0).getInvaitedUser());
				if (daigous.get(arg0).getProMoney()<1) {
					viewHolders.text_promoney.setText("0"+ddf1.format(daigous.get(arg0)
							.getProMoney()) + "元");
					
				} else {

					viewHolders.text_promoney.setText(ddf1.format(daigous.get(arg0)
							.getProMoney()) + "元");
				}
			}else {
				convertView = null;
				if (convertView == null) {
					convertView = LayoutInflater.from(MyDaiGouDetialNextActivity.this)
							.inflate(R.layout.benzhandaigou_item, null);
					viewHolders = new ViewHolders();
					viewHolders.text_no = (TextView) convertView
							.findViewById(R.id.text_no);
					viewHolders.text_state = (TextView) convertView
							.findViewById(R.id.text_state);
					viewHolders.text_jine = (TextView) convertView
							.findViewById(R.id.text_jine);
					viewHolders.text_shouyi = (TextView) convertView
							.findViewById(R.id.text_shouyi);
					viewHolders.text_daigou_name = (TextView) convertView.findViewById(R.id.text_daigou_name);
					viewHolders.mGallery = (Gallery) convertView
							.findViewById(R.id.gallery);
					viewHolders.daigou_money = (LinearLayout) convertView.findViewById(R.id.daigou_money);
					viewHolders.daigou_mingxi = (LinearLayout) convertView.findViewById(R.id.daigou_mingxi);
					convertView.setTag(viewHolders);
				}
//				else {
//					viewHolders = (ViewHolders) convertView.getTag();
//				}
				
				viewHolders.mGallery.setOnItemClickListener(new OnItemClickListener() {
					
					@Override
					public void onItemClick(AdapterView<?> arg, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
//						 panelView = new PanelView(mContext, daiGouMingXi.getItems().get(arg0).getProducts1().get(arg2));
						if (arg2>=0) {
							
							Products1 products1 = daiGouMingXi.getItems().get(arg0).getProducts1().get(arg2);
							Intent intent = new Intent(MyDaiGouDetialNextActivity.this, MydaigouWindousDialogActivity.class);
							if (products1.getCount().equals("")) {
								products1.setCount("---");
							}
							if (products1.getLucreMoney().equals("")) {
								products1.setLucreMoney("0.00");
							}
							if (products1.getMoney().equals("")) {
								products1.setMoney("0.00");
							}
							if (products1.getName().equals("")) {
								products1.setName("---");
							}
							if (products1.getPrice().equals("")) {
								products1.setPrice("0.00");
							}
							if (products1.getScale().equals("")) {
								products1.setScale("---");
							}
							if (products1.getSpec().equals("")) {
								products1.setSpec("---");
							}
							
							intent.putExtra("Product", (Serializable)products1);
							startActivity(intent);
						}
					}
				});
				
				viewHolders.daigou_mingxi.setVisibility(View.VISIBLE);
				viewHolders.daigou_money.setVisibility(View.GONE);
				viewHolders.text_no.setText(daigous.get(arg0).getOrderNo());
				viewHolders.text_state.setText(daigous.get(arg0).getState());
				if (daigous.get(arg0).getMoney()<1) {
					viewHolders.text_jine.setText("0"+ddf1.format(daigous.get(arg0)
							.getMoney()) + "元");
				} else {
					viewHolders.text_jine.setText(ddf1.format(daigous.get(arg0)
							.getMoney()) + "元");
				}
				
				if (daigous.get(arg0).getProMoney()<1) {
					viewHolders.text_shouyi.setText("0"+ddf1.format(daigous.get(arg0)
							.getProMoney()) + "元");
					
				} else {
					
					viewHolders.text_shouyi.setText(ddf1.format(daigous.get(arg0)
							.getProMoney()) + "元");
				}
				viewHolders.text_daigou_name.setText(daigous.get(arg0).getInvaitedUser());
				ImageShowAdapter adapter = new ImageShowAdapter(
						getApplicationContext(), daigous.get(arg0).getUrList());
				viewHolders.mGallery.setAdapter(adapter);
				
			}
			return convertView;
		}

		class ViewHolders {
			TextView text_no;
			TextView text_state;
			TextView text_jine;
			TextView text_shouyi;
			TextView text_daigou_name;
			Gallery mGallery;
			TextView text_is_or_not_tixian,text_daigoupeople_name;
			TextView text_promoney;
			LinearLayout daigou_money,daigou_mingxi;
			
		}
	}

	public class ImageShowAdapter extends BaseAdapter {
		private List<String> list;
		private Context mContext;

		public ImageShowAdapter(Context c, List<String> list) {
			mContext = c;
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ImageView i = new ImageView(mContext);
			sImageLoader.displayImage(list.get(position), i, options);
			i.setLayoutParams(new Gallery.LayoutParams(270, 190));
			return i;
		}

	}

	/*@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}*/
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		MyProgressDialog.show(MyDaiGouDetialNextActivity.this, true, true);
		uString = uString + user.getUser_Name() + "&deviceId="
				+ MyApplication.sUdid + "&pageIndex=1" + "&pageSize=200";
		Map<String, String> map = new TreeMap<>();
		map.put("loginKey", user.getUser_Name());
		map.put("deviceId", MyApplication.sUdid);
		map.put("pageIndex", "1");
		map.put("pageSize", "200");
		MyOkHttpUtils
				.getData(UrlContact.URL_STRING+ "/api/usercenter/MyShopperDetail",map)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						MyProgressDialog.cancel();
						MyToastUtil.showShortMessage("服务器连接失败!");
					}

					@Override
					public void onResponse(String response, int id) {
						MyProgressDialog.cancel();
						try {
								JSONObject jsonObject = new JSONObject(response);
								Boolean Result = jsonObject.getBoolean("Result");
								if (Result) {
									daiGouMingXi = DaiGouMingXiJsonUtil
											.getDaiGouMingXi(response);
									if (daiGouMingXi.getPurchasingState().equals("1")) {
//											状态是“1”的时候才显示
										if (daiGouMingXi.getCanIntoMoney()) {
											text_takeintomoney.setVisibility(View.GONE);
										} else {
											text_takeintomoney.setVisibility(View.VISIBLE);
										}

										Statistics statistics = daiGouMingXi.getStatistics().get(0);
										if (statistics.getAllMoney()<1) {
											text_daigou_price.setText("0"+ddf1.format(statistics.getAllMoney())+"");

										} else {
											text_daigou_price.setText(ddf1.format(statistics.getAllMoney())+"");

										}
										if (statistics.getAllRevenue()<1) {
											text_daigou_Revenue.setText("0"+ddf1.format(statistics.getAllRevenue())+"");

										} else {
											text_daigou_Revenue.setText(ddf1.format(statistics.getAllRevenue())+"");

										}
										if (statistics.getTodayRevenue()<1) {
											text_daigou_todayrevenue.setText("0"+ddf1.format(statistics.getTodayRevenue())+"");

										} else {
											text_daigou_todayrevenue.setText(ddf1.format(statistics.getTodayRevenue())+"");

										}
										if (statistics.getFreeze()<1) {
											text_freeze.setText("0"+ddf1.format(statistics.getFreeze())+"");

										} else {
											text_freeze.setText(ddf1.format(statistics.getFreeze())+"");

										}
										if (statistics.getCanCashout()<1) {
											text_cancashout.setText("0"+ddf1.format(statistics.getCanCashout())+"");

										} else {
											text_cancashout.setText(ddf1.format(statistics.getCanCashout())+"");

										}
										if (statistics.getCashingOut()<1) {
											text_cashingout.setText("0"+ddf1.format(statistics.getCashingOut())+"");

										} else {
											text_cashingout.setText(ddf1.format(statistics.getCashingOut())+"");

										}
										if (statistics.getCashedOut()<1) {
											text_cashedOut.setText("0"+ddf1.format(statistics.getCashedOut())+"");

										} else {
											text_cashedOut.setText(ddf1.format(statistics.getCashedOut())+"");

										}

										MyAdapters adapters = new MyAdapters(daiGouMingXi.getItems());
										listView1.setAdapter(adapters);
										listView1.setEmptyView(findViewById(R.id.empty12));
//											}
									}

								} else {
									MyToastUtil.showShortMessage(jsonObject.getString("Message"));
								}


						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
}
