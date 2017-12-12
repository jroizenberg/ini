package com.institucion.fm.wf.model;

public enum RoleType {
	HPA(1), SUPERVISOR(2), PLMANAGER(3), NORMAL(0);
	
	private int id;

	private RoleType(int id) { this.id = id; }

	public int toInt() { return id; }

	public static RoleType fromInt(int value) {    
		switch(value) {
			case 1:  return HPA;
			case 2:  return SUPERVISOR;
			case 3: return PLMANAGER;
			case 0: return NORMAL;
			default: return NORMAL;
		}
	}
}
