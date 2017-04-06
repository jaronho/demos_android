package com.focustech.electronicbrand;

import android.app.Activity;
import android.app.Application;

import com.focustech.electronicbrand.bridge.BridgeFactory;
import com.focustech.electronicbrand.bridge.BridgeLifeCycleSetKeeper;
import com.focustech.electronicbrand.bridge.Bridges;
import com.focustech.electronicbrand.bridge.cache.localstorage.LocalFileStorageManager;
import com.focustech.electronicbrand.util.ToastUtil;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <应用初始化> <功能详细描述>
 * 
 * @author cyf
 * @version [版本号, 2014-3-24]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class EBApplication extends Application
{
    /**
     * app实例
     */
    public static EBApplication ebApplication = null;
    
    /**
     * 本地activity栈
     */
    public static List<Activity> activitys = new ArrayList<Activity>();
    
    /**
     * 当前avtivity名称
     */
    public static String currentActivityName = "";
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        ebApplication = this;
        BridgeFactory.init(this);
        BridgeLifeCycleSetKeeper.getInstance().initOnApplicationCreate(this);
        LocalFileStorageManager manager = BridgeFactory.getBridge(Bridges.LOCAL_FILE_STORAGE);
        Picasso picasso = new Picasso.Builder(this).downloader(
                new OkHttpDownloader(new File(manager.getCacheImgFilePath(this)))).build();
        Picasso.setSingletonInstance(picasso);
    }


    @Override
    public void onTerminate()
    {
        super.onTerminate();
        onDestory();
    }

    /**
     * 退出应用，清理内存
     */
    private void onDestory() {
        BridgeLifeCycleSetKeeper.getInstance().clearOnApplicationQuit();
        ToastUtil.destory();
    }


    /**
     * 
     * <添加> <功能详细描述>
     * 
     * @param activity
     * @see [类、类#方法、类#成员]
     */
    public void addActivity(Activity activity)
    {
        activitys.add(activity);
    }
    
    /**
     * 
     * <删除>
     * <功能详细描述>
     * @param activity
     * @see [类、类#方法、类#成员]
     */
    public void deleteActivity(Activity activity)
    {
        if (activity != null)
        {
            activitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }
}
