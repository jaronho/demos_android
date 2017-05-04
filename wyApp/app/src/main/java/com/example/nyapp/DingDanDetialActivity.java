package com.example.nyapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classes.DingDanDetial;
import com.example.classes.DingdanDetialJsonUtil;
import com.example.classes.User;
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

import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;

import ASimpleCache.org.afinal.simplecache.ACache;
import okhttp3.Call;

public class DingDanDetialActivity extends BaseActivity {
	private ASimpleCache.org.afinal.simplecache.ACache mCache;
	private User user;
	private int id;
	private DingDanDetial danDetial;
	private TextView text_dingdanhao, text_zhuangtai, text_yonghuname,
			text_yonghuphone, text_xiadandata, text_peisong, text_fapiao,
			text_liuyan;
	private TextView deliveryName,deliveryPhone;
	private LinearLayout layout_phone;
	private RelativeLayout layout_deliveryname;
	private TextView text_zongyingfu, text_daijin, text_daijin1, text_wuliu,
			text_zonge;
	private LinearLayout chanpin_layout;
	private TextView text_recename, text_recephone, text_receaddress;
	private DisplayImageOptions options = null;
	private LinearLayout layout_back;
	private RelativeLayout daijin1, daijin2;
	DecimalFormat ddf1 = new DecimalFormat("#.00");

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dingdan_detial);
		mCache = ACache.get(this);
		user = (User) mCache.getAsObject("user");
		if (user == null) {

		} else {
		}
		options = new DisplayImageOptions.Builder()
				.displayer(new SimpleBitmapDisplayer()).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)

				.build();
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		layout_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		id = getIntent().getIntExtra("id", 0);
		text_dingdanhao = (TextView) findViewById(R.id.text_dingdanhao);
		text_zhuangtai = (TextView) findViewById(R.id.text_zhuangtai);
		text_yonghuname = (TextView) findViewById(R.id.text_yonghuname);
		text_yonghuphone = (TextView) findViewById(R.id.text_yonghuphone);
		text_xiadandata = (TextView) findViewById(R.id.text_xiadandata);
		text_peisong = (TextView) findViewById(R.id.text_peisong);
		text_fapiao = (TextView) findViewById(R.id.text_fapiao);
		text_liuyan = (TextView) findViewById(R.id.text_liuyan);
		text_zongyingfu = (TextView) findViewById(R.id.text_zongyingfu);
		text_daijin = (TextView) findViewById(R.id.text_daijin);

		deliveryName = (TextView) findViewById(R.id.text_delivery_name);
		deliveryPhone = (TextView) findViewById(R.id.text_delivery_phone);
		layout_deliveryname = (RelativeLayout) findViewById(R.id.layout_delivername);
		layout_phone = (LinearLayout) findViewById(R.id.layout_phone);
		
		text_daijin1 = (TextView) findViewById(R.id.text_daijin1);
		text_wuliu = (TextView) findViewById(R.id.text_wuliu);
		text_zonge = (TextView) findViewById(R.id.text_zonge);
		text_recename = (TextView) findViewById(R.id.text_recename);
		text_recephone = (TextView) findViewById(R.id.text_recephone);
		text_receaddress = (TextView) findViewById(R.id.text_receaddress);
		daijin1 = (RelativeLayout) findViewById(R.id.daijin1);
		daijin2 = (RelativeLayout) findViewById(R.id.daijin2);
		chanpin_layout = (LinearLayout) findViewById(R.id.chanpin_layout);

		MyProgressDialog.show(DingDanDetialActivity.this, true, true);

		Map<String, String> params2 = new TreeMap<>();
		params2.put("loginKey", user.getUser_Name());
		params2.put("deviceId", MyApplication.sUdid);
		params2.put("orderId", String.valueOf(id));
		MyOkHttpUtils
				.getData(UrlContact.URL_STRING + "/api/UserCenter/OrderDetail", params2)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						MyToastUtil.showShortMessage("服务器连接失败!");
						MyProgressDialog.cancel();
					}

					@Override
					public void onResponse(String response, int id) {
						MyProgressDialog.cancel();
						try {
							if (new JSONObject(response).getBoolean("Result")) {
								danDetial = DingdanDetialJsonUtil
										.getDingDanDetials(response);
								text_dingdanhao.setText(danDetial
										.getOrderNo());
								text_zhuangtai.setText(danDetial
										.getPayState());
								text_yonghuname.setText(danDetial
										.getUserName());
								text_yonghuphone.setText(user.getMobile());
								text_xiadandata.setText(danDetial
										.getAddDate());
								if (danDetial.getInvoiceType().equals("0")) {
									text_fapiao.setText("不开发票");
								} else {
									if (danDetial.getInvoiceTitle().equals(
											"null")) {
										text_fapiao.setText("开具发票：");
									} else {
										text_fapiao.setText("开具发票："+ danDetial.getInvoiceTitle());
									}

								}

								text_peisong.setText(danDetial
										.getDeliveryType());
								if (danDetial.getRemark().equals("null")) {
									text_liuyan.setText("");
								} else {
									text_liuyan.setText(danDetial
											.getRemark());
								}
								if (danDetial.getOrderTotalPrice()>=1) {
									text_zongyingfu.setText(ddf1.format(danDetial.getOrderTotalPrice()) + "元");

								} else {
									text_zongyingfu.setText("0"+ddf1.format(danDetial
											.getOrderTotalPrice()) + "元");

								}

								// ordercals2.get(position).
								int Type = danDetial.getType();
								// Type==6||Type==26||Type==36
								if (danDetial.getFree_Price() != 0) {
									if (danDetial.getFree_Price()>=1) {
										text_daijin1.setText("-"+ddf1
												.format(danDetial
														.getFree_Price())
												+ "元");
									} else {
										text_daijin1.setText("-0"+ddf1
												.format(danDetial
														.getFree_Price())
												+ "元");
									}

								} else {
									daijin2.setVisibility(View.GONE);
								}
								if (danDetial.getCouponPrice() == 0) {
									text_daijin.setText("0.00元");

								} else {

									text_daijin.setText(ddf1.format(danDetial
											.getCouponPrice()) + "元");
								}
								if (danDetial.getFreight() == 0) {
									text_wuliu.setText("0.00元");

								} else {

									text_wuliu.setText(ddf1.format(danDetial.getFreight())
											+ "元");
								}
								if (danDetial.getTotalPrice()>=1) {
									text_zonge.setText(ddf1.format(danDetial
											.getTotalPrice()) + "元");

								} else {
									text_zonge.setText("0"+ddf1.format(danDetial
											.getTotalPrice()) + "元");
								}
								for (int i = 0; i < danDetial
										.getSubOrders().size(); i++) {
									final int a = i;
									View view = LayoutInflater.from(
											DingDanDetialActivity.this)
											.inflate(
													R.layout.dingdan_item2,
													null);
									TextView tvName = (TextView) view
											.findViewById(R.id.tvName);
									TextView text_price = (TextView) view
											.findViewById(R.id.text_price);
									TextView text_spec = (TextView) view
											.findViewById(R.id.text_spec);
									TextView text_count = (TextView) view
											.findViewById(R.id.text_count);
									ImageView image_product = (ImageView) view
											.findViewById(R.id.image_product);
									tvName.setText(danDetial.getSubOrders()
											.get(i).getName());
									text_count.setText(danDetial
											.getSubOrders().get(i)
											.getCount()
											+ "");
									text_spec.setText(danDetial
											.getSubOrders().get(i)
											.getSpec());
									text_price.setText(danDetial
											.getSubOrders().get(i)
											.getPrice()
											+ "元");
									sImageLoader.displayImage(danDetial
													.getSubOrders().get(i)
													.getPicUrl(), image_product,
											options);
									view.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											// TODO Auto-generated method
											// stub
											Intent intent = new Intent(
													DingDanDetialActivity.this,
													ProductDetailActivity.class);
											intent.putExtra("id", danDetial
													.getSubOrders().get(a)
													.getId());
											intent.putExtra("name", "");
											intent.putExtra("type", "");
											// intent.putExtra("Manuf_Name",
											// news.get(position).getManuf_Name());
											startActivity(intent);
										}
									});
									chanpin_layout.addView(view);
								}
								text_recename.setText(danDetial
										.getReceiveName());
								text_recephone.setText(danDetial
										.getReceivePhone());
								text_receaddress.setText(danDetial
										.getAddress());
								deliveryName.setText(danDetial.getDeliveryName());
								deliveryPhone.setText(danDetial.getDeliveryPhone());
								if (danDetial.getDeliveryName().equals("null")&&danDetial.getDeliveryPhone().equals("null")) {
									layout_deliveryname.setVisibility(View.GONE);
									layout_phone.setVisibility(View.GONE);
								}
							} else {
								Toast.makeText(
										getApplicationContext(),
										new JSONObject(response)
												.getString("Message"), Toast.LENGTH_SHORT)
										.show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}
}
