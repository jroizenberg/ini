package com.institucion.fm.desktop.view;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.institucion.fm.desktop.delegated.GridScheduleDelegate;
import com.institucion.fm.desktop.service.I18N;

/**
 * The Class GridSchedule.
 */
public class GridSchedule extends Grid implements GridScheduleDelegate  {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant ID. */
	public static final String ID = "gridschedule";


	/** The update schedule button. */
	private Button updateScheduleButton;

	/** The delete schedule button. */
	private Button deleteScheduleButton;

	/** The insert schedule button. */
	private Button insertScheduleButton;

	/** The clean schedule button. */
	private Button cleanScheduleButton;

	/**
	 * Instantiates a new grid schedule.
	 */
	public GridSchedule() {
		setId(ID);
		Rows rows = new Rows();
		this.appendChild(rows);
		makeScheduleStructure();
		setInsertMode();
	}

	/**
	 * Make schedule structure.
	 */
	private void makeScheduleStructure() {
	

		Panel addressPanel = new Panel();
		Panelchildren addressPanelchild = new Panelchildren();
		addressPanel.appendChild(addressPanelchild);

		org.zkoss.zul.Toolbar toolbar = makeToolbar();
		addressPanelchild.appendChild(toolbar);

		Row listboxRow = new Row();
		listboxRow.appendChild(addressPanel);
		this.getRows().appendChild(listboxRow);
	}

	/**
	 * Make toolbar.
	 * 
	 * @return the org.zkoss.zul. toolbar
	 */
	private org.zkoss.zul.Toolbar makeToolbar() {
		org.zkoss.zul.Toolbar toolbar = new org.zkoss.zul.Toolbar();
		toolbar.setAlign("end");

		// boton Limpiar el grid de horario
		cleanScheduleButton = new Button();
		cleanScheduleButton.setLabel(I18N.getLabel("toolbar.cleanschedule"));
		cleanScheduleButton.setTooltip(I18N.getLabel("toolbar.cleanschedule.tooltip"));
		cleanScheduleButton.addEventListener(Events.ON_CLICK,new EventListener() {
					public void onEvent(Event evt) {
						cleanScheduleButton.setDisabled(true);
					}
				});
		toolbar.appendChild(cleanScheduleButton);

		// boton modificar un horario
		updateScheduleButton = new Button();
		updateScheduleButton.setLabel(I18N.getLabel("toolbar.updateschedule"));
		updateScheduleButton.setTooltip(I18N.getLabel("toolbar.updateschedule.tooltip"));
		updateScheduleButton.addEventListener(Events.ON_CLICK,	new EventListener() {
					public void onEvent(Event evt) {
						
					}
				});
		toolbar.appendChild(updateScheduleButton);

		// boton eliminar un horario
		deleteScheduleButton = new Button();
		deleteScheduleButton.setLabel(I18N.getLabel("toolbar.deleteschedule"));
		deleteScheduleButton.setTooltip(I18N
				.getLabel("toolbar.deleteschedule.tooltip"));
		deleteScheduleButton.addEventListener(Events.ON_CLICK,new EventListener() {
					public void onEvent(Event evt) {
					}
				});
		toolbar.appendChild(deleteScheduleButton);

		// boton eliminar un horario
		insertScheduleButton = new Button();
		insertScheduleButton.setLabel(I18N.getLabel("toolbar.insertschedule"));
		insertScheduleButton.setTooltip(I18N.getLabel("toolbar.insertschedule.tooltip"));
		insertScheduleButton.addEventListener(Events.ON_CLICK,new EventListener() {
					public void onEvent(Event evt) {
					}
				});
		toolbar.appendChild(insertScheduleButton);

		return toolbar;
	}


	/**
	 * Sets the insert mode.
	 */
	private void setInsertMode() {
		updateScheduleButton.setDisabled(true);
		deleteScheduleButton.setDisabled(true);
		insertScheduleButton.setDisabled(false);
		cleanScheduleButton.setDisabled(true);
	}

	/**
	 * Sets the delete mode.
	 */
	private void setDeleteMode() {
		updateScheduleButton.setDisabled(true);
		deleteScheduleButton.setDisabled(false);
		insertScheduleButton.setDisabled(false);
		cleanScheduleButton.setDisabled(false);
	}

	/**
	 * Sets the update mode.
	 */
	private void setUpdateMode() {
		updateScheduleButton.setDisabled(false);
		deleteScheduleButton.setDisabled(true);
		insertScheduleButton.setDisabled(false);
		cleanScheduleButton.setDisabled(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.instituto.fm.desktop.delegated.GridScheduleDelegate#enableCleanButton
	 * ()
	 */
	@Override
	public void enableCleanButton() {
		cleanScheduleButton.setDisabled(false);

	}

}