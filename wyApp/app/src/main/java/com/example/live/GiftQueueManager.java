package com.example.live;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nyapp.R;
import com.example.util.ImageViewUtil;
import com.jaronho.sdk.library.timer.Timer;
import com.jaronho.sdk.library.timer.TimerManager;
import com.jaronho.sdk.third.viewanimator.ViewAnimator;
import com.jaronho.sdk.utils.view.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:  Administrator
 * Date:    2017/5/26
 * Brief:   礼物队列管理器
 */

public class GiftQueueManager {
	private Map<String, Map<String, Integer>> mGiftHistoryMap = new HashMap<>();	// 礼物历史
	private List<GiftSendInfo> mGiftList = new ArrayList<>();   // 礼物列表
	private Activity mActivity = null;
	private ViewGroup mGiftSendView1 = null;
	private ViewGroup mGiftSendView2 = null;
	private GiftSendInfo mGiftSendInfo1 = null;
	private GiftSendInfo mGiftSendInfo2 = null;
	private GiftSendInfo mGiftSendInfoPop = null;

	public GiftQueueManager(Activity activity) {
		mActivity = activity;
		mGiftSendView1 = (ViewGroup)activity.findViewById(R.id.layout_gift_send_1);
		mGiftSendView2 = (ViewGroup)activity.findViewById(R.id.layout_gift_send_2);
	}

	// 更新礼物历史,返回最新的数量
	private int updateHistory(String senderId, String giftId, int giftCount) {
		Map<String, Integer> giftHistory;
		if (mGiftHistoryMap.containsKey(senderId)) {
			giftHistory = mGiftHistoryMap.get(senderId);
			if (giftHistory.containsKey(giftId)) {
				giftCount += giftHistory.get(giftId);
			}
			giftHistory.put(giftId, giftCount);
		} else {
			giftHistory = new HashMap<>();
			giftHistory.put(giftId, giftCount);
		}
		mGiftHistoryMap.put(senderId, giftHistory);
		return giftCount;
	}

	// 插入送礼信息
	private void push(GiftSendInfo info) {
		mGiftList.add(info);
	}

	// 弹出送礼信息
	private GiftSendInfo pop() {
		if (mGiftList.isEmpty()) {
			return null;
		}
		GiftSendInfo info = mGiftList.remove(0);
		info.giftNumber = String.valueOf(updateHistory(info.fromId, info.giftId, Integer.valueOf(info.giftNumber)));
		return info;
	}

	// 显示送礼信息
	private void showGiftInfo(final ViewGroup giftSendView, GiftSendInfo info, boolean isNew, final int index) {
		Log.d("NYLive", "gift send: " + info.toString());
		// 头像
		CircleImageView senderAvatarImage = (CircleImageView)giftSendView.findViewById(R.id.imageview_sender);
		if (null != info.fromPic && !info.fromPic.isEmpty()) {
			Picasso.with(mActivity).load(info.fromPic).into(senderAvatarImage);
		}
		// 昵称
		TextView nicknameText = (TextView)giftSendView.findViewById(R.id.textview_sender);
		nicknameText.setText(info.fromNickname.isEmpty() ? info.fromId : info.fromNickname);
		// 礼物信息
		TextView descText = (TextView)giftSendView.findViewById(R.id.textview_desc);
		descText.setText("送了一个" + info.giftName);
		// 礼物图标
		ImageView giftPicImage = (ImageView)giftSendView.findViewById(R.id.imageview_gift_pic);
		if (null != info.giftPic && !info.giftPic.isEmpty()) {
			Picasso.with(mActivity).load(info.giftPic).into(giftPicImage);
		}
		// 礼物个数
		TextView numText = (TextView)giftSendView.findViewById(R.id.textview_num);
		numText.setText("X" + info.giftNumber);
		// 动画
		int hideInterval = 3000;
		if (isNew) {
			int interval = 200;
			hideInterval += interval;
			giftSendView.setVisibility(View.VISIBLE);
			ViewAnimator.animate(giftSendView).dp().translationX(-100f, 0).duration(interval).decelerate().start();
		} else {
			int interval = 100;
			hideInterval += interval;
			ViewAnimator.animate(numText).scale(1, 2, 1).duration(interval).decelerate().start();
		}
		final String timerId = "Timer_gift_send_hide_" + index;
		TimerManager.getInstance().stop(timerId, false);
		TimerManager.getInstance().run(hideInterval, 1, new Timer.OverHandler() {
			@Override
			public void onCallback(Timer timer, Object o) {
				giftSendView.setVisibility(View.INVISIBLE);
				if (1 == index) {
					mGiftSendInfo1 = null;
				} else if (2 == index) {
					mGiftSendInfo2 = null;
				}
				popGift();
			}
		}, timerId);
	}

	// 两个礼物信息是否相等
	private boolean isGiftEqual(GiftSendInfo info1, GiftSendInfo info2) {
		return null != info1 && null != info2 && info1.fromId.equals(info2.fromId) && info1.giftId.equals(info2.giftId);
	}

	// 弹出礼物
	private void popGift() {
		if (null == mGiftSendInfoPop) {
			mGiftSendInfoPop = pop();
		}
		if (null == mGiftSendInfoPop) {
			return;
		}
		if (null == mGiftSendInfo1 && null == mGiftSendInfo2) { // 第1个和第2个都为空
			mGiftSendInfo1 = mGiftSendInfoPop;
			mGiftSendInfoPop = null;
			showGiftInfo(mGiftSendView1, mGiftSendInfo1, true, 1);
		} else if (null != mGiftSendInfo1 && null == mGiftSendInfo2) {  // 第1个不为空,第2个为空
			if (isGiftEqual(mGiftSendInfo1, mGiftSendInfoPop)) {    // 替换第1个
				mGiftSendInfo1 = mGiftSendInfoPop;
				mGiftSendInfoPop = null;
				showGiftInfo(mGiftSendView1, mGiftSendInfo1, false, 1);
			} else {    // 显示在第2个
				mGiftSendInfo2 = mGiftSendInfoPop;
				mGiftSendInfoPop = null;
				showGiftInfo(mGiftSendView2, mGiftSendInfo2, true, 2);
			}
		} else if (null == mGiftSendInfo1 && null != mGiftSendInfo2) {  // 第1个为空,第2个不为空
			if (isGiftEqual(mGiftSendInfo2, mGiftSendInfoPop)) {    // 替换第2个
				mGiftSendInfo2 = mGiftSendInfoPop;
				mGiftSendInfoPop = null;
				showGiftInfo(mGiftSendView2, mGiftSendInfo2, false, 2);
			} else {    // 显示在第1个
				mGiftSendInfo1 = mGiftSendInfoPop;
				mGiftSendInfoPop = null;
				showGiftInfo(mGiftSendView1, mGiftSendInfo1, true, 1);
			}
		} else {    // 第1个和第2个都不为空
			if (isGiftEqual(mGiftSendInfo1, mGiftSendInfoPop)) {    // 替换第1个
				mGiftSendInfo1 = mGiftSendInfoPop;
				mGiftSendInfoPop = null;
				showGiftInfo(mGiftSendView1, mGiftSendInfo1, false, 1);
			} else if (isGiftEqual(mGiftSendInfo2, mGiftSendInfoPop)) {    // 替换第2个
				mGiftSendInfo2 = mGiftSendInfoPop;
				mGiftSendInfoPop = null;
				showGiftInfo(mGiftSendView2, mGiftSendInfo2, false, 2);
			}
		}
	}

	// 接收礼物
	public void recvGift(GiftSendInfo info) {
		push(info);
		popGift();
	}
}
