package com.tencent.qcloud.xiaozhibo.mainui.list;

public class TCLiveInfo {
    public String   userid;
    public String   groupid;
    public int      timestamp;
    public int      type;
    public int      viewercount;
    public int      likecount;
    public String   title;
    public String   playurl;
    public String   hls_play_url;
    public String   fileid;

    //TCLiveUserInfo
    public TCLiveUserInfo userinfo;


    public class TCLiveUserInfo {
        public String nickname;
        public String headpic;
        public String frontcover;
        public String location;
    }
}
