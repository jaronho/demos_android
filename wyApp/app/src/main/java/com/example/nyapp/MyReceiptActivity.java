package com.example.nyapp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.example.classes.MyReceipt;
import com.example.classes.MyReceiptJson;
import com.example.classes.User;
import com.example.util.HttpconnectionUtil.ReturnResult2;
import com.example.util.ConnectionWork;
import com.example.util.HttpconnectionUtil;
import com.example.util.MyProgressDialog;
import com.example.util.UrlContact;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import ASimpleCache.org.afinal.simplecache.ACache;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class MyReceiptActivity extends BaseActivity implements OnClickListener {
	private ASimpleCache.org.afinal.simplecache.ACache mCache;
	private ViewHolder hold = null;
	private User user;
	private String loginString;
	private String url ;
	private String urlString= UrlContact.URL_STRING + "" ;
	private LinearLayout layout_back, layout_next;
	private Button next_btn;
	private CheckBox checkbox;
//	private ListView receipt_list;
	private PullToRefreshListView receipt_list;
	private TextView text_orders, text_money;
	private List<MyReceipt> myReceipts = new ArrayList<MyReceipt>();
	private MyOrdersList myOrdersList;
	private Double money_num;
	private String orderIds = "";
	List<Integer> selected = new ArrayList<Integer>();
	DecimalFormat ddf1 = new DecimalFormat("#.00");
	private int flag = 1;
	private int lastItem;
	private Boolean loding = false;
	public static final int HTTP_REQUEST_SUCCESS = -1;
	public static final int HTTP_REQUEST_ERROR = 0;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 10) { // 更改选中商品的总价格
				selected.clear();
				getMoney();

			}
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myreceipt);
		layout_next = (LinearLayout) findViewById(R.id.layout_next);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		layout_back.setOnClickListener(this);
//		receipt_list = (ListView) findViewById(R.id.receipt_list);
		receipt_list = (PullToRefreshListView) findViewById(R.id.receipt_list);
		next_btn = (Button) findViewById(R.id.btn_next);
		
		checkbox = (CheckBox) findViewById(R.id.checkbox);
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg0.isPressed() == true) {
					if (myOrdersList.receipts != null) {
						if (arg1) {
							int k = myOrdersList.receipts.size();
							for (int i = 0; i < k; i++) {
								if (myOrdersList.receipts.get(i).getClickButton()) {
									myOrdersList.receipts.get(i).checked = true;
								}
							}
							next_btn.setEnabled(true);
							next_btn.setBackgroundColor(0xffff4b00);
							myOrdersList.notifyDataSetChanged();
						} else {
							int k = myOrdersList.receipts.size();
							for (int i = 0; i < k; i++) {
								if (myOrdersList.receipts.get(i).getClickButton()) {
									myOrdersList.receipts.get(i).checked = false;
								}
							}
							next_btn.setEnabled(false);
							next_btn.setBackgroundColor(0x7fcccccc);
							myOrdersList.notifyDataSetChanged();
						}
					}
					selected.clear();
					getMoney();
				}
			}
		});
		next_btn.setOnClickListener(this);
		text_orders = (TextView) findViewById(R.id.text_orders);
		text_money = (TextView) findViewById(R.id.text_money);
		
	}
	public void initPullToRefreshListView(PullToRefreshListView rtflv, MyOrdersList adapter) {
		rtflv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				lastItem = receipt_list.getRefreshableView().getLastVisiblePosition();
				if (lastItem >= myOrdersList.getCount() - 3) {
//					System.out.println("loding----------Tag" + lastItem + "  count = " + myOrdersList.getCount());
					if (!loding) {
						new GetNewsTask(receipt_list).execute();
					}
				}

			}
		});
		rtflv.setAdapter(adapter);
		rtflv.setEmptyView(findViewById(R.id.empty4));
	}
	
