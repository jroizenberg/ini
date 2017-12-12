package com.institucion.fm.wf.model;

public enum ApprovalType
{
	SSFF(0), WFMANAGER(1);

	private int id;

	private ApprovalType(int id) { this.id = id; }
	public int toInt() { return id; }
 
	public static ApprovalType fromInt(int value)
	{    
		switch(value)
		{
			case 0: return SSFF;
			default: return WFMANAGER;
		}
	}
}