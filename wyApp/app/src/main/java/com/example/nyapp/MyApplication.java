package com.example.nyapp;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.support.multidex.MultiDexApplication;
import android.telephony.TelephonyManager;

import com.baidu.location.service.LocationService;
import com.baidu.location.service.WriteLog;
import com.example.live.LiveHelper;
import com.example.util.ImageViewUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.zhy.http.okhttp.OkHttpUtils;

import net.sourceforge.simcpux.Constants;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class MyApplication extends MultiDexApplication {
    public static Context sContext;
    public static int localVersion = 1;
    public static String sUdid;//获取设备号
    //	定位
    public LocationService locationService;
    public Vibrator mVibrator;


    @Override
    public void onCreate() {
        super.onCreate();
        PlatformConfig.setWeixin(Constants.APP_ID, Constants.APP_SECRET);
        UMShareAPI.get(this);
        sContext = this.getApplicationContext();

//		字体设置
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/wryh.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        //初始化OKHttpUtils
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)// 设置当前请求的连接超时时间
                .readTimeout(20000L, TimeUnit.MILLISECONDS)// 设置当前请求的读取超时时间
                .writeTimeout(20000L, TimeUnit.MILLISECONDS)// 设置当前请求的写入超时时间
                .build();
        OkHttpUtils.initClient(okHttpClient);
        ImageViewUtil.INSTANCE.init(this);

//	       消息推送
        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);

//	    定位
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        WriteLog.getInstance().init();

        try {
            PackageInfo packageInfo = getApplicationContext()
                    .getPackageManager().getPackageInfo(getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        init();
        LiveHelper.init(this);
    }

    public static Context getContextObject() {
        return sContext;
    }

    private void init() {
        initImageLoader(getApplicationContext());
        MyVolley.init(this);
        UuidUtil util = new UuidUtil(sContext);
        sUdid = util.getUDID();
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public static class UuidUtil {
        private static final String PREFS_FILE = "gank_device_id.xml";
        private static final String PREFS_DEVICE_ID = "gank_device_id";
        private static String uuid;
        private static Context contexts;

        private UuidUtil(Context context) {
            UuidUtil.contexts = context;
        }

        private String getUDID() {
            if (uuid == null) {
                synchronized (MyApplication.class) {
                    if (uuid == null) {
                        final SharedPreferences prefs = contexts.getSharedPreferences(PREFS_FILE, 0);
                        final String id = prefs.getString(PREFS_DEVICE_ID, null);

                        if (id != null) {
                            // Use the ids previously computed and stored in the prefs file
                            uuid = id;
                        } else {

                            final String androidId = Secure.getString(contexts.getContentResolver(), Secure.ANDROID_ID);

                            // Use the Android ID unless it's broken, in which case fallback on deviceId,
                            // unless it's not available, then fallback on a random number which we store
                            // to a prefs file
                            try {
                                if (!"9774d56d682e549c".equals(androidId)) {
                                    uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                                } else {
                                    final String deviceId = ((TelephonyManager) contexts.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                                    uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() : UUID.randomUUID().toString();
                                }
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }

                            // Write the value out to the prefs file
                            prefs.edit().putString(PREFS_DEVICE_ID, uuid).apply();
                        }
                    }
                }
            }
            return uuid;
        }
    }
}