//	获取分页信息
	class GetNewsTask extends AsyncTask<String, Void, Integer> {
		private PullToRefreshListView mPtrlv;

		public GetNewsTask(PullToRefreshListView ptrlv) {
			loding = true;
			this.mPtrlv = ptrlv;
		}

		@Override
		protected Integer doInBackground(String... params) {
			if (ConnectionWork.isConnect(MyReceiptActivity.this)) {
				return HTTP_REQUEST_SUCCESS;
			}
			return HTTP_REQUEST_ERROR;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case HTTP_REQUEST_SUCCESS:
				flag = flag + 1;
				// httpurlString= "http://api.hf002496.com/api/product/5-2";

				if (flag > 10) {
					url = url.substring(0, url.length() - 2) + flag;
				} else {
					url = url.substring(0, url.length() - 1) + flag;
				}

				Map<String, String> params2 = new HashMap<String, String>();
				HttpconnectionUtil.uploadFile(getApplicationContext(), new ReturnResult2() {

					@Override
					public void getResult(String result) {
						// TODO Auto-generated method stub
						loding = false;
						if (result.equals("error")) {
							Toast.makeText(getApplicationContext(), "网络连接异常请检查你的网络！", 1).show();
						}
						if (result.equals("SeverError")) {
							Toast.makeText(getApplicationContext(), "服务器异常请稍等！", 1).show();
						}
						if (!result.equals("error") && !result.equals("SeverError")) {
							JSONObject jsonObject;
							try {
								jsonObject = new JSONObject(result);

								Boolean Result = jsonObject.getBoolean("Result");
								if (Result) {
									myReceipts = MyReceiptJson.getMyReceipt(result);
									if (myReceipts.size()>0) {
										myOrdersList.addNews(myReceipts);
										myOrdersList.notifyDataSetChanged();
									}
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				}, Method.GET, url, params2);

				break;
			case HTTP_REQUEST_ERROR:
				Toast.makeText(MyReceiptActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
				break;
			}
			mPtrlv.onRefreshComplete();
		}

	}
	public void getMoney() {
		int select_num = myOrdersList.receipts.size();
		int No_checkbox = 0;

		for (int i = 0; i < select_num; i++) {
			if (myOrdersList.receipts.get(i).checked) {
				selected.add(i);
			}
		}
		for (int i = 0; i < myOrdersList.receipts.size(); i++) {
			if (!myOrdersList.receipts.get(i).getClickButton()) {
				No_checkbox++;
			}
		}
		if (myOrdersList.receipts.size()-No_checkbox > selected.size()) {
			if (checkbox.isChecked()) {
				checkbox.setChecked(false);
			}
		}
		if (myOrdersList.receipts.size()-No_checkbox == selected.size()) {
			if (!checkbox.isChecked()) {
				checkbox.setChecked(true);
			}
		}
		select_num = selected.size();
		if (select_num == 0) {
			text_money.setText(0 + "");
			text_orders.setText(0 + "");
			checkbox.setChecked(false);
			next_btn.setEnabled(false);
			next_btn.setBackgroundColor(0x7fcccccc);
		} else {
			next_btn.setEnabled(true);
			next_btn.setBackgroundColor(0xffff4b00);
			Double total_money = 0.0;
			for (int i = 0; i < select_num; i++) {
				int j = selected.get(i);
				total_money += Double.valueOf(myOrdersList.receipts.get(j).getPrice());
				if (total_money >= 1) {
					text_money.setText(ddf1.format(total_money) + "");
				} else {
					text_money.setText("0" + ddf1.format(total_money) + "");
				}
				orderIds += myOrdersList.receipts.get(j).getOrderId()+",";
			}

			if (select_num > 0) {
				text_orders.setText(select_num + "");
			} else {
				text_orders.setText(0 + "");
			}
			money_num = total_money;
		}

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		设置地址重新请求
		flag = 1;
		getReceiptList();
		text_money.setText(0 + "");
		text_orders.setText(0 + "");
		next_btn.setEnabled(false);
		next_btn.setBackgroundColor(0x7fcccccc);
	}
	private void getReceiptList() {
		
		mCache = ACache.get(this);
		loginString = mCache.getAsString("loginString");
		if (loginString == null) {
			loginString = "false";
			mCache.put("loginString", loginString);
		} else {
			if (loginString.equals("true")) {
				user = (User) mCache.getAsObject("user");
				url = urlString + "/api/usercenter/MyInvoiceList?loginKey=" + user.getUser_Name() + "&deviceId="
						+ MyApplication.sUdid+"&PageIndex="+flag;
			} else {
				Intent intent = new Intent(MyReceiptActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		}
		MyProgressDialog.show(MyReceiptActivity.this, true, true);
		Map<String, String> params = new HashMap<String, String>();
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
						if (Result) {
							myReceipts = MyReceiptJson.getMyReceipt(result);
							myOrdersList = new MyOrdersList(myReceipts);
//							receipt_list.setAdapter(myOrdersList);
							initPullToRefreshListView(receipt_list, myOrdersList);
							receipt_list.setEmptyView(findViewById(R.id.empty));
							selected.clear();
							getMoney();
							if (myReceipts.size()==0) {
								layout_next.setVisibility(View.GONE);
							}
						}else {
							layout_next.setVisibility(View.GONE);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}, Method.GET, url, params);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.btn_next:
			Intent intent = new Intent(this, MyReceipt_detialActivity.class);
			intent.putExtra("Total_Money", money_num+"");
			intent.putExtra("OrderIds", orderIds);
			startActivity(intent);
			break;
		}
	}

	public class MyOrdersList extends BaseAdapter {

		private List<MyReceipt> receipts = new ArrayList<MyReceipt>();
 
		public void addNews(List<MyReceipt> addreceipts) {
			for(MyReceipt hm:addreceipts) {
				receipts.add(hm);
			}
		}
		public MyOrdersList(List<MyReceipt> receipts) {
			// TODO Auto-generated constructor stub
			this.receipts = receipts;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return receipts.size();
		}

		@Override
		public Object getItem(int postion) {
			// TODO Auto-generated method stub
			return receipts.get(postion);
		}

		@Override
		public long getItemId(int postion) {
			// TODO Auto-generated method stub
			return postion;
		}

		@Override
		public View getView(final int postion, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			
			if (convertView == null) {
				convertView = LayoutInflater.from(MyReceiptActivity.this).inflate(R.layout.myrepcipt_item, null);
				holder = new ViewHolder();
//				holder.head = (LinearLayout) convertView.findViewById(R.id.head);
				holder.text_end = (TextView) convertView.findViewById(R.id.text_end);
				holder.cbCheck = (CheckBox) convertView.findViewById(R.id.cbCheck);
				holder.order_number = (TextView) convertView.findViewById(R.id.order_number);
				holder.order_state = (TextView) convertView.findViewById(R.id.order_state);
				holder.take_over_time = (TextView) convertView.findViewById(R.id.take_over_time);
				holder.payment = (TextView) convertView.findViewById(R.id.payment);
				holder.receipt_state = (TextView) convertView.findViewById(R.id.receipt_state);
				holder.handle_time = (TextView) convertView.findViewById(R.id.handle_time);
				holder.waybill_number = (TextView) convertView.findViewById(R.id.waybill_number);
				hold = holder;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				hold = holder;
			}
			String colors = receipts.get(postion).getColor();
			String [] color = colors.split(",");
			
			holder.order_number.setText(receipts.get(postion).getOrder_No() + "");
			holder.order_state.setText(receipts.get(postion).getOrderState());
			holder.take_over_time.setText(receipts.get(postion).getTakeTime());
			holder.payment.setText(receipts.get(postion).getPrice() + "");
			holder.receipt_state.setText(receipts.get(postion).getReceiptState());
			if (!colors.equals("null")) {
				holder.receipt_state.setTextColor(Color.rgb(Integer.valueOf(color[0]),Integer.valueOf(color[1]),Integer.valueOf(color[2])));
			}
			holder.cbCheck.setChecked(receipts.get(postion).checked);
			holder.cbCheck.setEnabled(receipts.get(postion).getClickButton());
			holder.handle_time.setText(receipts.get(postion).getHandleTime());
			holder.waybill_number.setText(receipts.get(postion).getTicketNumber() + "");
			holder.cbCheck.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					if (receipts.get(postion).getClickButton()) {  
						receipts.get(postion).checked = cb.isChecked();
						handler.sendMessage(handler.obtainMessage(10));
					} else {
						cb.setChecked(false);
					}
				}
			});
			if (receipts.size()-1 == postion && (myReceipts.size()==0||receipts.size()<10)) {
				holder.text_end.setVisibility(View.VISIBLE);
				holder.text_end.setText("已经全部加载完毕");
			}else {
				if (postion == receipts.size()-1) {
					holder.text_end.setVisibility(View.VISIBLE);
					holder.text_end.setText("正在加载中...");
				} else {
					holder.text_end.setVisibility(View.GONE);
				}
				
				
			}
			return convertView;
		}
	}

	static class ViewHolder {
		private CheckBox cbCheck;
		private TextView order_number, order_state, take_over_time, payment, receipt_state, handle_time, waybill_number;
		private TextView text_end;
	}

}
