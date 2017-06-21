package com.example.classes;

import java.util.List;

public class AddressArea {
	private int id;
	private String name;
	private List<AddressTowns> addressTowns;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<AddressTowns> getAddressTowns() {
		return addressTowns;
	}
	public void setAddressTowns(List<AddressTowns> addressTowns) {
		this.addressTowns = addressTowns;
	}
	

}
