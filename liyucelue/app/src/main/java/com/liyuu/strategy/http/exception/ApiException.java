package com.liyuu.strategy.http.exception;

/**
 * ApiException
 */
public class ApiException extends Exception{

    private int code;
    private String msg;

    public ApiException(String msg) {
        super(msg);
    }

    public ApiException(String msg, int code) {
        this.msg = msg;
        this.code = code;
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
}
