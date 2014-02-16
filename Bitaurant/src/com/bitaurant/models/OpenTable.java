package com.bitaurant.models;

import java.io.Serializable;
import java.util.ArrayList;

public class OpenTable implements Serializable{
	
	private static final long serialVersionUID = -8087463769282136592L;
	
	public String id;
	public String title;
	public String price;
	public String address;
	public ArrayList<Items> items;
	
	public OpenTable(){}

	public OpenTable(String title, String price) {
		super();
		this.title = title;
		this.price = price;
	}
}
