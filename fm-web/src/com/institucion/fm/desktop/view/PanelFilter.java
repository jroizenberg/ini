package com.institucion.fm.desktop.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Separator;

import com.institucion.fm.desktop.service.I18N;

public class PanelFilter extends Panel implements AfterCompose{
	private static Log log = LogFactory.getLog(PanelFilter.class);
	
	private static final long serialVersionUID = 1L;
	public static final String ID = "panelFilter";

	private String usefilter;
	private Panel innerPanel;
	private GridFilter gridfilter;

	public PanelFilter(){
	//(getId() == null)
			setId(ID);
		
		setWidth("auto");
	}

	public String getUsefilter() { return usefilter; }
	public void setUsefilter(String usefilter) { this.usefilter = usefilter; }

	public void clear(){
		innerPanel.setOpen(false);
	}
	
	public void collapsible(boolean coll){
		innerPanel.setCollapsible(coll);
		innerPanel.setOpen(true);
	}

	@Override
	public void afterCompose(){
		Panelchildren panelChild = new Panelchildren();
		this.appendChild(panelChild);

		innerPanel = new Panel();
		innerPanel.setId(this.getId()+"inner");
		innerPanel.setOpen(false);
		innerPanel.setBorder("normal");
		innerPanel.setCollapsible(true);
		innerPanel.setWidth("auto");
		innerPanel.setTitle(I18N.getLabel("filter.title"));
		Panelchildren filterPanelchild = new Panelchildren();
		innerPanel.appendChild(filterPanelchild);
		panelChild.appendChild(innerPanel);

		try{
			Class<?> filterClass = Class.forName(usefilter);
			gridfilter = (GridFilter) filterClass.newInstance();
			if (!getId().equals(ID))
				gridfilter.setId(this.getId()+"Grid");
		}catch (Exception e)	{
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new RuntimeException("'"+usefilter+"' no pudo crearse. Motivo del error: "+e.getMessage(), e);
		}

		filterPanelchild.appendChild(gridfilter);
		panelChild.appendChild(new Separator());
		
		loadHistory();
	}

	public void loadHistory(){
		String module = ((WindowSelector) this.getSpaceOwner().getFellow(WindowSelector.ID)).getEntity();

		Boolean isPanelFilterOpen = (Boolean) Sessions.getCurrent().getAttribute(module+".filtergrid.open");
		if (isPanelFilterOpen != null)
			innerPanel.setOpen(isPanelFilterOpen);

		gridfilter.loadHistory();
	}

	public void saveHistory(){
		String module = ((WindowSelector) this.getSpaceOwner().getFellow(WindowSelector.ID)).getEntity();
		Sessions.getCurrent().setAttribute(module+".filtergrid.open", innerPanel.isOpen());

		gridfilter.saveHistory();
	}

	public GridFilter getGridFilter(){
		return gridfilter;
	}

	public Panel getInnerPanel() {
		return innerPanel;
	}

	public void setInnerPanel(Panel innerPanel) {
		this.innerPanel = innerPanel;
	}
}