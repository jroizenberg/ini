package com.institucion.fm.desktop.view;

import java.util.Calendar;
import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Timebox;

import com.institucion.fm.desktop.service.I18N;

public class DateTimeBox extends Panel {

	private static final long serialVersionUID = 1L;

	private Datebox datebox;
	private Timebox timebox;

	public DateTimeBox() {
		this.setWidth("auto");
		this.initPanel();
	}

	public Date mergeValue(final Component comp, final Object value) {
		Date date = null;

		if (comp instanceof Timebox) {
			Constraint ct = this.datebox.getConstraint();
			Constraint ct2 = this.timebox.getConstraint();
			this.datebox.setConstraint("");
			this.timebox.setConstraint("");
			
			if (comp.getId().equals(this.timebox.getId())) {
				date = this.joinDateTime(this.datebox.getValue(), (Date) value);
			} else {
				date = this.getDateTime();
			}
			this.datebox.setConstraint(ct);
			this.timebox.setConstraint(ct2);
		}

		if (comp instanceof Datebox) {
			Constraint ct = this.datebox.getConstraint();
			Constraint ct2 = this.timebox.getConstraint();
			this.datebox.setConstraint("");
			this.timebox.setConstraint("");
			if (comp.getId().equals(this.datebox.getId())) {
				date = this.joinDateTime((Date) value, this.timebox.getValue());
			} else {
				date = this.getDateTime();
			}
			this.datebox.setConstraint(ct);
			this.timebox.setConstraint(ct2);
		}

		return date;
	}

	private void initPanel() {
		Panelchildren panelchildren = new Panelchildren();
		this.appendChild(panelchildren);

		datebox = new Datebox();
		datebox.setFormat(I18N.getDateFormat());
		timebox = new Timebox();
		Hbox hbox = new Hbox();
		hbox.appendChild(timebox);
		hbox.appendChild(datebox);
		panelchildren.appendChild(hbox);
	}

	public void setDateTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		datebox.setValue(cal.getTime());
		timebox.setValue(cal.getTime());
	}

	public Date getDateTime() {
		return this.joinDateTime(this.datebox.getValue(), this.timebox.getValue());
	}

	public Datebox getDatebox() {
		return datebox;
	}

	public Timebox getTimebox() {
		return timebox;
	}

	public Date joinDateTime(Date onlyDate, Date onlyHour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(onlyDate);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(onlyHour);
		Calendar res = Calendar.getInstance();
		res.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		res.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		res.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		res.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
		res.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
		res.set(Calendar.SECOND, cal2.get(Calendar.SECOND));

		return res.getTime();
	}

	public void setConstraint(Constraint constraint) {
		datebox.setConstraint(constraint);
		timebox.setConstraint(constraint);
	}

	public void setConstraint(String constraint) {
		datebox.setConstraint(constraint);
		timebox.setConstraint(constraint);
	}

	public boolean addEventListener(String event, EventListener listener) {

		return datebox.addEventListener(event, listener)
				&& timebox.addEventListener(event, listener);

	}

	public void clearErrorMessage() {
		datebox.clearErrorMessage();
		timebox.clearErrorMessage();
	}

	public void setId(String id) {
		
		datebox.setId(id);
		timebox.setId(id);
	}

}
