package com.example.classes;

/**
 * Created by NY on 2017/3/2.
 * 商品数量和ID
 */

public class CartItemBean {

    private int Id;
    private String Count;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String Count) {
        this.Count = Count;
    }
}
