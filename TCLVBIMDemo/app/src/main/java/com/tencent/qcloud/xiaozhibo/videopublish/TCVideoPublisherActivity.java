package com.tencent.qcloud.xiaozhibo.videopublish;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.qcloud.xiaozhibo.R;
import com.tencent.qcloud.xiaozhibo.common.activity.TCBaseActivity;
import com.tencent.qcloud.xiaozhibo.common.utils.TCConstants;
import com.tencent.qcloud.xiaozhibo.common.utils.TCHttpEngine;
import com.tencent.qcloud.xiaozhibo.common.utils.TCUtils;
import com.tencent.qcloud.xiaozhibo.userinfo.TCUserInfoMgr;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ugc.TXRecordCommon;
import com.tencent.rtmp.ugc.TXUGCPublish;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * Created by carolsuo on 2017/3/9.
 * UGC发布页面
 */
public class TCVideoPublisherActivity extends TCBaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, TXRecordCommon.ITXVideoPublishListener, ITXLivePlayListener {
    //分享相关
    private SHARE_MEDIA mShare_meidia = SHARE_MEDIA.MORE;
    private CompoundButton mCbLastChecked = null;

    private String mVideoPath = null;
    private String mCoverPath = null;

    private TextView mBtnBack;
    private TextView mBtnPublish;

    private LinearLayout mLayoutEdit;
    private RelativeLayout mLayoutPublish;

    private TXUGCPublish mVideoPublish= null;
    boolean mIsPlayRecordType = false;

    private ImageView  mIVPublishing;
    private TextView mTVPublish;
    private TextView mTVTitle;
    private EditText mTitleEditText;
    private boolean mIsFetchCosSig = false;
    String mCosSignature = null;
    Handler mHandler = new Handler();
    private String mShareUrl = TCConstants.SVR_LivePlayShare_URL;
    private boolean mAllDone = false;

    private TXLivePlayer mTXLivePlayer = null;
    private TXLivePlayConfig mTXPlayConfig = null;
    private TXCloudVideoView mTXCloudVideoView;
    private NetchangeReceiver mNetchangeReceiver = null;
    private int mRotation;
    private boolean mDisableCache;
    private String mLocalVideoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_publisher);

        mVideoPath = getIntent().getStringExtra(TCConstants.VIDEO_RECORD_VIDEPATH);
        mCoverPath = getIntent().getStringExtra(TCConstants.VIDEO_RECORD_COVERPATH);
        mRotation  = getIntent().getIntExtra(TCConstants.VIDEO_RECORD_ROTATION,TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mDisableCache = getIntent().getBooleanExtra(TCConstants.VIDEO_RECORD_NO_CACHE, false);
        mLocalVideoPath = getIntent().getStringExtra(TCConstants.VIDEO_RECORD_VIDEPATH);

        mIsPlayRecordType = getIntent().getIntExtra(TCConstants.VIDEO_RECORD_TYPE, 0) == TCConstants.VIDEO_RECORD_TYPE_PLAY;

        CheckBox cbShareWX = (CheckBox) findViewById(R.id.vpcb_share_wx);
        CheckBox cbShareCircle = (CheckBox) findViewById(R.id.vpcb_share_circle);
        CheckBox cbShareQQ = (CheckBox) findViewById(R.id.vpcb_share_qq);
        CheckBox cbShareQzone = (CheckBox) findViewById(R.id.vpcb_share_qzone);
        CheckBox cbShareWb = (CheckBox) findViewById(R.id.vpcb_share_wb);

        cbShareWX.setOnCheckedChangeListener(this);
        cbShareCircle.setOnCheckedChangeListener(this);
        cbShareQQ.setOnCheckedChangeListener(this);
        cbShareQzone.setOnCheckedChangeListener(this);
        cbShareWb.setOnCheckedChangeListener(this);

        mBtnBack = (TextView) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);

        mBtnPublish = (TextView) findViewById(R.id.btn_publish);
        mBtnPublish.setOnClickListener(this);

        mLayoutEdit = (LinearLayout) findViewById(R.id.layout_edit);
        mLayoutPublish = (RelativeLayout) findViewById(R.id.layout_publish);

        mIVPublishing = (ImageView) findViewById(R.id.publishing);
        mTVPublish = (TextView) findViewById(R.id.publish_text);

        mTVTitle = (TextView) findViewById(R.id.publish_title);

        mTitleEditText = (EditText) findViewById(R.id.edit_text);

        mTXLivePlayer = new TXLivePlayer(this);
        mTXPlayConfig = new TXLivePlayConfig();
        mTXCloudVideoView = (TXCloudVideoView) findViewById(R.id.video_view);
        mTXCloudVideoView.disableLog(true);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                startPlay();
            }
        }, 500);
