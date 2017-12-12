package com.institucion.controller;




import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;

import com.institucion.fm.ds.Row;


public abstract class BaseReport{

	protected static final Log log = LogFactory.getLog(BaseReport.class);

	protected JasperPrint jp;
	protected JasperReport jr;
	//@SuppressWarnings("unchecked")
	protected Map<String, List> params = new HashMap<String, List>();
	protected DynamicReport dr;
	private Map<Integer, List<RowModel>> dsList= new HashMap<Integer, List<RowModel>>();
	private Map<Integer, List<String>> fieldNamesList= new HashMap<Integer, List<String>>();


	public abstract DynamicReport buildReport() throws Exception;

	public Map<?, ?> getParams() {
		return params;
	}
	

	/**
	 * Getter del layoutManager
	 * @returns el LayoutManager de Dynamic Jasper
	 */
	protected abstract LayoutManager getLayoutManager();

	public void exportPdfReport(OutputStream os) throws Exception {
		
		dr = buildReport();

		/**
		 * Get a JRDataSource implementation
		 */
		JRDataSource ds = getDataSource();


		/**
		 * Creates the JasperReport object, we pass as a Parameter
		 * the DynamicReport, a new ClassicLayoutManager instance (this
		 * one does the magic) and the JRDataSource
		 */

		jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(), params);

