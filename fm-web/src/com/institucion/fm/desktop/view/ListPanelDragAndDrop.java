package com.institucion.fm.desktop.view;

import java.util.StringTokenizer;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zkex.zul.Columnchildren;
import org.zkoss.zkex.zul.Columnlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Vbox;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.wf.model.Role;

public class ListPanelDragAndDrop extends Panel implements AfterCompose{
	private static Log log = LogFactory.getLog(ListPanelDragAndDrop.class);
	
	private static final long serialVersionUID = 1L;

	public static final String ID = "listPanelDragAndDrop";

	private Columnchildren leftColumnchildren;
	private Columnchildren rightColumnchildren;
	private String leftpanel;
	private String rightpanel;
	private String updown;
	private String updownToFirstLast;

	Button buttonMoveTo = new Button(">");
	Button buttonMoveAllTo = new Button(">>");
	Button buttonMoveFrom = new Button("<");
	Button buttonMoveFromAll = new Button("<<");

	
	public ListPanelDragAndDrop() {
		super();
		setId(ID);
		setWidth("auto");
		setHeight("auto");
		setBorder("normal");
		setCollapsible(false);
	}

	public String getLeftpanel() { return leftpanel; }
	public void setLeftpanel(String leftpanel) { this.leftpanel = leftpanel; }

	public String getRightpanel() { return rightpanel; }
	public void setRightpanel(String rightpanel) { this.rightpanel = rightpanel; }

	public String getUpdown() { return updown; }
	public void setUpdown(String updown) { this.updown = updown; }

	public String getUpdownToFirstLast() { 	return updownToFirstLast;}
	public void setUpdownToFirstLast(String updownToFirstLast) {this.updownToFirstLast = updownToFirstLast;	}

	@Override
	public void afterCompose() {
		// armar la estructura para los paneles
		Panelchildren panelChild = new Panelchildren();
		this.appendChild(panelChild);
		Columnlayout columnLayout = new Columnlayout();
		leftColumnchildren = new Columnchildren();
		leftColumnchildren.setWidth("50%");
		Columnchildren middleColumnchildren = new Columnchildren();
		middleColumnchildren.setWidth("70px");
		rightColumnchildren = new Columnchildren();
		rightColumnchildren.setWidth("50%");
		columnLayout.appendChild(leftColumnchildren);
		columnLayout.appendChild(middleColumnchildren);
		columnLayout.appendChild(rightColumnchildren);
		panelChild.appendChild(columnLayout);

		// panel del medio
		middleColumnchildren.appendChild(makeMiddlePanel());
		resolvePanel(leftpanel, leftColumnchildren);
		resolvePanel(rightpanel, rightColumnchildren);
	}

	private void resolvePanel(String panel, Columnchildren columnChildren) {
		try{
			StringTokenizer strtoken = new StringTokenizer(panel, "|");
			Component component = null;
			while (strtoken.hasMoreTokens()){
				String token = strtoken.nextToken();

				if (token.indexOf('=') < 0) // no es propiedad, es el nombre de la clae
				{
					Class<?> componentClass = Class.forName(token);
					component = (Component) componentClass.newInstance();
				}
				else // viene una propiedad
				{
					int equalIndex = token.indexOf('=');
					String property = token.substring(0, equalIndex);
					String value = token.substring(equalIndex+1);
					PropertyUtils.setSimpleProperty(component, property, I18N.getStringLabel(value));
				}
			}

			if (component instanceof AfterCompose)
				((AfterCompose) component).afterCompose();
			columnChildren.appendChild(component);
		}
		catch (Exception e)
		{
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new RuntimeException("'"+panel+"' no pudo crearse. Motivo del error: "+e.getMessage(), e);
		}
	}

	private Panel makeMiddlePanel() {
		Panel middlePanel = new Panel();
		Panelchildren middlePanelChild = new Panelchildren();
		middlePanelChild.setStyle("margin: 10px 10px;");
		middlePanel.appendChild(middlePanelChild);
		Vbox middleVbox = new Vbox();
		middlePanelChild.appendChild(middleVbox);

		
		buttonMoveTo.setWidth("50px");
		buttonMoveTo.addForward("onClick", (Component) null, "onMoveRight");
		middleVbox.appendChild(buttonMoveTo);

		
		buttonMoveAllTo.setWidth("50px");
		buttonMoveAllTo.addForward("onClick", (Component) null, "onMoveAllRight");
		middleVbox.appendChild(buttonMoveAllTo);

		Separator middleSeparator = new Separator();
		middleSeparator.setSpacing("5px");
		middleVbox.appendChild(middleSeparator);

		buttonMoveFrom.setWidth("50px");
		buttonMoveFrom.addForward("onClick", (Component) null, "onMoveLeft");
		middleVbox.appendChild(buttonMoveFrom);

		buttonMoveFromAll.setWidth("50px");
		buttonMoveFromAll.addForward("onClick", (Component) null, "onMoveAllLeft");
		middleVbox.appendChild(buttonMoveFromAll);

		if (updown != null && updown.equals("true")){
			Separator updownSeparator = new Separator();
			updownSeparator.setSpacing("5px");
			middleVbox.appendChild(updownSeparator);

			Button buttonMoveUp = new Button(I18N.getLabel("up"));
			buttonMoveUp.setWidth("50px");
			buttonMoveUp.addForward("onClick", (Component) null, "onMoveUp");
			middleVbox.appendChild(buttonMoveUp);

			Button buttonMoveDown = new Button(I18N.getLabel("down"));
			buttonMoveDown.setWidth("50px");
			buttonMoveDown.addForward("onClick", (Component) null, "onMoveDown");
			middleVbox.appendChild(buttonMoveDown);
		}
		
		if (updownToFirstLast != null && updownToFirstLast.equals("true")){
			Separator updownSeparator = new Separator();
			updownSeparator.setSpacing("5px");
			middleVbox.appendChild(updownSeparator);

			Button buttonMoveUp = new Button(I18N.getLabel("first"));
			buttonMoveUp.setWidth("50px");
			buttonMoveUp.addForward("onClick", (Component) null, "onMoveFirstUp");
			middleVbox.appendChild(buttonMoveUp);

			Button buttonMoveDown = new Button(I18N.getLabel("last"));
			buttonMoveDown.setWidth("50px");
			buttonMoveDown.addForward("onClick", (Component) null, "onMoveLastDown");
			middleVbox.appendChild(buttonMoveDown);
		}
		return middlePanel;
	}
	
	public void disableButtons(Role role) {
		if(role.getChildren().size()==0){  // si no es la ultima hoja
			//if(role.getWfusers().size()>=1){
				buttonMoveTo.setDisabled(false);
				buttonMoveAllTo.setDisabled(false);
		//	}
		}else{
			buttonMoveTo.setDisabled(true);
			buttonMoveAllTo.setDisabled(true);
		}
	
		buttonMoveFrom.setDisabled(true);
		buttonMoveFromAll.setDisabled(true);
	}
}