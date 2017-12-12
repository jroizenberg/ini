package com.institucion.fm.desktop.view;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Toolbarbutton;

import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.security.service.PermissionTxManager;

public class TextToolbarButton extends Toolbarbutton {
	private static final long serialVersionUID = 1L;
	private String action;
	private boolean have;
	private static PermissionTxManager permTxMgr = null;
	
	/**
	 * 
	 * @param action Es el postfijo del token de seguridad (action)
	 */
	public TextToolbarButton(String action) {
		this.action = action;
		this.have = true;
	}

	public String getAction() {
		return this.action;
	}
	
	public void setOnClickMethod(String onClickMethod) {
		Component nullComponent = null;
		addForward("onClick", nullComponent, onClickMethod);
	}

	private void internalSetEnabled(boolean enabled) {
		if ((this.isDisabled() && !enabled) ||
			(!this.isDisabled() && enabled)) {
			//log.debug("@@ setEnabled no se ejecuta porque no hace falta");
			return;
		}
		if (enabled) {
			setStyle("text-decoration: none !important; color: #FFFFFF !important;");
		} else {
//			setStyle("text-decoration: line-through !important; color: #CCCCCC !important;");
			setStyle("color: #CCCCCC !important;");
		}
		setDisabled(!enabled);
	}

	public void setEnabled(boolean enabled) {
		if (enabled) {
			if (this.have) {
				this.internalSetEnabled(enabled);
			}
		} else {
			this.internalSetEnabled(enabled);
		}
	}

	public void setLabelAndTooltip(String tooltip) {
		this.setLabel(tooltip);
		this.setTooltiptext(tooltip);
	}

	public void setLabelAndTooltip(String text, String tooltip) {
		this.setLabel(text);
		this.setTooltiptext(tooltip);
	}

	public void setImage(String image) {
		// nothing
	}
	
	public void processTxToken(String tokenPrefix) {
		//log.debug("proceso al tokenPrefix [" + tokenPrefix + "] postFix [" + this.getAction() + "]");
		if (this.getAction() == null) {
			// no esta securizado
			return;
		}
		String token = "tx." + tokenPrefix + "." + this.getAction();
		//log.debug("proceso al token [" + token + "]");
		
		this.have = this.getManager().havePermission(token);
		//log.debug("have? [" + this.have + "]");
		if (!this.have) {
			// deshabilito el boton, no tiene permisos
			this.have = false;
			this.internalSetEnabled(false);
		}
	}

	private synchronized PermissionTxManager getManager() {
		if (permTxMgr == null) {
			permTxMgr = BeanFactory.<PermissionTxManager>getObject("fm.mgr.tx.permission");
		}
		return permTxMgr;
	}
}
