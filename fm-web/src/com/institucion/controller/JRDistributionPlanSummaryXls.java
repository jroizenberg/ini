package com.institucion.controller;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.core.layout.ListLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;

import com.institucion.fm.ds.Row;

public class JRDistributionPlanSummaryXls extends BaseReport {

	private List<String> fieldNames = null;
	private List<RowModel> ds = null;
	
	@Override
	public DynamicReport buildReport() throws Exception {
		DynamicReportBuilder drb = new DynamicReportBuilder();
		Integer margin = Integer.valueOf(20);
		drb.setTitleStyle(Styles.titleStyle()).setTitle("Cursos")
		.setLeftMargin(0)
		.setDetailHeight(35)
		.setRightMargin(margin)
		.setTopMargin(margin)
		.setBottomMargin(margin)
		.setDefaultStyles(Styles.titleStyle(), Styles.subtitleStyle(), Styles.headerStyle(), Styles.detailStyle())
		.setPrintColumnNames(true)
		.setFooterHeight(35).setOddRowBackgroundStyle(Styles.oddRowStyle());
		/** 
		 * COLUMNAS
		 */
		AbstractColumn column;
		for (String columnName : this.fieldNames) {
			if(columnName.equalsIgnoreCase("Nombre Curso/Tratamiento")){
				column = ColumnBuilder.getInstance()
						.setColumnProperty("values("+columnName+")", String.class.getName())
						.setTitle(columnName).setWidth(90)
						.setStyle(Styles.detailCursosStyle())
						.build();
				
			}else{
				column = ColumnBuilder.getInstance()
						.setColumnProperty("values("+columnName+")", String.class.getName())
						.setTitle(columnName).setWidth(25)
						.build();
			}
			drb.addColumn(column);
		}
		/**
		 * Debug:
		 * 
		if (ds != null && ds.size() > 0) {
			for (RowModel rowModel : ds) {
				for (String fieldName : fieldNames) {
					System.out.print(fieldName+" : "+rowModel.get(fieldName));
				}
				System.out.print("*****************************");
			}
		}
		/**
		 * Fin Debug
		 */
		/**
		 * sacamos la primer row para destacarla como un reporte concatenado al
		 * principio.
		 */
//		RowModel row = ds.get(0);
//		ds.remove(0);
		
		drb.setIgnorePagination(true);
		drb.setUseFullPageWidth(true);
		drb.setColumnsPerPage(1);
		drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
		DynamicReport dr = drb.build();
		return dr;
	}

	@Override
	protected JRDataSource getDataSource() {
		JRBeanCollectionDataSource jr = new JRBeanCollectionDataSource(ds);
		return jr;
	}

	@Override
	protected LayoutManager getLayoutManager() {
		return new ListLayoutManager();
	}

	@Override
	protected List<RowModel> parseRows() {
		return this.ds;
	}

	@Override
	public void setDataSourceList(List<Row> ds) {
	}
	
	public void setDataSourceTable(List<RowModel> distPlanViewModels) {
		this.ds = distPlanViewModels;
	}

	/**
	 * @return the fieldNames
	 */
	public List<String> getFieldNames() {
		return fieldNames;
	}

	/**
	 * @param fieldNames the fieldNames to set
	 */
	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}

}