//        startPlay();
    }

    private void startPlay() {
        mTXLivePlayer.setPlayerView(mTXCloudVideoView);
        mTXLivePlayer.setPlayListener(this);

        mTXLivePlayer.enableHardwareDecode(false);
        mTXLivePlayer.setRenderRotation(mRotation);
        mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);

        mTXLivePlayer.setConfig(mTXPlayConfig);

        mTXLivePlayer.startPlay(mVideoPath, TXLivePlayer.PLAY_TYPE_LOCAL_VIDEO); // result返回值：0 success;  -1 empty url; -2 invalid url; -3 invalid playType;
    }

    private void fetchSignature() {
        if (mIsFetchCosSig)
            return;
        mIsFetchCosSig = true;
        JSONObject req = new JSONObject();
        try {
//            req.put("Action", "GetVodSignature");
            req.put("Action", "GetVodSignatureV2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        TCHttpEngine.getInstance().post(req, new TCHttpEngine.Listener() {
            @Override
            public void onResponse(int retCode, String retMsg, JSONObject retData) {
                if (retCode == 0 && retData != null) {
                    try {
                        mCosSignature = retData.getString("signature");
                        startPublish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked == false) {
            mShare_meidia = SHARE_MEDIA.MORE;
            mCbLastChecked = null;
            return;
        }
        if (mCbLastChecked != null) {
            mCbLastChecked.setChecked(false);
        }
        mCbLastChecked = buttonView;
        switch (buttonView.getId()) {
            case R.id.vpcb_share_wx:
                mShare_meidia = SHARE_MEDIA.WEIXIN;
                break;
            case R.id.vpcb_share_circle:
                mShare_meidia = SHARE_MEDIA.WEIXIN_CIRCLE;
                break;
            case R.id.vpcb_share_qq:
                mShare_meidia = SHARE_MEDIA.QQ;
                break;
            case R.id.vpcb_share_qzone:
                mShare_meidia = SHARE_MEDIA.QZONE;
                break;
            case R.id.vpcb_share_wb:
                mShare_meidia = SHARE_MEDIA.SINA;
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_publish:
                if (mAllDone) {
                    finish();
                } else {
                    if (!TCUtils.isNetworkAvailable(this)) {
                        Toast.makeText(getApplicationContext(), "当前无网络连接", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    fetchSignature();
                    mLayoutEdit.setVisibility(View.GONE);
                    mLayoutPublish.setVisibility(View.VISIBLE);
                    mBtnPublish.setVisibility(View.GONE);
                    mTVTitle.setText("发布");
                }
                break;
            default:
                break;
        }
    }

    void startPublish() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mVideoPublish == null)
                    mVideoPublish = new TXUGCPublish(TCVideoPublisherActivity.this.getApplicationContext(), TCUserInfoMgr.getInstance().getUserId());
                mVideoPublish.setListener(TCVideoPublisherActivity.this);

                TXRecordCommon.TXPublishParam param = new TXRecordCommon.TXPublishParam();
                param.signature = mCosSignature;
                param.videoPath = mVideoPath;
                param.coverPath = mCoverPath;
                int publishCode = mVideoPublish.publishVideo(param);
                if (publishCode != 0) {
                    mIVPublishing.setVisibility(View.INVISIBLE);
                    mTVPublish.setText("发布失败，错误码：" + publishCode);
                }

                stopPlay(true);
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                if (null == mNetchangeReceiver) {
                    mNetchangeReceiver = new NetchangeReceiver();
                }
                TCVideoPublisherActivity.this.getApplicationContext().registerReceiver(mNetchangeReceiver, intentFilter);
            }
        });
    }

    void startShare(String videoId, String videoURL, String coverURL) {
        // 友盟的分享组件并不完善，为了各种异常情况下正常推流，要多做一些事情
        if (mShare_meidia == SHARE_MEDIA.MORE) {
            allDone();
            return;
        }

        boolean isSupportShare = false;
        if (mShare_meidia == SHARE_MEDIA.SINA) {
            isSupportShare = true;
        } else if (mShare_meidia == SHARE_MEDIA.QZONE) {
            if (UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.QQ) || UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.QZONE)) {
                isSupportShare = true;
            }
        } else if (UMShareAPI.get(this).isInstall(this, mShare_meidia)) {
            isSupportShare = true;
        }

        if(!isSupportShare) {
            allDone();
            return;
        }

        try {
            mShareUrl = mShareUrl + "?sdkappid=" + java.net.URLEncoder.encode(String.valueOf(TCConstants.IMSDK_APPID), "utf-8")
                    + "&acctype=" + java.net.URLEncoder.encode(String.valueOf(TCConstants.IMSDK_ACCOUNT_TYPE), "utf-8")
                    + "&userid=" +java.net.URLEncoder.encode(TCUserInfoMgr.getInstance().getUserId(), "utf-8")
                    + "&type=" + java.net.URLEncoder.encode(String.valueOf(2), "utf-8")
                    + "&fileid=" + java.net.URLEncoder.encode(String.valueOf(videoId), "utf-8")
                    + "&ts=" + java.net.URLEncoder.encode(String.valueOf(0), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ShareAction shareAction = new ShareAction(TCVideoPublisherActivity.this);

        String title = mTitleEditText.getText().toString();
        if (TextUtils.isEmpty(title)) {
            title = "小视频";
        }
        UMWeb web = new UMWeb(mShareUrl);
        web.setThumb(new UMImage(TCVideoPublisherActivity.this.getApplicationContext(), coverURL));
        web.setTitle(title);
        shareAction.withText(TCUserInfoMgr.getInstance().getNickname() + "的小视频");
        shareAction.withMedia(web);
        shareAction.setCallback(umShareListener);
        shareAction.setPlatform(mShare_meidia).share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            Toast.makeText(TCVideoPublisherActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            TCVideoPublisherActivity.this.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    allDone();
                }
            });
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(TCVideoPublisherActivity.this,"分享失败"+t.getMessage(),Toast.LENGTH_LONG).show();
            TCVideoPublisherActivity.this.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    allDone();
                }
            });
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(TCVideoPublisherActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            TCVideoPublisherActivity.this.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    allDone();
                }
            });
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPublishProgress(long uploadBytes, long totalBytes) {
        int index = (int) (uploadBytes*8/totalBytes);
        switch (index) {
            case 1:
                mIVPublishing.setImageResource(R.drawable.publish_1);
                break;
            case 2:
                mIVPublishing.setImageResource(R.drawable.publish_2);
                break;
            case 3:
                mIVPublishing.setImageResource(R.drawable.publish_3);
                break;
            case 4:
                mIVPublishing.setImageResource(R.drawable.publish_4);
                break;
            case 5:
                mIVPublishing.setImageResource(R.drawable.publish_5);
                break;
            case 6:
                mIVPublishing.setImageResource(R.drawable.publish_6);
                break;
            case 7:
                mIVPublishing.setImageResource(R.drawable.publish_7);
                break;
            case 8:
                mIVPublishing.setImageResource(R.drawable.publish_8);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPublishComplete(TXRecordCommon.TXPublishResult txPublishResult) {
        if (txPublishResult.retCode == TXRecordCommon.PUBLISH_RESULT_OK) {
            mIVPublishing.setImageResource(R.drawable.publish_success);
            mTVPublish.setText("发布成功啦！");
            UploadUGCVideo(txPublishResult.videoId, txPublishResult.videoURL, txPublishResult.coverURL);
        } else {
            mIVPublishing.setVisibility(View.INVISIBLE);
            mTVPublish.setText(txPublishResult.descMsg);
        }
    }

    private void deleteCache() {
        if (mDisableCache) {
            File file = new File(mVideoPath);
            if (file.exists()) {
                file.delete();
            }
            if (!TextUtils.isEmpty(mCoverPath)){
                file = new File(mCoverPath);
                if (file.exists()) {
                    file.delete();
                }
            }
            if (mLocalVideoPath != null) {
                Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                scanIntent.setData(Uri.fromFile(new File(mLocalVideoPath)));
                sendBroadcast(scanIntent);
            }
        }
    }

    private void UploadUGCVideo(final String videoId, final String videoURL, final String coverURL) {
        String title = mTitleEditText.getText().toString();
        if (TextUtils.isEmpty(title)) {
            title = "小视频";
        }
        try {
            JSONObject userInfo = new JSONObject();
            userInfo.put("nickname", TCUserInfoMgr.getInstance().getNickname());
            userInfo.put("headpic", TCUserInfoMgr.getInstance().getHeadPic());
            userInfo.put("frontcover", coverURL);
            userInfo.put("location", TCUserInfoMgr.getInstance().getLocation());

            JSONObject req = new JSONObject();
            req.put("Action", "UploadUGCVideo");
            req.put("userid", TCUserInfoMgr.getInstance().getUserId());
            req.put("file_id", videoId);
            req.put("title", title);
            req.put("play_url", videoURL);
            req.put("userinfo", userInfo);

            TCHttpEngine.getInstance().post(req, new TCHttpEngine.Listener() {
                @Override
                public void onResponse(int retCode, String retMsg, JSONObject retData) {
                    try {
                        if (retCode == 0) {
                            startShare(videoId, videoURL, coverURL);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 事情都做完了
    void allDone()
    {
        mBtnBack.setVisibility(View.INVISIBLE);
        mBtnPublish.setText("完成");
        mBtnPublish.setVisibility(View.VISIBLE);
        mAllDone = true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mTXCloudVideoView.onResume();
        mTXLivePlayer.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTXCloudVideoView.onPause();
        mTXLivePlayer.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTXCloudVideoView.onDestroy();
        stopPlay(true);
        if (mNetchangeReceiver != null) {
            this.getApplicationContext().unregisterReceiver(mNetchangeReceiver);
        }

        deleteCache();
    }

    protected void stopPlay(boolean clearLastFrame) {
        if (mTXLivePlayer != null) {
            mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer.stopPlay(clearLastFrame);
        }
    }


    @Override
    public void onPlayEvent(int event, Bundle param) {
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.setLogText(null,param,event);
        }
        if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {

            showErrorAndQuit(TCConstants.ERROR_MSG_NET_DISCONNECTED);

        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
            stopPlay(false);
            startPlay();
        }
//        else if (event == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {
//            int width = param.getInt(TXLiveConstants.EVT_PARAM1, 0);
//            int height = param.getInt(TXLiveConstants.EVT_PARAM2, 0);
//            int showWidth = width;
//            int showHeight = mTXCloudVideoView.getHeight();
//            if (0 != width && 0 != height) {
//                showWidth = width * showHeight / height;
//            }
//            if (0 != showWidth && 0 != showHeight) {
//                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mTXCloudVideoView.getLayoutParams();
//                if (null == layoutParams) {
//                    layoutParams = new LinearLayout.LayoutParams(showWidth, showHeight);
//                } else {
//                    layoutParams.width = showWidth;
//                    layoutParams.height = showHeight;
//                }
//                mTXCloudVideoView.setLayoutParams(layoutParams);
//            }
//
//        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {
//        if(bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) > bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT)) {
//            if(mTXLivePlayer != null) mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
//        }
//        else if(mTXLivePlayer != null) mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
    }

    public class NetchangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                if (!TCUtils.isNetworkAvailable(TCVideoPublisherActivity.this)) {
                    mIVPublishing.setVisibility(View.INVISIBLE);
                    mTVPublish.setText("网络连接断开，视频上传失败");
                }
            }
        }
    }
}
