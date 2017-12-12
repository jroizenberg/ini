package com.institucion.fm.security.controller;

import javax.ejb.EJBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Textbox;

import com.institucion.fm.conf.CallContextHelper;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.InputElementUtil;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.security.bz.SecurityEJB;
import com.institucion.fm.security.model.User;
import com.institucion.fm.security.service.AuthorizationService;
import com.institucion.fm.util.Cipher;
import com.institucion.fm.util.RegularExpressions;

public class PasswordCrudComposer extends CrudComposer
{
	private static Log log = LogFactory.getLog(PasswordCrudComposer.class);
	
	private SecurityEJB securityEjb = null;
	private Textbox currentPassword;
	private Textbox enterPassword;
	private Textbox reenterPassword;
//	private PanelCrud panelCrud;

	private SecurityEJB getService()
	{
		if (this.securityEjb == null) {
			this.securityEjb = BeanFactory.<SecurityEJB>getObject("fm.ejb.securityService");
		}
		return this.securityEjb;
	}

	public void onSave(Event event) throws Exception
	{
		User user = getService().getUserByName(CallContextHelper.getCallContext(), Session.getUsername());
//		String cipherPassword = Cipher.encrypt(this.getPasswordCrud().getCurrentPassword().getText());
		String cipherPassword = Cipher.encrypt(currentPassword.getText());
		if (user.getPassword().equals(cipherPassword))
		{
//			String enterPass = this.getPasswordCrud().getEnterPassword().getText();
//			String reenterPass =this.getPasswordCrud().getReenterPassword().getText();
			enterPassword.setConstraint(new TextConstraint(TextConstraint.NO_EMPTY, RegularExpressions.SIMPLE_VALID_TEXT_EXPRESSION));
			reenterPassword.setConstraint(new TextConstraint(TextConstraint.NO_EMPTY, RegularExpressions.SIMPLE_VALID_TEXT_EXPRESSION));
			String enterPass = enterPassword.getText();
			String reenterPass =reenterPassword.getText();
			
			if (enterPass.equals(reenterPass))
			{
				try{
					// this function is now on SecurityEJBImpl.saverUser()
					//user.setPassword(Cipher.encrypt(enterPass));
					user.getUserSecurity().setFirstLogin(true);
					getService().saveUser(CallContextHelper.getCallContext(), user,enterPass);
					MessageBox.info(I18N.getLabel("crud.password.passwordchanged"), I18N.getLabel("module.password"));
	
//					InputElementUtil.cleanWithConstraint(this.getPasswordCrud().getCurrentPassword());
//					InputElementUtil.cleanWithConstraint(this.getPasswordCrud().getEnterPassword());
//					InputElementUtil.cleanWithConstraint(this.getPasswordCrud().getReenterPassword());
					
					InputElementUtil.cleanWithConstraint(currentPassword);
					InputElementUtil.cleanWithConstraint(enterPassword);
					InputElementUtil.cleanWithConstraint(reenterPassword);
	
					refreshAcegiUsers();
					desktop.getExecution().sendRedirect("/logout.zul");
					
				}
				catch (EJBException ex) {
					log.error("Mensaje: " + ex.getMessage() + "StackTrace: " + ex.getStackTrace());
					WebExceptionHandler.handleThis(ex);
				}
			}
			else
				MessageBox.validation(I18N.getLabel("crud.password.newpasswordnotequal"));
		}
		else
			MessageBox.validation(I18N.getLabel("crud.password.badpassword"));
	}

	
	public void onCreate()
	{
//		this.getPasswordCrud().getCurrentPassword().setFocus(true);
		currentPassword.setFocus(true);
	}

	private void refreshAcegiUsers()
	{
		AuthorizationService authService = BeanFactory.<AuthorizationService>getObject("userDetailsService");
		authService.refresh();
	}
//	private PasswordGridCrud getPasswordCrud()
//	{
//		
//		return (PasswordGridCrud)(panelCrud.getGridCrud());
//	}
}