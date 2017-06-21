package com.example.live;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.classes.Share;
import com.example.nyapp.R;
import com.example.util.ShareUtil;
import com.jaronho.sdk.library.eventdispatcher.EventCenter;
import com.jaronho.sdk.third.circledialog.CircleDialog;
import com.jaronho.sdk.utils.UtilView;
import com.jaronho.sdk.utils.adapter.WrapRecyclerViewAdapter;
import com.jaronho.sdk.utils.view.VideoPlayer;
import com.jaronho.sdk.utils.view.ChatInputDialog;
import com.jaronho.sdk.utils.view.RefreshView;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.live.ConstantsLive.CHAT_MESSAGE_MAX_COUNT;

public class VideoActivity extends AppCompatActivity {
    private final int REQUEST_PHONE_PERMISSIONS = 0;
    private TextView mGuestCountTextView = null;
    private int mGuestCount = 0;
    private RefreshView mGuestView = null;
    private List<UserInfo> mGuestDatas = new ArrayList<>();
    private RefreshView mChatView = null;
    private List<ChatInfo> mChatDatas = new ArrayList<>();
    private ChannelInfo mChannelInfo = null;
    private TextView mScoreTextView = null;
    private int mPages = 0;
    private int mRecords = 0;
    private int mCurrentPage = 1;
    private VideoPlayer mVideoPlayer = null;
	private boolean mIsPrepared = false;
    private ImageView mPlayImageView = null;
    private TextView mTimeCurrentTextView = null;
    private SeekBar mSeekBar = null;
    private TextView mTimeTotalTextView = null;
	private Timer mTimer = null;
    private LiveHelper.Callback mCallback = new LiveHelper.Callback<ChatInfo>() {
        @Override
        public void onData(ChatInfo data) {
        }
        @Override
        public void onDataList(List<ChatInfo> dataList) {
            for (int i = 0, len = dataList.size(); i < len; ++i) {
                mChatDatas.add(dataList.get(i));
            }
            mChatView.getView().getAdapter().notifyDataSetChanged();
            mPages = LiveHelper.getTempPages();
            mRecords = LiveHelper.getTempRecords();
            Log.d("NYLive", "get list, pages: " + mPages + ", records: " + mRecords);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // 不锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.live_video_room_activity);
        checkPermission();
        mChannelInfo = getIntent().getExtras().getParcelable("channel_info");
        // 头像
        ImageView hostPic = (ImageView)findViewById(R.id.imageview_head_icon);
        if (null != mChannelInfo.PublisherAvatar && !mChannelInfo.PublisherAvatar.isEmpty()) {
            Picasso.with(VideoActivity.this).load(mChannelInfo.PublisherAvatar).into(hostPic);
        }
        TextView textviewSign = (TextView)findViewById(R.id.textview_desc);
        textviewSign.setText(mChannelInfo.Title);
        // 关闭图片
        ImageView imageviewClose = (ImageView)findViewById(R.id.imageview_close);
        imageviewClose.setOnClickListener(onClickImageviewClose);
        // 聊天图片
        ImageView imageviewMessage = (ImageView)findViewById(R.id.imageview_message);
        imageviewMessage.setOnClickListener(onClickImageviewMessage);
        // 分享图片
        ImageView imageviewShare = (ImageView)findViewById(R.id.imageview_share);
        imageviewShare.setOnClickListener(onClickImageviewShare);
        // 礼物图片
        ImageView imageviewGift = (ImageView)findViewById(R.id.imageview_gift);
        imageviewGift.setVisibility(View.GONE);
        // 收藏图片
        ImageView imageviewCollect= (ImageView)findViewById(R.id.imageview_collect);
        imageviewCollect.setOnClickListener(onClickImageviewCollect);
        // 房间详情图片
        ImageView imageviewRoomDetails = (ImageView)findViewById(R.id.imageview_room_details);
        imageviewRoomDetails.setOnClickListener(onClickImageviewRoomDetails);
        // 积分
        mScoreTextView = (TextView)findViewById(R.id.textview_score);
        mScoreTextView.setText(String.valueOf(mChannelInfo.Integral));
        // 播放按钮
        mPlayImageView = (ImageView)findViewById(R.id.imageview_play);
        // 当前时间
        mTimeCurrentTextView = (TextView)findViewById(R.id.textview_time_current);
        // 进度条
        mSeekBar = (SeekBar)findViewById(R.id.seekbar_video);
        // 总时间
        mTimeTotalTextView = (TextView)findViewById(R.id.textview_time_total);
        // 观众列表
        initGuestView();
        // 聊天列表
        initChatView();
        // 初始房间
        initRoom();
        // 视频
        initVideo();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
		if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }
        EventCenter.unsubscribe(this);
    }

