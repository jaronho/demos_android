package com.example.classes;

import java.util.List;

import android.R.integer;
/**
 * 
 * @author zhangying
 *
 */
public class Address {
	private int Id;
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	private String Area_Name;
	private List<Address1> list;
	public String getArea_Name() {
		return Area_Name;
	}
	public void setArea_Name(String area_Name) {
		Area_Name = area_Name;
	}
	public List<Address1> getList() {
		return list;
	}
	public void setList(List<Address1> list) {
		this.list = list;
	}
	
}
