package com.institucion.fm.desktop.validator;

import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;

import com.institucion.fm.desktop.service.I18N;

public class DateBeginEndConstraint implements Constraint
{
	private Datebox begin;
	private Datebox end;
	private Datebox close;
	
	
	public DateBeginEndConstraint(Datebox beginDate, Datebox endDate, Datebox closeDate)
	{
		this.begin = beginDate;
		this.end = endDate;
		this.close = closeDate;
	}
	
	@Override	
	public void validate(Component comp, Object value) throws WrongValueException
	{
		comp.setAttribute("dt", value);
		if (end != null && begin != null && close != null) {
			
			Date beginValue = null;
			if (comp == begin) {
				beginValue = (Date) value;
			} else {
				beginValue = (Date) begin.getAttribute("dt");
			}

			Date endValue = null;
			if (comp == end) {
				endValue = (Date) value;
			} else {					
				endValue = (Date)end.getAttribute("dt");
			}
			
			Date closeValue = null;
			if (comp == close) {
				closeValue = (Date) value;
			} else {
				closeValue = (Date) close.getAttribute("dt");
			}

			if (value == null){
				throw new WrongValueException(comp, I18N.getLabel("error.empty.field"));
			}
			
			if (beginValue != null && endValue != null && closeValue != null) {
				if (endValue.before(beginValue) || closeValue.before(endValue)) {
					throw new WrongValueException(comp, I18N.getLabel("error.invalid.date"));
				}
				begin.clearErrorMessage();
				end.clearErrorMessage();
				close.clearErrorMessage();
			}
		}
	}
}
