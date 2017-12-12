package com.institucion.fm.desktop.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

public class PanelList extends Panel implements AfterCompose {
	private static Log log = LogFactory.getLog(PanelList.class);

	private static final long serialVersionUID = 1L;
	public static final String ID = "panelList";

	private String usegrid;
	private GridList gridlist;

	public PanelList() {
		setId(ID);
		setWidth("auto");
		setHeight("auto");
		setBorder("normal");
		setCollapsible(false);
	}

	public String getUsegrid() {
		return usegrid;
	}

	public void setUsegrid(String usegrid) {
		this.usegrid = usegrid;
	}

	@Override
	public void afterCompose() {
		Panelchildren panelChild = new Panelchildren();
		this.appendChild(panelChild);

		try {
			Class<?> gridClass = Class.forName(usegrid);
			gridlist = (GridList) gridClass.newInstance();
			if (!getId().equals(ID))
				gridlist.setId(getId() + "Grid");
		} catch (Exception e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: "
					+ e.getStackTrace());
			throw new RuntimeException("'" + usegrid+ "' no existe o no extiende de GridList", e);
		}

		panelChild.appendChild(gridlist);
	}

	public GridList getGridList() {
		return gridlist;
	}
}