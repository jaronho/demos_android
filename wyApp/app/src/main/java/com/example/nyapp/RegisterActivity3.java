package com.example.nyapp;

import java.io.InputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.example.classes.AddressPovic;
import com.example.classes.AddressPovicJsonUtil;
import com.example.classes.MyGPS;
import com.example.classes.User;
import com.example.classes.UserJsonUtil;
import com.example.util.HttpconnectionUtil;
import com.example.util.MyProgressDialog;
import com.example.util.UrlContact;
import com.example.util.HttpconnectionUtil.ReturnResult2;

import ASimpleCache.org.afinal.simplecache.ACache;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity3 extends BaseActivity {
	private TextView edit_povic, edit_city, edit_area, text_regist, edit_towns, edit_vaillage;
	private PopupWindow popupwindow;
	private List<AddressPovic> addresses, address1s, address2s, address3s, address4s;
	private ASimpleCache.org.afinal.simplecache.ACache mCache;
	private LinearLayout layout_back;
	private String code;
	private Button btn_next;
	private String phone;
	private EditText edit_password, edit_password2;
	private int povince = 0;
	private int citys = 0;
	private int areas = 0;
	private int towns = 0;
	private int vaillage = 0;
	private int daigou;
	private MyGPS gps;
	private RelativeLayout view_vaillage, view_towns;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.register_3);
		initView();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		text_regist = (TextView) findViewById(R.id.text_regist);
		daigou = getIntent().getIntExtra("daigou", 0);
//		if (daigou==2) {
//			text_regist.setText("注册代购");
//		}else if(daigou == 1){
			text_regist.setText("注册");
