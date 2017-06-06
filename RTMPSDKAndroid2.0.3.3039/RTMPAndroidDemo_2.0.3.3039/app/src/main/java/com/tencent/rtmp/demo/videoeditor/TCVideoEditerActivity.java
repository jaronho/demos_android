package com.tencent.rtmp.demo.videoeditor;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tencent.rtmp.TXLog;
import com.tencent.rtmp.demo.R;
import com.tencent.rtmp.demo.common.activity.videochoose.TCVideoFileInfo;
import com.tencent.rtmp.demo.common.activity.videopreview.TCVideoPreviewActivity;
import com.tencent.rtmp.demo.common.utils.TCConstants;
import com.tencent.rtmp.demo.common.utils.TCUtils;
import com.tencent.rtmp.videoedit.TXVideoEditConstants;
import com.tencent.rtmp.videoedit.TXVideoEditer;
import com.tencent.rtmp.videoedit.TXVideoInfoReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * UGC短视频裁剪
 */
public class TCVideoEditerActivity extends Activity implements View.OnClickListener, TCVideoEditView.IOnRangeChangeListener,
        TXVideoEditer.TXVideoGenerateListener, TXVideoInfoReader.OnSampleProgrocess, TXVideoEditer.TXVideoPreviewListener {

    private static final String TAG = TCVideoEditerActivity.class.getSimpleName();
    private final int STATE_PLAY = 0;
    private final int STATE_PAUSE = 1;
    private int mCurrentState;

    private TextView mTvDone;
    private TextView mTvCurrent;
    private TextView mTvDuration;
    private ImageButton mBtnPlay;
    private FrameLayout mVideoView;
    private LinearLayout mLayoutEditer;
    private TCVideoEditView mVideoEditView;

    private TXVideoEditer mTXVideoEditer;
    private TCVideoFileInfo mTCVideoFileInfo;
    private TXVideoInfoReader mTXVideoInfoReader;

    private String mVideoOutputPath;
    private TXVideoEditConstants.TXVideoInfo mTXVideoInfo;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.activity_video_editer);

        initViews();
        initData();

        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
    }

    @Override
    protected void onDestroy() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Glide.get(TCVideoEditerActivity.this).clearDiskCache();
            }
        };
        thread.start();
        Glide.get(TCVideoEditerActivity.this).clearMemory();
        super.onDestroy();

        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
    }

    private void initViews() {
        mTvCurrent = (TextView) findViewById(R.id.tv_current);
        mTvDuration = (TextView) findViewById(R.id.tv_duration);

        mVideoView = (FrameLayout) findViewById(R.id.video_view);

        mBtnPlay = (ImageButton) findViewById(R.id.btn_play);
        mBtnPlay.setOnClickListener(this);

        LinearLayout backLL = (LinearLayout)findViewById(R.id.back_ll);
        backLL.setOnClickListener(this);

        mTvDone = (TextView) findViewById(R.id.btn_done);
        mTvDone.setOnClickListener(this);

        mLayoutEditer = (LinearLayout) findViewById(R.id.layout_editer);
        mLayoutEditer.setEnabled(true);

        mVideoEditView = (TCVideoEditView) findViewById(R.id.timelineItem);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setMax(100);
    }

    private void initData() {
        mTCVideoFileInfo = (TCVideoFileInfo) getIntent().getSerializableExtra(TCConstants.INTENT_KEY_SINGLE_CHOOSE);
        mTXVideoInfoReader = new TXVideoInfoReader();

        mTXVideoEditer = new TXVideoEditer(this);
        mTXVideoInfoReader.getSampleImages(TCConstants.THUMB_COUNT, mTCVideoFileInfo.getFilePath(), this);

        mTXVideoInfo = mTXVideoInfoReader.getVideoFileInfo(this, mTCVideoFileInfo.getFilePath());
        String duration = TCUtils.duration(mTXVideoInfo.duration);
        String position = TCUtils.duration(0);

        mTvCurrent.setText(position);
        mTvDuration.setText(duration);

        createThumbFile(mTXVideoInfo);
    }

    private void createThumbFile(TXVideoEditConstants.TXVideoInfo videoInfo) {
        if (null == videoInfo) {
            return;
        }
        AsyncTask<TXVideoEditConstants.TXVideoInfo, String, String> task = new AsyncTask<TXVideoEditConstants.TXVideoInfo, String, String>() {
            @Override
            protected String doInBackground(TXVideoEditConstants.TXVideoInfo... params) {
                String mediaFileName = mTCVideoFileInfo.getFileName();
                TXLog.d(TAG, "fileName = " + mediaFileName);
                if (mediaFileName == null)
                    mediaFileName = mTCVideoFileInfo.getFilePath().substring(mTCVideoFileInfo.getFilePath().lastIndexOf("/"), mTCVideoFileInfo.getFilePath().lastIndexOf("."));
                if (mediaFileName.lastIndexOf(".") != -1) {
                    mediaFileName = mediaFileName.substring(0, mediaFileName.lastIndexOf("."));
                }

                String folder = Environment.getExternalStorageDirectory() + File.separator + TCConstants.DEFAULT_MEDIA_PACK_FOLDER + File.separator + mediaFileName;
                File appDir = new File(folder);
                if (!appDir.exists()) {
                    appDir.mkdirs();
                }

                String fileName = "thumbnail" + ".jpg";
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    if (params[0].coverImage != null)
                        params[0].coverImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (mTCVideoFileInfo.getThumbPath() == null) {
                    mTCVideoFileInfo.setThumbPath(file.getAbsolutePath());
                }
                return null;
            }
        };
        task.execute(videoInfo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTXVideoEditer.setTXVideoPreviewListener(this);
        TXVideoEditConstants.TXPreviewParam param = new TXVideoEditConstants.TXPreviewParam();
        param.videoView = mVideoView;
        param.renderMode = TXVideoEditConstants.PREVIEW_RENDER_MODE_FILL_EDGE;
        mTXVideoEditer.initWithPreview(param);
        mTXVideoEditer.setVideoPath(mTCVideoFileInfo.getFilePath());
        mTXVideoEditer.startPlayFromTime(0, (int) mTXVideoInfo.duration);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoEditView.setMediaFileInfo(mTXVideoInfo);
        mVideoEditView.setRangeChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mTXVideoEditer.cancel();

            mTvDone.setEnabled(true);
            mTvDone.setClickable(true);

            mProgressBar.setVisibility(View.GONE);
        } else {
            mTXVideoInfoReader.cancel();
            mTXVideoEditer.stopPlay();
            mTXVideoEditer.setTXVideoPreviewListener(null);
            mTXVideoEditer.setVideoGenerateListener(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_done:
                doTranscode();
                break;
            case R.id.back_ll:
                mTXVideoInfoReader.cancel();
                mTXVideoEditer.stopPlay();
                mTXVideoEditer.setTXVideoPreviewListener(null);
                mTXVideoEditer.setVideoGenerateListener(null);
                finish();
                break;
            case R.id.btn_play:
                playVideo();
                break;
        }
    }

    private void playVideo() {
        if (mCurrentState == STATE_PLAY) {
            mTXVideoEditer.pausePlay();
            mCurrentState = STATE_PAUSE;
        } else {
            mTXVideoEditer.resumePlay();
            mCurrentState = STATE_PLAY;
        }
        mBtnPlay.setImageResource(mCurrentState == STATE_PLAY ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    private void doTranscode() {
        mTvDone.setEnabled(false);
        mTvDone.setClickable(false);

        mTXVideoInfoReader.cancel();
        mTXVideoEditer.stopPlay();
        mProgressBar.setVisibility(View.VISIBLE);
        mLayoutEditer.setEnabled(false);
        startTranscode();
    }

    private void startTranscode() {
        mCurrentState = STATE_PAUSE;
        mBtnPlay.setImageResource(R.drawable.ic_play);
        mTXVideoEditer.pausePlay();

        try {
            mTXVideoEditer.setCutFromTime(mVideoEditView.getSegmentFrom(), mVideoEditView.getSegmentTo());

            String outputPath = Environment.getExternalStorageDirectory() + File.separator + TCConstants.DEFAULT_MEDIA_PACK_FOLDER;
            File outputFolder = new File(outputPath);

            if (!outputFolder.exists()) {
                outputFolder.mkdirs();
            }
            String current = String.valueOf(System.currentTimeMillis() / 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String time = sdf.format(new Date(Long.valueOf(current + "000")));
            String saveFileName = String.format("TXVideo_%s.mp4", time);
            mVideoOutputPath = outputFolder + "/" + saveFileName;
            mTXVideoEditer.setVideoGenerateListener(this);
            mTXVideoEditer.generateVideo(TXVideoEditConstants.VIDEO_COMPRESSED_540P, mVideoOutputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGenerateProgress(final float progress) {
        final int prog = (int) (progress * 100);
//        TXLog.d(TAG, "onGenerateProgress = " + prog);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setProgress(prog);
            }
        });
    }

    @Override
    public void onGenerateComplete(TXVideoEditConstants.TXGenerateResult result) {
        if (result.retCode == TXVideoEditConstants.GENERATE_RESULT_OK) {
            Intent intent = new Intent(getApplicationContext(), TCVideoPreviewActivity.class);
            intent.putExtra(TCConstants.VIDEO_RECORD_TYPE, TCConstants.VIDEO_RECORD_TYPE_PLAY);
            intent.putExtra(TCConstants.VIDEO_RECORD_RESULT, result.retCode);
            intent.putExtra(TCConstants.VIDEO_RECORD_DESCMSG, result.descMsg);
            intent.putExtra(TCConstants.VIDEO_RECORD_VIDEPATH, mVideoOutputPath);
            intent.putExtra(TCConstants.VIDEO_RECORD_COVERPATH, mTCVideoFileInfo.getThumbPath());
            startActivity(intent);
            finish();
        } else {
            final TXVideoEditConstants.TXGenerateResult ret = result;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TCVideoEditerActivity.this, ret.descMsg, Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    mTvDone.setEnabled(true);
                    mTvDone.setClickable(true);
                }
            });
        }
    }

    @Override
    public void sampleProcess(int number, Bitmap bitmap) {
        final int num = number;
        final Bitmap bmp = bitmap;
        final String filePath = saveImage(num, bmp);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mVideoEditView.addBitmap(num, filePath);
            }
        });
        bmp.recycle();
        TXLog.d(TAG, "number = " + number + ",bmp = " + bitmap);
    }

    public String saveImage(int index, Bitmap bmp) {
        String mediaFileName = mTCVideoFileInfo.getFileName();
        TXLog.d(TAG, "fileName = " + mediaFileName);
        if (mediaFileName == null)
            mediaFileName = mTCVideoFileInfo.getFilePath().substring(mTCVideoFileInfo.getFilePath().lastIndexOf("/"), mTCVideoFileInfo.getFilePath().lastIndexOf("."));
        if (mediaFileName.lastIndexOf(".") != -1) {
            mediaFileName = mediaFileName.substring(0, mediaFileName.lastIndexOf("."));
        }

        String folder = Environment.getExternalStorageDirectory() + File.separator + TCConstants.DEFAULT_MEDIA_PACK_FOLDER + File.separator + mediaFileName;
        File appDir = new File(folder);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        String fileName = index + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    @Override
    public void onPreviewProgress(int time) {
        if (mTvCurrent != null) {
            mTvCurrent.setText(TCUtils.duration(time));
        }
    }

    @Override
    public void onPreviewFinished() {
        TXLog.d(TAG, "onPreviewFinished");
        mCurrentState = STATE_PLAY;
        mBtnPlay.setImageResource(R.drawable.ic_pause);
        mTXVideoEditer.startPlayFromTime(mVideoEditView.getSegmentFrom(), mVideoEditView.getSegmentTo());
    }

    @Override
    public void onKeyDown() {
        mCurrentState = STATE_PAUSE;
        mBtnPlay.setImageResource(R.drawable.ic_play);
        mTXVideoEditer.pausePlay();

    }

    @Override
    public void onKeyUp(int startTime, int endTime) {
        mCurrentState = STATE_PLAY;
        mBtnPlay.setImageResource(R.drawable.ic_pause);
        mTXVideoEditer.startPlayFromTime(mVideoEditView.getSegmentFrom(), mVideoEditView.getSegmentTo());
    }

    PhoneStateListener listener = new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                    if (mTXVideoEditer != null) mTXVideoEditer.pausePlay();
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (mTXVideoEditer != null) mTXVideoEditer.pausePlay();
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mTXVideoEditer != null) mTXVideoEditer.resumePlay();
                    break;
            }
        }
    };
}
