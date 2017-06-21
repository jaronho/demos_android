package com.example.classes;


public class GpsLocation {
	
	public static MyGPSloction gps = new MyGPSloction();
	public final static MyGPSloction getgps(){		
		return gps;
	}
	public static  Object cleangps(){
		MyGPSloction gps = new MyGPSloction();
		return gps;
	}

}