//		}
		code = getIntent().getStringExtra("code");
		phone = getIntent().getStringExtra("phone");
		view_towns = (RelativeLayout) findViewById(R.id.view_towns);
		view_vaillage = (RelativeLayout) findViewById(R.id.view_aillage);
		mCache = ACache.get(this);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		layout_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				finish();
			}
		});
		edit_password = (EditText) findViewById(R.id.edit_password);
		edit_password2 = (EditText) findViewById(R.id.edit_password2);
		edit_povic = (TextView) findViewById(R.id.edit_povic);

		edit_city = (TextView) findViewById(R.id.edit_city);

		edit_area = (TextView) findViewById(R.id.edit_area);
		edit_towns = (TextView) findViewById(R.id.edit_towns);
		edit_vaillage = (TextView) findViewById(R.id.edit_vaillage);
		new GetNewsTask().execute("0");
		btn_next = (Button) findViewById(R.id.btn_next);

	}

	public void submit() {
		// LoginActivity.loginActivity.finish();
		RegisterActivity1.registerActivity1.finish();
		RegisterActivity2.registerActivity2.finish();
		startActivity(new Intent(RegisterActivity3.this, LoginActivity.class));
		finish();
	}

	/**
	 * 重写onTouchEvent方法
	 * 
	 * @author nongyi-lenovo
	 *
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (popupwindow != null && popupwindow.isShowing()) {
			popupwindow.dismiss();
			popupwindow = null;
		}
		return super.onTouchEvent(event);
	}

	public void initmPopupWindowView() {

		// // 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow = new PopupWindow(customView, 360, ViewGroup.LayoutParams.FILL_PARENT);
		ListView listView = (ListView) customView.findViewById(R.id.list_povic);
		MyAdapter adapter = new MyAdapter();
		listView.setAdapter(adapter);
		/** 在这里可以实现自定义视图的功能 */
		// listView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int position,
		// long arg3) {
		// // TODO Auto-generated method stub
		// popupwindow.dismiss();
		// edit_povic.setText(addresses.get(position).getArea_Name());
		// povince=addresses.get(position).getId();
		// address1s=addresses.get(position).getList();
		// edit_city.setText("市");
		// edit_area.setText("县");
		// }
		// });

	}

	public void initmPopupWindowView2() {

		// // 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow = new PopupWindow(customView, 360, ViewGroup.LayoutParams.FILL_PARENT);
		ListView listView = (ListView) customView.findViewById(R.id.list_povic);
		MyAdapter2 adapter = new MyAdapter2();
		listView.setAdapter(adapter);
		/** 在这里可以实现自定义视图的功能 */
		// listView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int position,
		// long arg3) {
		// // TODO Auto-generated method stub
		// popupwindow.dismiss();
		// edit_area.setText("县");
		// edit_city.setText(address1s.get(position).getArea_Name());
		// citys=address1s.get(position).getId();
		//
		// address2s=address1s.get(position).getList();
		//// address1s= addresses.get(position).getList();
		// }
		// });

	}

	public void initmPopupWindowView3() {

		// // 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow = new PopupWindow(customView, 360, ViewGroup.LayoutParams.FILL_PARENT);
		ListView listView = (ListView) customView.findViewById(R.id.list_povic);
		MyAdapter3 adapter = new MyAdapter3();
		listView.setAdapter(adapter);
		/** 在这里可以实现自定义视图的功能 */
		// listView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int position,
		// long arg3) {
		// // TODO Auto-generated method stub
		// popupwindow.dismiss();
		// edit_area.setText(address2s.get(position).getArea_Name());
		// areas=address2s.get(position).getId();
		//// address2s=address1s.get(position).getList();
		//// address1s= addresses.get(position).getList();
		// }
		// });
	}

	public void initmPopupWindowView4() {

		// // 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow = new PopupWindow(customView, 360, ViewGroup.LayoutParams.FILL_PARENT);
		ListView listView = (ListView) customView.findViewById(R.id.list_povic);
		MyAdapter4 adapter = new MyAdapter4();
		listView.setAdapter(adapter);
		/** 在这里可以实现自定义视图的功能 */
		// listView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int position,
		// long arg3) {
		// // TODO Auto-generated method stub
		// popupwindow.dismiss();
		// edit_area.setText(address2s.get(position).getArea_Name());
		// areas=address2s.get(position).getId();
		//// address2s=address1s.get(position).getList();
		//// address1s= addresses.get(position).getList();
		// }
		// });
	}

	public void initmPopupWindowView5() {

		// // 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow = new PopupWindow(customView, 360, ViewGroup.LayoutParams.FILL_PARENT);
		ListView listView = (ListView) customView.findViewById(R.id.list_povic);
		MyAdapter5 adapter = new MyAdapter5();
		listView.setAdapter(adapter);
	}

	// 村/街道
	class MyAdapter5 extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return address4s.size();
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
		public View getView(final int position, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(RegisterActivity3.this).inflate(R.layout.provic_item, null);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView.findViewById(R.id.text_povice);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView.setText(address4s.get(position).getName());
			holder.textView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					popupwindow.dismiss();
					edit_vaillage.setText(address4s.get(position).getName());
					vaillage = address4s.get(position).getId();
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView textView;
		}
	}

	// 乡镇
	class MyAdapter4 extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return address3s.size();
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
		public View getView(final int position, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(RegisterActivity3.this).inflate(R.layout.provic_item, null);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView.findViewById(R.id.text_povice);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView.setText(address3s.get(position).getName());
			holder.textView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					popupwindow.dismiss();
					edit_vaillage.setText("村/街道");
					edit_towns.setText(address3s.get(position).getName());
					towns = address3s.get(position).getId();
					String urlString = UrlContact.URL_STRING + "/api/user/GetAreaByParentId?parentId=" + towns;
					Map<String, String> params2 = new HashMap<String, String>();
					HttpconnectionUtil.uploadFile(getApplicationContext(), new ReturnResult2() {

						@Override
						public void getResult(String result) {
							if (result.equals("error")) {
								Toast.makeText(getApplicationContext(), "网络断开连接！", 0).show();
							}
							if (result.equals("")) {
								Toast.makeText(getApplicationContext(), "服务器异常！", 0).show();
							}
							if (!result.equals("") && !result.equals("error")) {
								try {
									JSONObject jsonObject = new JSONObject(result);
									Boolean Result = jsonObject.getBoolean("Result");
									if (Result) {
										address4s = AddressPovicJsonUtil.getAddressPovic(result);
										if (address4s.equals("") || address4s == null || address4s.size() == 0) {
											view_vaillage.setVisibility(View.GONE);

										} else {
											view_vaillage.setVisibility(View.VISIBLE);
										}
									}

								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						}
					}, Method.GET, urlString, params2);
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView textView;
		}
	}

	// 县
	class MyAdapter3 extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return address2s.size();
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
		public View getView(final int position, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(RegisterActivity3.this).inflate(R.layout.provic_item, null);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView.findViewById(R.id.text_povice);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView.setText(address2s.get(position).getName());
			holder.textView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					popupwindow.dismiss();
					edit_towns.setText("乡镇");
					edit_vaillage.setText("村/街道");
					edit_area.setText(address2s.get(position).getName());
					areas = address2s.get(position).getId();
					String urlString = UrlContact.URL_STRING + "/api/user/GetAreaByParentId?parentId=" + areas;
					Map<String, String> params2 = new HashMap<String, String>();
					HttpconnectionUtil.uploadFile(getApplicationContext(), new ReturnResult2() {

						@Override
						public void getResult(String result) {
							if (result.equals("error")) {
								Toast.makeText(getApplicationContext(), "网络断开连接！", 0).show();
							}
							if (result.equals("")) {
								Toast.makeText(getApplicationContext(), "服务器异常！", 0).show();
							}
							if (!result.equals("") && !result.equals("error")) {
								try {
									JSONObject jsonObject = new JSONObject(result);
									Boolean Result = jsonObject.getBoolean("Result");
									if (Result) {
										address3s = AddressPovicJsonUtil.getAddressPovic(result);
										if (address3s.equals("") || address3s == null || address3s.size() == 0) {
											view_towns.setVisibility(View.GONE);
											view_vaillage.setVisibility(View.GONE);
										} else {
											view_towns.setVisibility(View.VISIBLE);
											view_vaillage.setVisibility(View.VISIBLE);
										}
									}

								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						}
					}, Method.GET, urlString, params2);
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView textView;
		}
	}

	// 市
	class MyAdapter2 extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return address1s.size();
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
		public View getView(final int position, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(RegisterActivity3.this).inflate(R.layout.provic_item, null);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView.findViewById(R.id.text_povice);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView.setText(address1s.get(position).getName());
			holder.textView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					popupwindow.dismiss();
					edit_area.setText("县");
					edit_towns.setText("乡镇");
					edit_vaillage.setText("村/街道");
					edit_city.setText(address1s.get(position).getName());
					citys = address1s.get(position).getId();
					String urlString = UrlContact.URL_STRING + "/api/user/GetAreaByParentId?parentId=" + citys;
					Map<String, String> params2 = new HashMap<String, String>();
					HttpconnectionUtil.uploadFile(getApplicationContext(), new ReturnResult2() {

						@Override
						public void getResult(String result) {
							if (result.equals("error")) {
								Toast.makeText(getApplicationContext(), "网络断开连接！", 0).show();
							}
							if (result.equals("")) {
								Toast.makeText(getApplicationContext(), "服务器异常！", 0).show();
							}
							if (!result.equals("") && !result.equals("error")) {
								try {
									JSONObject jsonObject = new JSONObject(result);
									Boolean Result = jsonObject.getBoolean("Result");
									if (Result) {
										address2s = AddressPovicJsonUtil.getAddressPovic(result);
									}

								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						}
					}, Method.GET, urlString, params2);
					// address2s=address1s.get(position).getList();
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView textView;
		}
	}

	// 省
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return addresses.size();
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
		public View getView(final int position, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(RegisterActivity3.this).inflate(R.layout.provic_item, null);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView.findViewById(R.id.text_povice);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView.setText(addresses.get(position).getName());
			holder.textView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					popupwindow.dismiss();
					edit_povic.setText(addresses.get(position).getName());
					povince = addresses.get(position).getId();
					// address1s=addresses.get(position).getList();
					String urlString = UrlContact.URL_STRING + "/api/user/GetAreaByParentId?parentId=" + povince;
					Map<String, String> params2 = new HashMap<String, String>();
					HttpconnectionUtil.uploadFile(getApplicationContext(), new ReturnResult2() {

						@Override
						public void getResult(String result) {
							if (result.equals("error")) {
								Toast.makeText(getApplicationContext(), "网络断开连接！", 0).show();
							}
							if (result.equals("")) {
								Toast.makeText(getApplicationContext(), "服务器异常！", 0).show();
							}
							if (!result.equals("") && !result.equals("error")) {
								try {
									JSONObject jsonObject = new JSONObject(result);
									Boolean Result = jsonObject.getBoolean("Result");
									if (Result) {
										address1s = AddressPovicJsonUtil.getAddressPovic(result);
									}

								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						}
					}, Method.GET, urlString, params2);
					edit_city.setText("市");
					edit_area.setText("县");
					edit_towns.setText("乡镇");
					edit_vaillage.setText("村/街道");
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView textView;
		}
	}

	public String getFromAssets(String fileName) {
		String result = "";
		try {
			InputStream in = getResources().getAssets().open(fileName);
			// 获取文件的字节数
			int lenght = in.available();
			// 创建byte数组
			byte[] buffer = new byte[lenght];
			// 将文件中的数据读到byte数组中
			in.read(buffer);
			result = EncodingUtils.getString(buffer, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	class GetNewsTask extends AsyncTask<String, Void, Integer> {
		private String string;

		@Override
		protected Integer doInBackground(String... params) {
			// try {
			// string=getFromAssets("bb.txt");
			// addresses= AddressJsonUtil.getAddresses(string);
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			/*
			 * String urlString =
			 * UrlContact.URL_STRING+"/api/user/GetCommonArea"; Map<String,
			 * String> params2 = new HashMap<String, String>();
			 * HttpconnectionUtil.uploadFile(getApplicationContext(), new
			 * ReturnResult2() {
			 * 
			 * @Override public void getResult(String result) { if
			 * (result.equals("error")) {
			 * Toast.makeText(getApplicationContext(), "网络断开连接！", 0).show(); }if
			 * (result.equals("")) { Toast.makeText(getApplicationContext(),
			 * "服务器异常！", 0).show(); }if
			 * (!result.equals("")&&!result.equals("error")) { try { string =
			 * result; addresses = AddressJsonUtil.getAddresses(string); } catch
			 * (JSONException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } } } }, Method.GET, urlString, params2);
			 */

			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			mCache.put("addString", (Serializable) addresses);
			edit_povic.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (addresses == null) {

					} else {
						if (popupwindow != null && popupwindow.isShowing()) {

						} else {
							initmPopupWindowView();
							popupwindow.showAsDropDown(arg0, 0, 5);
						}
					}

				}
			});
			edit_city.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (address1s == null) {

					} else {
						if (popupwindow != null && popupwindow.isShowing()) {

						} else {
							initmPopupWindowView2();
							popupwindow.showAsDropDown(arg0, 0, 5);
						}
					}

				}
			});
			edit_area.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (address2s == null) {

					} else {
						if (popupwindow != null && popupwindow.isShowing()) {

						} else {
							initmPopupWindowView3();
							popupwindow.showAsDropDown(arg0, 0, 5);
						}

					}
				}
			});
			edit_towns.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (address3s == null) {

					} else {
						if (popupwindow != null && popupwindow.isShowing()) {

						} else {
							initmPopupWindowView4();
							popupwindow.showAsDropDown(arg0, 0, 5);
						}

					}
				}
			});
			edit_vaillage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (address4s == null) {

					} else {
						if (popupwindow != null && popupwindow.isShowing()) {

						} else {
							initmPopupWindowView5();
							popupwindow.showAsDropDown(arg0, 0, 5);
						}

					}
				}
			});
			btn_next.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String password1 = edit_password.getText().toString();
					String password2 = edit_password2.getText().toString();
					final String povic = edit_povic.getText().toString();
					final String city = edit_city.getText().toString();
					final String area = edit_area.getText().toString();
					String towns = edit_towns.getText().toString();
					String vaillage = edit_vaillage.getText().toString();

					if (!password1.equals("") && !password2.equals("") && !povic.equals("省") && !city.equals("市")
							&& !area.equals("县")) {
						if (towns.equals("乡镇") && address3s.size() > 0) {
							Toast.makeText(getApplicationContext(), "请先填写资料", 0).show();
						} else {
							if (towns.equals("乡镇")) {
								towns = "";
								vaillage = "";

							}
							if (vaillage.equals("村/街道") && address4s.size() > 0) {
								Toast.makeText(getApplicationContext(), "请先填写资料", 0).show();
							} else {
								if (vaillage.equals("村/街道")) {
									vaillage = "";
								}
								if (password1.equals(password2)) {
									if (password1.toCharArray().length < 6) {
										Toast.makeText(getApplicationContext(), "密码不能小于6位", 0).show();
									} else {
										if (daigou == 1) {
											String urlString2 = UrlContact.URL_STRING + "/api/User/Regist?User_Name="
													+ phone + "&mobileVCode=" + code + "&deviceId="
													+ MyApplication.sUdid + "&User_Pwd="
													+ encryption(password1).toUpperCase() + "&txtProvince=" + povince
													+ "&txtCity=" + citys + "&txtCounty=" + areas + "&txtTown="
													+ RegisterActivity3.this.towns + "&txtVillage="
													+ RegisterActivity3.this.vaillage;
											MyProgressDialog.show(RegisterActivity3.this, true, true);
											Map<String, String> params2 = new HashMap<String, String>();
											HttpconnectionUtil.uploadFile(RegisterActivity3.this, new ReturnResult2() {

												@Override
												public void getResult(String result) {
													// TODO Auto-generated
													// method stub
													MyProgressDialog.cancel();
													try {
														if (result.equals("error")) {
															Toast.makeText(getApplicationContext(), "网络断开连接！", 0)
																	.show();
														}
														if (result.equals("")) {
															Toast.makeText(getApplicationContext(), "服务器异常！", 0).show();
														}
														if (!result.equals("") && !result.equals("error")) {
															JSONObject jsonObject = new JSONObject(result);
															Boolean Result = jsonObject.getBoolean("Result");
															if (Result) {
																User user = UserJsonUtil.getUser(result);

																Intent intent = new Intent(RegisterActivity3.this,
																		RegisterActivity4.class);
																intent.putExtra("phone", phone);
																startActivityForResult(intent, 22);
																mCache = ACache.get(RegisterActivity3.this);
																mCache.put("loginString", "true");
																mCache.put("user", user);
																/*if (user.getArea_Name().equals("") || user.getArea_Name() == null) {
																	
																}else {
																	
																	gps = (MyGPS) mCache.getAsObject("gps");
																	if (gps == null) {
																		gps = new MyGPS();
																	}
																	gps.setProId(user.getProvince_Id());
																	gps.setCityId(user.getCity_Id());
																	gps.setCouId(user.getCounty_Id());
																	gps.setProvinceName(povic);
																	gps.setCityName(city);
																	gps.setCountyName(area);
																	mCache.put("gpsString","true");
																	mCache.put("gps", gps);
																}*/
																finish();

															} else {
																String message = jsonObject.getString("Message");
																Toast.makeText(getApplicationContext(), message, 0)
																		.show();
															}

														}
													} catch (JSONException e) {
														// TODO Auto-generated
														// catch
														// block
														e.printStackTrace();
													} 

												}
											}, Method.GET, urlString2, params2);
										} else {

											// ----------------------------------------------------------------------------------------start
											Map<String, String> params2 = new HashMap<String, String>();
											params2.put("deviceId", MyApplication.sUdid);
											params2.put("User_Name", phone);
											params2.put("User_Pwd", encryption(password1).toUpperCase());
											params2.put("VCode", code);
											params2.put("txtProvince", povince + "");
											params2.put("txtCity", citys + "");
											params2.put("txtCounty", areas + "");
											params2.put("txtTown", RegisterActivity3.this.towns + "");
											params2.put("txtVillage", RegisterActivity3.this.vaillage + "");
											params2.put("CardType", 1 + "");
											HttpconnectionUtil.uploadFile(RegisterActivity3.this, new ReturnResult2() {

												@Override
												public void getResult(String result) {
													// TODO Auto-generated
													// method stub
													MyProgressDialog.cancel();
													if (result.equals("error")) {
														Toast.makeText(getApplicationContext(), "网络断开连接！", 0).show();
													}
													if (result.equals("")) {
														Toast.makeText(getApplicationContext(), "服务器异常！", 0).show();
													}
													if (!result.equals("") && !result.equals("error")) {
														try {
															JSONObject jsonObject = new JSONObject(result);
															Boolean Result = jsonObject.getBoolean("Result");
															if (Result) {
																// String
																// message=jsonObject.getString("Message");
																// Toast.makeText(getApplicationContext(),
																// message,
																// 0).show();
																User user = UserJsonUtil.getUser(result);

																Intent intent = new Intent(RegisterActivity3.this,
																		RegisterActivity4.class);
																intent.putExtra("phone", phone);
																startActivityForResult(intent, 22);
																mCache = ACache.get(RegisterActivity3.this);
																mCache.put("loginString", "true");
																mCache.put("user", user);

															} else {
																String message = jsonObject.getString("Message");
																Toast.makeText(getApplicationContext(), message, 0)
																		.show();
															}

														} catch (JSONException e) {
															// TODO
															// Auto-generated
															// catch
															// block
															e.printStackTrace();
														}
													}
												}
											}, Method.POST, UrlContact.URL_STRING + "/api/User/PurchasingRegist",
													params2);
										}
									}

								} else {
									Toast.makeText(getApplicationContext(), "两次密码输入不正确", 0).show();
								}
							}
						}
					} else {
						Toast.makeText(getApplicationContext(), "请先填写资料", 0).show();

					}

				}
			});
		}
	}

	public String encryption(String plainText) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			re_md5 = buf.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return re_md5;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String urlString = UrlContact.URL_STRING + "/api/user/GetAreaByParentId?parentId=1";
		Map<String, String> params2 = new HashMap<String, String>();
		HttpconnectionUtil.uploadFile(getApplicationContext(), new ReturnResult2() {

			@Override
			public void getResult(String result) {
				if (result.equals("error")) {
					Toast.makeText(getApplicationContext(), "网络断开连接！", 0).show();
				}
				if (result.equals("")) {
					Toast.makeText(getApplicationContext(), "服务器异常！", 0).show();
				}
				if (!result.equals("") && !result.equals("error")) {
					try {
						JSONObject jsonObject = new JSONObject(result);
						Boolean Result = jsonObject.getBoolean("Result");
						if (Result) {
							addresses = AddressPovicJsonUtil.getAddressPovic(result);
						}

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}, Method.GET, urlString, params2);
	}

}
