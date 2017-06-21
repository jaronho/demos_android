package com.example.classes;

import java.util.List;

public class ProductCart {
	private String Pro_Name;
	private int Pro_Id;
	private String Spec;
	private double Price;
	private int Count;
	private String Manuf_Name;
	private String Pic_Url;
	private List<Price> priceList;
	private String Common_Name;//化学名
	private double Free_Price;
	private int Marketing_Type;
	private boolean Is_Presell;
	private String TimeStamp;
	private String Total_Content;//总含量
	private String Dosageform;//剂型
	private String Type;

	public String getTimeStamp() {
		return TimeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		TimeStamp = timeStamp;
	}

	public boolean is_Presell() {
		return Is_Presell;
	}

	public void setIs_Presell(boolean is_Presell) {
		Is_Presell = is_Presell;
	}

	public int getMarketing_Type() {
		return Marketing_Type;
	}
	public void setMarketing_Type(int marketing_Type) {
		Marketing_Type = marketing_Type;
	}
	
	public double getFree_Price() {
		return Free_Price;
	}
	public void setFree_Price(double free_Price) {
		Free_Price = free_Price;
	}
	
	public String getCommon_Name() {
		return Common_Name;
	}
	public void setCommon_Name(String common_Name) {
		Common_Name = common_Name;
	}
	public String getTotal_Content() {
		return Total_Content;
	}
	public void setTotal_Content(String total_Content) {
		Total_Content = total_Content;
	}
	public String getDosageform() {
		return Dosageform;
	}
	public void setDosageform(String dosageform) {
		Dosageform = dosageform;
	}

	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public boolean checked=false;
	
	public String getPro_Name() {
		return Pro_Name;
	}
	public void setPro_Name(String pro_Name) {
		Pro_Name = pro_Name;
	}
	public int getPro_Id() {
		return Pro_Id;
	}
	public void setPro_Id(int pro_Id) {
		Pro_Id = pro_Id;
	}
	public String getSpec() {
		return Spec;
	}
	public void setSpec(String spec) {
		Spec = spec;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	public int getCount() {
		return Count;
	}
	public void setCount(int count) {
		Count = count;
	}
	public String getManuf_Name() {
		return Manuf_Name;
	}
	public void setManuf_Name(String manuf_Name) {
		Manuf_Name = manuf_Name;
	}
	
	public String getPic_Url() {
		return Pic_Url;
	}
	public void setPic_Url(String pic_Url) {
		Pic_Url = pic_Url;
	}
	public List<Price> getPriceList() {
		return priceList;
	}
	public void setPriceList(List<Price> priceList) {
		this.priceList = priceList;
	}

	
}