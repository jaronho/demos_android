package com.example.classes;

public class MyCouponGoods {
	private String Pro_Name;
	private String Total_Content;
	private String Common_Name;
	private String Dosageform;
	private String Pic_Url;//图片
	private String Price;//价格
	private String Spec;//规格
	private int Stock;
	private int Id;
	private String Cate_Name;
	private String Show_Price;//特殊价格显示
	private String Unit;//单件的单位
	private String SubUnit_Price;//单瓶的价格
	private String SubUnit;//单瓶的单位
	
	
	
	public String getPrice() {
		return Price;
	}
	public void setPrice(String price) {
		Price = price;
	}
	public String getShow_Price() {
		return Show_Price;
	}
	public void setShow_Price(String show_Price) {
		Show_Price = show_Price;
	}
	public String getUnit() {
		return Unit;
	}
	public void setUnit(String unit) {
		Unit = unit;
	}
	public String getSubUnit_Price() {
		return SubUnit_Price;
	}
	public void setSubUnit_Price(String subUnit_Price) {
		SubUnit_Price = subUnit_Price;
	}
	public String getSubUnit() {
		return SubUnit;
	}
	public void setSubUnit(String subUnit) {
		SubUnit = subUnit;
	}
	public String getCate_Name() {
		return Cate_Name;
	}
	public void setCate_Name(String cate_Name) {
		Cate_Name = cate_Name;
	}
	
	public String getPic_Url() {
		return Pic_Url;
	}
	public void setPic_Url(String pic_Url) {
		Pic_Url = pic_Url;
	}
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		this.Id = id;
	}
	public int getStock() {
		return Stock;
	}
	public void setStock(int stock) {
		Stock = stock;
	}
	public String getPro_Name() {
		return Pro_Name;
	}
	public void setPro_Name(String pro_Name) {
		Pro_Name = pro_Name;
	}
	public String getTotal_Content() {
		return Total_Content;
	}
	public void setTotal_Content(String total_Content) {
		Total_Content = total_Content;
	}
	public String getCommon_Name() {
		return Common_Name;
	}
	public void setCommon_Name(String common_Name) {
		Common_Name = common_Name;
	}
	public String getDosageform() {
		return Dosageform;
	}
	public void setDosageform(String dosageform) {
		Dosageform = dosageform;
	}
	

	public String getSpec() {
		return Spec;
	}
	public void setSpec(String spec) {
		Spec = spec;
	}
	
}
