package com.institucion.fm.desktop.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zul.Messagebox;

import com.institucion.fm.desktop.service.I18N;

public class MessageBox
{
	
	private static Log log = LogFactory.getLog(MessageBox.class);
	/**
	 * Retorna verdadero si el usuario acepta la pregunta
	 */
	public static boolean question(String message, String title)
	{
		try
		{
			return Messagebox.show(message, title, Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES;
		}
		catch (InterruptedException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}
		return false;
	}

	public static void info(String message, String title)
	{
		try
		{
			Messagebox.show(message, title, Messagebox.OK, Messagebox.INFORMATION);
		}
		catch (InterruptedException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}
	}

	public static void exclamation(String message, String title)
	{
		try
		{
			Messagebox.show(message, title, Messagebox.OK, Messagebox.EXCLAMATION);
		}
		catch (InterruptedException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}
	}

	public static void validation(String message)
	{
		validation(message, I18N.getLabel("validation.error"));
	}

	public static void validation(String message, String title)
	{
		try
		{
			Messagebox.show(message, title, Messagebox.OK, Messagebox.ERROR);
		}
		catch (InterruptedException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}
	}
}