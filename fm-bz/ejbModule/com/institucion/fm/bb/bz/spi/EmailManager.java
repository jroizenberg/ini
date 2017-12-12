package com.institucion.fm.bb.bz.spi;

import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.institucion.fm.bb.dao.EmailDAO;
import com.institucion.fm.bb.model.Email;
import com.institucion.fm.conf.exception.DAOException;

@Stateless
public class EmailManager
{
	static Log log = LogFactory.getLog(EmailManager.class);
	
	@Autowired
	private EmailDAO daoEmail;
	private Session sessionMail;

	public void sendAllEMails() throws DAOException
	{
		List<Email> mailList = daoEmail.findByCriteria("from com.institucion.fm.bb.model.Email e");
		if (mailList.isEmpty())
			return;

		for (int i = 0; i < mailList.size(); i++)
		{
			Email email = mailList.get(i);
			if (sendEmail(email))
				daoEmail.delete(email);
		}
	}

	public boolean sendEmail(Email email)
	{
		InitialContext context;
		try
		{
			context = new InitialContext();
			sessionMail = (Session) context.lookup("java:Mail");
			
			MimeMessage message = new MimeMessage(sessionMail);
			message.setSubject(email.getSubject());
			message.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(email.getTo(), false));
			message.setText(email.getBody());
			Transport.send(message);
			return true;
		}
		catch (NamingException e)
		{
			//log.debug("Mensaje: " + e.getMessage() + "Causa: " + e.getCause());
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}
		catch (MessagingException e)
		{
			//log.debug("Mensaje: " + e.getMessage() + "Causa: " + e.getCause());
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}
		return false;
	}
	
	public boolean sendEmailWithAttachment(Email email, ArrayList<String> attachmentNames)
	{
		InitialContext context;
		try
		{
			context = new InitialContext();
			sessionMail = (Session) context.lookup("java:Mail");
			
			
			
			MimeMessage message = new MimeMessage(sessionMail);
			message.setSubject(email.getSubject());
			message.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(email.getTo(), false));
			message.setText(email.getBody());
			for (String attachFile : attachmentNames){
				FileDataSource source = new FileDataSource(attachFile);
				DataHandler handler = new DataHandler(source);message.setDataHandler(handler);
				message.setFileName(attachFile);
			}
			
			Transport.send(message);
			return true;
		}
		catch (NamingException e)
		{
			//log.debug("Mensaje: " + e.getMessage() + "Causa: " + e.getCause());
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}
		catch (MessagingException e)
		{
			//log.debug("Mensaje: " + e.getMessage() + "Causa: " + e.getCause());
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}
		return false;
	}
}