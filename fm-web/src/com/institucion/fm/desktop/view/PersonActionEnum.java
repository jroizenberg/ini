package com.institucion.fm.desktop.view;

public enum PersonActionEnum {
	ADD(0), UPDATE(1), DELETE(2);
	private int id;

	/**
	 * @param id
	 */
	private PersonActionEnum(int id) {
		this.id = id;
	}
	
	public int toInt (){
		return id;
	}

}
