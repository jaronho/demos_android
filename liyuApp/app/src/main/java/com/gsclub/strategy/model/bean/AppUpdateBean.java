package com.gsclub.strategy.model.bean;

import java.io.Serializable;

/**
 * Created by 640 on 2018/1/17 0017.
 */

public class AppUpdateBean implements Serializable {
    public String id;
    public String platform;
    public String version;
    public String ver_num;
    public String img_url;
    public String update_url;
    public String desc;
    public int is_update;// 1提示弹窗 2 不提示弹窗
    public int must_update;// 1强制升级 2 不强制升级
    public boolean not_show_ignore;//是否显示忽略版本 true不显示
}
