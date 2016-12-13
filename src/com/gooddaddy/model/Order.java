package com.gooddaddy.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
	private Integer id;
	private String name;
	private String phone;
	private String address;
	private List<OrderItem> item = new ArrayList<OrderItem>();
	private Double total;
	private Boolean blackPepper;
	private Boolean mushroom;
	private Boolean tomato;
	private Date date;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<OrderItem> getItem() {
		return item;
	}

	public void setItem(List<OrderItem> item) {
		this.item = item;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Boolean getBlackPepper() {
		return blackPepper;
	}

	public void setBlackPepper(Boolean blackPepper) {
		this.blackPepper = blackPepper;
	}

	public Boolean getMushroom() {
		return mushroom;
	}

	public void setMushroom(Boolean mushroom) {
		this.mushroom = mushroom;
	}

	public Boolean getTomato() {
		return tomato;
	}

	public void setTomato(Boolean tomato) {
		this.tomato = tomato;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
