/**
 * 
 */
package com.institucion.controller;

import java.util.Map;
import java.util.TreeMap;

/**
 * The Class RowModel.
 *
 * @author mdenipotti
 */
public class RowModel {

	/** The values. */
	private Map<String, String> values;

	/**
	 * Instantiates a new row model.
	 */
	public RowModel() {
		this.values = new TreeMap<String, String>();
	}
	
	/**
	 * Gets the.
	 *
	 * @param fieldName the field name
	 * @return the string
	 */
	public String get(String fieldName) {
		return this.values.get(fieldName);
	}

	/**
	 * Sets the values.
	 *
	 * @param values the values to set
	 */
	public void set(String fieldName, String value) {
		this.values.put(fieldName, value);
	}

	/**
	 * @return the values
	 */
	public Map<String, String> getValues() {
		return values;
	}

}