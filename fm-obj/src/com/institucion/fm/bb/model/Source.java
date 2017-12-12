package com.institucion.fm.bb.model;

import java.util.Locale;

import com.institucion.fm.resource.I18NServer;

public enum Source {	
	WORKFLOW(0),CYCLE(1),APROVED(2),REJECTED(3), MESSAGEABLE(4);

	private int id;

	private Source(int id) { this.id = id; }
	public int toInt() { return id; }

	public static Source fromInt(int value) {
		switch(value) {
			case 0: return WORKFLOW;
			case 1: return CYCLE;
			case 2: return APROVED;
			case 3: return REJECTED;
			case 4: return MESSAGEABLE;
//			default:
//				return WORKFLOW;
			default:
				return null;
		}
	}

	/**
	 * Este metodo solo debe ejecutarse del lado servidor.
	 */
	public String getI18N(Locale locale) {
		switch (this) {
			case WORKFLOW: return I18NServer.getLabel(locale, "source.workflow");
			case CYCLE:	return I18NServer.getLabel(locale, "source.cycle");
			case APROVED:return I18NServer.getLabel(locale, "source.aproved");
			case REJECTED:return I18NServer.getLabel(locale, "source.rejected");
			case MESSAGEABLE: return I18NServer.getLabel(locale, "source.messageable");
			default: throw new java.lang.IllegalArgumentException(this+" no contemplado");
		}
	}
}