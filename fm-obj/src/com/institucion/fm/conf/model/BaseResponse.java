package com.institucion.fm.conf.model;

import java.io.Serializable;

import javax.ejb.EJBException;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.conf.exception.LocalizeValidationException;
import com.institucion.fm.conf.exception.ParseException;
import com.institucion.fm.conf.exception.WorkFlowException;
import com.institucion.fm.conf.exception.dao.ForeignKeyException;
import com.institucion.fm.conf.exception.dao.UniqueConstraintException;
import com.institucion.fm.fe.validator.ValidationException;

public abstract class BaseResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private StatusType status = StatusType.OK;

	private WSError wsError;

	public StatusType getStatus() {
		return status;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}

	public WSError getWsError() {
		return wsError;
	}

	public void setWsError(WSError wsError) {
		this.setStatus(StatusType.ERROR);
		this.wsError = wsError;
	}
	
	public void setError(WSError error,Throwable throwable) {
		this.setStatus(StatusType.ERROR);
		
		
		if ("error.portfolio.not.active".equals(throwable.getMessage())) {
			error.setErrorKey("error.portfolio.not.active");
			this.wsError = error;
		} else
		if ("error.client.not.active".equals(throwable.getMessage())) {
			error.setErrorKey("error.client.not.active");
			this.wsError = error;
		} else
		if ("error.invalid.portfolio.client.type".equals(throwable.getMessage())) {
			error.setErrorKey("error.invalid.portfolio.client.type");
			this.wsError = error;
		} else
		if ("error.portfolio.exist".equals(throwable.getMessage())) {
			error.setErrorKey("error.portfolio.exist");
			this.wsError = error;
		} else
		if ("error.cannot.use.this.promotionline".equals(throwable.getMessage())) {
			error.setErrorKey("error.cannot.use.this.promotionline");
			error.setErrorValues(null);
			this.wsError = error;
		} else
		if ("error.not.user.wf".equals(throwable.getMessage())) {
			error.setErrorKey("error.not.user.wf");
			error.setErrorValues(null);
			this.wsError = error;
		} else
		if ("error.not.hpa.user".equals(throwable.getMessage())) {
			error.setErrorKey("error.not.hpa.user");
			error.setErrorValues(null);
			this.wsError = error;
		} else
		if ("error.user.not.have.superior".equals(throwable.getMessage())) {
			error.setErrorKey("error.user.not.have.superior");
			error.setErrorValues(null);
			this.wsError = error;
		} else
		if ("error.invalid.id.promotion.line".equals(throwable.getMessage())) {
			error.setErrorKey("error.invalid.id.promotion.line");
			error.setErrorValues(null);
			this.wsError = error;
		} else
		if ("error.user.must.change.password".equals(throwable.getMessage())) {
			error.setErrorKey("error.user.must.change.password");
			error.setErrorValues(null);
			this.wsError = error;
		} else
		if ("error.invalid.state.entity".equals(throwable.getMessage())) {
			error.setErrorKey("error.invalid.state.entity");
			this.wsError = error;
		} else
		if ("error.no.entity.to.modify".equals(throwable.getMessage())) {
				error.setErrorKey("error.no.entity.to.modify");
				this.wsError = error;
			} else
		if ("error.schedule.invalid.days".equals(throwable.getMessage())) {
			error.setErrorKey("error.schedule.invalid.days");
			this.wsError = error;
		} else
		if ("error.schedule.invalid.hour.range".equals(throwable.getMessage())) {
			error.setErrorKey("error.schedule.invalid.hour.range");
			this.wsError = error;
		} else
		if ("error.schedule.invalid.hour".equals(throwable.getMessage())) {
			error.setErrorKey("error.schedule.invalid.hour");
			this.wsError = error;
		} else
		if ("error.invalid.type.task".equals(throwable.getMessage())) {
			error.setErrorKey("error.invalid.type.task");
			this.wsError = error;
		} else
		if ("error.invalid.id.task".equals(throwable.getMessage())) {
			error.setErrorKey("error.invalid.id.task");
			this.wsError = error;
		}else if ("error.value.required.missing".equals(throwable.getMessage())) {
			error.setErrorKey("error.value.required.missing");
			this.wsError = error;
			
		}
		if ("error.hpa.pending.process".equals(throwable.getMessage())){
			error.setErrorKey("error.hpa.pending.process");
			this.wsError = error;
		}
		
		else
		if(throwable instanceof ValidationException){
//			error.setErrorKey(((ValidationException) throwable).getTemporalMessages().get(0));
			ValidationException e = (ValidationException)throwable;
			if (e.getTemporalMessages() != null) {
				error.setErrorKey(e.getTemporalMessages().get(0));
			} else {
				error.setErrorKey("error.in.validation");
				error.setErrorValues(e.getValidationMessageAsArray());
			}
			this.wsError = error;
		}else
		if(throwable instanceof EJBException){
			Throwable cause = throwable.getCause();
			error.setErrorValues(null);
			if(cause instanceof ValidationException){
				ValidationException e = (ValidationException)cause;
				if (e.getTemporalMessages() != null) {
					error.setErrorKey(e.getTemporalMessages().get(0));
				} else {
					error.setErrorKey("error.in.validation");
					error.setErrorValues(e.getValidationMessageAsArray());
				}
				this.wsError = error;
			}else
			if(cause instanceof LocalizeValidationException){
				error.setErrorKey(((LocalizeValidationException) cause).getErrorMessage());
				this.wsError = error;
			}else
			if(cause instanceof DAOException){
				error.setErrorKey("error.invalid.access.DDBB");
				this.wsError = error;
			}else
			if(cause instanceof ForeignKeyException){
				error.setErrorKey("error.has.an.associate.key");
				this.wsError = error;
			}else
			if(cause instanceof WorkFlowException){
				String message = cause.getMessage();
				if ("wf.notwfuser".equals(message)) {
					error.setErrorKey("error.not.user.wf");
					this.wsError = error;
				} else
				if ("wf.notwfmanageruser".equals(message)) {
					error.setErrorKey("error.not.wf.manager.user");
					this.wsError = error;
				}
				this.wsError = error;
			}
			if(cause instanceof NullPointerException){
				error.setErrorKey("java.lang.NullPointerException");
				this.wsError = error;
			}
		}else
		if(throwable instanceof LocalizeValidationException){
			error.setErrorKey(((LocalizeValidationException) throwable).getErrorMessage());
			this.wsError = error;
		}else
		if ("com.institucion.fm.conf.exception.DAOException: DAO Exception".equals(throwable.getMessage())) {
			error.setErrorKey("error.invalid.access.DDBB");
			this.wsError = error;
		} else
		if ("error.user.isnt.in.role".equals(throwable.getMessage())) {
			error.setErrorKey("error.user.isnt.in.role");
			error.setErrorValues(null);
			this.wsError = error;
		} else
		if ("error.user.isnt.active".equals(throwable.getMessage())) {
			error.setErrorKey("error.user.isnt.active");
			error.setErrorValues(null);
			this.wsError = error;
		} else
		if ("com.institucion.fm.conf.exception.WorkFlowException: wf.notwfuser".equals(throwable.getMessage())) {
			error.setErrorKey("error.not.user.wf");
			this.wsError = error;
		} else
		if ("com.institucion.fm.conf.exception.WorkFlowException: wf.notwfmanageruser".equals(throwable.getMessage())) {
			error.setErrorKey("error.not.wf.manager.user");
			this.wsError = error;
		} else	
		if ("error.value.required.missing".equals(throwable.getMessage())) {
			error.setErrorKey("error.value.required.missing");
			this.wsError = error;
		}else
			if ("error.contact.not.created".equals(throwable.getMessage())) {
				error.setErrorKey("error.contact.not.created");
				this.wsError = error;
		}else
		if ("error.contact.not.active".equals(throwable.getMessage())) {
				error.setErrorKey("error.contact.not.active");
				this.wsError = error;
		}else
		if ("error.invalid.field.key".equals(throwable.getMessage())) {
			error.setErrorKey("error.invalid.field.key");
			this.wsError = error;
		}else
		if ("error.invalid.caracter".equals(throwable.getMessage())) {
			error.setErrorKey("error.invalid.caracter");
			this.wsError = error;
		}else
		if ("error.value.too.long".equals(throwable.getMessage())) {
			error.setErrorKey("error.value.too.long");
			this.wsError = error;
		}else
		if ("error.invalid.id.DT".equals(throwable.getMessage())) {
			error.setErrorKey("error.invalid.id.DT");
			this.wsError = error;
		}else
		if ("error.invalid.motive".equals(throwable.getMessage())) {
				error.setErrorKey("error.invalid.motive");
				this.wsError = error;
		}else
		if ("error.invalid.boolean.value".equals(throwable.getMessage())) {
				error.setErrorKey("error.invalid.boolean.value");
				this.wsError = error;
		}else
		if ("error.no.entity.to.create".equals(throwable.getMessage())) {
			error.setErrorKey("error.no.entity.to.create");
			this.wsError = error;
		}else
		if ("error.no.entity.to.transfer".equals(throwable.getMessage())) {
				error.setErrorKey("error.no.entity.to.transfer");
				this.wsError = error;
			}else
		if ("error.no.entity.to.update".equals(throwable.getMessage())) {
			error.setErrorKey("error.no.entity.to.update");
			this.wsError = error;
		}else
		if ("error.no.entity.to.copy".equals(throwable.getMessage())) {
				error.setErrorKey("error.no.entity.to.copy");
				this.wsError = error;
		}else
		if ("error.invalid.id.pharmacy".equals(throwable.getMessage())) {
			error.setErrorKey("error.invalid.id.pharmacy");
			this.wsError = error;
		}else
		if ("error.invalid.id.institution".equals(throwable.getMessage())) {
			error.setErrorKey("error.invalid.id.institution");
			this.wsError = error;
		}else
		if ("error.invalid.id.healthProfessional".equals(throwable.getMessage())) {
			error.setErrorKey("error.invalid.id.healthProfessional");
			this.wsError = error;
		}else
		if ("error.invalid.id.portfolio".equals(throwable.getMessage())) {
			error.setErrorKey("error.invalid.id.portfolio");
			this.wsError = error;
		}else
		if (throwable instanceof ParseException) {
			error.setErrorKey("error.invalid.value.datetime");
			error.setErrorValues(new String [] {throwable.getMessage()});
			this.wsError = error;
		}else
		if ("error.invalid.typeTask".equals(throwable.getMessage())) {
			error.setErrorKey("error.invalid.typeTask");
			this.wsError = error;
		}else
		if ("error.invalid.id.client".equals(throwable.getMessage())){
				error.setErrorKey("error.invalid.id.client");
				this.wsError = error;
		}else
		if ("error.invalid.id.contact".equals(throwable.getMessage())) {
				error.setErrorKey("error.invalid.id.contact");
				this.wsError = error;
		}else
		if ("error.invalid.id.promotionzone".equals(throwable.getMessage())){
			error.setErrorKey("error.invalid.id.promotionzone");
			this.wsError = error;
		}else
		if ("error.invalid.id.address".equals(throwable.getMessage())){
				error.setErrorKey("error.invalid.id.address");
				this.wsError = error;
		}else
		if ("error.invalid.id.brand".equals(throwable.getMessage())){
				error.setErrorKey("error.invalid.id.brand");
				this.wsError = error;
		}else
		if ("error.invalid.id.frequency".equals(throwable.getMessage())){
				error.setErrorKey("error.invalid.id.frequency");
				this.wsError = error;
		}else
		if ("error.invalid.id.categorization".equals(throwable.getMessage())){
				error.setErrorKey("error.invalid.id.categorization");
				this.wsError = error;
		}else
		if(throwable.getCause() instanceof UniqueConstraintException){
			error.setErrorKey("error.appointment.asigned");
			this.wsError = error;
		}else
		if(throwable.getCause() instanceof UniqueConstraintException){
			error.setErrorKey("error.appointment.asigned");
			this.wsError = error;
		}else{
			error.setErrorKey("error.unspected");
			error.setErrorValues(new String [] {throwable.getMessage()});
			this.wsError = error;
		}
		
	}
	
	public void setError(WSError error,Throwable throwable, String[] values) {
		if(values != null){
			error.setErrorValues(values);
		}
		setError(error, throwable);
	}
	
}
