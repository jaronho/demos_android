package com.tencent.rtmp.demo.videorecord;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.rtmp.demo.R;
import com.tencent.rtmp.demo.common.activity.videopreview.TCVideoPreviewActivity;
import com.tencent.rtmp.demo.common.widget.BeautySettingPannel;
import com.tencent.rtmp.demo.common.utils.TCConstants;
import com.tencent.rtmp.ugc.TXRecordCommon;
import com.tencent.rtmp.ugc.TXUGCRecord;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.Locale;

/**
 * UGC主播端录制界面
 */
public class TCVideoRecordActivity extends Activity implements View.OnClickListener, BeautySettingPannel.IOnBeautyParamsChangeListener
                                                            ,TXRecordCommon.ITXVideoRecordListener
{

    //录制相关
    private boolean mRecording = false;
    private TXUGCRecord mTXCameraRecord = null;
    private ProgressBar mRecordProgress = null;
    private long  mStartRecordTimeStamp = 0;

    private boolean mFlashOn = false;
    private boolean mFront = true;
    TXRecordCommon.TXRecordResult mTXRecordResult = null;
    TXCloudVideoView    mVideoView;
    TextView mProgressTime;

    AudioManager mAudioManager;
    AudioManager.OnAudioFocusChangeListener mOnAudioFocusListener;

    private BeautySettingPannel mBeautyPannel;
    private int              mBeautyLevel = 5;
    private int              mWhiteningLevel = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_video_record);

        LinearLayout backLL = (LinearLayout)findViewById(R.id.back_ll);
        backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecording && mTXCameraRecord != null) {
                    mTXCameraRecord.stopRecord();
                    mTXCameraRecord.setVideoRecordListener(null);
                }
                finish();
            }
        });

        mBeautyPannel = (BeautySettingPannel) findViewById(R.id.beauty_pannel);
        mBeautyPannel.setBeautyParamsChangeListener(this);
        mBeautyPannel.setViewVisibility(R.id.exposure_ll, View.GONE);

        mTXCameraRecord = TXUGCRecord.getInstance(this.getApplicationContext());

        // 预览
        if (mTXCameraRecord == null) {
            mTXCameraRecord = TXUGCRecord.getInstance(TCVideoRecordActivity.this.getApplicationContext());
        }
        mVideoView = (TXCloudVideoView) findViewById(R.id.video_view);
        mVideoView.enableHardwareDecode(true);
        TXRecordCommon.TXUGCSimpleConfig param = new TXRecordCommon.TXUGCSimpleConfig();
        param.videoQuality = TXRecordCommon.VIDEO_QUALITY_MEDIUM;
        param.isFront = mFront;
        mTXCameraRecord.startCameraSimplePreview(param,mVideoView);
        mTXCameraRecord.setBeautyDepth(mBeautyLevel, mWhiteningLevel);

        mProgressTime = (TextView) findViewById(R.id.progress_time);
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopRecord(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mTXCameraRecord != null) {
            mTXCameraRecord.stopCameraPreview();
            mTXCameraRecord.setVideoRecordListener(null);
            mTXCameraRecord = null;
        }
        }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_beauty:
                mBeautyPannel.setVisibility(View.VISIBLE);

                break;
            case R.id.btn_switch_camera:
                mFront = !mFront;
                if (mTXCameraRecord != null) {
                    mTXCameraRecord.switchCamera(mFront);
                }
                break;
            case R.id.record:
                switchRecord();
                break;
            default:
                if (mBeautyPannel.isShown()) {
                    mBeautyPannel.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void switchRecord() {
        if (mRecording) {
           stopRecord(true);
        } else {
            startRecord();
        }
    }

    private void stopRecord(boolean showToast) {
        // 录制时间要大于5s
        if (System.currentTimeMillis() <= mStartRecordTimeStamp + 5*1000) {
            if (showToast) {
                showTooShortToast();
                return;
            } else {
                if (mTXCameraRecord != null) {
                    mTXCameraRecord.setVideoRecordListener(null);
                }
            }
        }
        if (mTXCameraRecord != null) {
            mTXCameraRecord.stopRecord();
        }
        ImageView liveRecord = (ImageView) findViewById(R.id.record);
        if(liveRecord != null) liveRecord.setBackgroundResource(R.drawable.start_record);
        mRecording = false;

        if (mRecordProgress != null) {
            mRecordProgress.setProgress(0);
        }
        if (mProgressTime != null) {
            mProgressTime.setText(String.format(Locale.CHINA, "%s","00:00"));
        }
        abandonAudioFocus();
    }

    private void showTooShortToast() {
        if (mRecordProgress != null) {
            int statusBarHeight = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }

            int[] position = new int[2];
            mRecordProgress.getLocationOnScreen(position);
            Toast toast = Toast.makeText(this, "至少录到这里", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.LEFT, position[0], position[1] - statusBarHeight - 110);
            toast.show();
        }
    }

    private void startRecord() {
        if (mTXCameraRecord == null) {
            mTXCameraRecord = TXUGCRecord.getInstance(this.getApplicationContext());
        }
        mRecordProgress = (ProgressBar) findViewById(R.id.record_progress);
        mTXCameraRecord.setVideoRecordListener(this);
        int result = mTXCameraRecord.startRecord();
        if (result != 0) {
            Toast.makeText(TCVideoRecordActivity.this.getApplicationContext(),"录制失败，错误码：" + result, Toast.LENGTH_SHORT).show();
            mTXCameraRecord.setVideoRecordListener(null);
            mTXCameraRecord.stopRecord();
            return;
        }
        mRecording = true;
        ImageView liveRecord = (ImageView) findViewById(R.id.record);
        if(liveRecord != null) liveRecord.setBackgroundResource(R.drawable.stop_record);
        mStartRecordTimeStamp = System.currentTimeMillis();
        requestAudioFocus();
    }

    void startPreview() {
        if (mTXRecordResult != null && mTXRecordResult.retCode == TXRecordCommon.RECORD_RESULT_OK) {
            Intent intent = new Intent(getApplicationContext(), TCVideoPreviewActivity.class);
            intent.putExtra(TCConstants.VIDEO_RECORD_TYPE, TCConstants.VIDEO_RECORD_TYPE_PUBLISH);
            intent.putExtra(TCConstants.VIDEO_RECORD_RESULT, mTXRecordResult.retCode);
            intent.putExtra(TCConstants.VIDEO_RECORD_DESCMSG, mTXRecordResult.descMsg);
            intent.putExtra(TCConstants.VIDEO_RECORD_VIDEPATH, mTXRecordResult.videoPath);
            intent.putExtra(TCConstants.VIDEO_RECORD_COVERPATH, mTXRecordResult.coverPath);
            startActivity(intent);
        }
    }

