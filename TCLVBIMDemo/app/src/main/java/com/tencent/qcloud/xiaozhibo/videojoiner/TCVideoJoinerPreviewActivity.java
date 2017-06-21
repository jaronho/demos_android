package com.tencent.qcloud.xiaozhibo.videojoiner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.qcloud.xiaozhibo.R;
import com.tencent.qcloud.xiaozhibo.common.utils.TCConstants;
import com.tencent.qcloud.xiaozhibo.common.utils.TCVideoFileInfo;
import com.tencent.qcloud.xiaozhibo.common.widget.VideoWorkProgressFragment;
import com.tencent.qcloud.xiaozhibo.videopublish.TCVideoPublisherActivity;
import com.tencent.rtmp.TXLog;
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

    private final int STATE_NONE = 0;
    private final int STATE_PLAY = 1;
    private final int STATE_PAUSE = 2;
    private final int STATE_TRANSCODE = 3;
    private int mCurrentState = STATE_NONE;

    private ArrayList<TCVideoFileInfo> mTCVideoFileInfoList;

    private Button mBtnSave;
    private Button mBtnPublish;
    private TextView mBtnBack;
    private ImageButton mBtnPlay;
    private RelativeLayout mLayoutJoiner;
    private FrameLayout mVideoView;
    private Button mDialogBtnOnlyPublish;

    private String mVideoOutputPath;

    private TXVideoJoiner mTXVideoJoiner;
    private TXVideoEditConstants.TXVideoInfo mTXVideoInfo;
    private boolean mPublish = false;
    private ArrayList<String> mVideoSourceList;
    private TXVideoInfoReader mVideoInfoReader;
    private boolean mNoCache = false;

    private VideoWorkProgressFragment mWorkProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_joiner_preview);

        mVideoInfoReader = TXVideoInfoReader.getInstance();
        initViews();
        initData();

        TXVideoEditConstants.TXPreviewParam param = new TXVideoEditConstants.TXPreviewParam();
        param.videoView = mVideoView;
        param.renderMode = TXVideoEditConstants.PREVIEW_RENDER_MODE_FILL_EDGE;
        mTXVideoJoiner.initWithPreview(param);
        mTXVideoJoiner.setTXVideoPreviewListener(this);

        mTXVideoJoiner.setVideoPathList(mVideoSourceList);
        mTXVideoJoiner.startPlay();

        mCurrentState = STATE_PLAY;
        mBtnPlay.setImageResource(mCurrentState == STATE_PLAY ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    private void initViews() {
        mLayoutJoiner = (RelativeLayout) findViewById(R.id.layout_joiner);

        mBtnBack = (TextView) findViewById(R.id.btn_back);
        mBtnSave = (Button) findViewById(R.id.btn_save);
        mBtnPublish = (Button) findViewById(R.id.btn_publish);
        mBtnPlay = (ImageButton) findViewById(R.id.btn_play);
        mDialogBtnOnlyPublish = (Button) findViewById(R.id.only_publish);

        mVideoView = (FrameLayout) findViewById(R.id.video_view);


        mBtnPlay.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
        mBtnPublish.setOnClickListener(this);
        mDialogBtnOnlyPublish.setOnClickListener(this);

        initWorkProgressDialog();
    }

    private void initWorkProgressDialog() {
        if (mWorkProgressDialog == null) {
            mWorkProgressDialog = new VideoWorkProgressFragment();
            mWorkProgressDialog.setOnClickStopListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(TCVideoJoinerPreviewActivity.this, "取消视频合成", Toast.LENGTH_SHORT).show();
                    mWorkProgressDialog.dismiss();
                    finish();
                }
            });
        }
        mWorkProgressDialog.setProgress(0);
    }

    private void initData() {
        mTCVideoFileInfoList = (ArrayList<TCVideoFileInfo>) getIntent().getSerializableExtra(TCConstants.INTENT_KEY_MULTI_CHOOSE);
        if (mTCVideoFileInfoList == null || mTCVideoFileInfoList.size() == 0) {
            finish();
            return;
        }
        mTXVideoJoiner = new TXVideoJoiner(this);
        mTXVideoInfo = mVideoInfoReader.getVideoFileInfo(mTCVideoFileInfoList.get(0).getFilePath());

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
            case R.id.btn_save:
                mPublish = false;
                startTranscode();
                break;
            case R.id.btn_publish:
                mPublish = true;
                mNoCache = false;
                startTranscode();
                break;
            case R.id.only_publish:
                mNoCache = true;
                mPublish = true;
                startTranscode();
                break;
            case R.id.btn_back:
                mTXVideoJoiner.pausePlay();
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

        if (mCurrentState == STATE_PLAY) {
            mTXVideoJoiner.pausePlay();
            mCurrentState = STATE_PAUSE;
        } else if (mCurrentState == STATE_TRANSCODE) {
            mTXVideoJoiner.cancel();
            mCurrentState = STATE_NONE;
        }
        mBtnPlay.setImageResource(mCurrentState == STATE_PLAY ? R.drawable.ic_pause : R.drawable.ic_play);

        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCurrentState == STATE_PLAY) {
            mTXVideoJoiner.pausePlay();
            mCurrentState = STATE_PAUSE;
        } else if (mCurrentState == STATE_TRANSCODE) {
            mTXVideoJoiner.cancel();
            mCurrentState = STATE_NONE;
        }

        mWorkProgressDialog.dismiss();
        mBtnPlay.setImageResource(mCurrentState == STATE_PLAY ? R.drawable.ic_pause : R.drawable.ic_play);

        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        listener = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTXVideoJoiner.stopPlay();
        mTXVideoJoiner.cancel();
        mTXVideoJoiner.setTXVideoPreviewListener(null);
        mTXVideoJoiner.setVideoJoinerListener(null);
    }

    private void playVideo() {
        if (mCurrentState == STATE_NONE) {
            mTXVideoJoiner.startPlay();
            mCurrentState = STATE_PLAY;
        } else if (mCurrentState == STATE_PLAY) {
            mTXVideoJoiner.pausePlay();
            mCurrentState = STATE_PAUSE;
        } else if (mCurrentState == STATE_PAUSE) {
            mTXVideoJoiner.resumePlay();
            mCurrentState = STATE_PLAY;
        } else if (mCurrentState == STATE_TRANSCODE) {
            //do nothing
        }
        mBtnPlay.setImageResource(mCurrentState == STATE_PLAY ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    public void startTranscode() {

        if (mCurrentState == STATE_PLAY || mCurrentState == STATE_PAUSE) {
            mTXVideoJoiner.stopPlay();
            mCurrentState = STATE_NONE;
            mBtnPlay.setImageResource(mCurrentState == STATE_PLAY ? R.drawable.ic_pause : R.drawable.ic_play);
        }
        mWorkProgressDialog.setProgress(0);
        mWorkProgressDialog.setCancelable(false);
        mWorkProgressDialog.show(getFragmentManager(), "progress_dialog");
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
            mCurrentState = STATE_TRANSCODE;
            mTXVideoJoiner.joinVideo(TXVideoEditConstants.VIDEO_COMPRESSED_540P, mVideoOutputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void publishVideo() {
        Intent intent = new Intent(getApplicationContext(), TCVideoPublisherActivity.class);
        intent.putExtra(TCConstants.VIDEO_RECORD_TYPE, TCConstants.VIDEO_RECORD_TYPE_PLAY);
        intent.putExtra(TCConstants.VIDEO_RECORD_VIDEPATH, mVideoOutputPath);
        intent.putExtra(TCConstants.VIDEO_RECORD_COVERPATH, mTCVideoFileInfoList.get(0).getThumbPath());
        intent.putExtra(TCConstants.VIDEO_RECORD_NO_CACHE, mNoCache);
        startActivity(intent);
    }

    private void updateMediaStore() {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(mVideoOutputPath)));
        sendBroadcast(scanIntent);
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
        int prog = (int) (progress * 100);
        TXLog.d(TAG, "composer progress = " + prog);
        mWorkProgressDialog.setProgress(prog);
    }

    @Override
    public void onJoinComplete(TXVideoEditConstants.TXJoinerResult result) {
        mWorkProgressDialog.dismiss();
        if (result.retCode == TXVideoEditConstants.JOIN_RESULT_OK) {
            updateMediaStore();
            if (mPublish) {
                publishVideo();
                mPublish = false;
            }
            finish();
        } else {
             TXVideoEditConstants.TXJoinerResult ret = result;
            //Toast.makeText(TCVideoJoinerPreviewActivity.this, ret.descMsg, Toast.LENGTH_SHORT).show();
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(TCVideoJoinerPreviewActivity.this,R.style.ConfirmDialogStyle);
            //normalDialog.setIcon(R.drawable.icon_dialog);
            normalDialog.setTitle("视频合成失败");
            normalDialog.setMessage(ret.descMsg);
            normalDialog.setCancelable(false);
            normalDialog.setPositiveButton("知道了",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            //避免WorkProgressDialog和normaldialog快速交替出现导致一瞬间的闪屏，这里延迟60ms显示
            mBtnSave.postDelayed(new Runnable() {
                @Override
                public void run() {
                    normalDialog.show();
                }
            },60);

        }
        mCurrentState = STATE_NONE;
    }

    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                    if (mTXVideoJoiner != null && mCurrentState == STATE_PLAY) {
                        mTXVideoJoiner.pausePlay();
                        mCurrentState = STATE_PAUSE;
                    }
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (mTXVideoJoiner != null && mCurrentState == STATE_PLAY) {
                        mTXVideoJoiner.pausePlay();
                        mCurrentState = STATE_PAUSE;
                    }
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mTXVideoJoiner != null && mCurrentState == STATE_PAUSE) {
                        mTXVideoJoiner.resumePlay();
                        mCurrentState = STATE_PLAY;
                    }
                    break;
            }
            mBtnPlay.setImageResource(mCurrentState == STATE_PLAY ? R.drawable.ic_pause : R.drawable.ic_play);
        }
    };
}

