package com.bitaurant.models;

import java.io.Serializable;

public class Items implements Serializable{
	
	private static final long serialVersionUID = -8087463769282136592L;
	
	public String title;
	public String price;
	
	public Items(){}
	
	public Items(String title, String price){
		this.title = title;
		this.price = price;
	}

}