    @Override
    public void onBackPressed() {
        showQuitDialog();
    }

    void checkPermission() {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CAMERA);
            if ((checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
            if ((checkSelfPermission(Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WAKE_LOCK);
            if ((checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
            if (permissionsList.size() != 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_PHONE_PERMISSIONS);
            }
        }
    }

    // 初始观众列表
    private void initGuestView() {
        mGuestCountTextView = (TextView)findViewById(R.id.textview_guest_count);
        mGuestView = (RefreshView)findViewById(R.id.refreshview_guests);
        mGuestView.setHorizontal(true);
        LinearLayoutManager llmGuest = new LinearLayoutManager(this);
        llmGuest.setOrientation(LinearLayoutManager.HORIZONTAL);
        mGuestView.getView().setLayoutManager(llmGuest);
        mGuestView.getView().setHasFixedSize(true);
        mGuestView.getView().setAdapter(new WrapRecyclerViewAdapter<UserInfo>(this, mGuestDatas, R.layout.live_item_guest) {
            @Override
            public void onBindViewHolder(QuickViewHolder quickViewHolder, UserInfo data) {
                if (null != data.ProfilePicture && !data.ProfilePicture.isEmpty()) {
                    Picasso.with(VideoActivity.this).load(data.ProfilePicture).into((ImageView)quickViewHolder.getView(R.id.imageview_guest));
                }
            }
        });
        mGuestView.getView().addItemDecoration(new SpaceItemDecoration(true, (int)getResources().getDimension(R.dimen.live_guest_item_space)));
        LiveHelper.reqGetChannelUsersList(mChannelInfo.Id, 1, ConstantsLive.GUEST_MAX_COUNT, new LiveHelper.Callback<UserInfo>() {
            @Override
            public void onData(UserInfo data) {
            }
            @Override
            public void onDataList(List<UserInfo> dataList) {
                mGuestCount = LiveHelper.getTempRecords();
                mGuestCountTextView.setText(mGuestCount > 0 ? mGuestCount + "人" : "无人观看");
                for (int i = 0, len = dataList.size(); i < len; ++i) {
                    mGuestDatas.add(dataList.get(i));
                }
                int count = mGuestDatas.size();
                if (count > ConstantsLive.GUEST_MAX_COUNT) {
                    for (int i = 0, len = count - ConstantsLive.GUEST_MAX_COUNT; i < len; ++i) {
                        mGuestDatas.remove(ConstantsLive.GUEST_MAX_COUNT);
                    }
                }
                mGuestView.getView().getAdapter().notifyDataSetChanged();
                UserInfo ui = new UserInfo();
                ui.UserId = LiveHelper.getUserId();
                ui.ProfilePicture = LiveHelper.getUserAvatar();
                insertGuest(ui);
            }
        });
    }

    // 初始聊天列表
    private void initChatView() {
        mChatView = (RefreshView)findViewById(R.id.refreshview_message);
        mChatView.setHorizontal(false);
        LinearLayoutManager llmMessage = new LinearLayoutManager(this);
        llmMessage.setOrientation(LinearLayoutManager.VERTICAL);
        mChatView.getView().setLayoutManager(llmMessage);
        mChatView.getView().setHasFixedSize(true);
        mChatView.getView().setAdapter(new WrapRecyclerViewAdapter<ChatInfo>(this, mChatDatas, R.layout.live_item_chat) {
            @Override
            public void onBindViewHolder(QuickViewHolder quickViewHolder, ChatInfo data) {
                if (null != data.getAvatar() && !data.getAvatar().isEmpty()) {
                    Picasso.with(VideoActivity.this).load(data.getAvatar()).into((ImageView)quickViewHolder.getView(R.id.imageview_avater));
                }
                String content = "<font color=\"#FFFF00\">" + (data.getName().isEmpty() ? data.getSenderId() : data.getName()) + ": </font>" + data.getContent();
                UtilView.setTextViewHtml((TextView)quickViewHolder.getView(R.id.textview_chat), content);
            }
        });
        mChatView.setHeaderWrapper(mLoadWrapper);
        mChatView.getView().addItemDecoration(new SpaceItemDecoration(false, (int)getResources().getDimension(R.dimen.live_chat_item_space)));
        LiveHelper.reqGetChatList(mChannelInfo.Id, 1, 10, mCallback);
    }

    // 初始房间
    private void initRoom() {
        LiveHelper.reqGetUserIntegral(mChannelInfo.PublisherID, false, new LiveHelper.Callback<String>() {
            @Override
            public void onData(String data) {
                mScoreTextView.setText(data);
            }
            @Override
            public void onDataList(List<String> dataList) {
            }
        });
    }

    // 视频
    private void initVideo() {
        if (mChannelInfo.Url.isEmpty()) {
            return;
        }
        ViewGroup videoLayout = (ViewGroup)findViewById(R.id.layout_video);
        mVideoPlayer = UtilView.createVideoPlayer(this);
        mVideoPlayer.setFitType(VideoPlayer.FitType.SHOW_ALL);
        videoLayout.addView(mVideoPlayer.getView(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mVideoPlayer.setPreparedHandler(new VideoPlayer.PreparedHandler() {
            @Override
            public boolean onCallback(VideoPlayer videoPlayer) {
				int length = mVideoPlayer.getLength();
				mTimeCurrentTextView.setText("00:00");
				mSeekBar.setMax(length);
				mSeekBar.setProgress(0);
                mTimeTotalTextView.setText(formatTime(length));
                mPlayImageView.setImageResource(R.mipmap.live_btn_pause);
				mIsPrepared = true;
                return true;
            }
        });
        mVideoPlayer.setCompleteHandler(new VideoPlayer.CompleteHandler() {
            @Override
            public void onCallback(VideoPlayer videoPlayer) {
				mVideoPlayer.seekTo(0);
				mPlayImageView.setImageResource(R.mipmap.live_btn_play);
				mTimeCurrentTextView.setText("00:00");
				mSeekBar.setProgress(0);
            }
        });
        mVideoPlayer.play(mChannelInfo.Url, false);
        mPlayImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
				if (mIsPrepared) {
					if (mVideoPlayer.isPlaying()) {
						mVideoPlayer.pause();
						mPlayImageView.setImageResource(R.mipmap.live_btn_play);
					} else {
						mVideoPlayer.resume();
						mPlayImageView.setImageResource(R.mipmap.live_btn_pause);
					}
				}
            }
        });
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private boolean mPlayFlag = false;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (mIsPrepared) {
					mTimeCurrentTextView.setText(formatTime(progress));
				}
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
				if (mIsPrepared) {
                    if (mVideoPlayer.isPlaying()) {
                        mPlayFlag = true;
                        mVideoPlayer.pause();
                    }
				}
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
				if (mIsPrepared) {
					int position = seekBar.getProgress();
					mTimeCurrentTextView.setText(formatTime(position));
					mVideoPlayer.seekTo(position);
                    if (mPlayFlag) {
                        mVideoPlayer.resume();
                    }
                    mPlayFlag = false;
				}
            }
        });
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mIsPrepared && mVideoPlayer.isPlaying()) {
                    int position = mVideoPlayer.getPosition();
                    mTimeCurrentTextView.setText(formatTime(position));
                    mSeekBar.setProgress(position);
                }
            }
        };
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
                runOnUiThread(runnable);
			}
		}, 0, 100);
    }

    // 格式化时间
    private String formatTime(int microsecond) {
        if (microsecond <= 0) {
            return "00:00";
        }
        int seconds = microsecond / 1000;
        int hour = seconds / 3600;
        seconds = seconds % 3600;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        if (hour > 0) {
            return String.format(Locale.CHINA, "%d:%02d:%02d", hour, minutes, seconds);
        }
        return String.format(Locale.CHINA, "%02d:%02d", minutes, seconds);
    }

    // 加载包装器
    private RefreshView.Wrapper mLoadWrapper = new RefreshView.Wrapper() {
        @Override
        public View createView(Context context, RelativeLayout parent) {
            return null;
        }
        @Override
        public void onPullReady() {
            if (mCurrentPage < mPages) {
                ++mCurrentPage;
                LiveHelper.reqGetChatList(mChannelInfo.Id, mCurrentPage, 10, mCallback);
            } else {
                mCurrentPage = mPages;
            }
        }
        @Override
        public void onPull(float maxOffset, float offset, boolean isForward, boolean isReady) {
        }
        @Override
        public void onPullAbort() {
        }
        @Override
        public void onRefreshing() {
            mChatView.restore();
        }
    };

    // 显示退出对话框
    private void showQuitDialog() {
        new CircleDialog.Builder(this)
                .setText("是否退出房间？")
                .setPositive("确定", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setNegative("取消", null)
                .setCancelable(true)
                .show();
    }

    // 添加观众
    private void insertGuest(UserInfo guest) {
        for (int i = 0, len = mGuestDatas.size(); i < len; ++i) {
            if (guest.UserId.equals(mGuestDatas.get(i).UserId)) {
                return;
            }
        }
        if (mGuestDatas.size() > ConstantsLive.GUEST_MAX_COUNT) {
            mGuestDatas.remove(0);
        }
        mGuestDatas.add(0, guest);
        mGuestView.getView().getAdapter().notifyDataSetChanged();
        ++mGuestCount;
        mGuestCountTextView.setText(mGuestCount > 0 ? mGuestCount + "人" : "无人观看");
    }

    // 删除观众
    private void removeGuest(UserInfo guest) {
        boolean flag = false;
        for (int i = 0, len = mGuestDatas.size(); i < len; ++i) {
            if (guest.UserId.equals(mGuestDatas.get(i).UserId)) {
                mGuestDatas.remove(i);
                flag = true;
                break;
            }
        }
        if (!flag) {
            return;
        }
        mGuestView.getView().getAdapter().notifyDataSetChanged();
        --mGuestCount;
        if (mGuestCount < 0) {
            mGuestCount = 0;
        }
        mGuestCountTextView.setText(mGuestCount > 0 ? mGuestCount + "人" : "无人观看");
    }

    // 点击关闭
    OnClickListener onClickImageviewClose = new OnClickListener() {
        @Override
        public void onClick(View v) {
            showQuitDialog();
        }
    };

    // 点击聊天
    OnClickListener onClickImageviewMessage = new OnClickListener() {
        @Override
        public void onClick(View v) {
            LiveHelper.reqGetAuthority(LiveHelper.getUserId(), mChannelInfo.Id, false, new LiveHelper.Callback<AuthorityInfo>() {
                @Override
                public void onData(AuthorityInfo data) {
                    if (data.IsWords) { // 被禁言
                        LiveHelper.toast("您已被禁言，无法发送消息");
                        return;
                    }
                    final ChatInputDialog dlg = new ChatInputDialog(VideoActivity.this, R.style.live_dialog_message_input, R.layout.live_dialog_chat_input);
                    dlg.show();
                    final EditText editTextInput = (EditText)dlg.findViewById(R.id.edittext_input);
                    editTextInput.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (KeyEvent.ACTION_UP != event.getAction()) {   // 忽略其它事件
                                return false;
                            }
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_ENTER:
                                    Editable text = editTextInput.getText();
                                    if (text.length() > 0) {
                                        dlg.dismiss();
                                    }
                                    sendChatMessage(text.toString());
                                    return true;
                            }
                            return false;
                        }
                    });
                    TextView textViewSend = (TextView)dlg.findViewById(R.id.textview_send);
                    textViewSend.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Editable text = editTextInput.getText();
                            if (text.length() > 0) {
                                dlg.dismiss();
                            }
                            sendChatMessage(text.toString());
                        }
                    });
                }
                @Override
                public void onDataList(List<AuthorityInfo> dataList) {
                }
            });
        }
    };

    // 点击分享
    OnClickListener onClickImageviewShare = new OnClickListener() {
        @Override
        public void onClick(View v) {
            LiveHelper.reqShareChannel(mChannelInfo.Id, new LiveHelper.Callback<Share.DataBean>() {
                @Override
                public void onData(Share.DataBean data) {
                    ShareUtil.initShareDate(VideoActivity.this, data, new ShareUtil.ShareListener() {
                        @Override
                        public void onSuccess() {
                            LiveHelper.reqGetUserIntegral(LiveHelper.getUserId(), false, new LiveHelper.Callback<String>() {
                                @Override
                                public void onData(String data) {
                                    LiveHelper.reqInteractiveAdd(mChannelInfo.Id, LiveHelper.getUserId(), 2);
                                }
                                @Override
                                public void onDataList(List<String> dataList) {
                                }
                            });
                        }
                        @Override
                        public void onError(String err) {
                            Log.d("NYLive", "share error: " + err);
                        }
                        @Override
                        public void onCancel() {
                        }
                    });
                }
                @Override
                public void onDataList(List<Share.DataBean> dataList) {
                }
            });
        }
    };

    // 点击收藏
    OnClickListener onClickImageviewCollect = new OnClickListener() {
        @Override
        public void onClick(View v) {
            LiveHelper.reqInteractiveAdd(mChannelInfo.Id, LiveHelper.getUserId(), 1);
        }
    };

    // 点击房间详情
    OnClickListener onClickImageviewRoomDetails = new OnClickListener() {
        @Override
        public void onClick(View v) {
            new CircleDialog.Builder(VideoActivity.this)
                    .setTitle("房间详情")
                    .setText(mChannelInfo.Details)
                    .setCancelable(true)
                    .show();
        }
    };

    // 消息输入监听器
    private void sendChatMessage(String msg) {
        if (0 == msg.length()) {
            LiveHelper.toast("输入不能为空");
            return;
        }
        try {
            byte[] byteNum = msg.getBytes("utf8");
            if (byteNum.length > ConstantsLive.CHAT_MESSAGE_MAX_LENGTH) {
                LiveHelper.toast("消息输入太长");
                return;
            }
            LiveHelper.reqChatAdd(LiveHelper.getUserId(), mChannelInfo.Id, msg, new LiveHelper.Callback<ChatInfo>() {
                @Override
                public void onData(ChatInfo data) {
                    refreshMessageView(data);
                }
                @Override
                public void onDataList(List<ChatInfo> dataList) {
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
    }

    // 刷新消息框
    private void refreshMessageView(ChatInfo cm) {
        if (mChatDatas.size() > CHAT_MESSAGE_MAX_COUNT) {
            mChatDatas.remove(0);
        }
        mChatDatas.add(cm);
        mChatView.getView().getAdapter().notifyDataSetChanged();
        mChatView.getView().scrollToPosition(mChatDatas.size() - 1);
    }
}
