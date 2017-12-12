package com.institucion.fm.conf.exception;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.EJBException;

import com.institucion.fm.conf.exception.LocalizeValidationException;
import com.institucion.fm.conf.exception.LpcChecktionException;
import com.institucion.fm.conf.exception.WorkFlowException;
import com.institucion.fm.conf.exception.dao.CheckConstraintException;
import com.institucion.fm.conf.exception.dao.ForeignKeyException;
import com.institucion.fm.conf.exception.dao.NotNullConstraintException;
import com.institucion.fm.conf.exception.dao.UniqueConstraintException;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.fe.validator.ValidationException;

public class WebExceptionHandler //extends ExceptionHandlerSpringAware
{

	// workaround dado que no puedo poner un interceptor spring en el composer...
	public static void handleThis(EJBException ex) {
		Throwable t = ex.getCause();
		if (t == null) {
			throw ex;
		}
		if (t instanceof CheckConstraintException) {
			MessageBox.validation(I18N.getLabel("check.constraint.problem.message"), I18N.getLabel("check.constraint.problem.error")); 
		} else if (t instanceof ForeignKeyException) {
			MessageBox.validation(I18N.getLabel("foreignkey.problem.message"), I18N.getLabel("foreignkey.problem.error")); 
		} else if (t instanceof UniqueConstraintException) {
			MessageBox.validation(I18N.getLabel("duplicate.message"), I18N.getLabel("duplicate.error"));
		} else if(t instanceof NotNullConstraintException){
			MessageBox.validation(I18N.getLabel("check.constraint.problem.message"), I18N.getLabel("check.constraint.problem.error"));
		} else if (t instanceof ValidationException) {
			ValidationException newEx = (ValidationException) ex.getCause();
			//TODO bug 308
			MessageBox.validation(getMessage(newEx));
		} else if (t instanceof WorkFlowException) {
			MessageBox.validation(I18N.getLabel(((WorkFlowException) t).getMessage()), I18N.getLabel("wf.title"));
		}else if(t instanceof LocalizeValidationException){
			LocalizeValidationException pvex = (LocalizeValidationException)t;
			MessageBox.validation(I18N.getLabel(pvex.getErrorMessage(),pvex.getParams()));
		}else if(t instanceof LpcChecktionException){
			LpcChecktionException newExc = (LpcChecktionException)t;
			LocalizeValidationException[] locExc= newExc.getParams();
			StringBuffer errorLabel = new StringBuffer();
			for (int i = 0; i < locExc.length; i++) {
				errorLabel.append(I18N.getLabel("crud.cycle.validate.promotionline",locExc[i].getParams()));
				
			}

			MessageBox.validation(errorLabel.toString());
		}
		else {
			//log.debug("La excepcion se envia para arriba: " + ex);
			throw ex;
		}
	}

	public static void handleThis(ValidationException ex) {
		MessageBox.validation(getMessage(ex));
	}

	private static String getMessage(ValidationException ex)
	{
		StringBuffer message = new StringBuffer();

		Iterator<Entry<Object,List<String>>> it = ex.getValidatorMessages().entrySet().iterator();
		while (it.hasNext())
		{
			Entry<Object,List<String>> entry = it.next();
			Object field = entry.getKey();
			String strfield = "";
//			if (field instanceof FEField)
//			{
//				int lang = I18N.getLanguage();
//				FEFieldView view = ((FEField) field).getView();
//				strfield = view.getDescription(lang);
//			}
//			else 
				if (field instanceof String)
			{
				strfield = I18N.getStringLabel((String) field);
			}
			else
				throw new RuntimeException("ValidatorException no entiende el field de tipo '"+field.getClass()+"'");

			message.append(strfield+": ");
			List<String> messages = entry.getValue();
			for (int i = 0; i < messages.size(); i++)
				message.append('\t'+I18N.getStringLabel(messages.get(i))+'\n');

			if (it.hasNext())
				message.append(" | ");
		}

		// Raro no? pero funca bien y tiene su logica, ya vas a ver...
		if (message.length() == 0 && ex.getTemporalMessages() != null)
		{
			Iterator<String> itTmp = ex.getTemporalMessages().iterator();
			while (itTmp.hasNext())
			{
				String tmpMessage = itTmp.next();
				message.append(I18N.getStringLabel(tmpMessage));
				if (it.hasNext())
					message.append(" | ");
			}
		}

		return message.toString();
	}


}