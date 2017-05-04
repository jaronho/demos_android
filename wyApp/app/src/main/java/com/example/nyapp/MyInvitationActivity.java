package com.example.nyapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.example.classes.User;
import com.example.classes.UserDetail;
import com.example.util.HttpconnectionUtil.ReturnResult2;
import com.example.util.MyMsgDialog;
import com.example.util.MyProgressDialog;
import com.example.util.UrlContact;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ASimpleCache.org.afinal.simplecache.ACache;

public class MyInvitationActivity extends Activity {

	EditText text;
	private ListView listView;
	String username, usernumber;
	private UserDetail detil = new UserDetail();
	private TextView button, add;
	private List<UserDetail> lists = new ArrayList<UserDetail>();
	private Button requset;
	private LinearLayout layout_back;
	private MyAdapter adapter;
	private List<String> numbers = new ArrayList<String>();
	private TextView request_number;
	private String num_order, num_people;
	private String message;
	private User user;
	private ACache mCache;
	private String mobiles;
	private String mobile;
	private MyMsgDialog mBindingDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myinvitation);
		text = (EditText) findViewById(R.id.number);
		button = (TextView) findViewById(R.id.button);
		add = (TextView) findViewById(R.id.add);
		findViewById(R.id.invitation_remove).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager)  
				         getSystemService(Context.INPUT_METHOD_SERVICE);  
				         imm.hideSoftInputFromWindow(arg0.getWindowToken(), 0);  
			}
		});
		listView = (ListView) findViewById(R.id.listView1);
		request_number = (TextView) findViewById(R.id.request_number);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		mCache = ACache.get(getApplicationContext());
		user = (User) mCache.getAsObject("user");
		layout_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				finish();
			}
		});
		requset = (Button) findViewById(R.id.request);
		requset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Integer.parseInt(num_order) == 0) {
					Toast.makeText(getApplicationContext(), "您今天已经没有邀请次数了！", Toast.LENGTH_SHORT).show();
				} else {
					if (lists.size() >= Integer.parseInt(num_people)) {
						
					
					mobile = lists.get(0).getPhoneNumber();
					mobiles = mobile;
					String mobile1 = null;
					for (int i = 1; i < lists.size(); i++) {
						mobile1 = lists.get(i).getPhoneNumber();

						mobiles = mobile1 + "," + mobiles;
						mobile = mobile + ";" + mobile1;
					}
					if (user.getMobile().equals("")) {
						String message1 = "参与比赛需要绑定手机号码，您当前没有绑定，请先绑定手机号码！";
						showBindingDialog(message1);
					}
//					else {
							/*
							 * if (lists.size() >= Integer.parseInt(num_people))
							 * { setNumbers(); } else { String message1 =
							 * "每次邀请至少" + num_people + "人，请继续添加！";
							 * showBindingDialog(message1); }
							 */

//					}
					setNumbers();
					} else {
						String message1 = "每次邀请至少" + num_people + "人，请继续添加！";
						showBindingDialog(message1);
					}

				}
			}

		});
		add.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unused")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (text.getText().toString().equals("")) {
					Toast.makeText(MyInvitationActivity.this, "电话号码不可为空", Toast.LENGTH_SHORT).show();
				} else {
					Boolean flag = false;
					if (lists.size() == 0) {
						flag = text.getText().toString().equals(user.getMobile());
					}
					for (int i = 0; i < lists.size(); i++) {
						flag = lists.get(i).getPhoneNumber().equals(text.getText().toString())
								|| text.getText().toString().equals(user.getMobile());
						if (flag) {
							break;
						}
					}
					if (flag) {
						Toast.makeText(MyInvitationActivity.this, "不能重复邀请同一手机号。注：邀请人已默认在小组内，无需再次邀请。", Toast.LENGTH_LONG)
								.show();
					} else {
						if (isMobile(text.getText().toString())) {
							flag = text.getText().toString().equals(user.getMobile());

							detil = new UserDetail();
							detil.setPhoneName("未命名");
							String phone = text.getText().toString();
							phone = phone.substring(phone.length() - 11, phone.length());
							detil.setPhoneNumber(phone);
							lists.add(detil);

						} else {
							Toast.makeText(getApplicationContext(), "手机格式不正确", 0).show();
						}

					}
					adapter = new MyAdapter((ArrayList<UserDetail>) lists);
					// request_number.setText(lists.size() + "");
					listView.setAdapter(adapter);
					listView.setEmptyView(findViewById(R.id.empty));
					text.setText(null);
				}

			}
		});

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);
			}
		});

	}
	public void onClick(View v) {  
	    switch (v.getId()) {  
	    case R.id.invitation_remove:  
	         
	        break;  
	    }  
	  
	} 
	public static boolean isMobile(String mobiles) {
		String regExp = "^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9])|(14[0-9])|(16[0-9]))\\d{8}$";

		Pattern p = Pattern.compile(regExp);

		Matcher m = p.matcher(mobiles);

		return m.find();// boolean
	}

	@SuppressWarnings("null")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (resultCode == Activity.RESULT_OK) {
				// lists.add(detil);
				ContentResolver reContentResolverol = getContentResolver();
				Uri contactData = data.getData();
				@SuppressWarnings("deprecation")
				Cursor cursor = managedQuery(contactData, null, null, null, null);
				cursor.moveToFirst();
				Boolean a = cursor.moveToFirst();
//				System.out.println("-------------------  "+a);
				
//					System.out.println("-----------------------------------    "+cursor.getColumnIndex(android.provider.ContactsContract.Contacts.DISPLAY_NAME));
					if (cursor.getColumnIndex(android.provider.ContactsContract.Contacts.DISPLAY_NAME) == -1) {
						Toast.makeText(getApplicationContext(), "添加失败，请重新添加。", 0).show();
					} else {
						if (cursor.moveToFirst()) {
							numbers = new ArrayList<String>();
						username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
						
						String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
						Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
						String phoneNumber;
						while (phone.moveToNext()) {
							do {
								// 遍历所有的联系人下面所有的电话号码
								phoneNumber = phone
										.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
								// 使用Toast技术显示获得的号码
								numbers.add(phoneNumber);
							} while (phone.moveToNext());
							if (numbers.size() > 1) {
								showDilog(username, numbers);
							} else if (numbers.size() == 1) {
								if (lists.size() == 0) {
									phoneNumber = phoneNumber.replaceAll(" ", "");
									phoneNumber = phoneNumber.replaceAll("-", "");
									Boolean flag = false;
									if (phoneNumber.equals(user.getMobile())) {
										flag = true;
									}
									if (flag) {
										Toast.makeText(MyInvitationActivity.this, "不能重复邀请同一手机号。注：邀请人已默认在小组内，无需再次邀请。",
												Toast.LENGTH_LONG).show();
										break;
									} else {
										phoneNumber = phoneNumber.substring(phoneNumber.length() - 11,
												phoneNumber.length());
										if (isMobile(phoneNumber)) {
											detil = new UserDetail();
											detil.setPhoneName(username);
											phoneNumber = phoneNumber.substring(phoneNumber.length() - 11,
													phoneNumber.length());
											detil.setPhoneNumber(phoneNumber);
											// detil.setPhoneNumber(phoneNumber);
											lists.add(detil);
										} else {
											Toast.makeText(getApplicationContext(), "手机格式不正确", 0).show();
										}
									}
								} else {
									phoneNumber = phoneNumber.replaceAll(" ", "");
									phoneNumber = phoneNumber.replaceAll("-", "");
									Boolean flag = false;
									for (int i = 0; i < lists.size(); i++) {
										flag = lists.get(i).getPhoneNumber().equals(phoneNumber)
												|| phoneNumber.equals(user.getMobile());
										if (flag) {
											break;
										}
									}
									if (flag) {
										Toast.makeText(MyInvitationActivity.this, "不能重复邀请同一手机号。注：邀请人已默认在小组内，无需再次邀请。",
												Toast.LENGTH_LONG).show();
										break;
									} else {
										phoneNumber = phoneNumber.substring(phoneNumber.length() - 11,
												phoneNumber.length());
										if (isMobile(phoneNumber)) {
											detil = new UserDetail();
											detil.setPhoneName(username);
											
											detil.setPhoneNumber(phoneNumber);
											// detil.setPhoneNumber(phoneNumber);
											lists.add(detil);
										} else {
											Toast.makeText(getApplicationContext(), "手机格式不正确", 0).show();
										}
										
										break;
									}
								}
							}
							
							// text.setText(phoneNumber + " (" + username + ")");
						}
					}else {
						Toast.makeText(getApplicationContext(), "您的手机已阻止本应用对通讯录的请求，欲使用本功能须先到 设置-应用管理 里找到 农一网，在权限管理里开启 联系人信息 权限。", 0).show();
					}
					
				}
					adapter = new MyAdapter((ArrayList<UserDetail>) lists);
					// request_number.setText(lists.size() + "");
					listView.setAdapter(adapter);
					listView.setEmptyView(findViewById(R.id.empty));
			}
		}
	}

	class MyAdapter extends BaseAdapter {
		private List<UserDetail> maps;

		public MyAdapter(ArrayList<UserDetail> maps) {
			this.maps = maps;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return maps.size();
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
				convertView = LayoutInflater.from(MyInvitationActivity.this).inflate(R.layout.myinvitation_item, null);
				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.phonename);
				holder.number = (TextView) convertView.findViewById(R.id.phonenumber);
				holder.delete = (TextView) convertView.findViewById(R.id.delete);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (maps.size() > 0) {
				holder.name.setText(maps.get(position).getPhoneName().toString());
				holder.number.setText(maps.get(position).getPhoneNumber().toString());
				holder.delete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						maps.remove(position);
						notifyDataSetChanged();
					}
				});
			}
			return convertView;
		}

		class ViewHolder {
			TextView name;
			TextView number;
			TextView delete;
		}
	}

	// 提交数据
	private void setNumbers() {
		// String url = UrlContact.URL_STRING +
		// "/api/usercenter/AddInviteMobile?mobile="+user.getMobile()+"&mobiles="+mobiles+"&loginKey="+user.getUser_Name()+"&deviceId="+MyApplication.sUdid;
		String url = UrlContact.URL_STRING + "/api/usercenter/AddInviteMobile" + "?loginKey=" + user.getUser_Name()
				+ "&deviceId=" + MyApplication.sUdid;
		Map<String, String> params = new HashMap<String, String>();
		params.put("Mobile", user.getMobile());
		params.put("Mobiles", mobiles);
		params.put("LoginKey", user.getUser_Name());
		params.put("DeviceId", MyApplication.sUdid);
		MyProgressDialog.show(MyInvitationActivity.this, false, true);
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
					lists.clear();
					adapter = new MyAdapter((ArrayList<UserDetail>) lists);
					// request_number.setText(lists.size() + "");
					listView.setAdapter(adapter);
					listView.setEmptyView(findViewById(R.id.empty));
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.putExtra("address", mobile);
					intent.putExtra("sms_body", message);
					intent.setType("vnd.android-dir/mms-sms");
					startActivity(intent);

				}
			}
		}, Method.POST, url, params);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 加载数据
		String url = UrlContact.URL_STRING +"/api/usercenter/GetInviteCount?m=" + user.getMobile() + "&loginKey=" + user.getUser_Name()
				+ "&deviceId=" + MyApplication.sUdid;
		final Map<String, String> params = new HashMap<String, String>();
		MyProgressDialog.show(MyInvitationActivity.this, false, true);
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
					try {
						JSONObject jsonObject = new JSONObject(result);
						message = jsonObject.getString("Message");
						if (jsonObject.getBoolean("Result")) {
							String data = jsonObject.getString("Data");
							String s1[] = data.split(",");
							num_order = s1[0];
							num_people = String.valueOf(Integer.parseInt(s1[1])-1);
							request_number.setText(num_order);
							
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}, Method.GET,url, params);

		// ---------------------end
	}

	private void showDilog(final String name, List<String> list) {
		final String[] array = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = (String) list.get(i);
		}

		Dialog dialog = new AlertDialog.Builder(this).setTitle("请选择有效电话：")
				.setSingleChoiceItems(array, 0, new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (lists.size() == 0) {
							array[which] = array[which].replaceAll(" ", "");
							array[which] = array[which].replaceAll("-", "");
							array[which] = array[which].substring(array[which].length() - 11,
									array[which].length());
							if (isMobile(array[which])) {
								detil = new UserDetail();
								detil.setPhoneName(username);
								detil.setPhoneNumber(array[which]);
								// detil.setPhoneNumber(array[which]);
								lists.add(detil);
							} else {
								Toast.makeText(getApplicationContext(), "手机格式不正确", 0).show();
							}
						} else {
							array[which] = array[which].replaceAll(" ", "");
							array[which] = array[which].replaceAll("-", "");
							
							Boolean flag = false;
							for (int i = 0; i < lists.size(); i++) {
								flag = lists.get(i).getPhoneNumber().equals(array[which])
										|| array[which].equals(user.getMobile());
								if (flag) {
									break;
								}
							}
							if (flag) {

								Toast.makeText(MyInvitationActivity.this, "不能重复邀请同一手机号。注：邀请人已默认在小组内，无需再次邀请。",
										Toast.LENGTH_LONG).show();
							} else {
								array[which] = array[which].substring(array[which].length() - 11,
										array[which].length());
								if (isMobile(array[which])) {
									detil = new UserDetail();
									detil.setPhoneName(username);
									detil.setPhoneNumber(array[which]);
									lists.add(detil);
								} else {
									Toast.makeText(getApplicationContext(), "手机格式不正确", 0).show();
								}
							}
						}
						adapter = new MyAdapter((ArrayList<UserDetail>) lists);
						// request_number.setText(lists.size() + "");
						listView.setAdapter(adapter);
						listView.setEmptyView(findViewById(R.id.empty));
						dialog.cancel();
					}
				}

		).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		}).create();
		dialog.show();

	}

	private void showBindingDialog(String message) {
		mBindingDialog = new MyMsgDialog(this, "温馨提示", message, new MyMsgDialog.ConfirmListener() {
			@Override
			public void onClick() {
				mBindingDialog.dismiss();
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				user.setPurchasing_State(1 + "");
				user.setPermit_Type(2 + "");
				mCache.put("user", user);
				startActivity(intent);
			}
		}, null);
		mBindingDialog.show();
	}

}
