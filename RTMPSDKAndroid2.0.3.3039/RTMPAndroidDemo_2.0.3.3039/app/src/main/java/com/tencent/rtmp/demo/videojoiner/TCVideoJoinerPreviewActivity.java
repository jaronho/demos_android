package com.tencent.rtmp.demo.videojoiner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.rtmp.TXLog;
import com.tencent.rtmp.demo.R;
import com.tencent.rtmp.demo.common.activity.videochoose.TCVideoFileInfo;
import com.tencent.rtmp.demo.common.utils.TCConstants;
import com.tencent.rtmp.demo.common.activity.videopreview.TCVideoPreviewActivity;
import com.tencent.rtmp.videoedit.TXVideoEditConstants;
import com.tencent.rtmp.videoedit.TXVideoInfoReader;
import com.tencent.rtmp.videoedit.TXVideoJoiner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TCVideoJoinerPreviewActivity extends Activity implements View.OnClickListener, TXVideoJoiner.TXVideoPreviewListener, TXVideoJoiner.TXVideoJoinerListener {
    private static final String TAG = TCVideoJoinerPreviewActivity.class.getSimpleName();

    private final int STATE_PLAY = 0;
    private final int STATE_PAUSE = 1;
    private int mCurrentState;

    private ArrayList<TCVideoFileInfo> mTCVideoFileInfoList;

    private TextView mBtnDone;
    private ImageButton mBtnPlay;
    private ProgressBar mProgressBar;
    private RelativeLayout mLayoutJoiner;
    private FrameLayout mVideoView;

    private boolean mCoverImagePath;
    private String mVideoOutputPath;

    private TXVideoJoiner mTXVideoJoiner;
    private TXVideoEditConstants.TXVideoInfo mTXVideoInfo;
    private ArrayList<String> mVideoSourceList;
    private TXVideoInfoReader mVideoInfoReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_joiner_preview);

        mVideoInfoReader = new TXVideoInfoReader();
        initViews();
        initData();
    }

    private void initViews() {
        mLayoutJoiner = (RelativeLayout) findViewById(R.id.layout_joiner);

        mBtnDone = (TextView) findViewById(R.id.btn_done);
        mBtnPlay = (ImageButton) findViewById(R.id.btn_play);

        mVideoView = (FrameLayout) findViewById(R.id.video_view);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setMax(100);

        LinearLayout backLL = (LinearLayout)findViewById(R.id.back_ll);
        backLL.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);
        mBtnDone.setOnClickListener(this);
    }

    private void initData() {
        mTCVideoFileInfoList = (ArrayList<TCVideoFileInfo>) getIntent().getSerializableExtra(TCConstants.INTENT_KEY_MULTI_CHOOSE);
        if (mTCVideoFileInfoList == null || mTCVideoFileInfoList.size() == 0) {
            finish();
            return;
        }
        mTXVideoJoiner = new TXVideoJoiner(this);
        mTXVideoInfo = mVideoInfoReader.getVideoFileInfo(this, mTCVideoFileInfoList.get(0).getFilePath());

        mVideoSourceList = new ArrayList<>();
        for (int i = 0; i < mTCVideoFileInfoList.size(); i++) {
            mVideoSourceList.add(mTCVideoFileInfoList.get(i).getFilePath());
        }
        createThumbFile(mTXVideoInfo);
    }

    private void createThumbFile(TXVideoEditConstants.TXVideoInfo videoInfo) {
        if (null == videoInfo) {
            return;
        }
        final TCVideoFileInfo fileInfo = mTCVideoFileInfoList.get(0);
        if (fileInfo == null)
            return;
        AsyncTask<TXVideoEditConstants.TXVideoInfo, String, String> task = new AsyncTask<TXVideoEditConstants.TXVideoInfo, String, String>() {
            @Override
            protected String doInBackground(TXVideoEditConstants.TXVideoInfo... params) {
                String mediaFileName = fileInfo.getFileName();
                TXLog.d(TAG, "fileName = " + mediaFileName);
                if (mediaFileName == null)
                    mediaFileName = fileInfo.getFilePath().substring(fileInfo.getFilePath().lastIndexOf("/"), fileInfo.getFilePath().lastIndexOf("."));
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

                if (fileInfo.getThumbPath() == null) {
                    fileInfo.setThumbPath(file.getAbsolutePath());
                }
                return null;
            }
        };
        task.execute(videoInfo);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_done:
                startTranscode();
                break;
            case R.id.back_ll:
                mTXVideoJoiner.stopPlay();
                mTXVideoJoiner.cancel();
                mTXVideoJoiner.setTXVideoPreviewListener(null);
                mTXVideoJoiner.setVideoJoinerListener(null);
                finish();
                break;
            case R.id.btn_play:
                playVideo();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mProgressBar.setVisibility(View.GONE);
        mLayoutJoiner.setVisibility(View.VISIBLE);

        TXVideoEditConstants.TXPreviewParam param = new TXVideoEditConstants.TXPreviewParam();
        param.videoView = mVideoView;
        param.renderMode = TXVideoEditConstants.PREVIEW_RENDER_MODE_FILL_EDGE;
        mTXVideoJoiner.initWithPreview(param);
        mTXVideoJoiner.setTXVideoPreviewListener(this);

        mTXVideoJoiner.setVideoPathList(mVideoSourceList);
        mTXVideoJoiner.startPlay();

        mCurrentState = STATE_PLAY;

        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTXVideoJoiner.stopPlay();
        mTXVideoJoiner.cancel();
        mTXVideoJoiner.setTXVideoPreviewListener(null);
        mTXVideoJoiner.setVideoJoinerListener(null);
        mProgressBar.setVisibility(View.GONE);

        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
    }

    private void playVideo() {
        if (mCurrentState == STATE_PLAY) {
            mTXVideoJoiner.pausePlay();
            mCurrentState = STATE_PAUSE;
        } else {
            mTXVideoJoiner.resumePlay();
            mCurrentState = STATE_PLAY;
        }
        mBtnPlay.setImageResource(mCurrentState == STATE_PLAY ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    public void startTranscode() {
        mTXVideoJoiner.pausePlay();

        mProgressBar.setVisibility(View.VISIBLE);
        mLayoutJoiner.setVisibility(View.GONE);
        try {
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
            TXLog.d(TAG, mVideoOutputPath);
            mTXVideoJoiner.setVideoJoinerListener(this);
            mTXVideoJoiner.joinVideo(TXVideoEditConstants.VIDEO_COMPRESSED_540P, mVideoOutputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPreviewProgress(int time) {
        TXLog.d(TAG, "onPreviewProgress curPos = " + time);
    }

    @Override
    public void onPreviewFinished() {
        TXLog.d(TAG, "onPreviewFinished");
        mTXVideoJoiner.startPlay();
    }

    @Override
    public void onJoinProgress(float progress) {
        final int prog = (int) (progress * 100);
        TXLog.d(TAG, "composer progress = " + prog);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setProgress(prog);
            }
        });
    }

    @Override
    public void onJoinComplete(TXVideoEditConstants.TXJoinerResult result) {
        if (result.retCode == TXVideoEditConstants.JOIN_RESULT_OK) {
            Intent intent = new Intent(getApplicationContext(), TCVideoPreviewActivity.class);
            intent.putExtra(TCConstants.VIDEO_RECORD_TYPE, TCConstants.VIDEO_RECORD_TYPE_PLAY);
            intent.putExtra(TCConstants.VIDEO_RECORD_RESULT, result.retCode);
            intent.putExtra(TCConstants.VIDEO_RECORD_DESCMSG, result.descMsg);
            intent.putExtra(TCConstants.VIDEO_RECORD_VIDEPATH, mVideoOutputPath);
            intent.putExtra(TCConstants.VIDEO_RECORD_COVERPATH, mTCVideoFileInfoList.get(0).getThumbPath());
            startActivity(intent);
            finish();
        } else {
            final TXVideoEditConstants.TXJoinerResult ret = result;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLayoutJoiner.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);

                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(TCVideoJoinerPreviewActivity.this);
                    normalDialog.setTitle("视频合成失败");
                    normalDialog.setMessage(ret.descMsg);
                    normalDialog.setCancelable(false);
                    normalDialog.setPositiveButton("知道了",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    normalDialog.show();
                }
            });
        }
    }

    PhoneStateListener listener = new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                    if (mTXVideoJoiner != null) mTXVideoJoiner.pausePlay();
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (mTXVideoJoiner != null) mTXVideoJoiner.pausePlay();
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mTXVideoJoiner != null) mTXVideoJoiner.resumePlay();
                    break;
            }
        }
    };
}

