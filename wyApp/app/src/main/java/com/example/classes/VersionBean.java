package com.example.classes;

/**
 * Created by NY on 2016/12/23.
 * 获取版本号和新版本下载地址
 */

public class VersionBean {

    /**
     * Version : 62
     * Url : http://app.16899.com/dl/nongyi/
     */

    private String Version;
    private String Url;

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }
}
