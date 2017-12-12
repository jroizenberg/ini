package com.institucion.fm.bb.model;

public enum Priority
{	
	LOW(0), MEDIUM(1), HIGH(2);

	private int id;

	private Priority(int id) { this.id = id; }
	public int toInt() { return id; }

	public static Priority fromInt(int value)
	{
		switch(value)
		{
			case 0: return LOW;
			case 1: return MEDIUM;
			default:
				return HIGH;
		}
	}
}