package com.inlog.ecommerce.model;

import org.json.JSONObject;

import java.util.ArrayList;

public class varient {
	private int id;
	private String name;

	public varient(int id, String name) {
		this.id = id;
		this.name = name;
	}

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



}
