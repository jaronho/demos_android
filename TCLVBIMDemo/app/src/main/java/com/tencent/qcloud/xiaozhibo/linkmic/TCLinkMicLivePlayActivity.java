package com.tencent.qcloud.xiaozhibo.linkmic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.qcloud.xiaozhibo.R;
import com.tencent.qcloud.xiaozhibo.common.utils.TCConstants;
import com.tencent.qcloud.xiaozhibo.common.utils.TCUtils;
import com.tencent.qcloud.xiaozhibo.login.TCLoginMgr;
import com.tencent.qcloud.xiaozhibo.play.TCPlayerMgr;
import com.tencent.qcloud.xiaozhibo.push.TCPusherMgr;
import com.tencent.qcloud.xiaozhibo.userinfo.TCUserInfoMgr;
import com.tencent.qcloud.xiaozhibo.play.TCLivePlayerActivity;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import tencent.tls.platform.TLSUserInfo;

/**
 * Created by dennyfeng on 16/11/16.
 */
public class TCLinkMicLivePlayActivity extends TCLivePlayerActivity implements TCLinkMicMgr.TCLinkMicListener, TCLivePushListenerImpl.ITCLivePushListener, TCLivePlayListenerImpl.ITCLivePlayListener {

    private static final String TAG = TCLinkMicLivePlayActivity.class.getName();

    private boolean             mWaitingLinkMicResponse = false;
    private boolean             mIsBeingLinkMic         = false;

    private String              mNotifyPlayUrl;

    private Button              mBtnLinkMic;
    private Button              mBtnSwitchCamera;
    private ImageView           mImgViewRecordVideo;

    private TXLivePusher        mTXLivePusher;
    private TXLivePushConfig    mTXLivePushConfig;
    private TCLivePushListenerImpl mTCLivePushListener;

    private TCLinkMicMgr        mTCLinkMicMgr;

    private Vector<TCLinkMicPlayItem> mVecPlayItems = new Vector<>();

    private String              mResponseParams;

