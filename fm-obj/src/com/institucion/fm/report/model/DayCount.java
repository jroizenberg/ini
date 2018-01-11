package com.institucion.fm.report.model;

import java.io.Serializable;

public class DayCount implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int date;
	
	private String dateShow;
	
	private int count;

	public String getDateShow() {
		return dateShow;
	}

	public void setDateShow(String dateshow) {
		this.dateShow = dateshow;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
