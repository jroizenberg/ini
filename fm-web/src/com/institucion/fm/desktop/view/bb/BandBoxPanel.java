package com.institucion.fm.desktop.view.bb;

import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

import com.institucion.fm.desktop.view.GridList;
import com.institucion.fm.desktop.view.PanelDaDFilter;
import com.institucion.fm.filteradv.model.CriteriaClause;

/**
 * The Class BandBoxPanel.
 *
 * @param <T> the generic type
 */
public abstract class BandBoxPanel<T> extends Panel {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id unique. */
	private static long idUnique = 0L;

	/** The da d filter. */
	private PanelDaDFilter daDFilter;
	
	/** The da d list. */
	private BandBoxList<T> daDList;

	/**
	 * Instantiates a new band box panel.
	 *
	 * @param panelId the panel id
	 */
	public BandBoxPanel(String panelId) {
		super();
		this.createPanel(panelId, null);
	}

	/**
	 * Instantiates a new band box panel.
	 *
	 * @param panelId the panel id
	 * @param filterType the filter type
	 */
	public BandBoxPanel(String panelId, String filterType) {
		super();
		this.createPanel(panelId + Long.toString(idUnique), filterType);
		idUnique++;
	}

	/**
	 * Creates the panel.
	 *
	 * @param panelId the panel id
	 * @param filterType the filter type
	 */
	protected void createPanel(String panelId, String filterType) {
		setId(panelId);
		if (Executions.getCurrent().isBrowser("ie6-")
				|| Executions.getCurrent().isBrowser("ie7-")) {
			int width = this.getPanelWidth() + 3;
			setWidth(width + "px");
		} else {
			setWidth("auto");
		}
		setHeight("auto");
		setBorder("normal");
		Panelchildren panelchildren = new Panelchildren();
		this.appendChild(panelchildren);

		daDFilter = new PanelDaDFilter();
		daDFilter.setIdBandBox(panelId);
		daDFilter.setId(getId() + "_filter");
		daDFilter.setUsefilter(filterType == null ? getBandBoxFilterClassName()
				: filterType);
		daDFilter.afterCompose();
		this.getPanelchildren().appendChild(daDFilter);

		daDList = getBandBoxList();
		daDList.initPanel(this.getPanelWidth());
		daDList.setId(getId() + "_list");
		this.getPanelchildren().appendChild(daDList);
	}

	/**
	 * Gets the list from panel.
	 *
	 * @return the list from panel
	 */
	public GridList getListFromPanel() {
		return this.daDList;
	}

	/**
	 * Sets the list.
	 *
	 * @param list the new list
	 */
	public void setList(List<T> list) {
		this.daDList.setList(list);
	}

	/**
	 * Gets the criteria filters.
	 *
	 * @return the criteria filters
	 */
	public CriteriaClause getCriteriaFilters() {
		return this.daDFilter.getGridFilter().getCriteriaFilters();
	}

	/**
	 * Gets the band box filter class name.
	 *
	 * @return the band box filter class name
	 */
	abstract protected String getBandBoxFilterClassName();

	/**
	 * Gets the band box list.
	 *
	 * @return the band box list
	 */
	abstract protected BandBoxList<T> getBandBoxList();

	/**
	 * String value.
	 *
	 * @param value the value
	 * @return the string
	 */
	abstract public String stringValue(T value);

	/**
	 * Gets the panel width.
	 *
	 * @return the panel width
	 */
	abstract protected int getPanelWidth();

	/**
	 * Gets the serialversionuid.
	 *
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Gets the id unique.
	 *
	 * @return the idUnique
	 */
	public static final long getIdUnique() {
		return idUnique;
	}
}