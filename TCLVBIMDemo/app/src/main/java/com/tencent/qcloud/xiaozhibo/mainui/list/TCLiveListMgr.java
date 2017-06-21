package com.tencent.qcloud.xiaozhibo.mainui.list;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.qcloud.xiaozhibo.common.utils.TCHttpEngine;
import com.tencent.rtmp.TXLog;

import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class TCLiveListMgr {
    private static final String TAG = TCLiveListMgr.class.getSimpleName();
    private static final int PAGESIZE = 20;
    public static final int LIST_TYPE_LIVE = 1;
    public static final int LIST_TYPE_VOD  = 2;
    public static final int LIST_TYPE_ALL  = 3;
    public static final int LIST_TYPE_UGC  = 5;
    private boolean mIsFetching;

    private ArrayList<TCLiveInfo> mLiveInfoList = new ArrayList<>();
    private ArrayList<TCLiveInfo> mVodInfoList = new ArrayList<>();
    private ArrayList<TCLiveInfo> mUGCInfoList = new ArrayList<>();

    private TCLiveListMgr() {
        mIsFetching = false;
    }

    private static class TCLiveListMgrHolder {
        private static TCLiveListMgr instance = new TCLiveListMgr();
    }

    public static TCLiveListMgr getInstance() {
        return TCLiveListMgrHolder.instance;
    }

    /**
     * 获取内存中缓存的列表
     * @return 完整列表
     * @param mDataType 类型 1：直播 2： 点播 4：UGC
     */
    public ArrayList<TCLiveInfo> getDataList(int mDataType) {
        switch (mDataType) {
            case LIST_TYPE_LIVE:
                return mLiveInfoList;
            case LIST_TYPE_VOD:
                return mVodInfoList;
            case LIST_TYPE_UGC:
                return mUGCInfoList;
        }
        return null;
    }

    /**
     * 分页获取完整列表
     * @param type 类型。1：直播 2：点播 3： 全部 4： 短视频
     * @param listener 列表回调，每获取到一页数据回调一次
     */
    public boolean reloadLiveList(int type, Listener listener) {
        if (mIsFetching) {
            TXLog.w(TAG,"reloadLiveList ignore when fetching");
            return false;
        }
        TXLog.d(TAG,"fetchLiveList start");
        switch (type) {
            case LIST_TYPE_LIVE:
                mLiveInfoList.clear();
                break;
            case LIST_TYPE_VOD:
                mVodInfoList.clear();
                break;
            case LIST_TYPE_UGC:
                mUGCInfoList.clear();
                break;
        }
        fetchLiveList(type, 1, PAGESIZE, listener);
        mIsFetching = true;
        return true;
    }

    /**
     * 获取列表
     *
     * @param type     1:拉取在线直播列表 2:拉取7天内录播列表 3:拉取在线直播和7天内录播列表，直播列表在前，录播列表在后 4:UGC
     * @param pageNo   页数
     * @param pageSize 每页个数
     */
    private void fetchLiveList(final int type, final int pageNo, final int pageSize, final Listener listener) {
        JSONObject req = new JSONObject();
        try {
            req.put("flag", type);
            req.put("pageno", pageNo);
            req.put("pagesize", pageSize);
            req.put("Action","FetchList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG,"fetchLiveList type:"+type+",pagNo:"+pageNo+",pageSize:"+pageSize);
        TCHttpEngine.getInstance().post(req, new TCHttpEngine.Listener() {
            @Override
            public void onResponse(int retCode, String retMsg, JSONObject retData) {
                if (retCode == 0 && retData != null) {
                    try {
                        int totalcount = retData.getInt("totalcount");
                        JSONArray record = retData.getJSONArray("pusherlist");
                        Type listType = new TypeToken<ArrayList<TCLiveInfo>>() {
                        }.getType();

                        ArrayList<TCLiveInfo> dataList;
                        switch (type) {
                            case LIST_TYPE_LIVE:
                                dataList = mLiveInfoList;
                                break;
                            case LIST_TYPE_VOD:
                                dataList = mVodInfoList;
                                break;
                            case LIST_TYPE_UGC:
                                dataList = mUGCInfoList;
                                break;
                            default:
                                dataList = new ArrayList<TCLiveInfo>();
                                break;
                        }

                        ArrayList<TCLiveInfo> result = new Gson().fromJson(record.toString(), listType);
                        if (result != null && !result.isEmpty()) {
//                            Log.d(TAG,"fetchLiveList result,totalCount:"+totalcount+",curCount:"+result.size());
                            dataList.addAll(result);

                            if (dataList.size() >= totalcount || pageNo*PAGESIZE >= totalcount) {
                                TXLog.d(TAG,"fetchLiveList finish count:"+totalcount);
                                mIsFetching = false;
                            } else {
                                fetchLiveList(type, pageNo+1, PAGESIZE, listener);
                            }
                        } else {
                            TXLog.w(TAG,"fetchLiveList broken result,retCode:"+retCode+",retMsg:"+retMsg);
                            mIsFetching = false;
                        }
                        if (listener != null) {
                            listener.onLiveList(retCode,dataList,pageNo == 1);
                        }
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (listener != null) {
                    listener.onLiveList(retCode,null,true);
                }
                mIsFetching = false;
            }
        });
    }

    /**
     * 视频列表获取结果回调
     */
    public interface Listener {
        /**
         * @param retCode 获取结果，0表示成功
         * @param result  列表数据
         * @param refresh 是否需要刷新界面，首页需要刷新
         */
        public void onLiveList(int retCode, final ArrayList<TCLiveInfo> result, boolean refresh);
    }
}

