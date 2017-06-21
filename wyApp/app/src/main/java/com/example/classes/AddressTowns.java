package com.example.classes;

import java.util.List;

public class AddressTowns {

	private int id;
	private String name;
	private List<AddressVaillage> addressVaillages;
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
	public List<AddressVaillage> getAddressVaillages() {
		return addressVaillages;
	}
	public void setAddressVaillages(List<AddressVaillage> addressVaillages) {
		this.addressVaillages = addressVaillages;
	}
	
	
}
