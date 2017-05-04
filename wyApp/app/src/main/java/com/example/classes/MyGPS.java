package com.example.classes;

import java.io.Serializable;

public class MyGPS implements Serializable{
	private String ProvinceName;//省
	private String CityName;//市
	private String CountyName;//县
	private String TownName;//乡镇
	private String longitude;//经度
	private String latitude;//纬度
	private int proId;
	private int cityId;
	private int couId;
	private int townId;
	
	
	public String getTownName() {
		return TownName;
	}
	public void setTownName(String townName) {
		TownName = townName;
	}
	public int getTownId() {
		return townId;
	}
	public void setTownId(int townId) {
		this.townId = townId;
	}
	public int getProId() {
		return proId;
	}
	public void setProId(int proId) {
		this.proId = proId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public int getCouId() {
		return couId;
	}
	public void setCouId(int couId) {
		this.couId = couId;
	}
	public String getProvinceName() {
		return ProvinceName;
	}
	public void setProvinceName(String provinceName) {
		ProvinceName = provinceName;
	}
	public String getCityName() {
		return CityName;
	}
	public void setCityName(String cityName) {
		CityName = cityName;
	}
	public String getCountyName() {
		return CountyName;
	}
	public void setCountyName(String countyName) {
		CountyName = countyName;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	
}
