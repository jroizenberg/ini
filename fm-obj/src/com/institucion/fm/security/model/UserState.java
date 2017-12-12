package com.institucion.fm.security.model;

public enum UserState {
	ACTIVE(0), INACTIVE(1), BLOCKED(2);

	private int id;

	private UserState(int id) { this.id = id; }

	public int toInt() { return id; }

	public static UserState fromInt(int value)
	{    
		switch(value)
		{
			case 0:  return ACTIVE;
			case 1:  return INACTIVE;
			default: return BLOCKED;
		}
	}

}
