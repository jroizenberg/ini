package com.institucion.fm.wf.model;

public enum ClassType
{
	FECLASS(0), JAVACLASS(1);

	private int id;

	private ClassType(int id) { this.id = id; }

	public int toInt() { return id; }

	public static ClassType fromInt(int value)
	{
		switch (value)
		{
			case 0:  return FECLASS;
			default: return JAVACLASS;
		}
	}
}
