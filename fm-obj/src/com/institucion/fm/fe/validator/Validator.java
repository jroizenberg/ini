package com.institucion.fm.fe.validator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.institucion.fm.fe.validator.ValidationException;
import com.institucion.fm.fe.validator.dao.ValidationDAO;

public class Validator implements Serializable{
	private static Log log = LogFactory.getLog(Validator.class);
	
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private Set<ValidatorValidation> validatorValidations;
	private List<Validation> validations;

	public Validator() { }

	public Long getId() { return id; }
	@SuppressWarnings("unused")
	private void setId(Long id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public void setValidatorValidations(Set<ValidatorValidation> validatorValidations){
		this.validatorValidations = validatorValidations;

		validations = new ArrayList<Validation>();
		Iterator<ValidatorValidation> itValVal = validatorValidations.iterator();
		while (itValVal.hasNext()){
			ValidatorValidation valval = itValVal.next();
			Validation validation = ValidationDAO.instance().findByName(valval.getValidationName());
			validations.add(validation);
		}
	}

	@SuppressWarnings("unused")
	private Set<ValidatorValidation> getValidatorValidations(){
		return validatorValidations;
	}

	public List<Validation> getValidations(){
		return validations;
	}

	public void validate(Object value) throws ValidationException{
		boolean hasException = false;
		ValidationException valex = new ValidationException();

		for (int i = 0; i < validations.size(); i++){
			Validation validation = validations.get(i);
			try{
				validation.validate(value);
			}catch (ValidationException e){
				log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
				hasException = true;
				valex.addValidationException(e);
			}
		}

		if (hasException)
			throw valex;
	}

	public boolean equals(Object object){
		return id.intValue() == ((Validator) object).getId().intValue();
	}

	/**
	 * Itera los validadores para ver si encuentra uno. Lo mejor es pasarle un
	 * objeto class como NullValidation.class. 
	 */
	public boolean exist(Object value){
		for (int i = 0; i < validations.size(); i++){
			Validation validation = validations.get(i);
			if (validation.equals(value))
				return true;
		}
		return false;
	}

	public String toString(){
		return "id="+id+" name="+name+" "+super.toString();
	}
}