package com.example.nyapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.classes.BaseEventBean;
import com.example.classes.CouponPic;
import com.example.classes.VersionBean;
import com.example.jpush.ExampleUtil;
import com.example.jpush.MyReceiver;
import com.example.service.DownloadService;
import com.example.util.ImageViewUtil;
import com.example.util.MyGlideUtils;
import com.example.util.MyMsgDialog;
import com.example.util.MyOkHttpUtils;
import com.example.util.MyToastUtil;
import com.example.util.UrlContact;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * 开屏页
 */
public class SplashActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "SplashActivity.class";
    private LinearLayout rootLayout;
    private MyApplication myApplication;
    private int serverVersion = 1;
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public String PicUrl;
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;
    private static final int sleepTime = 2000;
    private String mPicUrl;
    private int mType;
    private String mUrl;
    private SplashActivity mActivity;
    private int mPermissionsType;
    private String mVersionUrl;
    private MyMsgDialog mVersionDialog;
    private ProgressDialog mProgressDialog;

    // IWXAPI 是第三方app和微信通信的openapi接口
//	 private IWXAPI api;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        EventBus.getDefault().register(this);
        mActivity = SplashActivity.this;

        initPermissions();//动态权限申请

        //消息推送
        initView1();
        registerMessageReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        isForeground = true;
        JPushInterface.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        isForeground = false;
        JPushInterface.onPause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mMessageReceiver);
        Intent localIntent = new Intent();
        localIntent.setClass(this, MyReceiver.class); // 销毁时重新启动Service
        this.startService(localIntent);
    }

    private void initPermissions() {
        //所要申请的权限
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION};

        if (EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            Log.i(TAG, "已获取权限");
            initView();
        } else {
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            mPermissionsType = 1;
            EasyPermissions.requestPermissions(this, "APP正常运行必须的权限", 0, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (mPermissionsType == 2) {
            mVersionDialog.dismiss();
            loadNewVersion();
        } else {
            initView();
        }
        MyToastUtil.showShortMessage("权限申请成功");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        initView();
        MyToastUtil.showShortMessage("权限申请失败");
    }

    private void initView1() {

//			TextView mImei = (TextView) findViewById(R.id.tv_imei);
        String udid = ExampleUtil.getImei(getApplicationContext(), "");
//	        if (null != udid) mImei.setText("IMEI: " + udid);

//			TextView mAppKey = (TextView) findViewById(R.id.tv_appkey);
        String appKey = ExampleUtil.getAppKey(getApplicationContext());
//			if (null == appKey) appKey = "AppKey异常";
//			mAppKey.setText("AppKey: " + appKey);

        String packageName = getPackageName();
//			TextView mPackage = (TextView) findViewById(R.id.tv_package);
//			mPackage.setText("PackageName: " + packageName);

        String versionName = ExampleUtil.GetVersion(getApplicationContext());
//			TextView mVersion = (TextView) findViewById(R.id.tv_version);
//			mVersion.setText("Version: " + versionName);
    }

    @Override
    public void initView() {
        getPicUrl();
    }

    private void startActivity() {
        Map<String, String> map = new TreeMap<>();
        map.put("deviceId", MyApplication.sUdid);
        MyOkHttpUtils
                .getData(UrlContact.URL_VERSION, map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i(TAG, "onError: " + e.toString());
                        start2MainActivity();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            Gson gson = new Gson();
                            VersionBean versionBean = gson.fromJson(response, VersionBean.class);
                            serverVersion = Integer.parseInt(versionBean.getVersion());
                            mVersionUrl = versionBean.getUrl();
                            showUpdateDialog();
                        } else {
                            start2MainActivity();
                        }
                    }
                });
    }

    private void showUpdateDialog() {
        if (MyApplication.localVersion < serverVersion) {
            mVersionDialog = new MyMsgDialog(mActivity, true, "更新提醒", "APP有新版本啦，请点击确认更新或者到应用市场下载最新版APP！",
                    new MyMsgDialog.ConfirmListener() {
                        @Override
                        public void onClick() {
                            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            if (EasyPermissions.hasPermissions(mActivity, perms)) {
                                mVersionDialog.dismiss();
                                loadNewVersion();
                            } else {
                                mPermissionsType = 2;
                                EasyPermissions.requestPermissions(mActivity, "APP下载更新所必须的权限", 0, perms);
                            }

                        }
                    }, null);
            mVersionDialog.setCancelable(false);
            mVersionDialog.show();
        } else {
            start2MainActivity();
        }
    }

    private void loadNewVersion() {
        Intent intent = new Intent(mActivity, DownloadService.class);
        intent.putExtra("url", mVersionUrl);
        mActivity.startService(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBaseEventBean(BaseEventBean eventBean) {
        int progress = eventBean.getNum();
        int type = eventBean.getType();
        if (type == 0) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setIcon(R.drawable.progress_icon);
                mProgressDialog.setTitle("正在下载中...");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setMax(100);
                mProgressDialog.show();
                mProgressDialog.setCancelable(false);
                mProgressDialog.setProgress(0);
            } else {
                mProgressDialog.setProgress(progress);
                if (progress == 100) {
                    mProgressDialog.dismiss();
                }
            }
        }
    }

    public void start2MainActivity() {
        final Intent in = new Intent(SplashActivity.this, MainActivity.class);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(in);

                SplashActivity.this.myFinish();

            }
        };
        timer.schedule(task, sleepTime);
    }

    public void myFinish() {
        this.finish();
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public void getPicUrl() {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "1");
        MyOkHttpUtils
                .getData(UrlContact.URL_AD, map)
                .build()
                .connTimeOut(5000)      // 设置当前请求的连接超时时间
                .readTimeOut(5000)      // 设置当前请求的读取超时时间
                .writeTimeOut(5000)     // 设置当前请求的写入超时时间
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        startActivity();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            Gson gson = new Gson();
                            CouponPic couponPic = gson.fromJson(response, CouponPic.class);
                            if (couponPic.isResult()) {
                                List<CouponPic.DataBean> dataBeen = couponPic.getData();
                                if (dataBeen != null && dataBeen.size() > 0) {
                                    CouponPic.DataBean dataBean = dataBeen.get(0);
                                    mPicUrl = dataBean.getPic_Path();
                                    mUrl = dataBean.getUrl();
                                    mType = dataBean.getType();
                                    SplashActivity.this.setAd();
                                } else {
                                    startActivity();
                                }
                            } else {
                                startActivity();
                            }
                        }
                    }
                });
    }

    private void setAd() {
        if (mPicUrl != null) {
            PicUrl = mPicUrl;

            WindowManager wm = this.getWindowManager();

            final int width = wm.getDefaultDisplay().getWidth();

            ImageView AdPic = (ImageView) findViewById(R.id.logo);
//
            ImageViewUtil.INSTANCE.loadImageView(AdPic, PicUrl);
            MyGlideUtils.loadNativeImage(this, PicUrl, AdPic);

            TranslateAnimation translateAnimation = new TranslateAnimation(width, 0, 0, 0);
            translateAnimation.setDuration(1000);
            AdPic.startAnimation(translateAnimation);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    SplashActivity.this.startActivity();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            SplashActivity.this.startActivity();
        }
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
//              setCostomMsg(showMsg.toString());
            }
        }
    }

}
