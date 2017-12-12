package com.institucion.fm.desktop.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

public class PanelDaDFilter extends Panel implements AfterCompose
{
	private static Log log = LogFactory.getLog(PanelDaDFilter.class);
	
	private static final long serialVersionUID = 1L;

	public static final String ID = "panelDaDFilter";

	private String usefilter;
	private String idBandBox;
	private GridFilter gridfilter;

	public PanelDaDFilter()
	{
		setId(ID);
		setWidth("auto");
		setBorder("normal");
		setWidth("auto");
	}

	
	public String getIdBandBox() { return idBandBox; }
	public void setIdBandBox(String idBandBox) { this.idBandBox = idBandBox; }

	public String getUsefilter() { return usefilter; }
	public void setUsefilter(String usefilter) { this.usefilter = usefilter; }

	@Override
	public void afterCompose()
	{
		Panelchildren panelChild = new Panelchildren();
		this.appendChild(panelChild);

		try
		{
			Class<?> filterClass = Class.forName(usefilter);
			gridfilter = (GridFilter) filterClass.newInstance();
			gridfilter.setIdBandBox(getIdBandBox());
			if (!getId().equals(ID))
				gridfilter.setId(this.getId()+"Grid");
		}
		catch (Exception e)
		{
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new RuntimeException("'"+usefilter+"' no pudo crearse. Motivo del error: "+e.getMessage(), e);
		}

		panelChild.appendChild(gridfilter);
	}

	public GridFilter getGridFilter()
	{
		return gridfilter;
	}
}