package com.example.lanuchertest.wegdit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.classes.DaiGouMingXi;
import com.example.classes.Products1;
import com.example.classes.User;
import com.example.nyapp.R;

import java.text.DecimalFormat;

public class PanelView extends RelativeLayout {
	private ASimpleCache.org.afinal.simplecache.ACache mCache;
	private Context mContext;
	private LayoutInflater inflater;
	private Button button;
	public WindowManager manager;
	public WindowManager.LayoutParams params;
	public RelativeLayout bgLayout;// �������Ƶ������������ʧ�����
	public LinearLayout contentLayout;// ����չʾ���ݵ����
	private TextView product_name, text_spec, text_price, text_count,
			text_shouyibili, text_daigou_price, text_daigoushouyi;
	private User user;
	private DaiGouMingXi daiGouMingXi;
	// private Products products;
	private Products1 products;
	private DaiGouMingXi daiGouMingXi2;
	DecimalFormat ddf1 = new DecimalFormat("#.00");
	public PanelView(Context context) {
		super(context);
		mContext = context;
		// loading();
		ini();
		iniWindowManager();
	}

	public PanelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		ini();
		iniWindowManager();
		// TODO Auto-generated constructor stub
	}

	public PanelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		// loading();
		ini();
		iniWindowManager();

	}

	public PanelView(Context context, Products1 products) {
		super(context);
		mContext = context;
		loading(products);
		ini();
		iniWindowManager();
	}

	public void loading(Products1 products) {
		this.products = products;
	}

	public void ini() {
		inflater = ((Activity) mContext).getLayoutInflater();
		// ����viewinflate�����view�ĸ��ؼ�
		inflater.inflate(R.layout.wordpanel, this);
		product_name = (TextView) findViewById(R.id.text_prodctname);
		text_spec = (TextView) findViewById(R.id.text_spec);
		text_price = (TextView) findViewById(R.id.text_price);
		text_count = (TextView) findViewById(R.id.text_count);
		text_shouyibili = (TextView) findViewById(R.id.text_shouyibili);
		text_daigou_price = (TextView) findViewById(R.id.text_daigou_price);
		text_daigoushouyi = (TextView) findViewById(R.id.text_daigoushouyi);

		product_name.setText(products.getName());

		text_spec.setText(products.getSpec());

		text_shouyibili.setText(products.getScale());


		text_price.setText(products.getPrice());
		text_count.setText(products.getCount());

		text_daigou_price.setText(products.getMoney());
		text_daigoushouyi.setText(products.getLucreMoney());

		getView();
		setView();
	}


	public void iniWindowManager() {
		manager = (WindowManager) mContext.getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		params = new WindowManager.LayoutParams();
		//这一项非常重要，用于设置背景透明
//		params.format = PixelFormat.TRANSPARENT; // 设置图片格式，效果为背景透明
		
		 params.alpha=0.9f;//用于设置窗体本身的透明度

		// params.gravity = Gravity.CENTER;
		params.gravity = Gravity.FILL;
		params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
				| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
				| WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
				| WindowManager.LayoutParams.FLAG_FULLSCREEN;
		// // 设置window type,此控件位于打电话的那一层
		// 这两点是设置的关键的地方
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		// params.x = 50;
		// params.y = 50;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//		Rect rect = new Rect();
//		contentLayout.getGlobalVisibleRect(rect);
		/*if (!rect.contains(params.width, params.height)) {
            PanelView.hidePopupWindow();
        }*/
		manager.addView(this, params);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.e("out touch", "");

		return false;
	}

	public void getView() {
		bgLayout = (RelativeLayout) findViewById(R.id.bg_panel);
		contentLayout = (LinearLayout) findViewById(R.id.content);
	}

	public void setView() {
		bgLayout.setBackgroundColor(Color.TRANSPARENT);
		contentLayout.setBackgroundColor(Color.WHITE);

//		contentLayout.setOnTouchListener(onTouchListener);
		bgLayout.setOnClickListener(onClickListener);
	}

	private OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (v.getId()) {
			}
//			manager.removeView(PanelView.this);
			return true;
		}
	};
	/**
	 * 
	 * �ڱ�����һ�㲶�����¼��������view�ؼ�
	 */
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			manager.removeView(PanelView.this);
		}
	};

}
