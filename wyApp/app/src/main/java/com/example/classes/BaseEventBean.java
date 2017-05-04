package com.example.classes;

/**
 * Created by NY on 2017/2/27.
 *
 */

public class BaseEventBean {
    private int type;
    private int num;

    /**
     * type 0：版本更新进度条
     * type 2：刷新购物车
     */
    public BaseEventBean(int type, int num) {
        this.type = type;
        this.num = num;
    }

    public int getType() {
        return type;
    }

    public int getNum() {
        return num;
    }
}
