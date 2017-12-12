package com.institucion.fm.security.model;

public enum RoleType {
	USER(1), FMADMIN(2), WFMANAGER(3), SYSADMIN(4);
	
	private int id;

	private RoleType(int id) { this.id = id; }

	public int toInt() { return id; }

	public static RoleType fromInt(int value)
	{    
		switch(value)
		{
			case 1:  return USER;
			case 2:  return FMADMIN;
			case 3: return WFMANAGER;
			case 4: return SYSADMIN;
			default: return USER;
		}
	}

}
