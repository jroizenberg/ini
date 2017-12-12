package com.institucion.fm.security.bz.spi;

import com.institucion.fm.conf.exception.LocalizeValidationException;
import com.institucion.fm.security.model.User;
/**
 * 	This bean, validate a password of a user when it is 
 *  created or changed and when he changed his password,
 *  the validations are: 
 *	The password must not be empty 
 *	The password must be greater than or equal to 8 characters 
 *	The password must be different from the 3 last entered.
 * */
public class PasswordValidation {
	private String minLength = new String();

	public void validatePass(String pass, User user){
		
		if ("".equals(pass.trim())){
			throw new LocalizeValidationException("error.invalid.password.empty");
		}
 		if (pass.length()<Integer.valueOf(getMinLength()).intValue()){
 			Object [] param ={getMinLength()};
			throw new LocalizeValidationException("error.invalid.password.length",param);
		}
 		
 		if(user.isValidatePass()){
 			if (user.getUserSecurity().getOldPassword1()!=null 
 					&& user.getUserSecurity().getOldPassword1().equals(user.getPassword())){
 				throw new LocalizeValidationException("error.invalid.password.repeat");
 			}
 			if (user.getUserSecurity().getOldPassword2()!=null 
 					&& user.getUserSecurity().getOldPassword2().equals(user.getPassword())){
 				throw new LocalizeValidationException("error.invalid.password.repeat");
 			}
 			if (user.getUserSecurity().getOldPassword3()!=null 
 					&& user.getUserSecurity().getOldPassword3().equals(user.getPassword())){
 				throw new LocalizeValidationException("error.invalid.password.repeat");
 			}
 		}
	}
		
	
	public String getMinLength(){
		return minLength;
	}
	public void setMinLength(String min){
		minLength=min;
	}
}
