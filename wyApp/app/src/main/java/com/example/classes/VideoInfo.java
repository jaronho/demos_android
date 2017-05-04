package com.example.classes;

/**
 * Author:  jaron.ho
 * Date:    2017-04-10
 * Brief:   VideoData
 */

public class VideoInfo {
    public static final int TYPE_LIVE = 1;              // 直播类型
    public static final int TYPE_VIDEO = 2;             // 视频类型
    public static final int STATUS_NOTSTART = 1;        // 未开始
    public static final int STATUS_STARTED = 2;         // 已开始

    public int type = TYPE_LIVE;
    public String url = "";
    public String describe = "";
    public String date = "";
    public int people = 0;
    public int status = STATUS_NOTSTART;
}
