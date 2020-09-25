package com.koreait.matzip.rest.model;

import com.koreait.matzip.model.RestaurantVO;

public class RestRecMenuVO extends RestaurantVO{
	
	private int seq;
	private String menu_nm;
	private int menu_price;
	private String menu_pic;
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getMenu_nm() {
		return menu_nm;
	}
	public void setMenu_nm(String menu_nm) {
		this.menu_nm = menu_nm;
	}
	public int getMenu_price() {
		return menu_price;
	}
	public void setMenu_price(int menu_price) {
		this.menu_price = menu_price;
	}
	public String getMenu_pic() {
		return menu_pic;
	}
	public void setMenu_pic(String menu_pic) {
		this.menu_pic = menu_pic;
	}
	
	

}