//    private void retryRecord() {
//        if (mRecording ) {
//            stopRecord();
//        }
//        View recordLayout = TCVideoRecordActivity.this.findViewById(R.id.record_layout);
//        View publishLayout = TCVideoRecordActivity.this.findViewById(R.id.publishLayout);
//        View controlLayout = TCVideoRecordActivity.this.findViewById(R.id.record_control);
//        if (recordLayout != null) {
//            recordLayout.setVisibility(View.VISIBLE);
//        }
//        if (publishLayout != null) {
//            publishLayout.setVisibility(View.GONE);
//        }
//        if (controlLayout != null) {
//            controlLayout.setVisibility(View.VISIBLE);
//        }
//
//        if (mRecordProgress != null) {
//            mRecordProgress.setProgress(0);
//        }
//
//        mLayoutPitu.setVisibility(View.GONE);
//        mPitu.setVisibility(View.VISIBLE);
//        mClosePitu.setVisibility(View.GONE);
//    }

    @Override
    public void onBeautyParamsChange(BeautySettingPannel.BeautyParams params, int key) {
        switch (key) {
            case BeautySettingPannel.BEAUTYPARAM_BEAUTY:
                mBeautyLevel = params.mBeautyLevel;
                if (mTXCameraRecord != null) {
                    mTXCameraRecord.setBeautyDepth(mBeautyLevel, mWhiteningLevel);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_WHITE:
                mWhiteningLevel = params.mWhiteLevel;
                if (mTXCameraRecord != null) {
                    mTXCameraRecord.setBeautyDepth(mBeautyLevel, mWhiteningLevel);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_BIG_EYE:
                if (mTXCameraRecord != null) {
                    mTXCameraRecord.setEyeScaleLevel(params.mBigEyeLevel);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_FACE_LIFT:
                if (mTXCameraRecord != null) {
                    mTXCameraRecord.setFaceScaleLevel(params.mFaceSlimLevel);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_FILTER:
                if (mTXCameraRecord != null) {
                    mTXCameraRecord.setFilter(params.mFilterBmp);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_GREEN:
                if (mTXCameraRecord != null) {
                    mTXCameraRecord.setGreenScreenFile(params.mGreenFile, true);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_MOTION_TMPL:
                if (mTXCameraRecord != null) {
                    mTXCameraRecord.setMotionTmpl(params.mMotionTmplPath);
                }
                break;
        }
    }

    @Override
    public void onRecordEvent(int event, Bundle param) {

    }

    @Override
    public void onRecordProgress(long milliSecond) {
        if (mRecordProgress != null) {
            float progress = milliSecond / 60000.0f;
            mRecordProgress.setProgress((int) (progress * 100));
            mProgressTime.setText(String.format(Locale.CHINA, "00:%02d", milliSecond/1000));
            if (milliSecond >= 60000.0f) {
                stopRecord(true);
            }
        }
    }

    @Override
    public void onRecordComplete(TXRecordCommon.TXRecordResult result) {
        mTXRecordResult = result;
        if (mTXRecordResult.retCode != TXRecordCommon.RECORD_RESULT_OK) {
            ImageView liveRecord = (ImageView) findViewById(R.id.record);
            if(liveRecord != null) liveRecord.setBackgroundResource(R.drawable.start_record);
            mRecording = false;

            if (mRecordProgress != null) {
                mRecordProgress.setProgress(0);
            }
            if (mProgressTime != null) {
                mProgressTime.setText(String.format(Locale.CHINA, "%s","00:00"));
            }
            Toast.makeText(TCVideoRecordActivity.this.getApplicationContext(),"录制失败，原因：" + mTXRecordResult.descMsg, Toast.LENGTH_SHORT).show();
        } else {
            View recordLayout = TCVideoRecordActivity.this.findViewById(R.id.record_layout);
            View publishLayout = TCVideoRecordActivity.this.findViewById(R.id.publishLayout);
            if (recordLayout != null) {
                recordLayout.setVisibility(View.VISIBLE);
            }
            if (publishLayout != null) {
                publishLayout.setVisibility(View.GONE);
            }

            if (mRecordProgress != null) {
                mRecordProgress.setProgress(0);
            }
            mProgressTime.setText(String.format(Locale.CHINA, "%s","00:00"));
            startPreview();
        }
    }

    private void requestAudioFocus() {

        if (null == mAudioManager) {
            mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        }

        if (null == mOnAudioFocusListener) {
            mOnAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {

                @Override
                public void onAudioFocusChange(int focusChange) {
                    try {
                        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                            mTXCameraRecord.setVideoRecordListener(null);
                            stopRecord(false);
                        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                            mTXCameraRecord.setVideoRecordListener(null);
                            stopRecord(false);
                        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };
        }
        try {
            mAudioManager.requestAudioFocus(mOnAudioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abandonAudioFocus() {
        try {
            if (null != mAudioManager && null != mOnAudioFocusListener) {
                mAudioManager.abandonAudioFocus(mOnAudioFocusListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
