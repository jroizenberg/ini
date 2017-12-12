package com.institucion.fm.desktop.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

public class PanelCrud extends Panel implements AfterCompose {
	static Log log = LogFactory.getLog(PanelCrud.class);
	private static final long serialVersionUID = 1L;
	public static final String ID = "panelCrud";

	private String usecrud;
	private GridCrud gridcrud;

	public PanelCrud() {
		setId(ID);
		setWidth("auto");
		setHeight("auto");
		setBorder("normal");
		setCollapsible(false);
	}

	public String getUsecrud() { return usecrud; }
	public void setUsecrud(String usecrud) { this.usecrud = usecrud; }

	@Override
	public void afterCompose() {
		Panelchildren panelChild = new Panelchildren();
		this.appendChild(panelChild);
		try	{
			Class<?> gridClass = Class.forName(usecrud);
			gridcrud = (GridCrud) gridClass.newInstance();
			gridcrud.setParentPanel(this);
			if (!getId().equals(ID))
				gridcrud.setId(getId()+"Grid");
		} catch (Exception e)	{
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new RuntimeException("'"+usecrud+"' no se pudo crear.", e);
		}
		panelChild.appendChild(gridcrud);
	}

	public GridCrud getGridCrud() {
		return gridcrud;
	}
}