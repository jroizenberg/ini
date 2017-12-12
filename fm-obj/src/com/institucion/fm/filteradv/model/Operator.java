package com.institucion.fm.filteradv.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.institucion.fm.filteradv.model.Predicate.Type;

/**
 * El operador de la expresion SQL. 
 */
public class Operator implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String name;
	private String text; // string que ve el usuario cuando elige un operador
	private String pattern; // patron para formar la expresion
	private List<Type> types = new ArrayList<Type>();

	public Operator() {}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getText() { return text; }
	public void setText(String text) { this.text = text; }

	public String getPattern() { return pattern; }
	public void setPattern(String pattern) { this.pattern = pattern; }

	public List<Type> getTypes() { return types; }
	public void setTypes(List<Type> types) { this.types = types; }

	/** Usado por el digester. */
	public void addType(String type)
	{
		types.add(Type.valueOf(type));
	}

	/**
	 * Un operador simple tiene una sola expresion (EXP). Un operador compuesto
	 * tiene dos expresiones (EXP1 y EXP2). 
	 */
	public boolean isSimple()
	{
		return pattern.indexOf("EXP1") < 0;
	}
}