		/**
		 * Creates the JasperPrint object, we pass as a Parameter
		 * the JasperReport object, and the JRDataSource
		 */
		if (ds != null)
			jp = JasperFillManager.fillReport(jr, params, ds);
		else
			jp = JasperFillManager.fillReport(jr, params);

		
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT,	jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporter.exportReport();
	}

	public void exportXlsReportRestructuring(OutputStream os, String[] sheetNames, String reportName) throws Exception {
		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();

		Iterator it =  dsList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, List<RowModel>> e = (Map.Entry)it.next();
			DynamicReport dr = buildReport(reportName, e.getKey());
			List<RowModel> rr= e.getValue();
			JRDataSource ds = new JRBeanCollectionDataSource(rr);
			dr.getOptions().setIgnorePagination(true);
			JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(), params);
			
			if (ds != null){
				JasperPrint jp = JasperFillManager.fillReport(jr, params, ds);
				jasperPrintList.add(jp);

			}else{
				JasperPrint jp = JasperFillManager.fillReport(jr, params);
				jasperPrintList.add(jp);
			}		
		}

		JRXlsExporter exporter = new JRXlsExporter();
	
		exporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, sheetNames );
	    exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "BatchExportReport.xls");
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
       
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os); //and output stream
        //Excel specific parameter
        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
              
        exporter.exportReport();
	}
	
	public DynamicReport buildReport(String reportName, int numberField) throws Exception {
		DynamicReportBuilder drb = new DynamicReportBuilder();
		Integer margin = Integer.valueOf(20);
		
		if(reportName != null && reportName.contains("Caja del dia")){
			drb.setTitleStyle(Styles.titleStyle()).setTitle("Reestruct")
			.setSubtitle("Este reporte ha sido generado " )
			.setSubtitleStyle(Styles.subtitleStyle())
			.setLeftMargin(0)
			.setReportName(reportName)
			.setDetailHeight(10)
			.setRightMargin(margin)
			.setTopMargin(margin)
			.setBottomMargin(margin)
			.setDefaultStyles(Styles.titleStyle(), Styles.subtitleStyle(), Styles.headerStyle2(), Styles.detailStyle())
			.setPrintColumnNames(true)
			.setFooterHeight(25)
			.setOddRowBackgroundStyle(Styles.oddRowStyle());

			
		}else{
			drb.setTitleStyle(Styles.titleStyle()).setTitle("Reestruct")
			.setSubtitle("Este reporte ha sido generado " )
			.setSubtitleStyle(Styles.subtitleStyle())
			.setLeftMargin(0)
			.setReportName(reportName)
			.setDetailHeight(15)
			.setRightMargin(margin)
			.setTopMargin(margin)
			.setBottomMargin(margin)
			.setDefaultStyles(Styles.titleStyle(), Styles.subtitleStyle(), Styles.headerStyle(), Styles.detailStyle())
			.setPrintColumnNames(true)
			.setFooterHeight(35)
			.setOddRowBackgroundStyle(Styles.oddRowStyle());

		}
		/** 
		 * COLUMNAS
		 */
		
		List<String> listcolumns= fieldNamesList.get(numberField);

		boolean esDeCaja=false;
		AbstractColumn column;
		for (String columnName : listcolumns) {
			
			
			if(columnName != null && columnName.equalsIgnoreCase("Fecha y Hora")){
				esDeCaja= true;
				column = ColumnBuilder.getInstance().setColumnProperty("values("+columnName+")", String.class.getName())
						.setTitle(columnName).setWidth(35).build();
			}else if(columnName != null && columnName.equalsIgnoreCase("Tipo Movimiento")){
				esDeCaja= true;
				column = ColumnBuilder.getInstance().setColumnProperty("values("+columnName+")", String.class.getName())
						.setTitle(columnName).setWidth(25).build();
			}else if(columnName != null && columnName.equalsIgnoreCase("Resp")){
				column = ColumnBuilder.getInstance().setColumnProperty("values("+columnName+")", String.class.getName())
						.setTitle(columnName).setWidth(24).build();
			}else if(columnName != null && columnName.equalsIgnoreCase("Cliente")){
				column = ColumnBuilder.getInstance().setColumnProperty("values("+columnName+")", String.class.getName())
						.setTitle(columnName).setWidth(40).build();

			}else if(columnName != null && columnName.equalsIgnoreCase("Concepto")){
				column = ColumnBuilder.getInstance().setColumnProperty("values("+columnName+")", String.class.getName())
						.setTitle(columnName).setWidth(90).build();
			}else if(columnName != null && columnName.equalsIgnoreCase("Importe abonado")){
				column = ColumnBuilder.getInstance().setColumnProperty("values("+columnName+")", String.class.getName())
						.setTitle(columnName).setWidth(16).build();
			}else if(esDeCaja && columnName != null && columnName.equalsIgnoreCase("Observaciones")){
				column = ColumnBuilder.getInstance().setColumnProperty("values("+columnName+")", String.class.getName())
						.setTitle(columnName).setWidth(60).build();	
			}else
				column = ColumnBuilder.getInstance().setColumnProperty("values("+columnName+")", String.class.getName())
					.setTitle(columnName).setWidth(50).build();	
	
			drb.addColumn(column);
		}
		
		if(esDeCaja){
			drb.setIgnorePagination(true);

			drb.setUseFullPageWidth(true);
			drb.setColumnsPerPage(1);
			
//			Page aa= new Page();
//			aa.setWidth(800);
//			aa.setHeight(1400);
//			aa.setOrientationPortrait(false);
//			drb.setPageSizeAndOrientation(aa);

		}else{
			drb.setIgnorePagination(true);

			drb.setUseFullPageWidth(true);
			drb.setColumnsPerPage(1);
			
			Page aa= new Page();
			aa.setWidth(1400);
			aa.setHeight(1400);
			aa.setOrientationPortrait(false);
			drb.setPageSizeAndOrientation(aa);
		}
		DynamicReport dr = drb.build();

		return dr;
	}
	
	
	public void exportXlsReport(OutputStream os) throws Exception {
		dr = buildReport();

		/**
		 * Get a JRDataSource implementation
		 */
		JRDataSource ds = getDataSource();


		/**
		 * Creates the JasperReport object, we pass as a Parameter
		 * the DynamicReport, a new ClassicLayoutManager instance (this
		 * one does the magic) and the JRDataSource
		 */
		dr.getOptions().setIgnorePagination(true);
		jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(), params);
		
		/**
		 * Creates the JasperPrint object, we pass as a Parameter
		 * the JasperReport object, and the JRDataSource
		 */
		if (ds != null)
			jp = JasperFillManager.fillReport(jr, params, ds);
		else
			jp = JasperFillManager.fillReport(jr, params);
		JRXlsExporter exporter = new JRXlsExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os); //and output stream
        //Excel specific parameter
        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporter.exportReport();
	}
	
	/**
	 * Metodo utilizado para abastecer al reporte de datos.
	 * @param ds Lista de de objetos Row obtenidos desde la base de datos.
	 */
	public abstract void setDataSourceList(List<Row> ds);
	
	/**
	 * Me©todo que parsea los datos del datasource al correspondiente
	 * modelo de datos del reporte (ubicados en <i>com.asofarma.fm.report.model</i>).
	 * <br/><b>La lista devuelta es utilizada en getDataSource para construir el
	 * DataSource de Jasper Reports.</b> 
	 * @return Lista de POJOs conteniendo los datos encapsulados.
	 */
	protected abstract List<?> parseRows();
	
	/**
	 * Devuelve el DataSource.
	 * @return DataSource de Jasper Reports.
	 */
	protected abstract JRDataSource getDataSource();
	
	
	public DynamicReport getDynamicReport() {
		return dr;
	}
	


	public Map<Integer, List<RowModel>> getDsList() {
		return dsList;
	}

	public void setDsList(Map<Integer, List<RowModel>> dsList) {
		this.dsList = dsList;
	}

	public Map<Integer, List<String>> getFieldNamesList() {
		return fieldNamesList;
	}

	public void setFieldNamesList(Map<Integer, List<String>> fieldNamesList) {
		this.fieldNamesList = fieldNamesList;
	}
}
