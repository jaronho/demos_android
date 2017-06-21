package com.example.classes;

public class UserAddress {
	private int Id;
	private String Province_Name;
	private String City_Name;
	private String County_Name;
	private String Town_Name;
	private String Vaillage_Name; 
	private String Receive_Address;
	private String Receive_Name;
	private String Receive_Phone;
	private int Province_Id;
	private int City_Id;
	private int County_Id;
	private int Town_Id;
	private int Vaillage_Id;
	private int State;
	
	
	public int getState() {
		return State;
	}
	public void setState(int state) {
		State = state;
	}
	public String getTown_Name() {
		return Town_Name;
	}
	public void setTown_Name(String town_Name) {
		Town_Name = town_Name;
	}
	public String getVaillage_Name() {
		return Vaillage_Name;
	}
	public void setVaillage_Name(String vaillage_Name) {
		Vaillage_Name = vaillage_Name;
	}
	
	
	
	public int getTown_Id() {
		return Town_Id;
	}
	public void setTown_Id(int town_Id) {
		Town_Id = town_Id;
	}
	public int getVaillage_Id() {
		return Vaillage_Id;
	}
	public void setVaillage_Id(int vaillage_Id) {
		Vaillage_Id = vaillage_Id;
	}
	public int getProvince_Id() {
		return Province_Id;
	}
	public void setProvince_Id(int province_Id) {
		Province_Id = province_Id;
	}
	public int getCity_Id() {
		return City_Id;
	}
	public void setCity_Id(int city_Id) {
		City_Id = city_Id;
	}
	public int getCounty_Id() {
		return County_Id;
	}
	public void setCounty_Id(int county_Id) {
		County_Id = county_Id;
	}
	/*private String applyMessage;
	
	private boolean IsApplying;//是否审核通过
	
	public boolean isIsApplying() {
		return IsApplying;
	}
	public void setIsApplying(boolean isApplying) {
		IsApplying = isApplying;
	}*/
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getProvince_Name() {
		return Province_Name;
	}
	public void setProvince_Name(String province_Name) {
		Province_Name = province_Name;
	}
	public String getCity_Name() {
		return City_Name;
	}
	public void setCity_Name(String city_Name) {
		City_Name = city_Name;
	}
	public String getCounty_Name() {
		return County_Name;
	}
	public void setCounty_Name(String county_Name) {
		County_Name = county_Name;
	}
	public String getReceive_Address() {
		return Receive_Address;
	}
	public void setReceive_Address(String receive_Address) {
		Receive_Address = receive_Address;
	}
	public String getReceive_Name() {
		return Receive_Name;
	}
	public void setReceive_Name(String receive_Name) {
		Receive_Name = receive_Name;
	}
	public String getReceive_Phone() {
		return Receive_Phone;
	}
	public void setReceive_Phone(String receive_Phone) {
		Receive_Phone = receive_Phone;
	}
	@Override
	public String toString() {
		return "UserAddress [Id=" + Id + ", Province_Name=" + Province_Name
				+ ", City_Name=" + City_Name + ", County_Name=" + County_Name
				+ ", Receive_Address=" + Receive_Address + ", Receive_Name="
				+ Receive_Name + ", Receive_Phone=" + Receive_Phone + "]";
	}

}