    @Override
    protected void initLiveView() {
        super.initLiveView();

        if (mIsLivePlay == false || TCLinkMicMgr.supportLinkMic() == false) {
            return;
        }

        mBtnLinkMic = (Button) findViewById(R.id.btn_linkmic);
        mBtnLinkMic.setVisibility(View.VISIBLE);
        mBtnLinkMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBeingLinkMic == false) {
                    startLinkMic();
                } else {
                    stopLinkMic();
                    startPlay();
                }
            }
        });

        mBtnSwitchCamera = (Button) findViewById(R.id.btn_switch_cam);
        mBtnSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBeingLinkMic) {
                    mTXLivePusher.switchCamera();
                }
            }
        });

        mImgViewRecordVideo = (ImageView) findViewById(R.id.btn_record);
        
        // 连麦推流
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause_publish,options);

        mTXLivePushConfig = new TXLivePushConfig();
        mTXLivePushConfig.setPauseImg(bitmap);
        mTXLivePushConfig.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO | TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);
        mTXLivePushConfig.setAudioSampleRate(48000);
        mTXLivePushConfig.setBeautyFilter(TCUtils.filtNumber(9, 100, 50), TCUtils.filtNumber(3, 100, 0));

        mTCLivePushListener = new TCLivePushListenerImpl();
        mTCLivePushListener.setListener(this);

        mTXLivePusher = new TXLivePusher(this);
        mTXLivePusher.setPushListener(mTCLivePushListener);
        mTXLivePusher.setConfig(mTXLivePushConfig);
        mTXLivePusher.setMicVolume(2.0f);
        
        // 连麦拉流
        mVecPlayItems.add(new TCLinkMicPlayItem(this, this, mShowLog, 1));
        mVecPlayItems.add(new TCLinkMicPlayItem(this, this, mShowLog, 2));
        mVecPlayItems.add(new TCLinkMicPlayItem(this, this, mShowLog, 3));

        // 连麦消息
        mTCLinkMicMgr = TCLinkMicMgr.getInstance();
        mTCLinkMicMgr.setLinkMicListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        for (TCLinkMicPlayItem item: mVecPlayItems) {
            item.pause();
        }
    }

    @Override
    protected void onResume() {
        for (TCLinkMicPlayItem item: mVecPlayItems) {
            item.resume();
            if (!TextUtils.isEmpty(item.mPlayUrl) && !TextUtils.isEmpty(item.mUserID)) {
                item.startLoading();
                item.startPlay(item.mPlayUrl);
            }
        }

        if (mIsBeingLinkMic) {
            mTXLivePusher.resumePusher();
            mPausing = false;
        }

        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mIsBeingLinkMic) {
            mTXLivePusher.pausePusher();

            for (TCLinkMicPlayItem item: mVecPlayItems) {
                item.stopPlay(true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (TCLinkMicPlayItem item: mVecPlayItems) {
            item.destroy();
        }

        if (mIsLivePlay) {
            stopLinkMic();
        }

        if (mTCLinkMicMgr != null) {
            mTCLinkMicMgr.setLinkMicListener(null);
            mTCLinkMicMgr = null;
        }

        if (mTXLivePusher != null) {
            mTXLivePusher.setPushListener(null);
        }

        if (mTCLivePushListener != null) {
            mTCLivePushListener.setListener(null);
        }

        hideNoticeToast();
    }

    @Override
    public void onGroupDelete() {
        stopLinkMic();
        super.onGroupDelete();
    }

    private Runnable mRunnableLinkMicTimeOut = new Runnable() {
        @Override
        public void run() {
            if (mWaitingLinkMicResponse) {
                mWaitingLinkMicResponse = false;
                mBtnLinkMic.setEnabled(true);
                mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_on);
                hideNoticeToast();
                Toast.makeText(getApplicationContext(), "连麦请求超时，主播没有做出回应", Toast.LENGTH_SHORT).show();

                //连麦超时，允许录制小视频
                mImgViewRecordVideo.setEnabled(true);
            }
        }
    };

    private void startLinkMic() {
        if (mIsBeingLinkMic || mWaitingLinkMicResponse) {
            return;
        }

        mTCLinkMicMgr.sendLinkMicRequest(mPusherId);

        mWaitingLinkMicResponse = true;

        mBtnLinkMic.setEnabled(false);
        mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_off);

        mHandler.removeCallbacks(mRunnableLinkMicTimeOut);
        mHandler.postDelayed(mRunnableLinkMicTimeOut, 10000);   //10秒超时

        showNoticeToast("等待主播接受......");

        //开始连麦，不允许录制小视频
        mImgViewRecordVideo.setEnabled(false);
    }

    private synchronized void stopLinkMic() {
        if (mIsBeingLinkMic) {
            mIsBeingLinkMic = false;
            TLSUserInfo userInfo = TCLoginMgr.getInstance().getLastUserInfo();
            if (userInfo != null) {
                mTCLinkMicMgr.sendMemberExitNotify(mPusherId, userInfo.identifier);
            }
        }

        if (mWaitingLinkMicResponse) {
            mWaitingLinkMicResponse = false;
            mHandler.removeCallbacks(mRunnableLinkMicTimeOut);
        }

        //启用连麦Button
        if (mBtnLinkMic != null) {
            mBtnLinkMic.setEnabled(true);
            mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_on);
        }

        //隐藏切换摄像头Button
        if (mBtnSwitchCamera != null) {
            mBtnSwitchCamera.setVisibility(View.INVISIBLE);
        }

        //结束推流
        if (mTXLivePusher != null) {
            mTXLivePusher.stopCameraPreview(true);
            mTXLivePusher.stopPusher();
        }

        //结束拉流
        for (TCLinkMicPlayItem item: mVecPlayItems) {
            item.stopPlay(true);
            item.empty();
        }

        //结束连麦，允许录制小视频
        mImgViewRecordVideo.setEnabled(true);
    }

    private void handleLinkMicFailed(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

        //结束连麦
        stopLinkMic();

        //重新从CDN拉流播放
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!mPausing && !mIsBeingLinkMic) { //退到后台不用启动拉流
                    startPlay();
                }
            }
        });
    }

    @Override
    public void onReceiveLinkMicRequest(final String strUserId, final String strNickName) {
    }

    @Override
    public void onReceiveLinkMicResponse(final String strUserId, final int responseType, final String strParams) {
        if (mWaitingLinkMicResponse == false) {
            return;
        }

        mWaitingLinkMicResponse = false;
        mBtnLinkMic.setEnabled(true);
        mResponseParams = strParams;

        hideNoticeToast();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (TCConstants.LINKMIC_RESPONSE_TYPE_ACCEPT == responseType) {
                    mIsBeingLinkMic = true;
                    mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_off);
                    Toast.makeText(getApplicationContext(), "主播接受了您的连麦请求，开始连麦", Toast.LENGTH_SHORT).show();

                    TCPusherMgr.getInstance().getPusherUrlForLinkMic(TCUserInfoMgr.getInstance().getUserId(),
                            "连麦",
                            TCUserInfoMgr.getInstance().getCoverPic(),
                            TCUserInfoMgr.getInstance().getNickname(),
                            TCUserInfoMgr.getInstance().getHeadPic(),
                            TCUserInfoMgr.getInstance().getLocation(),
                            new TCPusherMgr.OnGetPusherUrlForLinkMic() {
                                @Override
                                public void onGetPusherUrlForLinkMic(int errCode, String pusherUrl, String playUrl, String timeStamp) {
                                    if (errCode == 0) {
                                        mNotifyPlayUrl = playUrl;

                                        //兼容大主播是旧版本，小主播是新版本的情况：“新版本小主播推流地址后面加上混流参数mix=layer:s;session_id:xxx;t_id:1”
                                        //1、如果连麦的大主播是旧版本，保证和旧版本大主播连麦时，能够互相拉取到对方的低时延流，也能够保证混流成功
                                        //2、如果连麦的大主播是新版本，那么大主播推流地址后面带的是&mix=session_id:xxx，这种情况下可以互相拉取低时延流，也可以混流成功（不会触发自动混流，由大主播通过CGI调用的方式启动混流）
                                        String sessionID = getStrFieldFromResponseParams("sessionID", strParams);
                                        if (sessionID != null && sessionID.length() > 0) {
                                            pusherUrl = pusherUrl + "&mix=layer:s;t_id:1;session_id:" + sessionID;
                                        }

                                        //结束从CDN拉流
                                        stopPlay(true);

                                        //启动推流
                                        if (mTXLivePusher != null) {
                                            mTXCloudVideoView.setVisibility(View.VISIBLE);
                                            mTXCloudVideoView.clearLog();
                                            mTCLivePushListener.setPushUrl(pusherUrl);
                                            mTXLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_LINKMIC_SUB_PUBLISHER);
                                            mTXLivePusher.startCameraPreview(mTXCloudVideoView);
                                            mTXLivePusher.startPusher(pusherUrl);
                                        }

                                        //查找空闲的TCLinkMicPlayItem, 开始loading
                                        for (TCLinkMicPlayItem item : mVecPlayItems) {
                                            if (item.mUserID == null || item.mUserID.length() == 0) {
                                                item.mPending = true;
                                                item.mUserID = mUserId;
                                                item.startLoading();
                                                break;
                                            }
                                        }

                                        // 推流允许前后切换摄像头
                                        mBtnSwitchCamera.setVisibility(View.VISIBLE);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "拉取连麦推流地址失败，error=" + errCode, Toast.LENGTH_SHORT).show();
                                        mIsBeingLinkMic = false;
                                        mWaitingLinkMicResponse = false;
                                        mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_on);
                                    }
                                }
                            }
                    );
                } else if (TCConstants.LINKMIC_RESPONSE_TYPE_REJECT == responseType) {
                    String reason = getStrFieldFromResponseParams("reason", strParams);
                    if (reason != null && reason.length() > 0) {
                        Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();
                    }
                    mIsBeingLinkMic = false;
                    mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_on);

                    //主播拒绝连麦，允许录制小视频
                    mImgViewRecordVideo.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onReceiveKickedOutNotify() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "不好意思，您被主播踢开", Toast.LENGTH_SHORT).show();
                //结束连麦
                stopLinkMic();
                //重新从CDN拉流播放
                startPlay();
            }
        });
    }

    @Override
    public void onReceiveMemberJoinNotify(String strUserId, String strPlayUrl) {
        if (mIsBeingLinkMic == false && mWaitingLinkMicResponse == false) {
            return;
        }

        if (strUserId != null && strUserId.equalsIgnoreCase(mUserId)) {
            return;
        }

        startPlayVideoStream(strUserId, strPlayUrl);
    }

    @Override
    public void onReceiveMemberExitNotify(String strUserId) {
        if (strUserId != null && strUserId.equalsIgnoreCase(mUserId)) {
            return;
        }

        stopPlayVideoStream(strUserId);
    }

    @Override
    public void onLivePushEvent(String pushUrl, int event, Bundle bundle) {
        if (event == TXLiveConstants.PUSH_EVT_PUSH_BEGIN && mIsBeingLinkMic) {                     //开始推流事件通知
            // 1.向主播发送加入连麦的通知
            mTCLinkMicMgr.sendMemberJoinNotify(mPusherId, TCLoginMgr.getInstance().getLastUserInfo().identifier, mNotifyPlayUrl);

            // 2.拉取主播的低时延流
            mTCPlayerMgr.getPlayUrlWithSignature(mUserId, mPlayUrl, new TCPlayerMgr.OnGetPlayUrlWithSignature() {
                @Override
                public void onGetPlayUrlWithSignature(int errCode, String strPlayUrl) {
                    if (errCode == 0 && strPlayUrl != null && strPlayUrl.length() > 0) {
                        if (mIsBeingLinkMic) {
                            TCLinkMicPlayItem item = getPlayItemByUserID(mUserId);
                            if (item != null) {
                                item.mPlayUrl = strPlayUrl;
                                if (!mPausing) {
                                    item.startPlay(strPlayUrl);
                                }
                            }
                        }
                    } else {
                        handleLinkMicFailed("获取防盗链key失败，结束连麦");
                    }
                }
            });

            //3.拉取其它正在和大主播连麦的小主播的视频流
            try {
                JSONObject jsonObj = new JSONObject(mResponseParams);
                JSONArray jsonArray = jsonObj.getJSONArray("streams");
                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    if (item != null) {
                        startPlayVideoStream(item.getString("userID"), item.getString("playUrl"));
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT) {            //推流失败事件通知
            handleLinkMicFailed("推流失败，结束连麦");
        }  else if (event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL) {       //未获得摄像头权限
            handleLinkMicFailed("未获得摄像头权限，结束连麦");
        } else if (event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {           //未获得麦克风权限
            if (mIsBeingLinkMic) {
                handleLinkMicFailed("未获得麦克风权限，结束连麦");
            }
        }

        mTXCloudVideoView.setLogText(null, bundle, event);
    }

    @Override
    public void onLivePushNetStatus(String pushUrl, Bundle status) {
        mTXCloudVideoView.setLogText(status, null, 0);
    }

    @Override
    public void onLivePlayEvent(final String playUrl, final int event, final Bundle param) {
        TCLinkMicPlayItem item = getPlayItemByPlayUrl(playUrl);
        if (item == null) {
            return;
        }

        item.setLogText(null, param, event);

        if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            //开始拉流，或者拉流失败，结束loading
            item.stopLoading();
        } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || event == TXLiveConstants.PLAY_EVT_PLAY_END || event == TXLiveConstants.PLAY_ERR_GET_RTMP_ACC_URL_FAIL) {
            if (item.mUserID.equalsIgnoreCase(mUserId)) {
                handleLinkMicFailed("主播的流拉取失败，结束连麦");
            }
            else {
                stopPlayVideoStream(item.mUserID);
            }
        }
    }

    @Override
    public void onLivePlayNetStatus(final String playUrl, final Bundle status) {
        TCLinkMicPlayItem item = getPlayItemByPlayUrl(playUrl);
        if (item != null) {
            item.setLogText(status, null, 0);
        }
    }

    private TCLinkMicPlayItem getPlayItemByUserID(String strUserID) {
        if (strUserID == null || strUserID.length() == 0) {
            return null;
        }

        for (TCLinkMicPlayItem item: mVecPlayItems) {
            if (strUserID.equalsIgnoreCase(item.mUserID)) {
                return item;
            }
        }

        return null;
    }

    private TCLinkMicPlayItem getPlayItemByPlayUrl(String playUrl) {
        if (playUrl == null || playUrl.length() == 0) {
            return null;
        }

        for (TCLinkMicPlayItem item: mVecPlayItems) {
            if (playUrl.equalsIgnoreCase(item.mPlayUrl)) {
                return item;
            }
        }

        return null;
    }

    private void startPlayVideoStream(String userID, String playUrl) {
        if (userID == null  || userID.length() == 0 || playUrl == null || playUrl.length() == 0) {
            return;
        }

        boolean bExist = false;
        for (TCLinkMicPlayItem item: mVecPlayItems) {
            if (userID.equalsIgnoreCase(item.mUserID)/* || playUrl .equalsIgnoreCase(item.mPlayUrl)*/) {
                bExist = true;
                break;
            }
        }

        if (bExist == false) {
            for (TCLinkMicPlayItem item: mVecPlayItems) {
                if (item.mUserID == null || item.mUserID.length() == 0) {
                    item.mUserID = userID;
                    item.startLoading();
                    item.mPlayUrl = playUrl;
                    if (!mPausing) {
                        item.startPlay(playUrl);
                    }
                    break;
                }
            }
        }
    }

    private void stopPlayVideoStream(String userID) {
        TCLinkMicPlayItem item = getPlayItemByUserID(userID);
        if (item != null) {
            item.stopPlay(true);
            item.empty();
        }
    }

    private String getStrFieldFromResponseParams(String key, String params) {
        if (key == null || params == null) {
            return null;
        }

        try {
            JSONObject json = new JSONObject(params);
            if (json.has(key)) {
                return json.getString(key);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void showLog() {
        super.showLog();
        for (TCLinkMicPlayItem item : mVecPlayItems) {
            item.showLog(!mShowLog);
        }
    }

    private Toast mNoticeToast;
    private Timer mNoticeTimer;

    private void showNoticeToast(String text) {
        if (mNoticeToast == null) {
            mNoticeToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        }

        if (mNoticeTimer == null) {
            mNoticeTimer = new  Timer();
        }

        mNoticeToast.setText(text);
        mNoticeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mNoticeToast.show();
            }
        }, 0, 3000);

    }

    private void hideNoticeToast() {
        if (mNoticeToast != null) {
            mNoticeToast.cancel();
            mNoticeToast = null;
        }
        if (mNoticeTimer != null) {
            mNoticeTimer.cancel();
            mNoticeTimer = null;
        }
    }
}
