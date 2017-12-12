package com.institucion.fm.desktop.validator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Constraint;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.util.RegularExpressions;

public class TextConstraint implements Constraint, RegularExpressions {
	public static final int NO_EMPTY = 0x0100;
	public static final int NO_EMPTY_W_SP = 2;
//	public static final String VALID_TEXT_EXPRESSION = "^[_\\-\\.'`������������a-zA-Z0-9 ]+$";
	private int flags;
	private String regex;
	
	public TextConstraint() {
		this(0);
	}
	
	public TextConstraint(int flags) {
		this(flags, VALID_TEXT_BASIC_EXPRESSION);
	}

	public TextConstraint(String regex) {
		this(0, regex);
	}

	public TextConstraint(int flags, String regex) {
		this.flags = flags;
		this.regex = regex;
		//log.debug("flags: " + this.flags);
	}
	
	protected String getInvalidRegExText() {
		return I18N.getLabel("error.invalid.chars");
	}
	
	public void validate(Component comp, Object value) throws WrongValueException {
		//log.debug("flags: " + this.flags);
	
		switch (this.flags ) {
		case NO_EMPTY:
			if (value == null || "".equals(value.toString().trim())) {
				throw new WrongValueException(comp, I18N.getLabel("error.empty.field"));			
			}
			break;
		case NO_EMPTY_W_SP:
			if (value == null || "".equals(value.toString())) {
				throw new WrongValueException(comp, I18N.getLabel("error.empty.field"));			
			}
			break;
	
		default:
			break;
		}
		
		/*if ((this.flags & NO_EMPTY) != 0) {
			//log.debug("es empty?");
			if (value == null || "".equals(value.toString().trim())) {
				throw new WrongValueException(comp, I18N.getLabel("error.empty.field"));			
			}
		}
		
		if ((this.flags & NO_EMPTY_W_SP) != 0) {
			//log.debug("es empty?");
			if (value == null || "".equals(value.toString())) {
				throw new WrongValueException(comp, I18N.getLabel("error.empty.field"));			
			}
		}*/
		
		if (value == null || "".equals(value.toString())) {
			//log.debug("no debo chequear nulidad pero es nulo");
			return;
		}
		//log.debug("{" + this.regex + "}{" + value.toString() + "}");
		if (!value.toString().matches(this.regex)) {
			throw new WrongValueException(comp, this.getInvalidRegExText());
		}
	}
}
