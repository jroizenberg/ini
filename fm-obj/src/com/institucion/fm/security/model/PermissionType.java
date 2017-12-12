package com.institucion.fm.security.model;

public enum PermissionType
{
	TX(0), WEB(1), WS(2);

	private int id;

	private PermissionType(int id) { this.id = id; }

	public int toInt() { return id; }

	public static PermissionType fromInt(int value)
	{    
		switch(value)
		{
			case 0:  return TX;
			case 1:  return WEB;
			default: return WS;
		}
	}
}