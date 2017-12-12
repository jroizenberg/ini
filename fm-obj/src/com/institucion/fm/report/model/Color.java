package com.institucion.fm.report.model;

public enum Color {	
	
	BLUE(0), GREEN(1),YELLOW(2),RED(3);
	
	private int id;

	private Color(int id) {
		this.id = id;
	}
	public int toInt() { return id; }
	
	public static Color fromInt(int value){
		switch (value){
			case 0:
				return BLUE;
			case 1:
				return  GREEN;
			case 2:
				return  YELLOW;
			case 3:
				return  RED;
			default:
				return null;
		}
	}

}
