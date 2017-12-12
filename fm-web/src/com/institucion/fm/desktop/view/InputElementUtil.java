package com.institucion.fm.desktop.view;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Textbox;

public class InputElementUtil
{
	public static void cleanWithConstraint(Component cmp)
	{
		if (cmp instanceof Textbox)
		{
			Textbox textbox = (Textbox) cmp;
			Constraint currentConstraint = textbox.getConstraint();
			textbox.setConstraint("");
			textbox.setText(null);
			textbox.setConstraint(currentConstraint);
		}
		else
			throw new IllegalArgumentException("El componente "+cmp.getClass()+" no se puede conoce");
	}
}