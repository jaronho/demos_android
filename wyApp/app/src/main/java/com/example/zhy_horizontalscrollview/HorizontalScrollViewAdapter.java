package com.example.zhy_horizontalscrollview;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.List;

import com.example.classes.MyCouponGoods;
import com.example.nyapp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HorizontalScrollViewAdapter {

	private LayoutInflater mInflater;
	private List<String> listImg;
	private ImageLoader imageLoader = null;
	private DisplayImageOptions options = null;
	private List<MyCouponGoods> couponGoods;
	DecimalFormat ddf1 = new DecimalFormat("#.00");

	public HorizontalScrollViewAdapter(FragmentActivity activity, List<String> listImg, List<MyCouponGoods> goods) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(activity);
		this.listImg = listImg;
		this.couponGoods = goods;
	}

	public int getCount() {
		return listImg.size();
	}

	public Object getItem(int position) {
		return listImg.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.coupon_gallery_item, parent, false);
			viewHolder.mImg = (ImageView) convertView.findViewById(R.id.cou_img);
			viewHolder.cou_pro_name = (TextView) convertView.findViewById(R.id.cou_pro_name);
			viewHolder.cou_spec = (TextView) convertView.findViewById(R.id.cou_spec);
			viewHolder.cou_sprice = (TextView) convertView.findViewById(R.id.cou_price);
//			viewHolder.cou_unil_price = (TextView) convertView.findViewById(R.id.unil_cou_price);
//			viewHolder.cou_cate_name = (TextView) convertView.findViewById(R.id.cou_cate_name);
			viewHolder.cou_unil = (TextView) convertView.findViewById(R.id.cou_unil);
//			viewHolder.layout_cou_unil_sub = (LinearLayout) convertView.findViewById(R.id.layout_cou_unil_sub);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().displayer(new SimpleBitmapDisplayer()).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.build();
		imageLoader.displayImage(listImg.get(position), viewHolder.mImg, options);
//		for (int i = 0; i < couponGoods.size(); i++) {
		if(position<=couponGoods.size()-1){
			viewHolder.cou_pro_name.setText(couponGoods.get(position).getPro_Name());
			viewHolder.cou_spec.setText(couponGoods.get(position).getSpec());
			if (couponGoods.get(position).getPrice().equals("")||couponGoods.get(position).getPrice().equals("0")) {
				viewHolder.cou_sprice.setText(couponGoods.get(position).getShow_Price()+"");
//				viewHolder.layout_cou_unil_sub.setVisibility(View.GONE);
				viewHolder.cou_unil.setVisibility(View.GONE);
			}else {
//				viewHolder.layout_cou_unil_sub.setVisibility(View.VISIBLE);
				viewHolder.cou_unil.setVisibility(View.VISIBLE);
				if (Double.valueOf(couponGoods.get(position).getPrice())<1) {
					viewHolder.cou_sprice.setText("0"+ddf1.format(Double.valueOf(couponGoods.get(position).getPrice())));
					
				} else {
					viewHolder.cou_sprice.setText(ddf1.format(Double.valueOf(couponGoods.get(position).getPrice())));

				}
				viewHolder.cou_unil.setText("元/"+couponGoods.get(position).getUnit());
//				if (Double.valueOf(couponGoods.get(position).getSubUnit_Price())<1) {
//					viewHolder.cou_unil_price.setText("0"+ddf1.format(Double.valueOf(couponGoods.get(position).getSubUnit_Price())));
//					
//				} else {
//					viewHolder.cou_unil_price.setText(ddf1.format(Double.valueOf(couponGoods.get(position).getSubUnit_Price())));
//
//				}
//				viewHolder.cou_cate_name.setText(couponGoods.get(position).getSubUnit());
				
			}

		}

		return convertView;
	}

	private class ViewHolder {
		ImageView mImg;
		TextView cou_pro_name, cou_spec, cou_sprice,cou_unil;
//		LinearLayout layout_cou_unil_sub;
//		TextView cou_unil_price, cou_cate_name；
	}

	public static Bitmap GetLocalOrNetBitmap(String url) {
		Bitmap map = null;
		try {
			URL url1 = new URL(url);
			URLConnection conn = url1.openConnection();
			conn.connect();
			InputStream in;
			in = conn.getInputStream();
			map = BitmapFactory.decodeStream(in);
			// TODO Auto-generated catch block
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

}
