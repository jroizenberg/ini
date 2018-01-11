package com.institucion.fm.ds;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * La clase Row representa una fila en una tabla.
 *
 * @version 1.0, 27/03/2009
 * @since   1.0, 27/03/2009
 */
public class Row implements Serializable {
	private static final long serialVersionUID = 1L;
	private Object[] fields;

	public Row() { }

	public Row(Object[] fields)
	{
		this.fields = fields;
	}

	public Row(int columns)
	{
		fields = new Object[columns];
	}

	public void setColumns(int columns)
	{
		fields = new Object[columns];
	}

	public int length()
	{
		return fields.length;
	}

	public void setField(int column, Object value)
	{
		fields[column] = value;
	}

	public Object getField(int column)
	{
		return fields[column];
	}

	public String getString(int column)
	{
		return (String) fields[column];
	}

	public Integer getInteger(int column)
	{
		return (Integer) fields[column];
	}

	public BigDecimal getBigDecimal(int column)
	{
		return (BigDecimal) fields[column];
	}

	public String toString()
	{
		StringBuffer rowstr = new StringBuffer();
		for (int i = 0; i < fields.length; i++)
			rowstr.append(fields[i]).append(" "); 
		return rowstr.toString();
	}
}