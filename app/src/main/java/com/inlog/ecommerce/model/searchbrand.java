package com.inlog.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

public class searchbrand {
	private int brandid;
	private String brandname;
	private int count;
	public boolean checked;
	public searchbrand(){

	}
	public searchbrand(int brandid, String brandname,boolean checked) {
		this.brandid = brandid;
		this.brandname = brandname;
		this.checked = checked;
	}

	public int getBrandid() {
		return brandid;
	}
	public String getBrandname() {
		return brandname;
	}

	public boolean checked() {
		return checked;
	}

	public void setBrandid(int brandid) {
		this.brandid = brandid;
	}

	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
