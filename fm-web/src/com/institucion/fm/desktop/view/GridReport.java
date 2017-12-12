package com.institucion.fm.desktop.view;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

public class GridReport extends Grid {
	private static final long serialVersionUID = 1L;
	public static final String ID = "gridReport";

	private Auxhead currentAuxhead;

	private Row currentRow;

	public GridReport() {
		setFixedLayout(true);
		setWidth("auto");
		setId(ID);
	}

	public void addAuxhead() {
		currentAuxhead = new Auxhead();
		
		this.appendChild(currentAuxhead);
	}

	public void addAuxheader(String label, int colspan, int rowspan) {
		Auxheader auxheader = new Auxheader();
		auxheader.setLabel(label);
		auxheader.setColspan(colspan);
		auxheader.setRowspan(rowspan);
		this.addAuxheader(auxheader);
	}
	
	public Auxheader getAuxheader(String label, int colspan, int rowspan) {
		Auxheader auxheader = new Auxheader();
		auxheader.setLabel(label);
		auxheader.setColspan(colspan);
		auxheader.setRowspan(rowspan);
		return auxheader;
		//this.addAuxheader(auxheader);
	}
	
	public void addAuxheader(Auxheader auxheader) {
		if (currentAuxhead == null)
			addAuxhead();
		currentAuxhead.appendChild(auxheader);
	}
	
	public void addAuxheader(String label, int colspan, int rowspan, String width) {
		Auxheader auxheader = new Auxheader();
		auxheader.setLabel(label);
		auxheader.setColspan(colspan);
		auxheader.setRowspan(rowspan);
		auxheader.setWidth(width);
		this.addAuxheader(auxheader);
	}

	public void addAuxheader2(String label, int colspan, int rowspan, String width) {
		Auxheader auxheader = new Auxheader();
		auxheader.setLabel(label);
//		auxheader.setColspan(colspan);
//		auxheader.setRowspan(rowspan);
		auxheader.setWidth(width);
		auxheader.setStyle("width:"+width +" !important;");
		this.addAuxheader(auxheader);
	}
	
	public void addAuxheader(String label, int colspan) {
		Auxheader auxheader = new Auxheader();
		auxheader.setLabel(label);
		auxheader.setColspan(colspan);
		this.addAuxheader(auxheader);
	}
	
	public void addAuxheader(String value, String align) {
		Auxheader auxheader = new Auxheader();
		Label label = new Label(value);
		label.setStyle("font-weight:bold;text-align: " + align + "; display: block;");
		auxheader.appendChild(label);
		this.addAuxheader(auxheader);
	}
	
	public void addColumn(String label) {
		Column column = new Column();
		column.setLabel(label);
		if (this.getColumns() == null) initColumns(); 
		this.getColumns().appendChild(column);
	}

	public void addColumn(String label, boolean conwidth, String width) {
		Column column = new Column();
		column.setLabel(label);
		column.setWidth(width);
		if (this.getColumns() == null) initColumns(); 
		this.getColumns().appendChild(column);
	}

	
	public void addColumn(String label, Component  comp) {
		Column column = new Column();
		column.appendChild(comp);

		column.setLabel(label);
		if (this.getColumns() == null) initColumns(); 
		this.getColumns().appendChild(column);
	}
	
	public void addColumn(String label, String  comp) {
		Column column = new Column();

		column.setLabel(label);
		if (this.getColumns() == null) initColumns(); 
		this.getColumns().appendChild(column);
	}

	public void addColumn(String label,int id) {
		Column column = new Column();
		column.setLabel(label);
		column.setId(String.valueOf(id));
		if (this.getColumns() == null) initColumns(); 
		this.getColumns().appendChild(column);
	}
	
	public Column getTColumn(String label, long width, boolean isPorcentage) {
		Column column = new Column();
		column.setLabel(label);
		if(isPorcentage){
			column.setWidth(String.valueOf(width) + String.valueOf(isPorcentage==true?"%":"px"));
		}
		return column;
	}

	public Column getTColumn(String label) {
		Column column = new Column();
		column.setLabel(label);
		return column;
	}
	public void setTColumn(Column column) {
		if (this.getColumns() == null) initColumns(); 
		this.getColumns().appendChild(column);
	}
	
	public void addColumn(String label, long width, boolean isPorcentage) {
		Column column = new Column();
		column.setLabel(label);
		column.setWidth(String.valueOf(width) + String.valueOf(isPorcentage==true?"%":"px"));
		if (this.getColumns() == null) initColumns(); 
		this.getColumns().appendChild(column);
	}
	
	public void addColumn(String label,Component comp,  long width, boolean isPorcentage) {
		Column column = new Column();
		column.appendChild(comp);
		column.setLabel(label);
		column.setWidth(String.valueOf(width) + String.valueOf(isPorcentage==true?"%":"px"));
		if (this.getColumns() == null) initColumns(); 
		this.getColumns().appendChild(column);
	}
	
	public void addRow() {
		currentRow = new Row();
		if (this.getRows() == null) initRows();
		this.getRows().appendChild(currentRow);
	}

	public void addRow(String style) {
		currentRow = new Row();
		currentRow.setStyle(style);
		if (this.getRows() == null) initRows();
		this.getRows().appendChild(currentRow);
	}
	
	public void addField(Label label) {
		if (currentRow == null)
			addRow();
		currentRow.appendChild(label);
	}

	public void addField(Label label, String style) {
		if (currentRow == null)
			addRow();
		currentRow.setStyle(style);
		currentRow.appendChild(label);
	}

	
	
	public void addField(Component cmp) {
		if (currentRow == null)
			addRow();
		currentRow.appendChild(cmp);
	}
	
	public void addField22(Component cmp, Component cmp2Check) {
		if (currentRow == null){
			addRow();
		}
		
		cmp.addForward(Events.ON_CHANGE,cmp,"onChangeClickEvt");
		
		currentRow.appendChild(cmp);
	}
	
	
	
	private void initRows() {
		Rows rows = new Rows();
		this.appendChild(rows);
	}

	private void initColumns() {
		Columns columns = new Columns();
		this.appendChild(columns);
	}

	public Row getCurrentRow() {
		return currentRow;
	}

	public void setCurrentRow(Row currentRow) {
		this.currentRow = currentRow;
	}

	
	public Row getCurrentRow2(String style) {
		if(currentRow == null)
			addRow();
		currentRow.setStyle(style);
		return currentRow;
	}
}