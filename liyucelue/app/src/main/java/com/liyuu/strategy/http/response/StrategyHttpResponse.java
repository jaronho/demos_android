package com.liyuu.strategy.http.response;

import java.io.Serializable;

public class StrategyHttpResponse<T> implements Serializable {
    /**
     * time : 2018-05-18 15:24:14
     * code : 1000
     * data : T
     * msg : 成功
     * url :
     */

    private String time;
    private int code;
    private String msg;
    private String url;
    private T data;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StrategyHttpResponse{" +
                "time='" + time + '\'' +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", url='" + url + '\'' +
                ", data=" + data +
                '}';
    }
}
