package com.example.classes;

import java.io.Serializable;

public class Products1 implements Serializable {
    private int id;
    private String Name;//产品
    private String spec;//规格
    private String price;//单价
    private String count;//数量
    //	private int count;
    private String scale;//收益比例
    private String money;//代购金额
    private String lucreMoney;//代购收益
    private String urlImage;


    //	public Products1(int id,String urlImg) {
//		this.id = id;
//		this.urlImage = urlImg;
//	}
//	public String toString() {
//		// TODO Auto-generated method stub
////		return super.toString();
//		return urlImage;
//	}
    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getLucreMoney() {
        return lucreMoney;
    }

    public void setLucreMoney(String lucreMoney) {
        this.lucreMoney = lucreMoney;
    }


}
